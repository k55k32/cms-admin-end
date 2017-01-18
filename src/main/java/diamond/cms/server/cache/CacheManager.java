package diamond.cms.server.cache;

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
}
