package br.com.battlebits.battlecraft.status;

import br.com.battlebits.battlecraft.warp.Warp;

import java.util.HashMap;
import java.util.Map;

public abstract class WarpStatus {

    private Map<String, Object> values;

    public WarpStatus() {
        values = new HashMap<>();
    }

    protected <T> boolean containsValue(T value) {
        return values.containsValue(value);
    }

    protected boolean containsKey(String key) {
        return values.containsKey(key);
    }

    protected <T> void save(String key, T t) {
        values.put(key, t);
    }

    protected <T> T get(String key) {
        return (T) values.get(key);
    }
}
