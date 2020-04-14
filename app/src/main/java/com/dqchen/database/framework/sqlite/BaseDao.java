package com.dqchen.database.framework.sqlite;

import android.database.sqlite.SQLiteDatabase;

import com.dqchen.database.framework.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDao<T> implements IBaseDao<T> {
    //持有操作数据库的引用
    private SQLiteDatabase sqLiteDatabase;
    //持有操作数据库的数据类型
    private Class<T> entity;
    //表名
    private String tableName;
    //标记位,标记数据库是否已经初始化
    private boolean isInit = false;
    //定义一个缓存空间(key--字段名,value--缓存空间)
    private Map<String, Field> cacheMap;

    /**
     * 自动建数据库和表 初始化
     * @param sqLiteDatabase 数据库操作的引用
     * @param entity 数据库操作的数据类型
     * @return 返回是否初始化成功
     */
    public boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entity) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entity = entity;
        if (!isInit){

         if (entity==null || sqLiteDatabase==null){
             return false;
         }

         //自动建表 得到表名
         if (entity.getAnnotation(DbTable.class)!=null){
             tableName = entity.getAnnotation(DbTable.class).value();
         }else {
             tableName = entity.getSimpleName();
         }
         String creatTableSql = getCreateTableSql();
         sqLiteDatabase.execSQL(creatTableSql);
         cacheMap = new HashMap<>();
         initCacheMap();
         isInit = true;
        }
        return isInit;
    }

    private void initCacheMap() {

    }

    private String getCreateTableSql() {
        //create table if not exists tb_user(_id integer,name varchar(20),password varchar(20),status integer)
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");

        return stringBuffer.toString();
    }

    @Override
    public long insert(Object entity) {
        return 0;
    }

    @Override
    public int delete(Object entity) {
        return 0;
    }

    @Override
    public int update(Object entity, Object where) {
        return 0;
    }

    @Override
    public List query(Object where) {
        return null;
    }

    @Override
    public List query(Object where, String orderBy, Integer startIndex, Integer limit) {
        return null;
    }
}
