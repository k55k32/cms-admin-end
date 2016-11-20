package diamond.cms.server.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Field;

public class Fields {

    public static Collection<Field<?>> all(Field<?> [] fields, Field<?> ... fields2){
        List<Field<?>> list = new ArrayList<>();
        list.addAll(Arrays.asList(fields));
        list.addAll(Arrays.asList(fields2));
        return list ;
    }
}
