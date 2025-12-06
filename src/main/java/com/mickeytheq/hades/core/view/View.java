package com.mickeytheq.hades.core.view;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface View {
    String interfaceLanguageKey();
}
