package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.SmsRepository;
import org.muzima.turkana.model.Sms;
import org.muzima.turkana.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Samuel Owino
 */

@Service
public class SmsServiceImpl implements SmsService {

    @Autowired
    SmsRepository smsRepository;

    @Override
    public Sms saveSms(Sms sms) {
        return smsRepository.save(sms);
    }

    @Override
    public List<Sms> saveAllSms(List<Sms> smsList) {
        return smsRepository.saveAll(smsList);
    }

    @Override
    public Sms getSms(Long id) {
        Optional<Sms> smsOptional = smsRepository.findById(id);
        return smsOptional.orElseGet(Sms::new);
    }

    @Override
    public List<Sms> getAllSms() {
        return smsRepository.findAll();
    }

    @Override
    public void updateSms(Sms sms) {
        smsRepository.save(sms);
    }

    @Override
    public void deleteSms(Long id) {
        smsRepository.deleteById(id);
    }
}
