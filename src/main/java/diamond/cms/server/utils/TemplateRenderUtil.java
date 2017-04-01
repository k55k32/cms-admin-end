package diamond.cms.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateRenderUtil {
    
    
    public static String renderResource(String resourcePath, Object value) throws IOException {
        InputStream input = TemplateRenderUtil.class.getResourceAsStream(resourcePath);
        return render(input, value);
    }
    
    /**
     * @see diamond.cms.server.utils.TemplateRenderUtil#render(String, Map)
     * @param file
     * @param value
     * @return
     * @throws IOException
     */
    public static String render(File file, Object value) throws IOException {
        return render(new FileInputStream(file), value);
    }
    
    /**
     * @see diamond.cms.server.utils.TemplateRenderUtil#render(String, Map)
     * @param in
     * @param value
     * @return
     * @throws IOException
     */
    public static String render(InputStream in, Object value) throws IOException {
        BufferedReader re = new BufferedReader(new InputStreamReader(in));
        String line = null;
        StringBuffer fileContent = new StringBuffer();
        while((line = re.readLine()) != null) {
            fileContent.append(line);
        }
        re.close();
        return render(fileContent.toString(), value);
    }

    /**
     * @see diamond.cms.server.utils.TemplateRenderUtil#render(String, Map)
     * @param temp
     * @param value
     * @return
     */
    public static String render(String temp, Object value) {
        if (value instanceof Map) {
            Map<?,?> map = (Map<?, ?>) value;
            Map<String,String> stringMap = new HashMap<>();
            map.entrySet().forEach(entry -> {
                String key = entry.getKey() == null ? null : entry.getKey().toString();
                String mapValue = entry.getValue() == null ? null : entry.getValue().toString();
                stringMap.put(key, mapValue);
            });
            return render(temp, stringMap);
        }
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
            String value = data.get(key);
            temp = temp.replace(mp, value == null ? "" : value);
        }
        return temp;
    }
}
