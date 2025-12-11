package com.mickeytheq.hades.util;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
    // convenience to quiet the exception handling in normal code
    public static URL fromString(String str) {
        try {
            return new URL(str);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
