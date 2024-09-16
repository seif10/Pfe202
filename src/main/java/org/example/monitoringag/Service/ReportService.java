package org.example.monitoringag.Service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Entity.Report;
import org.example.monitoringag.Repository.ParserLogRepository;
import org.example.monitoringag.Repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    ParserLogRepository parserLogRepository;

    @Autowired
    ReportRepository reportRepository;


    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReport(long id){
        return reportRepository.findById(id).orElse(null);
    }


    public String exportLogReport(String reportFormat) throws FileNotFoundException, JRException {

        String path = "C:\\Users\\seifb\\Desktop\\pfe\\JaspReports";
        List<ParsedLog> logs = parserLogRepository.findAll();

        //load file and compile it
        File file = ResourceUtils.getFile("classpath:reports/parsedLogReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(logs);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\logs(html).html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\logs(Pdf).pdf");
        }

        return "report generated in path : " + path;
    }

    public String exportLogReportParThreadOrUser(String threadOrUser,String reportName) throws FileNotFoundException, JRException {

        String path = "C:\\Users\\seifb\\Desktop\\pfe\\JaspReports";

        // Assuming you have a method to find logs by date as a string
        List<ParsedLog> logs = parserLogRepository.findByComponentContaining(threadOrUser);

        System.out.println("thread: " + threadOrUser);
        System.out.println("Number of logs found: " + logs.size());

        // Load file and compile it
        File file = ResourceUtils.getFile("classpath:reports/logReportParThread.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(logs);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        String reportPath = path + "\\logsParThreadOrUser(Pdf)"+reportName+".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath);

        //saving report to DB
        Report report = new Report();
        report.setTitle(reportName);
        report.setContent("Generated report for component: " + threadOrUser);
        report.setFilePath(reportPath);

        reportRepository.save(report);
        return "PDF report generated in path : " + path;

    }

    public String exportLogReportParDate(String dateString) throws FileNotFoundException, JRException {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.FRENCH);
        //String dateString = dateFormat.format(date);

        String path = "C:\\Users\\seifb\\Desktop\\pfe\\JaspReports";

        // Assuming you have a method to find logs by date as a string
        List<ParsedLog> logs = parserLogRepository.findByDate(dateString);

        // Load file and compile it
        File file = ResourceUtils.getFile("classpath:reports/parsedLogReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(logs);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\logsParDate(Pdf).pdf");
        return "PDF report generated in path : " + path;

    }
}
