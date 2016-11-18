package diamond.cms.server.services;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import diamond.cms.server.exceptions.AppException;
import diamond.cms.server.exceptions.Error;
import diamond.cms.server.model.User;
import diamond.cms.server.utils.PwdUtils;

@Service
public class UserService extends AbstractService<User, String>{

    public static final long EXPIRED_TIME = 1000 * 60 * 60 * 2;

    public String register(String username, String password) {
        User user =new User();
        user.setUsername(username);
        user.setPassword(PwdUtils.pwd(password));
        user.setCreateTime(new Date());
        user.setLastLoginTime(new Date());
        generateToken(user);
        this.save(user);
        return user.getToken();
    }

    public String login(String username,String password) {
        User user = (User) createQuery("From User where username = ? and password = ?")
        .setString(0, username)
        .setString(1, PwdUtils.pwd(password)).uniqueResult();
        if (user == null) {
            throw new AppException(Error.USERNAME_OR_PASSWORD_ERROR);
        }
        generateToken(user);
        user.setLastLoginTime(new Date());
        update(user);
        return user.getToken();
    }

    private void generateToken(User user) {
        user.setToken(UUID.randomUUID().toString());
        user.setExpired(System.currentTimeMillis() + EXPIRED_TIME);
    }

    public User getByToken(String token) {
        User user = (User) createQuery("From User where token = ? and expired > ?")
                .setString(0, token)
                .setLong(1, System.currentTimeMillis()).uniqueResult();
        return user;
    }
}
