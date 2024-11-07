package com.teamobi.avatar2.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;

public class GsonProvider {

    private static final Gson gson = createGson();

    private static Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public static Gson getGson() {
        return GsonProvider.gson;
    }

}
