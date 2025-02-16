package com.duclan.local_cache.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;

@RequiredArgsConstructor
@Data
public class DataObject {
    private final String data;
    @NonFinal
    private static int counter = 0;

    public static DataObject get(String data) {
        counter++;
        return new DataObject(data);
    }
}
