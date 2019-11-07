package br.com.battlebits.battlecraft.backend.status;

import java.util.HashMap;
import java.util.Map;

public class WarpStatus {

    private Map<String, Object> values;

    public WarpStatus() {
        values = new HashMap<>();
    }

    public <T> boolean containsValue(T value) {
        return values.containsValue(value);
    }

    public boolean containsKey(String key) {
        return values.containsKey(key);
    }

    public <T> void save(String key, T t) {
        values.put(key, t);
    }

    public <T> T get(String key) {
        return (T) values.get(key);
    }
}
