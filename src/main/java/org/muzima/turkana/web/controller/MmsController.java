package org.muzima.turkana.web.controller;

import io.swagger.annotations.Api;
import org.muzima.turkana.model.Mms;
import org.muzima.turkana.service.MmsService;
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

import static org.muzima.turkana.web.controller.MmsController.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
@Api(tags = "MMS", description = "GSM Multimedia")
public class MmsController {

    public static final String BASE_URL = "api/mms";

    @Autowired
    MmsService mmsService;

    @PostMapping(consumes = "application/json")
    public void saveMms(@RequestBody Mms mms){
        mmsService.saveMms(mms);
    }

    @PostMapping(path = "/collection",consumes = "application/json")
    public void saveMmsCollection(@RequestBody List<Mms> mmsList){
        mmsService.saveAllMms(mmsList);
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
