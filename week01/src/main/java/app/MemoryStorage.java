package app;

import java.util.HashMap;
import java.util.Map;

public class MemoryStorage<T> implements DataStorage<T> {
    Map<String, T> dataMap = new HashMap();

    @Override
    public String store(T data) {
        dataMap.put("" + data.hashCode(), data);
        return "" + data.hashCode();
    }

    @Override
    public T retrieve(String source) {
        return dataMap.get(source);
    }

}
