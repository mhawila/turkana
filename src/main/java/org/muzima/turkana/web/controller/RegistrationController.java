package org.muzima.turkana.web.controller;

import org.muzima.turkana.data.RegistrationRepository;
import org.muzima.turkana.model.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.muzima.turkana.web.controller.RegistrationController.BASE_PATH;

@RestController
@RequestMapping(BASE_PATH)
public class RegistrationController {
    public static final String BASE_PATH = "api/registration";

    @Autowired
    private RegistrationRepository regRepo;

    @GetMapping(path = { "", "/{phoneNumber}" }, produces = {"application/json"})
    public List<Registration> get(@PathVariable(required = false) final String phoneNumber) {
        List<Registration> l = new ArrayList<>();
        if("55".equals(phoneNumber)) {
            Registration r = new Registration();
            r.setPhoneNumber(phoneNumber);
            l.add(r);
        }
        return l;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Registration post(@RequestBody Registration registration) {
        if(registration == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration data not passed", null);
        }

        if(registration.getPhoneNumber() == null || registration.getDeviceId() == null || registration.getPublicKey() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phone number, public key and device ID must all be provided", null);
        }
        return regRepo.save(registration);
    }
}
