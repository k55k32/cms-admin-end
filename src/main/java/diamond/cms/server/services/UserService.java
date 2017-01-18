package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_USER;

import java.util.UUID;

import org.springframework.stereotype.Service;

import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.Error;
import diamond.cms.server.model.User;

@Service
public class UserService extends GenericService<User>{

    public static final long EXPIRED_TIME = 1000 * 60 * 60 * 2;

    public String register(String username, String password) {
        User user = new User();
//        if (!dao.fetch().isEmpty()) {
//            throw new AppException(Error.SINGLE_USER_ERROR);
//        }
        user.setUsername(username);
        user.setPassword(password);
        generateToken(user);
        this.save(user);
        return user.getToken();
    }

    public String login(String username,String password) {
        User user = dao
                .fetchOne(C_USER.USERNAME.eq(username).and(C_USER.PASSWORD.eq(password)))
                .orElseThrow(() -> new AppException(diamond.cms.server.exceptions.Error.USERNAME_OR_PASSWORD_ERROR));
        generateToken(user);
        user.setLastLoginTime(currentTime());
        this.update(user);
        return user.getToken();
    }



    private void generateToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setExpired(System.currentTimeMillis() + EXPIRED_TIME);
    }

    public User getByToken(String token) {
        User user = dao.fetchOne(C_USER.TOKEN.eq(token).and(C_USER.EXPIRED.ge(System.currentTimeMillis()))).orElse(null);
        return user;
    }

    public void logout(String token) {
        dao.execute(e -> {
            return e.update(C_USER).set(C_USER.TOKEN, "")
            .where(C_USER.TOKEN.eq(token))
            .execute();
        });
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

}
