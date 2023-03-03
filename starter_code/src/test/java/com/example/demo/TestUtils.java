package com.example.demo;

import java.lang.reflect.Field;

public class TestUtils {
    public static void injectObject(Object target, String fieldName, Object toBeInjected){
        boolean wasPrivate = false;

        try {
            Field field = target.getClass().getDeclaredField(fieldName);

            if(!field.isAccessible()){
                field.setAccessible(true);
                wasPrivate = true;
            }
            field.set(target,toBeInjected);
            if(wasPrivate){
                wasPrivate = false;
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
