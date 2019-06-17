package org.muzima.turkana.service;

import org.muzima.turkana.model.Sms;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public interface SmsService {

    public void saveSms(Sms sms);

    public void saveAllSms(List<Sms> smsList);

    public Sms getSms(String uuid);

    public List<Sms> getAllSms();

    public void updateSms(Sms sms);

    public void deleteSms(String uuid);


}
