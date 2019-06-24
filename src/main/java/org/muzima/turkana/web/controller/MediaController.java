package org.muzima.turkana.web.controller;

import org.muzima.turkana.model.MediaMetadata;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 6/21/19.
 */
@RestController
@RequestMapping(MediaController.BASE_PATH)
public class MediaController {
    public static final String BASE_PATH = "api/media";

    @Autowired
    private MediaService mediaService;

    @PostMapping(consumes = {"multipart/form-data", "application/json"})
    public void post(@RequestPart(name = "metadata") MediaMetadata mediaMetadata, @RequestPart(name = "file") MultipartFile mediaFile,
                       @RequestAttribute Registration registration) throws IOException {
        mediaService.saveMedia(registration, mediaMetadata, mediaFile.getInputStream());
    }
}
