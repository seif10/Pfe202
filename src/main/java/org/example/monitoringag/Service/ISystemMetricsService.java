package org.example.monitoringag.Service;

import com.alibaba.fastjson.JSONObject;

public interface ISystemMetricsService {


    JSONObject fetchFullItemFromZabbix(String itemName);

    //same as the first action but return only value as double
    double fetchItemValueFromZabbix(String itemName);
}
