package diamond.cms.server.mvc;

public interface Const {
    int STATE_DELETE = 0;
    int STATE_NORMAL = 1;
    boolean ENABLE = true;
    boolean DISABLE = false;
    String KEY_CURRENT_USER = "KEY_CURRENT_USER";

    String CACHE_PREFIX_USER_TOKEN ="user_token_";
    String CACHE_PREFIX_TOKEN = "token_";
    
    String CACHE_PREFIX_TOKEN_GUEST_VERSION = "guest_token_guest_";
    String CACHE_PREFIX_GUEST_TOKEN = "guest_token_";
    long TOKEN_EXPIRE = 1000 * 60 * 60 * 2;
}
