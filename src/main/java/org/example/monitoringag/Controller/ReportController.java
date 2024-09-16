package org.example.monitoringag.Controller;

import net.sf.jasperreports.engine.JRException;
import org.example.monitoringag.DTO.LogDateDTO;
import org.example.monitoringag.Entity.Report;
import org.example.monitoringag.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<String> exportReport(@RequestParam String threadOrUser, @RequestParam String reportName) {
        try {
            String result = reportService.exportLogReportParThreadOrUser(threadOrUser, reportName);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to generate report: " + e.getMessage());
        }
    }

    @GetMapping
    public List<Report> getAllReports() {
        return reportService.getAllReports();
    }

    @GetMapping("/{id}")
    public Report getReportById(@PathVariable long id) {
        return reportService.getReport(id);
    }

}
