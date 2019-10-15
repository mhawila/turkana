package org.muzima.turkana.web.controller;

import com.google.common.io.Files;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.muzima.turkana.data.MediaMetadataRepository;
import org.muzima.turkana.data.RegistrationQuery;
import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 6/21/19.
 */
@RestController
@RequestMapping(MediaController.BASE_PATH)
@Api(tags = "Media", description = "Images, Audios, Videos")
public class MediaController {
    private static Logger LOGGER = Logger.getLogger(MediaController.class.getName());
    public static final String BASE_PATH = "api/media";

    @Autowired
    private MediaService mediaService;

    @Autowired
    private MediaMetadataRepository metadataRepository;

    @Autowired
    private RegistrationQuery registrationQuery;

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    @ApiOperation(value = "Posting Media File plus metadata in one request", notes = "Requires to pass a message signature as a request parameter")
    public void post(@RequestPart(name = "metadata") MediaMetadata mediaMetadata, @RequestPart(name = "file") MultipartFile mediaFile,
                       @RequestAttribute Registration registration, @RequestParam String signature) throws IOException {
        //Decode signature.
        byte[] binarySignature = Base64.getUrlDecoder().decode(signature);
        try {
            mediaService.verifyAndSaveMedia(registration, mediaMetadata, mediaFile.getInputStream(), binarySignature);
        } catch (Exception e) {
            String errorMessage = "Something went wrong while verifying signature, data not backed up";
            LOGGER.warning(errorMessage);
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage, e);
        }
    }

    @GetMapping(path = "/{id}", produces = { "application/json" })
    @ApiOperation(value = "Retrieves one mediaMetadata given its id, or the corresponding file if query string parameter file is true")
    public MediaMetadata retrieveMedia(HttpServletRequest request, HttpServletResponse response, @PathVariable final Long id,
                                       @RequestParam(required = false, defaultValue = "false") final Boolean file) throws IOException {
        Optional<MediaMetadata> metadataOptional = metadataRepository.findById(id);
        if (!metadataOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource with id " + id + " does not exist");
        }

        MediaMetadata mediaMetadata = metadataOptional.get();
        if(!file) {
            return mediaMetadata;
        }

        //Get the file.
        File associatedFile = new File(mediaMetadata.getServerFilePath());
        if(!associatedFile.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media file for id " + id + " is no longer available");
        }

        response.setHeader("Content-Type", mediaMetadata.getMediaType());

        // TODO: Maybe there is a special need to handle browser requests (to be verified)
        response.setHeader("Content-Disposition", "attachment; filename=\"" + associatedFile.getName() + "\"");

        Files.copy(associatedFile, response.getOutputStream());
        return null;
    }

    @GetMapping(produces = "application/json")
    @ApiOperation(value = "Retrieves a list of media metadata for a registered user represented by the passed phone number")
    public List<MediaMetadata> searchMediaMetadata(@RequestParam String phoneNumber, @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "50") Integer size) {
        // find registration.
        Registration registration = registrationQuery.getActiveRegistrationByPhoneNumber(phoneNumber);
        if(registration == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No registration for phone number " + phoneNumber + " found");
        }

        return metadataRepository.findAllByRegistration(registration, PageRequest.of(page, size));
    }
}
