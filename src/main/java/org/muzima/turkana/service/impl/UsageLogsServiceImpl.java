package org.muzima.turkana.service.impl;

import org.muzima.turkana.data.UsageLogsRepository;
import org.muzima.turkana.model.UsageLogs;
import org.muzima.turkana.service.UsageLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public class UsageLogsServiceImpl implements UsageLogsService {

    @Autowired
    UsageLogsRepository usageLogsRepository;

    @Override
    public void saveUsageLogs(UsageLogs usageLogs) {
        usageLogsRepository.save(usageLogs);
    }

    @Override
    public void saveAllLogs(List<UsageLogs> usageLogsList) {
        usageLogsRepository.saveAll(usageLogsList);
    }

    @Override
    public UsageLogs getUsageLogsEntry(String uuid) {
        return usageLogsRepository.findById(uuid).orElseGet(UsageLogs::new);
    }

    @Override
    public List<UsageLogs> getAllUsageLogs() {
        return usageLogsRepository.findAll();
    }

    @Override
    public UsageLogs updateUsageLogs(UsageLogs usageLogs) {
        return usageLogsRepository.save(usageLogs);
    }

    @Override
    public void deleteUsageLogs(String uuid) {
        usageLogsRepository.deleteById(uuid);
    }
}
