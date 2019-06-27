package com.kraken.project_unsplash.Network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kraken.project_unsplash.Utils.Constants;

public class UrlBuilder {
    private static String url;

    /**
     * url to get all photos
     * @param count @Nullable int
     * @return url
     */
    public static String getAllPhotos(@Nullable Integer count, String param, Integer page) {
        url = Constants.BASE_URL;
        url += count == null ?
                "/photos?per_page=50&page=" + page + "@order_by=\"" + param + "\"" :
                "/photos?per_page=" + count + "&page=" + page + "&order_by=\"" + param + "\"";
        return url;
    }

    /**
     * url of curated photos
     * @param count @Nullable int
     * @return url
     */
    public static String getCuratedPhotos(@Nullable Integer count, @NonNull String param, Integer page) {
        url = Constants.BASE_URL;
        url += count == null ? "/photos/curated?per_page=50&page=" + page + "@order_by=\"" + param + "\"" :
                "/photos/curated?per_page=" + count + "&page=" + page + "&order_by=\"" + param +
                        "\"";
        return url;
    }

    /**
     * url for one photo
     * @param id : ID of the photo
     * @return url
     */
    public static String getPhoto(String id) {
        url = Constants.BASE_URL;
        url += "/photos/" + id;
        return url;
    }

    /**
     * url for featured collections
     * @param count : count of collections (default 10)
     * @return url
     */
    public static String getFeaturedCollections(@Nullable Integer count, Integer page) {
        url = Constants.BASE_URL;
        url += count == null ? "/collections/featured&page=" + page :
                "/collections/featured?per_page=" + count + "&page=" + page;
        return url;
    }

    /**
     * url for curated collections
     * @param count : count of collections (default 10)
     * @return url
     */
    public static String getCuratedCollections(@Nullable Integer count, Integer page) {
        url = Constants.BASE_URL;
        url += count == null ? "/collections/curated&page=" + page :
                "/collections/curated?per_page=" + count + "&page=" + page;
        return url;
    }

    /**
     * get a collection
     * @param id : id of collection
     * @return url
     */
    public static String getCollection(int id) {
        url = Constants.BASE_URL;
        url += "/collections/" + id;
        return url;
    }

    /**
     * get photos in a collection
     * @param id : id of collection
     * @return url
     */
    public static String getCollectionPhotos(@NonNull Integer id, @Nullable Integer count, Integer page) {
        url = Constants.BASE_URL;
        url += count == null ? "/collections/" + id + "/photos?per_page=50&page=" + page :
                "/collections/" + id + "/photos?per_page=" + count + "&page=" + page;
        return url;
    }

    /**
     * search photos related to @param "key"
     * @param key : keyword
     * @return url
     */
    public static String searchPhoto(String key, Integer page) {
        url = Constants.BASE_URL;
        url += "/search/photos?per_page=50&query=" + key + "\"&page=" + page;
        return url;
    }

    /**
     * search collections related to @param "key"
     * @param key : keyword
     * @return url
     */
    public static String searchCollections(String key, Integer page) {
        url = Constants.BASE_URL;
        url += "/search/collections?per_page=25&query=" + key + "\"&page=" + page;
        return url;
    }

    /**
     * like <POST> / unlike <DELETE> a photo
     * @param id : id of photo
     * @return url
     */
    public static String likePhoto(String id) {
        url = Constants.BASE_URL;
        url += "/photos/" + id + "/like";
        return url;
    }

    /**
     * get a random photo
     * @return url
     */
    public static String getRandomPhoto() {
        url = Constants.BASE_URL;
        url += "/photos/random";
        return url;
    }

    /**
     * related collections
     * @param id : id source collection
     * @return url
     */
    public static String getRelatedCollections(String id) {
        url = Constants.BASE_URL;
        url += "/collections/" + id + "/related";
        return url;
    }

    public static String getUser(String username) {
        url = Constants.BASE_URL;
        url += "/users/" + username;
        return url;
    }

    public static String getUserPhotos(@NonNull String username, @NonNull Integer page) {
        url = Constants.BASE_URL;
        url += "/users/" + username + "/photos?" + "page=" + page;
        return url;
    }

    public static String getUserLikes(@NonNull String username, @NonNull Integer page) {
        url = Constants.BASE_URL;
        url += "/users/" + username + "/likes?" + "page=" + page;
        return url;
    }

    public static String getUserCollections(@NonNull String username, @NonNull Integer page) {
        url = Constants.BASE_URL;
        url += "/users/" + username + "/collections?" + "page=" + page;
        return url;
    }

    public static String getCategoryUrl(@NonNull String category, @NonNull int page) {
        // /search/photos?query="football"&page=10
        url = Constants.BASE_URL;
        url += "/search/photos?query=\"" + category + "\"&page=" + page;
        return url;
    }

    public static String getProfile() {
        url = Constants.BASE_URL;
        url += "/me";
        return url;
    }
}
