package diamond.cms.server.services;

import java.io.IOException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.ImmutableMap;

import diamond.cms.server.config.Auth2Config;
import diamond.cms.server.core.cache.CacheManager;
import diamond.cms.server.core.exceptions.AppException;
import diamond.cms.server.core.exceptions.Error;
import diamond.cms.server.model.Guest;

@Service
public class GithubAuth2Service {

    Logger log  = LoggerFactory.getLogger(getClass());

    static String AUTH2_URL = "https://github.com/login/oauth/authorize?client_id=";

    static String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    static String USER_URL = "https://api.github.com/user";
    @Autowired
    Auth2Config config;

    @Resource
    CacheManager cacheManager;

    ObjectMapper om = new ObjectMapper();

    public String getToken(String code) throws JsonProcessingException, IOException {
        String body = HttpRequest.post(ACCESS_TOKEN_URL,
                ImmutableMap.of(
                        "client_id", config.getGithubClientId(),
                        "client_secret", config.getGithubClientSecret(),
                        "code", code
                        ),
                false).header("Accept", "application/json").body();
        JsonNode node = om.readTree(body);
        if (node.has("access_token")) {
            String token = node.get("access_token").asText();
            return token;
        } else {
            throw new AppException(Error.AUTH2_CODE_ERROR, body);
        }
    }

    public Guest getGuest(String code) throws JsonProcessingException, IOException {
        String token = getToken(code);
        String body = HttpRequest.get(USER_URL, ImmutableMap.of("access_token", token), true).body();
        JsonNode jsonNode = om.readTree(body);
        String email = jsonNode.get("email").asText(null);
        String nickname = jsonNode.get("login").asText(null);
        String avatar = jsonNode.get("avatar_url").asText(null);
        Guest guest = new Guest();
        guest.setEmail(email);
        guest.setNickname(nickname);
        guest.setAvatar(avatar);
        guest.setThirdData(body);
        return guest;

    }
}
