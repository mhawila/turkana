package org.muzima.turkana.web.controller;

import org.muzima.turkana.model.Sms;
import org.muzima.turkana.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.muzima.turkana.web.controller.SmsController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class SmsController {

    public static final String BASE_URL = "api/sms";

    @Autowired
    SmsService smsService;

    @PostMapping(consumes = "application/json")
    public void saveSms(@RequestBody Sms sms) {
        smsService.saveSms(sms);
    }

    @PostMapping(path = "/collection",consumes = "application/json")
    public void saveAllSms(@RequestBody List<Sms> smsList) {
        smsService.saveAllSms(smsList);
    }

    @GetMapping(path = "/{uuid}", produces = "application/json")
    public Sms getSms(@PathVariable(required = true) String uuid) {
        return smsService.getSms(uuid);
    }

    @GetMapping(produces = "application/json")
    public List<Sms> getAllSms() {
        return smsService.getAllSms();
    }

    @PutMapping(consumes = "application/json")
    public void updateSms(@RequestBody Sms sms) {
        smsService.updateSms(sms);
    }

    @DeleteMapping(path = "/{uuid}", consumes = "application/json")
    public void deleteSms(@PathVariable(required = true) String uuid) {
        smsService.deleteSms(uuid);
    }
}
