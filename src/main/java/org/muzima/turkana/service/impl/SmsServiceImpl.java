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
    public void saveSms(Sms sms) {
        smsRepository.save(sms);
    }

    @Override
    public void saveAllSms(List<Sms> smsList) {
        smsRepository.saveAll(smsList);
    }

    @Override
    public Sms getSms(String uuid) {
        Optional<Sms> smsOptional = smsRepository.findById(uuid);
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
    public void deleteSms(String uuid) {
        smsRepository.deleteById(uuid);
    }
}
