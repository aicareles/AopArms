package cn.com.superLei.aoparms.common.collection;


import java.util.HashMap;
import java.util.Map;

import cn.com.superLei.aoparms.common.utils.Preconditions;

/**
 * description $desc$
 * created by jerry on 2019/5/30.
 */
public class NoEmptyHashMap<K,V> extends HashMap<K, V> {

    private static NoEmptyHashMap noEmptyHashMap = new NoEmptyHashMap();
    public static <K,V>NoEmptyHashMap <K,V>getInstance() {
        return noEmptyHashMap;
    }

    private NoEmptyHashMap() {
        super();
    }

    @Override
    public V put(final K key, final V value) {
        if (Preconditions.isBlank(key) || Preconditions.isBlank(value)) {
            return null;
        }
        return super.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }
}
