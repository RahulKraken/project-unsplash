package com.kraken.project_unsplash.Utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

    private static final String ACCESS_KEY = "15dbbf8531d6a54d5325124fe9a0f21b897aa7b27b09c982d01cf3b5dd92256c";
    private static final String BASE_URL = "https://api.unsplash.com";

    private static List<String> orderByOptions = Arrays.asList("latest", "oldest", "popular");

    public static List<String> getOrderByOptions() {
        return orderByOptions;
    }

    public static String getAccessKey() {
        return ACCESS_KEY;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}
