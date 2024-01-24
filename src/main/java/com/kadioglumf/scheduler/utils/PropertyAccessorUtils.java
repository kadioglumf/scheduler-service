package com.kadioglumf.scheduler.utils;

import java.util.function.Supplier;

public class PropertyAccessorUtils {

    private PropertyAccessorUtils() {
    }

    public static <T> T valueOrDefault(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        }
        catch (NullPointerException ex) {
            return defaultValue;
        }
    }
}
