package org.example.monitoringag.Controller;



import net.sf.jasperreports.engine.JRException;
import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Service.LogFileService;
import org.example.monitoringag.Service.LogsService;
import org.example.monitoringag.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class testController {

    @Autowired
    LogsService logsService ;

    @Autowired
    ReportService reportService;

    @Autowired
    LogFileService logFileService;

    @PostMapping("/add")
    public void fetchAndSaveLogsValue(@RequestBody Map<String, Object> requestBody) {
        String[] itemIds = (String[]) requestBody.get("itemIds");
        int limit = (int) requestBody.get("limit");


        logsService.fetchAndSaveLogsValueByIds(itemIds,limit);
    }


    @PostMapping("/adds")
    public void fetchAndSaveLogsValue2(@RequestBody Map<String, Object> requestBody) {
        // Extraction des données JSON du corps de la requête
        List<String> itemIds = (List<String>) requestBody.get("itemIds");
        int limit = (int) requestBody.get("limit");

        logsService.fetchAndSaveLogsValueByIds(itemIds.toArray(new String[0]), limit);
    }

    @GetMapping("report/{format}")
    public String logReport(@PathVariable String format) throws JRException, FileNotFoundException {
        return reportService.exportLogReport(format);
    }

    @GetMapping("/extract")
    public ParsedLog extractLog(@RequestBody String log) {
        return logsService.parseLog(log);
    }

    @GetMapping("/list")
    public List<String> getRemoteLogFileNames() {
        return logFileService.getRemoteLogFilesAndAddOrUpdateItems();
    }

}
