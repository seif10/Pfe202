package org.example.monitoringag;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.hengyunabc.zabbix.api.ZabbixApi;
import net.sf.jasperreports.engine.JRException;
import org.example.monitoringag.Service.ILogService;
import org.example.monitoringag.Service.ISystemMetricsService;
import org.example.monitoringag.Service.ReportService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class monitoringTest {
    @Autowired
    private ILogService logMonitoringService;

    @Autowired
   private ISystemMetricsService systemMetricsService;

    @Mock
    private ZabbixApi zabbixApi;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFetchItemFromZabbix() {
        // Mocking the behavior of zabbixApi.call(request) to return a sample JSONObject
        JSONObject expectedResult = new JSONObject();
        when(zabbixApi.call(any())).thenReturn(expectedResult);

        // Calling the method to be tested
        JSONObject result = systemMetricsService.fetchItemFromZabbix("Linux: CPU utilization");
        JSONObject result2 = systemMetricsService.fetchItemFromZabbix("Linux: Memory utilization");

        Assertions.assertNotNull(result, "Result should not be null");

        System.out.println(result.toJSONString());
        System.out.println(result2.toJSONString());
    }

    /*@Test
    public void testFetchLogsFromZabbix() {
        String[] itemIds = {"46559"};
        // Mocking the behavior of zabbixApi.call(request) to return a sample JSONObject
        JSONObject expectedResult = new JSONObject();
        when(zabbixApi.call(any())).thenReturn(expectedResult);

        // Calling the method to be tested
        JSONObject result = logMonitoringService.fetchLogs(itemIds, 2);
        JSONArray result2 = logMonitoringService.fetchLogsValue(itemIds, 2);
        Assertions.assertNotNull(result, "Result should not be null");

        System.out.println(result);
        System.out.println(result2);
    }*/

    /*@Test
    public void testSaveLogs() {
        String[] itemIds = {"46559"};
        logMonitoringService.fetchAndSaveLogsValue(itemIds, 10);
    }*/

   /* @Test
    public void testGetLos(){
        List<Logs> listLogs;
        listLogs= logMonitoringService.showLogsFromDB();
        System.out.println(listLogs);
        for (Logs l : listLogs){
            System.out.println(l);
        }
    }*/

    /*@Test
    public void addItem(){
         logMonitoringService.addItem(
                "10607",
                "test",
                "log[/home/vagrant/ApplicationLogs/LifeCycleLog/123,error,,,skip]",
                7,
                2,
                "30s");
    }*/

    @Test
    public void addOrUpdateItem(){
         logMonitoringService.addOrUpdateItem(
                "10607",
                "test",
                "log[/home/vagrant/ApplicationLogs/LifeCycleLog/12,error,,,skip]",
                7,
                2,
                "30s");
    }

}
