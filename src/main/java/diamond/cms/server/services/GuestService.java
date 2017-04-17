package diamond.cms.server.services;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import diamond.cms.server.core.cache.CacheManager;
import diamond.cms.server.model.Guest;
import diamond.cms.server.model.jooq.Tables;
import diamond.cms.server.model.jooq.tables.CGuest;
import diamond.cms.server.mvc.Const;

@Service
public class GuestService extends GenericService<Guest>{
    
    @Autowired
    CacheManager cacheManager;
    
    @Autowired
    GithubAuth2Service githubAuth2Service;

    public String loginByGithub(String code) throws JsonProcessingException, IOException{
        Guest guest = githubAuth2Service.getGuest(code);
        guest = this.loginOrRegister(guest);
        return generateToken(guest);
    }

    public Guest getByToken(String token) {
        Guest guest = cacheManager.get(tokenCacheKey(token), Guest.class, Const.TOKEN_EXPIRE);
        return guest;
    }
    
    private String generateToken(Guest guest) {
        String userKey = guestCacheKey(guest.getId());
        cacheManager.getOptional(userKey, String.class).ifPresent(tokenKey -> {
            cacheManager.del(tokenKey);
        });
        String token = UUID.randomUUID().toString();
        String tokenKey = tokenCacheKey(token);
        cacheManager.set(userKey, tokenKey);
        cacheManager.set(tokenKey, guest, Const.TOKEN_EXPIRE);
        return token;
    }

    
    private String tokenCacheKey(String token) {
        return Const.CACHE_PREFIX_TOKEN_GUEST_VERSION + token;
    }
    private String guestCacheKey(String id) {
        return Const.CACHE_PREFIX_GUEST_TOKEN + id;
    }
    
    private Guest loginOrRegister(Guest guest) {
        Guest existsGuest = getByEmail(guest.getEmail());
        if (existsGuest == null) {
            guest.setCreateTime(currentTime());
            guest.setLoginTime(currentTime());
            save(guest);
        } else {
            existsGuest.setAvatar(guest.getAvatar());
            existsGuest.setLoginTime(currentTime());
            existsGuest.setNickname(guest.getNickname());
            existsGuest.setThirdData(guest.getThirdData());
            update(existsGuest);
            guest = existsGuest;
        }
        return guest;
    }
    
    public Guest getByEmail(String email) {
        CGuest GUEST_TABLE = Tables.C_GUEST;
        Guest guest = dao.execute(e -> {
            return e.select(GUEST_TABLE.fields()).from(GUEST_TABLE).fetchOptionalInto(Guest.class).orElse(null);
        });
        return guest;
    }
}
