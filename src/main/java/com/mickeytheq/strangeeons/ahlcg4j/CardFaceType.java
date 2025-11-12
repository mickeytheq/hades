package com.mickeytheq.strangeeons.ahlcg4j;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CardFaceType {
    String typeCode();

    String interfaceLanguageKey();
}
