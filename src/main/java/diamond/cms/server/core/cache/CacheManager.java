package diamond.cms.server.core.cache;

import java.util.Optional;

public interface CacheManager {

    /**
     * set cache object
     * @param key cache_key
     * @param data cache_data
     * @param expire expire_time millisecond
     */
    void set(String key, Object data, long expire);

    void set(String key, Object data);

    <T>T get(String key, Class<T> type, long expire);

    <T>T get(String key, Class<T> type);

    Object get(String key, long expire);

    Object get(String key);

    Optional<Object> getOptional(String key);

    void del(String key);

    <T>Optional<T> getOptional(String key, Class<T> type);

}
