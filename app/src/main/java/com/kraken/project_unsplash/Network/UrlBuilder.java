package com.kraken.project_unsplash.Network;

import android.support.annotation.Nullable;

import com.kraken.project_unsplash.Utils.Constants;

public class UrlBuilder {
    private static String url;

    /**
     * url to get all photos
     * @param count @Nullable int
     * @return url
     */
    public static String getAllPhotos(@Nullable Integer count, String param, Integer page) {
        url = Constants.getBaseUrl();
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
    public static String getCuratedPhotos(@Nullable Integer count, @Nullable String param, Integer page) {
        url = Constants.getBaseUrl();
        url += count == null ? "/photos/curated?per_page=50&page=" + page + "@order_by=\"" + param + "\"" :
                "/photos/curated?per_page=" + count + "&page=" + page + "@order_by=\"" + param + "\"";
        return url;
    }

    /**
     * url for one photo
     * @param id : ID of the photo
     * @return url
     */
    public static String getPhoto(String id) {
        url = Constants.getBaseUrl();
        url += "/photos/" + id;
        return url;
    }

    /**
     * url for featured collections
     * @param count : count of collections (default 10)
     * @return url
     */
    public static String getFeaturedCollections(@Nullable Integer count,
                                                @Nullable String param, Integer page) {
        url = Constants.getBaseUrl();
        url += count == null ? "/collections/featured&page=" + page + "@order_by=\"" + param + "\"" :
                "/collections/featured?per_page=" + count + "&page=" + page + "@order_by=\"" + param + "\"";
        return url;
    }

    /**
     * url for curated collections
     * @param count : count of collections (default 10)
     * @return url
     */
    public static String getCuratedCollections(@Nullable Integer count,
                                               @Nullable String param, Integer page) {
        url = Constants.getBaseUrl();
        url += count == null ? "/collections/curated&page=" + page + "@order_by=\"" + param + "\"" :
                "/collections/curated?per_page=" + count + "&page=" + page + "@order_by=\"" + param + "\"";
        return url;
    }

    /**
     * get a collection
     * @param id : id of collection
     * @return url
     */
    public static String getCollection(int id) {
        url = Constants.getBaseUrl();
        url += "/collections/" + id;
        return url;
    }

    /**
     * get photos in a collection
     * @param id : id of collection
     * @return url
     */
    public static String getCollectionPhotos(int id) {
        url = Constants.getBaseUrl();
        url += "/collections/" + id + "/photos";
        return url;
    }

    /**
     * search photos related to @param "key"
     * @param key : keyword
     * @return url
     */
    public static String searchPhoto(String key, Integer page) {
        url = Constants.getBaseUrl();
        url += "/search/photos?per_page=50&query=" + key + "\"&page=" + page;
        return url;
    }

    /**
     * search collections related to @param "key"
     * @param key : keyword
     * @return url
     */
    public static String searchCollections(String key, Integer page) {
        url = Constants.getBaseUrl();
        url += "/search/collections?per_page=25&query=" + key + "\"&page=" + page;
        return url;
    }

    /**
     * like <POST> / unlike <DELETE> a photo
     * @param id : id of photo
     * @return url
     */
    public static String likePhoto(String id) {
        url = Constants.getBaseUrl();
        url += "/photos/" + id + "/like";
        return url;
    }

    /**
     * get a random photo
     * @return url
     */
    public static String getRandomPhoto() {
        url = Constants.getBaseUrl();
        url += "/photos/random";
        return url;
    }

    /**
     * related collections
     * @param id : id source collection
     * @return url
     */
    public static String getRelatedCollections(String id) {
        url = Constants.getBaseUrl();
        url += "/collections/" + id + "/related";
        return url;
    }
}
