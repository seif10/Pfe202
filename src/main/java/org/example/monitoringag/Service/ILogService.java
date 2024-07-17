package org.example.monitoringag.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.example.monitoringag.Entity.ParsedLog;

import java.util.List;
import java.util.Map;

public interface ILogService {

    void fetchAndSaveLogsValueByIds(String[] itemIds, int limit);

    void fetchAndSaveLogsValueByNames(String[] itemIds, int limit);

    List<ParsedLog> showLogsFromDB();

    JSONObject addOrUpdateItem(String hostId, String name, String key, int type, int valueType, String delay);
}
