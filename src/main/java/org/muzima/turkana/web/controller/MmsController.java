package org.muzima.turkana.web.controller;

import io.swagger.annotations.Api;
import org.muzima.turkana.model.Mms;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.model.Sms;
import org.muzima.turkana.service.MmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

import static org.muzima.turkana.web.controller.MmsController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Api(tags = "MMS", description = "GSM Multimedia")
public class MmsController {
    private static Logger LOGGER = Logger.getLogger(MmsController.class.getName());
    public static final String BASE_URL = "api/mms";

    @Autowired
    MmsService mmsService;

    @PostMapping(consumes = "application/json")
    public void saveMms(@RequestBody Mms mms, @RequestAttribute Registration registration, @RequestParam String signature){
        mmsService.saveMms(mms);
        byte[] binarySignature = Base64.getUrlDecoder().decode(signature);

        try {
            mmsService.verifyMmsAndSave(mms, binarySignature, registration);
        } catch (Exception e) {
            String errorMessage = "Signature could not be verified, data not backed up";
            LOGGER.warning(errorMessage);
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage, e);
        }
    }

    @PostMapping(path = "/collection",consumes = "application/json")
    public void saveAllMms(@RequestBody List<Mms> mmsList, @RequestAttribute Registration registration, @RequestParam String signature) {
        byte[] binarySignature = Base64.getUrlDecoder().decode(signature);

        try {
            mmsService.verifyMultipleMmsAndSave(mmsList, binarySignature, registration);
        } catch (Exception e) {
            String errorMessage = "Signature could not be verified, data not backed up";
            LOGGER.warning(errorMessage);
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage, e);
        }
    }

    @GetMapping(value = "/{id}",produces = "application/json")
    public Mms getMms(@PathVariable(required = true) Long id){
        return mmsService.getMms(id);
    }

    @GetMapping(produces = "application/json")
    public List<Mms> getAllMms(){
        return mmsService.getAllMms();
    }

    @PutMapping(consumes = "application/json")
    public void updateMms(@RequestBody Mms mms){
        mmsService.updateMms(mms);
    }

    @DeleteMapping("/{id}")
    public void deleteMms(Long id){
        mmsService.deleteMms(id);
    }
}
