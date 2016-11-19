package diamond.cms.server.dao;

import java.io.Serializable;

import org.jooq.Configuration;
import org.jooq.Schema;

public class CommonDao<E> extends JOOQGenericDao<E, Serializable>{
    public CommonDao(Class<E> entityClass, Schema schema, Configuration configuration) {
        super(entityClass, schema, configuration);
    }
}
