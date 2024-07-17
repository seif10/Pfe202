package org.example.monitoringag.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.ZabbixApi;
import org.example.monitoringag.Repository.SystmMetricsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@Service
public class SystemMetricsService implements ISystemMetricsService{
    @Autowired
    private SystmMetricsRepository systmMetricsRepository;

    private ZabbixApi zabbixApi;

    public SystemMetricsService (){
        this.zabbixApi = new DefaultZabbixApi("http://192.168.33.10/zabbix/api_jsonrpc.php");
        this.zabbixApi.init();
    }

    //this function return full response as JsonObject
    @Override
    public JSONObject fetchFullItemFromZabbix(String itemName) {
        String[] output = {"value_type", "lastvalue"};
        String host = "ubuntu-bionic";
        String method="item.get";

        JSONObject filter=new JSONObject();
        filter.put("name",itemName);
        try{
            //implementation de la requete
            Request request = new Request();

            request.setMethod(method);
            request.setId(1);
            request.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

            request.putParam("host",host);
            request.putParam("filter", filter);
            request.putParam("output",output);

            //sending request
            return zabbixApi.call(request);

        }catch(Exception e){
            LOGGER.error( e.getMessage());
        }
        return null;
    }


    // this function return only the value as double
    @Override
    public double fetchItemValueFromZabbix(String itemName) {
        String[] output = {"value_type", "lastvalue"};
        String host = "ubuntu-bionic";
        String method = "item.get";

        JSONObject filter = new JSONObject();
        filter.put("name", itemName);
        try {
            // implementation of the request
            Request request = new Request();

            request.setMethod(method);
            request.setId(1);
            request.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

            request.putParam("host", host);
            request.putParam("filter", filter);
            request.putParam("output", output);

            // sending request
            JSONObject response = zabbixApi.call(request);

            // Extracting the lastvalue from the response
            if (response != null){
                JSONArray resultArray = response.getJSONArray("result");
                if (resultArray.size() > 0) {
                    JSONObject resultObject = resultArray.getJSONObject(0);
                    String lastvalueStr = resultObject.getString("lastvalue");

                    // Converting the lastvalue to double
                    return Double.parseDouble(lastvalueStr);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return 0.0; // return a default value or throw an exception as needed
    }
}



