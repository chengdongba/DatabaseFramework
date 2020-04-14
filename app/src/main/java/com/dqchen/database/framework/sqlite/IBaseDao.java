package com.dqchen.database.framework.sqlite;

import java.util.List;

public interface IBaseDao<T> {
    long insert(T entity);
    int delete(T entity);
    int update(T entity,T where);
    List<T> query(T where);
    List<T> query(T where,String orderBy,Integer startIndex,Integer limit);
}
