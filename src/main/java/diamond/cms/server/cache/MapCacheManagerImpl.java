package diamond.cms.server.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MapCacheManagerImpl implements CacheManager{
    Logger log = LoggerFactory.getLogger(this.getClass());
    private final Map<String, Object> CACHE_MAP = new ConcurrentHashMap<>();
    private final Map<String, Long> CACHE_EXPIRE = new ConcurrentHashMap<>();

    private final Thread expireThread;

    public MapCacheManagerImpl() {
        expireThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    List<String> expireKey = new ArrayList<>();
                    CACHE_EXPIRE.keySet().forEach(key -> {
                        Long expireTime = CACHE_EXPIRE.get(key);
                        if (System.currentTimeMillis() >= expireTime) {
                            expireKey.add(key);
                        }
                    });
                    expireKey.forEach(k -> removeCache(k));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
        expireThread.start();
    }


    protected void removeCache(String key) {
        log.debug("remove cache:" + key);
        CACHE_MAP.remove(key);
        CACHE_EXPIRE.remove(key);
    }


    @Override
    public void set(String key, Object object, long expire) {
        CACHE_MAP.put(key, object);
        if (expire > 0) {
            setExpiress(key, expire);
        }
    }

    @Override
    public void set(String key, Object object) {
        this.set(key, object, 0);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        return get(key, type, 0);
    }

    @Override
    public Object get(String key, long expire) {
        return get(key, Object.class, expire);
    }

    @Override
    public Object get(String key) {
        return get(key, Object.class);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, Class<T> type, long changeExpire) {
        Object data = CACHE_MAP.get(key);
        if (data != null) {
            Long expire = CACHE_EXPIRE.get(key);
            if (expire != null && System.currentTimeMillis() >= expire) {  // expire data
                removeCache(key);
                return null;
            }
            if (changeExpire != 0) {
                setExpiress(key, changeExpire);
            }
            return (T) data;
        }
        return null;
    }


    private void setExpiress(String key, long changeExpire) {
        CACHE_EXPIRE.put(key, System.currentTimeMillis() + changeExpire);
    }


    @Override
    public void del(String key) {
        CACHE_MAP.remove(key);
        CACHE_EXPIRE.remove(key);
    }



    @Override
    public Optional<Object> getOptional(String key) {
        return Optional.ofNullable(this.get(key));
    }


    @Override
    public <T> Optional<T> getOptional(String key, Class<T> type) {
        return Optional.ofNullable(this.get(key, type));
    }

}
