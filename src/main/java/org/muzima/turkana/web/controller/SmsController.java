package org.muzima.turkana.web.controller;

import io.swagger.annotations.Api;
import org.muzima.turkana.model.Registration;
import org.muzima.turkana.model.Sms;
import org.muzima.turkana.service.SmsService;
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

import static org.muzima.turkana.web.controller.SmsController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Api(tags = "SMS", description = "Text Messages")
public class SmsController {
    private static Logger LOGGER = Logger.getLogger(SmsController.class.getName());
    public static final String BASE_URL = "api/sms";

    @Autowired
    SmsService smsService;

    @PostMapping(consumes = "application/json")
    public void saveSms(@RequestBody Sms sms, @RequestAttribute Registration registration, @RequestParam String signature) {
        //Decode signature.
        byte[] binarySignature = Base64.getUrlDecoder().decode(signature);

        try {
            smsService.verifySmsAndSave(sms, binarySignature, registration);
        } catch (Exception e) {
            String errorMessage = "Signature could not be verified, data not backed up";
            LOGGER.warning(errorMessage);
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage, e);
        }
    }

    @PostMapping(path = "/collection",consumes = "application/json")
    public void saveAllSms(@RequestBody List<Sms> smsList, @RequestAttribute Registration registration, @RequestParam String signature) {
        byte[] binarySignature = Base64.getUrlDecoder().decode(signature);

        try {
            smsService.verifyMultipleSmsAndSave(smsList, binarySignature, registration);
        } catch (Exception e) {
            String errorMessage = "Signature could not be verified, data not backed up";
            LOGGER.warning(errorMessage);
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage, e);
        }
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Sms getSms(@PathVariable(required = true) Long id) {
        return smsService.getSms(id);
    }

    @GetMapping(produces = "application/json")
    public List<Sms> getAllSms() {
        return smsService.getAllSms();
    }

    @PutMapping(consumes = "application/json")
    public void updateSms(@RequestBody Sms sms) {
        smsService.updateSms(sms);
    }

    @DeleteMapping(path = "/{id}", consumes = "application/json")
    public void deleteSms(@PathVariable(required = true) Long id) {
        smsService.deleteSms(id);
    }
}
