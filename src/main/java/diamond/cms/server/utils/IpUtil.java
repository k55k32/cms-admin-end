package diamond.cms.server.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.collect.ImmutableMap;

public class IpUtil {
    final static String token = "34579df219c0eadf6c9f02f610c8169b";  // don't ask me why I save the token in the code. good for you
    final static String api = "http://api.ip138.com/query/";
    public static String getLocation(String ip) throws JsonProcessingException, IOException {
        String result = HttpRequest.get(api, ImmutableMap.of("ip", ip, "datatype", "jsonp", "token", token), false).body();
        ObjectMapper om = new ObjectMapper();
        JsonNode node = om.readTree(result);
        ArrayNode data = (ArrayNode) node.get("data");
        return data.get(0).asText() + data.get(1).asText() + data.get(2).asText() + data.get(3).asText();
    }
}
