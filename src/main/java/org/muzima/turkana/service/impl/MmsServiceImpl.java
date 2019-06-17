package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.MmsRepository;
import org.muzima.turkana.model.Mms;
import org.muzima.turkana.service.MmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public class MmsServiceImpl implements MmsService {

    @Autowired
    MmsRepository mmsRepository;

    @Override
    public void saveMms(Mms mms) {
        mmsRepository.save(mms);
    }

    @Override
    public void saveAllMms(List<Mms> mmsList) {
        mmsRepository.saveAll(mmsList);
    }

    @Override
    public Mms getMms(String uuid) {
        return mmsRepository.findById(uuid).orElseGet(Mms::new);
    }

    @Override
    public List<Mms> getAllMms() {
        return mmsRepository.findAll();
    }

    @Override
    public void updateMms(Mms mms) {
        mmsRepository.save(mms);
    }

    @Override
    public void deleteMms(String uuid) {
        mmsRepository.deleteById(uuid);
    }
}
