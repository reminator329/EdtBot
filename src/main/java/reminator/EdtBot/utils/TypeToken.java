package reminator.EdtBot.utils;

import java.lang.reflect.ParameterizedType;

public abstract class TypeToken<K> {
    public Class<K> getType() {
        return (Class<K>)((ParameterizedType)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]).getRawType();
    }
}