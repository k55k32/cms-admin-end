package diamond.cms.server.dao;

import static org.jooq.impl.DSL.using;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.Condition;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.SelectSeekStepN;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.UniqueKey;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DSL;

import diamond.cms.server.core.PageResult;


public class JOOQGenericDao<T, ID extends Serializable> implements GenericDao<T, ID> {

    private Table<? extends Record> table = null;
    private Class<T> entityClass = null;
    private Configuration configuration = null;
    private Field<ID> primaryKey = null;

    public JOOQGenericDao(Class<T> entityClass, Schema schema, Configuration configuration) {
        this.entityClass = entityClass;
        this.configuration = configuration;
        initTable(schema);
        primaryKey = pk();
    }

    @Override
    public T insert(T entity) {
        record(entity, false, using(configuration)).store();
        return entity;
    }

    @Override
    public void insert(Collection<T> entities) {
        if (entities.isEmpty()) {
            return;
        }

        List<UpdatableRecord<?>> rs = records(entities, false);

        if (rs.size() > 1) {
            using(configuration).batchInsert(rs).execute();
            return;
        }

        rs.get(0).insert();
    }

    @Override
    public T update(T entity) {
        record(entity, true, using(configuration)).update();
        return entity;
    }

    @Override
    public void update(Collection<T> entities) {
        if (entities.isEmpty()) {
            return;
        }

        List<UpdatableRecord<?>> rs = records(entities, false);

        if (rs.size() > 1) {
            using(configuration).batchUpdate(rs).execute();
            return;
        }

        rs.get(0).update();
    }

    @Override
    public int delete(ID id) {
        return using(configuration).delete(table).where(primaryKey.equal(id)).execute();
    }

    @Override
    public void delete(Collection<ID> ids) {
        using(configuration).delete(table).where(primaryKey.in(ids)).execute();
    }

    @Override
    public void delete(Condition... conditions) {
        delete(Stream.of(conditions));
    }

    @Override
    public void deleteWithOptional(Stream<Optional<Condition>> conditions) {
        delete(conditions.filter(Optional::isPresent).map(Optional::get));
    }

    @Override
    public void delete(Stream<Condition> conditions) {
        Optional<Condition> o = conditions.reduce((acc, item) -> acc.and(item));
        Condition c = o.orElseThrow(() -> new IllegalArgumentException("At least one condition is needed to perform deletion"));
        using(configuration).delete(table).where(c).execute();
    }

    @Override
    public T get(ID id) {
        return getOptional(id).orElse(null);
    }

    @Override
    public Optional<T> getOptional(ID id) {
        Record record = using(configuration).select().from(table).where(primaryKey.eq(id)).fetchOne();
        return Optional.ofNullable(record).map(r -> r.into(entityClass));
    }

    @Override
    public List<T> get(Collection<ID> ids) {
        return using(configuration).select().from(table).where(primaryKey.in(ids)).fetch().into(entityClass);
    }

    @Override
    public int count(Condition... conditions) {
        return count(Stream.of(conditions));
    }

    @Override
    public int countWithOptional(Stream<Optional<Condition>> conditions) {
        return count(conditions.filter(Optional::isPresent).map(Optional::get));
    }

    @Override
    public int count(Stream<Condition> conditions) {
        Condition c = conditions.reduce((acc, item) -> acc.and(item)).orElse(DSL.trueCondition());
        return using(configuration).fetchCount(table, c);
    }

    @Override
    public List<T> fetch(Condition... conditions) {
        return fetch(Stream.of(conditions));
    }

    @Override
    public List<T> fetchWithOptional(Stream<Optional<Condition>> conditions, SortField<?>...sorts) {
        return fetch(conditions.filter(Optional::isPresent).map(Optional::get), sorts);
    }

    @Override
    public List<T> fetch(Stream<Condition> conditions, SortField<?>...sorts) {
        Condition c = conditions.reduce((acc, item) -> acc.and(item)).orElse(DSL.trueCondition());
        SelectSeekStepN<Record> step = using(configuration).select().from(table).where(c).orderBy(sorts);
        return step.fetchInto(entityClass);
    }

    @Override
    public PageResult<T> fetch(PageResult<T> page, Condition... conditions) {
        return fetch(page, Stream.of(conditions));
    }

    @Override
    public PageResult<T> fetchWithOptional(PageResult<T> page, Stream<Optional<Condition>> conditions, SortField<?>...sorts) {
        return fetch(page, conditions.filter(Optional::isPresent).map(Optional::get), sorts);
    }

    @Override
    public PageResult<T> fetch(PageResult<T> page, Stream<Condition> conditions, SortField<?>...sorts) {
        Condition c = conditions.reduce((acc, item) -> acc.and(item)).orElse(DSL.trueCondition());
        SelectSeekStepN<Record> step = using(configuration).select().from(table).where(c).orderBy(sorts);
        int firstResult = (page.getCurrentPage()- 1) * page.getPageSize();
        List<T> items = step.limit(firstResult, page.getPageSize()).fetch().into(entityClass);
        page.setData(items);
        page.setTotal(count(c));
        return page;
    }

    @Override
    public Optional<T> fetchOne(Condition... conditions) {
        return fetchOne(Stream.of(conditions));
    }

    @Override
    public Optional<T> fetchOneWithOptional(Stream<Optional<Condition>> conditions) {
        return fetchOne(conditions.filter(Optional::isPresent).map(Optional::get));
    }

    @Override
    public Optional<T> fetchOne(Stream<Condition> conditions) {
        List<T> list = fetch(conditions);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public <O> O execute(Executor<O> cb) {
        return cb.execute(using(configuration));
    }

    private void initTable(Schema schema) {
        Class<?> is = findInterface(entityClass).orElseThrow(() -> new RuntimeException("Entity class must implements one interface at least."));
        table = schema.getTables().stream()
                .filter(t -> is.isAssignableFrom(t.getRecordType())).findFirst()
                .orElseThrow(() -> new RuntimeException("Can't find a table for the entity."));
    }

    @SuppressWarnings("unchecked")
    private Field<ID> pk() {
        UniqueKey<?> uk = table.getPrimaryKey();
        Field<?>[] fs = uk.getFieldsArray();
        return (Field<ID>)fs[0];
    }

    private List<UpdatableRecord<?>> records(Collection<T> objects, boolean forUpdate) {
        DSLContext context = using(configuration);
        return objects.stream().map(obj -> record(obj, forUpdate, context)).collect(Collectors.toList());
    }

    private UpdatableRecord<?> record(T object, boolean forUpdate, DSLContext context) {
        UpdatableRecord<?> r = (UpdatableRecord<?>)context.newRecord(table, object);
        if (forUpdate) {
            r.changed(primaryKey, false);

        }

        int size = r.size();

        for (int i = 0; i < size; i++) {
            if (r.getValue(i) == null && r.field(i).getDataType().nullable()) {
                r.changed(i, false);
            }
        }
        return r;
    }




    private Optional<Class<?>> findInterface(Class<?> clazz){
        if(Object.class == clazz){
            return Optional.empty();
        }
        Class<?>[] is = clazz.getInterfaces();
        for(Class<?> c : is){
            if(c.getSimpleName().startsWith("I")){
                return Optional.of(c);
            }
        }
        return findInterface(clazz.getSuperclass());
    }
}
