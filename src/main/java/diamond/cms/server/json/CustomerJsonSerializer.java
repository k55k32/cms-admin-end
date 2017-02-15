package diamond.cms.server.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * depend on alibaba.fastjson
 * @author Diamond
 */
public class CustomerJsonSerializer {

    FieldPropertyFilter fieldFilter = new FieldPropertyFilter();

    class FieldPropertyFilter implements PropertyFilter{
        Map<Class<?>, Set<String>> include = new HashMap<>();
        Map<Class<?>, Set<String>> filter = new HashMap<>();
        @Override
        public boolean apply(Object object, String name, Object value) {
            Set<String> includeFields = include.get(object.getClass());
            Set<String> filterFields = filter.get(object.getClass());
            if (includeFields != null && includeFields.contains(name)) {
                return true;
            } else if (filterFields != null && !filterFields.contains(name)) {
                return true;
            } else if (includeFields == null && filterFields == null) {
                return true;
            }
            return false;
        }

        public void include(Class<?> type, String [] fields) {
            addToMap(include, type, fields);
        }

        public void filter(Class<?> type, String [] fields) {
            addToMap(filter, type, fields);
        }

        private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
            Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
            fieldSet.addAll(Arrays.asList(fields));
            map.put(type, fieldSet);
        }
    }

    class JacksonFilter extends SimpleBeanPropertyFilter{
        @Override
        protected boolean include(BeanPropertyWriter writer) {
            writer.getType();
            return super.include(writer);
        }
    }

    /**
     * @param clazz target type
     * @param include include fields
     * @param filter filter fields
     */
    public void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null) return;
        if (include != null && include.length() > 0) {
            fieldFilter.include(clazz, include.split(","));
        } else if (filter !=null && filter.length() > 0) {
            fieldFilter.filter(clazz, filter.split(","));
        }
    }

    public String toJson(Object object){
        SerializeWriter sw = new SerializeWriter();
        JSONSerializer serializer = new JSONSerializer(sw);
        serializer.getPropertyFilters().add(fieldFilter);
        serializer.write(object);
        return sw.toString();
    }

    public void filter(JSON json) {
        this.filter(json.type(), json.include(), json.filter());
    }
}
