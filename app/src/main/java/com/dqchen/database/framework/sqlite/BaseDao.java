package com.dqchen.database.framework.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dqchen.database.framework.annotation.DbField;
import com.dqchen.database.framework.annotation.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     *
     * @param sqLiteDatabase 数据库操作的引用
     * @param entity         数据库操作的数据类型
     * @return 返回是否初始化成功
     */
    protected boolean init(SQLiteDatabase sqLiteDatabase, Class<T> entity) {
        this.sqLiteDatabase = sqLiteDatabase;
        this.entity = entity;
        if (!isInit) {

            if (entity == null || sqLiteDatabase == null) {
                return false;
            }

            //自动建表 得到表名
            if (entity.getAnnotation(DbTable.class) != null) {
                tableName = entity.getAnnotation(DbTable.class).value();
            } else {
                tableName = entity.getSimpleName();
            }
            String creatTableSql = getCreateTableSql();
            sqLiteDatabase.execSQL(creatTableSql);
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    /**
     * 缓存数据库的字段名和对应的field
     */
    private void initCacheMap() {
        cacheMap = new HashMap<>();
        //查询字段名
        String sql = "select * from " + tableName + " limit 1,0";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        Field[] declaredFields = entity.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
        }
        for (String columnName : columnNames) {
            Field columnField = null;
            for (Field declaredField : declaredFields) {
                DbField annotation = declaredField.getAnnotation(DbField.class);
                String fieldName;
                if (annotation != null) {
                    fieldName = annotation.value();
                } else {
                    fieldName = declaredField.getName();
                }
                if (columnName.equals(fieldName)) {
                    columnField = declaredField;
                }
            }
            if (columnField != null) {
                cacheMap.put(columnName, columnField);
            }
        }
    }

    private String getCreateTableSql() {
        //create table if not exists tb_user(_id integer,name varchar(20),password varchar(20),status integer)
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + "(");
        //反射得到数据库操作类的属性
        Field[] fields = entity.getDeclaredFields();
        for (Field field : fields) {
            String fieldName, filedType;
            if (field.getAnnotation(DbField.class) != null) {
                fieldName = field.getAnnotation(DbField.class).value();
            } else {
                fieldName = field.getName();
            }

            if (field.getType() == Integer.class) {
                filedType = " INTEGER,";
            } else if (field.getType() == String.class) {
                filedType = " TEXT,";
            } else if (field.getType() == Long.class) {
                filedType = " BIGINT,";
            } else if (field.getType() == Double.class) {
                filedType = " DOUBLE,";
            } else if (field.getType() == Byte[].class) {
                filedType = " BLOB,";
            } else {
                //不支持的类型
                continue;
            }
            stringBuffer.append(fieldName);
            stringBuffer.append(filedType);
        }
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        stringBuffer.append(")");
        return stringBuffer.toString();
    }

    @Override
    public long insert(T entity) {
        Map<String, String> map = getValues(entity);
        //ContentValues 存放entity的属性名和属性值
        ContentValues values = getContentValues(map);
        long insert = sqLiteDatabase.insert(tableName, null, values);
        return insert;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            String value = map.get(name);
            if (value != null) {
                contentValues.put(name, value);
            }
        }
        return contentValues;
    }

    /**
     * @param entity 数据库操作的类
     * @return map(key - - 属性名, value - - 属性值)
     */
    private Map<String, String> getValues(T entity) {
        Map<String, String> map = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                if (value == null) {
                    continue;
                }
                String key = null;
                if (field.getAnnotation(DbField.class) != null) {
                    key = field.getAnnotation(DbField.class).value();
                } else {
                    key = field.getName();
                }
                if (key != null && value != null) {
                    map.put(key, value.toString());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public int delete(T entity) {
//        sqLiteDatabase.delete(tableName,"name=? && password=?",new String[]{"张三","123123"});
        Map<String, String> values = getValues(entity);
        Condition condition = new Condition(values);
        int result = sqLiteDatabase.delete(tableName, condition.whereCaluse, condition.whereArgs);
        return result;
    }

    @Override
    public int update(T entity, T where) {
        Map<String, String> values = getValues(entity);
        ContentValues contentValues = getContentValues(values);

        Map<String,String> whereCause = getValues(where);
        Condition condition = new Condition(whereCause);
        int update = sqLiteDatabase.update(tableName, contentValues, condition.whereCaluse, condition.whereArgs);
        return update;
    }

    @Override
    public List<T> query(T where) {
        return query(where,null,null,null);
    }

    @Override
    public List<T> query(T where, String orderBy, Integer startIndex, Integer limit) {
        String limitString = null;
        if (startIndex!=null && limit!=null){
            limitString = startIndex + " , " + limit;
        }

        Map<String,String> whereCause = getValues(where);
        Condition condition = new Condition(whereCause);
        Cursor cursor = sqLiteDatabase.query(tableName, null, condition.whereCaluse, condition.whereArgs, null, null, orderBy, limitString);
        List<T> list = getResult(cursor,where);
        cursor.close();
        return list;
    }

    /**
     * 将查询出来的额cursor转化为list
     * @param cursor 查询数据库得到cursor
     * @param where 数据库操作的数据类型
     * @return list
     */
    private List<T> getResult(Cursor cursor, T where) {
        ArrayList list = new ArrayList();
        Object item;
        while (cursor.moveToNext()){
            try {
                item = where.getClass().newInstance();
                Set<Map.Entry<String, Field>> entries = cacheMap.entrySet();
                Iterator<Map.Entry<String, Field>> iterator = entries.iterator();
                while (iterator.hasNext()){
                    Map.Entry<String, Field> entry = iterator.next();
                    String columnName = entry.getKey();
                    int columnIndex = cursor.getColumnIndex(columnName);
                    Field field = entry.getValue();
                    if (field!=null){
                        Class<?> type = field.getType();
                        if (type == String.class){
                           field.set(item,cursor.getString(columnIndex));
                        }else if (type == Integer.class){
                            field.set(item,cursor.getInt(columnIndex));
                        }else if (type == Double.class){
                            field.set(item,cursor.getDouble(columnIndex));
                        }else if (type == Long.class){
                            field.set(item,cursor.getLong(columnIndex));
                        }else if (type == Byte[].class){
                            field.set(item,cursor.getBlob(columnIndex));
                        }else {
                            //无法识别的类型
                            continue;
                        }
                    }
                }
                list.add(item);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private class Condition{
        private String whereCaluse;//"name=? && password=?..."
        private String[] whereArgs;//new String[]{"张三"}
        public Condition(Map<String,String> whereCause){
            ArrayList list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("1=1");
            Set<String> keySet = whereCause.keySet();
            Iterator<String> iterator = keySet.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                String value = whereCause.get(key);
                if (value!=null){
                    stringBuilder.append(" and "+key+"=?");
                    list.add(value);
                }
            }
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
            this.whereCaluse = stringBuilder.toString();
        }
    }
}
