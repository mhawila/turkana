package org.muzima.turkana.service;

import org.muzima.turkana.model.Mms;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public interface MmsService {

    public void saveMms(Mms mms);

    public void saveAllMms(List<Mms> mmsList);

    public Mms getMms(String uuid);

    public List<Mms> getAllMms();

    public void updateMms(Mms mms);

    public void deleteMms(String uuid);
}
