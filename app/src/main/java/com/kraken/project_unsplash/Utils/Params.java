package com.kraken.project_unsplash.Utils;

import java.util.HashMap;
import java.util.Map;

public class Params {

    public static Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("Accept-Version", "v1");
        params.put("Authorization", "Client-ID " + Constants.ACCESS_KEY);
        return params;
    }
}
