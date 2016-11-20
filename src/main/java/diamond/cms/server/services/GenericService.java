package diamond.cms.server.services;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import diamond.cms.server.core.PageResult;
import diamond.cms.server.dao.CommonDao;
import diamond.cms.server.dao.GenericDao;

public abstract class GenericService<E> {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CommonDao<E> dao;

    private GenericDao<E, Serializable> getDao() {
        return dao;
    }


    public E get(String id) {
        return getDao().get(id);
    }

    public E save(E entity) {
        try {
            Method method = entity.getClass().getMethod("setId", String.class);
            method.invoke(entity, UUID.randomUUID().toString());
        } catch (Exception e) {
        }
        return getDao().insert(entity);
    }

    public E update(E entity) {
        return getDao().update(entity);
    }

    public PageResult<E> page(PageResult<E> page) {
        return getDao().fetch(page);
    }

    public String delete(String id) {
        getDao().delete(id);
        return id;
    }

    protected Timestamp currentTime() {
        return new Timestamp(System.currentTimeMillis());
    }
}
