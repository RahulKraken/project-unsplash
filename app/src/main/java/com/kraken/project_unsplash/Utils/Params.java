package com.kraken.project_unsplash.Utils;

import android.content.Context;

import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;

import java.util.HashMap;
import java.util.Map;

public class Params {

  public static Map<String, String> getParams(Context context) {
    return MyApplication.AUTHENTICATED ? getAuthenticatedParams(context) : getGeneralParams();
  }

  public static Map<String, String> getAuthenticatedParams(Context context) {
    Map<String, String> params = new HashMap<>();
    params.put("Accept-Version", "v1");
    params.put("Authorization", "Bearer " + context.getSharedPreferences(context.getResources().getString(R.string.access_token_shared_preferences), Context.MODE_PRIVATE).getString(context.getResources().getString(R.string.access_token_storage_key), null));
    return params;
  }

  private static Map<String, String> getGeneralParams() {
    Map<String, String> params = new HashMap<>();
    params.put("Accept-Version", "v1");
    params.put("Authorization", "Client-ID " + Constants.ACCESS_KEY);
    return params;
  }
}
