package diamond.cms.server.dao;

import org.jooq.DSLContext;

@FunctionalInterface
public interface Executor<E> {
    E execute(DSLContext context);
}
