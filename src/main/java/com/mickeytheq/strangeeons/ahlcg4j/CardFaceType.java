package com.mickeytheq.strangeeons.ahlcg4j;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

// TODO: review this - name should probably just be @Model and interfaceLanguageKey doesn't belong here - should be with the view
@Retention(RetentionPolicy.RUNTIME)
public @interface CardFaceType {
    String typeCode();

    String interfaceLanguageKey();
}
