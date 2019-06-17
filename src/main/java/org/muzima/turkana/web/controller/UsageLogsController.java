package org.muzima.turkana.web.controller;


import org.muzima.turkana.model.UsageLogs;
import org.muzima.turkana.service.UsageLogsService;
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

import static org.muzima.turkana.web.controller.UsageLogsController.BASE_URL;

/**
 * @author Samuel Owino
 */

@RestController
@RequestMapping(BASE_URL)
public class UsageLogsController {

    public static final String BASE_URL = "api/logs";

    @Autowired
    UsageLogsService usageLogsService;

    @PostMapping(consumes = "application/json")
    public void saveUsageLogs(@RequestBody UsageLogs usageLogs){
        usageLogsService.saveUsageLogs(usageLogs);
    }

    @PostMapping(consumes = "application/json")
    public void saveAllLogs(@RequestBody List<UsageLogs> usageLogsList){
        usageLogsService.saveAllLogs(usageLogsList);
    }

    @GetMapping(path = "/{uuid}",produces = "appplication/json")
    public UsageLogs getUsageLogsEntry(@PathVariable String uuid){
        return usageLogsService.getUsageLogsEntry(uuid);
    }

    @GetMapping(produces = "application/json")
    public List<UsageLogs> getAllUsageLogs(){
        return usageLogsService.getAllUsageLogs();
    }

    @PutMapping(consumes = "application/json")
    public UsageLogs updateUsageLogs(@RequestBody UsageLogs usageLogs){
        return usageLogsService.updateUsageLogs(usageLogs);
    }

    @DeleteMapping(path = "/{uuid}")
    public void deleteUsageLogs(String uuid){
        usageLogsService.deleteUsageLogs(uuid);
    }
}
