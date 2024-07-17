package org.example.monitoringag.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;

import io.github.hengyunabc.zabbix.api.ZabbixApi;
import org.example.monitoringag.Entity.ParsedLog;
import org.example.monitoringag.Repository.ParserLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LogsService implements ILogService{

    private ZabbixApi zabbixApi;
    private static final String LOG_REGEX = "(\\d{2} [\\p{L}]+\\. \\d{4} \\d{2}:\\d{2}:\\d{2}) \\|(\\w+-\\[(?:\\w+(?:\\.\\w+)?(?: : \\d+)?|Thread-\\d+|[\\p{L}\\s]+ Thread)\\]):(\\w+):(\\w+):(\\w+)(?: \\[(.*)\\])?" ;

    @Autowired
    private ParserLogRepository parserLogRepository;

    public LogsService() {
        this.zabbixApi = new DefaultZabbixApi("http://192.168.33.10/zabbix/api_jsonrpc.php");
        this.zabbixApi.init();
    }

    public ParsedLog parseLog(String log) {

            Pattern pattern = Pattern.compile(LOG_REGEX);
            Matcher matcher = pattern.matcher(log);
            if (matcher.find()) {

                System.out.println("match successful");
                String date = matcher.group(1);
                String component = matcher.group(2) ;
                String module = matcher.group(3);
                String process = matcher.group(4);
                String action = matcher.group(5);
                String details = matcher.group(6) != null ? matcher.group(6) : "No details";

                return new ParsedLog(null, date, component, module, process, action, details);
            }
        return null;
    }

    public Map<String, String> mapItemNamesToIds(String[] itemNames) {
        Map<String, String> itemNameToIdMap = new HashMap<>();

        Request request = new Request();
        request.setMethod("item.get");
        request.setId(1);
        request.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

        // Ajouter les noms d'articles dans les paramètres de la requête
        JSONArray searchNames = new JSONArray();
        for (String name : itemNames) {
            searchNames.add(name);
        }
        JSONObject filter = new JSONObject();
        filter.put("name", searchNames);
        request.putParam("filter", filter);

        try {
            // Appel à l'API Zabbix
            JSONObject resultData = zabbixApi.call(request);

            if (resultData != null && resultData.containsKey("result")) {
                JSONArray resultList = resultData.getJSONArray("result");

                for (Object obj : resultList) {
                    if (obj instanceof JSONObject) {
                        JSONObject item = (JSONObject) obj;
                        String itemName = item.getString("name");
                        String itemId = item.getString("itemid");
                        itemNameToIdMap.put(itemName, itemId);
                    }
                }
            } else {
                System.out.println("Result data is null or does not contain 'result' key.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while fetching or processing items: " + e.getMessage());
        }

        return itemNameToIdMap;
    }

    //fct show parsed logs from db
    @Override
    public List<ParsedLog> showLogsFromDB(){
        return parserLogRepository.findAll();
    }

    //fct fetch by id app parsed logs values and save them in db
    @Override
    public void fetchAndSaveLogsValueByIds(String[] itemIds, int limit) {
        Request request = new Request();
        request.setMethod("history.get");
        request.setId(1);
        request.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

        request.putParam("output", "extend");
        request.putParam("history", "2");
        request.putParam("limit", limit);
        request.putParam("sortfield", "clock");
        request.putParam("sortorder", "DESC");
        request.putParam("itemids", itemIds);

        JSONObject resultData = zabbixApi.call(request);

        if (resultData != null && resultData.containsKey("result")) {
            JSONArray resultList = resultData.getJSONArray("result");

            for (Object obj : resultList) {
                if (obj instanceof JSONObject) {
                    JSONObject item = (JSONObject) obj;
                    String logValue = item.getString("value");

                    ParsedLog log = parseLog(logValue); // Parse log value
                    if (log != null) {
                        System.out.println(log);
                        parserLogRepository.save(log); // Save parsed log
                    } else {
                        System.out.println("Parsed log is null for log value: " + logValue);
                        // Handle the case where log parsing failed or returned null
                    }
                }
            }
        }
    }

    //fct fetch by name app parsed logs values and save them in db
    @Override
    public void fetchAndSaveLogsValueByNames(String[] itemNames, int limit) {
        // Get the mapping of item names to item IDs
        Map<String, String> itemNameToIdMap = mapItemNamesToIds(itemNames);

        // Collect all item IDs in a JSONArray
        JSONArray itemIds = new JSONArray();
        for (String itemName : itemNames) {
            String itemId = itemNameToIdMap.get(itemName);
            if (itemId != null) {
                itemIds.add(itemId);
            } else {
                System.out.println("No item ID found for item name: " + itemName);
            }
        }

        // Create a single request to fetch logs for all item IDs
        if (itemIds.size()>0) {
            Request request = new Request();
            request.setMethod("history.get");
            request.setId(1);
            request.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

            request.putParam("output", "extend");
            request.putParam("history", "2");
            request.putParam("limit", limit);
            request.putParam("sortfield", "clock");
            request.putParam("sortorder", "DESC");
            request.putParam("itemids", itemIds); // Use all collected item IDs

            JSONObject resultData = zabbixApi.call(request);

            if (resultData != null && resultData.containsKey("result")) {
                JSONArray resultList = resultData.getJSONArray("result");

                for (Object obj : resultList) {
                    if (obj instanceof JSONObject) {
                        JSONObject item = (JSONObject) obj;
                        String logValue = item.getString("value");

                        ParsedLog log = parseLog(logValue); // Parse log value
                        if (log != null) {
                            System.out.println(log);
                            parserLogRepository.save(log); // Save parsed log
                        } else {
                            System.out.println("Parsed log is null for log value: " + logValue);
                            // Handle the case where log parsing failed or returned null
                        }
                    }
                }
            } else {
                System.out.println("Result data is null or does not contain 'result' key.");
            }
        } else {
            System.out.println("No valid item IDs found.");
        }
    }

    //fct add zabbix item for each file ,update item if exist
    @Override
    public JSONObject addOrUpdateItem(String hostId, String name, String key, int type, int valueType, String delay) {

        // Find the item by key and hostId
        Request findRequest = new Request();
        findRequest.setMethod("item.get");
        findRequest.setId(1);
        findRequest.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

        findRequest.putParam("hostids", hostId);
        findRequest.putParam("search", Collections.singletonMap("key_", key));

        JSONObject findResponse = zabbixApi.call(findRequest);
        JSONArray result = findResponse.getJSONArray("result");

        System.out.println("Received response from Zabbix API (find item): " + findResponse);

        if (result == null || result.isEmpty()) {

            Request request = new Request();
            request.setMethod("item.create");
            request.setId(1);
            request.setAuth("192c278ef5e3b1c943d54493c0cf33e7");

            request.putParam("name", name);
            request.putParam("key_", key);
            request.putParam("hostid", hostId);
            request.putParam("type", type);
            request.putParam("value_type", valueType);
            request.putParam("delay", delay);

            System.out.println("Sending request to Zabbix API: {}" + request);
            JSONObject response = zabbixApi.call(request);
            System.out.println("Received response from Zabbix API: {}" + response);

            return response;
        }
        System.out.println("item already exist");
        return findResponse;
    }
}
