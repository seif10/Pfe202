package org.example.monitoringag.Controller;

import net.sf.jasperreports.engine.JRException;
import org.example.monitoringag.DTO.LogDateDTO;
import org.example.monitoringag.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/report")
//@CrossOrigin(origins = "http://localhost:4200")
public class ReportController {
    @Autowired
    ReportService reportService;

    @PostMapping("export-par-date")
    public String exportLogReport( @RequestBody LogDateDTO date) throws JRException, FileNotFoundException, ParseException {

        System.out.println("date :: "+ date);
        if (date == null) {
            return "Error generating report: date must not be null";
        }
        return reportService.exportLogReportParDate(date.getTransformedDate());
    }

    @PostMapping("export-par-thread")
    public String exportLogReportParThread(@RequestBody String component) throws JRException, FileNotFoundException{
        if (component == null) {
            return "Error generating report: thread or user must not be null";
        }
        return reportService.exportLogReportParThreadOrUser(component);
    }
}
