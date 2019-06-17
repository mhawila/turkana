package org.muzima.turkana.service;

import org.muzima.turkana.data.UsageLogsRepository;
import org.muzima.turkana.model.UsageLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Samuel Owino
 */

@Service
public interface UsageLogsService {

    public void saveUsageLogs(UsageLogs usageLogs);

    public void saveAllLogs(List<UsageLogs> usageLogsList);

    public UsageLogs getUsageLogsEntry(Long id);

    public List<UsageLogs> getAllUsageLogs();

    public UsageLogs updateUsageLogs(UsageLogs usageLogs);

    public void deleteUsageLogs(Long id);
}
