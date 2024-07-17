package org.example.monitoringag.Controller;

import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Service.ILogService;
import org.example.monitoringag.Service.ISystemMetricsService;
import org.example.monitoringag.Service.LogFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    ILogService logService;

    @Autowired
    ISystemMetricsService systemMetricsService;

    @Autowired
    LogFileService logFileService;


    @GetMapping("/systemLog")
    public double fetchItem(@RequestParam String itemName) {
        return systemMetricsService.fetchItemValueFromZabbix(itemName);
    }

    @GetMapping("/getParsedLog")
    public List<ParsedLog> showParsedLog(){
        return logService.showLogsFromDB();
    }

    @PostMapping("/addLogs")
    public void fetchAndSaveLogsValueByNames(@RequestBody Map<String, Object> requestBody) {
        // Extraction des données JSON du corps de la requête
        List<String> itemIds = (List<String>) requestBody.get("itemNames");
        int limit = (int) requestBody.get("limit");

        logService.fetchAndSaveLogsValueByNames(itemIds.toArray(new String[0]), limit);
    }

    @GetMapping("/addOrUpdateItem")
    public List<String> getRemoteLogFilesAndAddOrUpdateZabbixItems() {
        return logFileService.getRemoteLogFilesAndAddOrUpdateItems();
    }

}
