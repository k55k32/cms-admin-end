package diamond.cms.server.services;

import static diamond.cms.server.model.jooq.Tables.C_USER;

import java.util.UUID;

import org.springframework.stereotype.Service;

import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.model.User;
import diamond.cms.server.utils.PwdUtils;

@Service
public class UserService extends GenericService<User>{

    public static final long EXPIRED_TIME = 1000 * 60 * 60 * 2;

    public String register(String username, String password) {
        User user =new User();
        user.setUsername(username);
        user.setPassword(PwdUtils.pwd(password));
        generateToken(user);
        this.save(user);
        return user.getToken();
    }

    public String login(String username,String password) {
        User user = dao
                .fetchOne(C_USER.USERNAME.eq(username).and(C_USER.PASSWORD.eq(PwdUtils.pwd(password))))
                .orElseThrow(() -> new AppException(diamond.cms.server.exceptions.Error.USERNAME_OR_PASSWORD_ERROR));
        generateToken(user);
        user.setLastlogintime(currentTime());
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
//

}
