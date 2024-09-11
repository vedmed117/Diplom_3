package org.praktikum.utils;

public class BaseURL {

    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    public static String getBaseUrl() {
        return BASE_URL;
    }
    public static String getBaseUrlWithSlash() {
        return BASE_URL.endsWith("/") ? BASE_URL : BASE_URL + "/";
    }
}
