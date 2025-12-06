package com.mickeytheq.hades.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LoggerUtils {
    public static String toLoggable(String message, Throwable t) {
        return message + ". " + toLoggable(t);
    }

    public static String toLoggable(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return t.getMessage() + " " + stringWriter;
    }
}
