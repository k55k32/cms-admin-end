package diamond.cms.server.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateRenderUtil {

    /**
     * @see diamond.cms.server.utils.TemplateRenderUtil#render(String, Map)
     * @param temp
     * @param value
     * @return
     */
    public static String render(String temp, Object value) {
        Map<String, String> map = new HashMap<>();
                Arrays.asList(value.getClass().getMethods()).stream().filter(m -> {
            return m.getName().startsWith("get") && m.getParameterCount() == 0;
        }).forEach(method -> {
            String name = method.getName().substring(3);
            String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
            String stringResult = null;
            try {
                Object result = method.invoke(value);
                stringResult = (result == null ? null : result.toString());
            } catch (Exception e) {
            }
            map.put(fieldName, stringResult);
        });
        return render(temp, map);
    }

    /**
     * render template string
     * @param temp like "my name is {{name}}"
     * @param data <Map> {"name": "diamond"}
     * @return "my name is diamond
     */
    public static String render(String temp, Map<String, String> data) {
        Pattern pattern = Pattern.compile("\\{\\{[\\w]{0,}\\}\\}");
        Matcher m = pattern.matcher(temp);
        while (m.find()) {
            String mp = m.group();
            String key = mp.substring(2).substring(0, mp.length() - 4);
            temp = temp.replace(mp, data.get(key) == null ? "" : data.get(key));
        }
        return temp;
    }
}
