package diamond.cms.server.json;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

/**
 * depend on jackson
 * @author Diamond
 */
public class CustomerJsonSerializer {

    static final String DYNC_INCLUDE = "DYNC_INCLUDE";
    static final String DYNC_FILTER = "DYNC_FILTER";
    ObjectMapper mapper = new ObjectMapper();
    Map<Class<?>, Set<String>> include = new HashMap<>();
    Map<Class<?>, Set<String>> filter = new HashMap<>();

    @JsonFilter(DYNC_FILTER)
    interface DynamicFilter {
    }

    @JsonFilter(DYNC_INCLUDE)
    interface DynamicInclude {
    }

    class FieldPropertyFilter extends SimpleBeanPropertyFilter{
        public boolean apply(Class<?> type, String name) {
            Set<String> includeFields = include.get(type);
            if (includeFields == null) {
                includeFields = findDeepType(include, type);
            }
            Set<String> filterFields = filter.get(type);
            if (filterFields == null) {
                filterFields = findDeepType(filter, type);
            }
            if (includeFields != null && includeFields.contains(name)) {
                return true;
            } else if (filterFields != null && !filterFields.contains(name)) {
                return true;
            } else if (includeFields == null && filterFields == null) {
                return true;
            }
            return false;
        }

        private Set<String> findDeepType(Map<Class<?>, Set<String>> map, Class<?> type) {
            for (Entry<Class<?>, Set<String>> entry : map.entrySet()) {
                if (type.isAssignableFrom(entry.getKey())) {
                    return map.get(entry.getKey());
                }
            }
            return null;
        }

        @Override
        protected boolean include(BeanPropertyWriter writer) {
            Class<?> clazz = writer.getMember().getRawType();
            return apply(clazz, writer.getName());
        }

        @Override
        protected boolean include(PropertyWriter writer) {
            Class<?> clazz = writer.getMember().getDeclaringClass();
            return apply(clazz, writer.getName());
        }

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


    /**
     * @param clazz target type
     * @param include include fields
     * @param filter filter fields
     */
    public void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null) return;
        if (include != null && include.length() > 0) {
            mapper.setFilterProvider(new SimpleFilterProvider().addFilter(DYNC_INCLUDE,
                    new FieldPropertyFilter()));
            this.include(clazz, include.split(","));
            mapper.addMixIn(clazz, DynamicInclude.class);
        } else if (filter !=null && filter.length() > 0) {
            mapper.setFilterProvider(new SimpleFilterProvider().addFilter(DYNC_FILTER,
                    new FieldPropertyFilter()));
            this.filter(clazz, filter.split(","));
            mapper.addMixIn(clazz, DynamicFilter.class);
        }
    }

    public String toJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }
    public void filter(JSON json) {
        this.filter(json.type(), json.include(), json.filter());
    }
}
