package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_USER;

import java.util.Comparator;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import diamond.cms.server.core.cache.CacheManager;
import diamond.cms.server.core.exceptions.AppException;
import diamond.cms.server.core.exceptions.Error;
import diamond.cms.server.model.User;
import diamond.cms.server.mvc.Const;

@Service
public class UserService extends GenericService<User>{

    @Autowired
    CacheManager cacheManager;

    public static final long EXPIRED_TIME = 1000 * 60 * 60 * 2;

    public String register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        this.save(user);
        return generateToken(user);
    }

    public String login(String username,String password) {
        User user = dao
                .fetchOne(C_USER.USERNAME.eq(username).and(C_USER.PASSWORD.eq(password)))
                .orElseThrow(() -> new AppException(diamond.cms.server.core.exceptions.Error.USERNAME_OR_PASSWORD_ERROR));
        user.setLastLoginTime(currentTime());
        this.update(user);
        return generateToken(user);
    }



    private String generateToken(User user) {
        String userKey = userCacheKey(user.getId());
        cacheManager.getOptional(userKey, String.class).ifPresent(tokenKey -> {
            cacheManager.del(tokenKey);
        });
        String token = UUID.randomUUID().toString();
        String tokenKey = tokenCacheKey(token);
        cacheManager.set(userKey, tokenKey);
        cacheManager.set(tokenKey, user, Const.TOKEN_EXPIRE);
        return token;
    }

    private String tokenCacheKey(String token) {
        return Const.CACHE_PREFIX_TOKEN + token;
    }
    private String userCacheKey(String id) {
        return Const.CACHE_PREFIX_USER_TOKEN + id;
    }

    public User getByToken(String token) {
        User user = cacheManager.get(tokenCacheKey(token), User.class, Const.TOKEN_EXPIRE);
        return user;
    }

    public void logout(String token) {
        User user = getByToken(token);
        cacheManager.del(tokenCacheKey(token));
        if (user != null) {
            cacheManager.del(userCacheKey(user.getId()));
        }
    }

    public User modify(String id, String password) {
        User user = dao.get(id);
        user.setPassword(password);
        dao.update(user);
        return user;
    }

    public boolean isInit() {
        return !dao.fetch().isEmpty();
    }

    public void checkoutInit() {
        if (isInit()) throw new AppException(Error.USER_INIT_API_REFUSE);
    }

    public User findAdmin() {
        User user = this.findAll().stream().min(new Comparator<User>() {
            @Override
            public int compare(User user1, User user2) {
                return user1.getCreateTime().compareTo(user2.getCreateTime());
            }

        }).orElse(null);
        return user;
    }

}
