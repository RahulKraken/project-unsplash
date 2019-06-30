package com.kraken.project_unsplash.Utils;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public class Constants {

    public final static String BASE_URL_AUTH = "https://unsplash.com/oauth";

    public final static String ACCESS_KEY = "15dbbf8531d6a54d5325124fe9a0f21b897aa7b27b09c982d01cf3b5dd92256c";
    public final static String SECRET_KEY = "0a20b1c09a09db21767dc2fc691dd2d3083c2b7b279eb460c78bc00e075b3309";
    public final static String RESPONSE_URL = "kraken://unsplash-auth-callback";
    public final static String SCOPE = "public+read_user+write_user+read_photos+write_photos+write_likes+write_followers+read_collections+write_collections";
    public final static String RESPONSE_URL_CALLBACK = "unsplash-auth-callback";

    public final static String BASE_URL = "https://api.unsplash.com";

    public static final List<String> ORDER_BY_OPTIONS = Arrays.asList("latest", "oldest", "popular");

    public static final int NUM_COLUMNS = 2;

    public static final String[] CATEGORIES = {"Wallpapers", "Textures & Patterns", "Nature", "Architecture", "Animals", "Travel", "Fashion", "Food & Drink", "Spiritual", "Experimental", "People", "Health", "Arts & Culture"};

    public static final List<String> THEME_OPTIONS = Arrays.asList("Light", "Dark", "AMOLED");
    public static final List<String> LAYOUT_OPTIONS = Arrays.asList("Grid", "Linear");
    public static final List<String> QUALITY_OPTIONS = Arrays.asList("Raw", "Full", "Regular", "Small");

    public static final String GITHUB_LINK = "https://github.com/RahulKraken/project-unsplash";
}
