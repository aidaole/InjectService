package com.aidaole.injectservice.runtime;

import java.util.HashMap;

public class InjectManager {
    private static final HashMap<Class<?>, Object> services = new HashMap<>();

    public static void init() {
        registerAll();
    }

    public static void destroy(){
        services.clear();
    }

    private static void registerAll(){}

    private static void register(Class<?> clazz, Object obj) {
        services.put(clazz, obj);
    }

    public static <T> T get(Class<T> clazz) {
        return (T) services.get(clazz);
    }
}
