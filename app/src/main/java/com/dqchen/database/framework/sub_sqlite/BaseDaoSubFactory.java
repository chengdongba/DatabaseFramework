package com.dqchen.database.framework.sub_sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dqchen.database.framework.sqlite.BaseDao;
import com.dqchen.database.framework.sqlite.BaseDaoFactory;

public class BaseDaoSubFactory extends BaseDaoFactory {

    private static final BaseDaoSubFactory ourInstance = new BaseDaoSubFactory();

    public static BaseDaoSubFactory getInstance(){
        return ourInstance;
    }

    //用于实现分库
    SQLiteDatabase subSqliteDataBase;

    public synchronized <T extends BaseDao<M>,M> T getBaseDao(Class<T> daoClass, Class<M> entityClass){
        BaseDao baseDao = null;
        if (map.get(daoClass.getSimpleName())!=null){
            return (T) map.get(PrivateDataBaseEnums.database.getValue());
        }
        Log.i("dqchen-->","生成数据库文件"+PrivateDataBaseEnums.database.getValue());
        subSqliteDataBase = SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnums.database.getValue(),null);
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(subSqliteDataBase,entityClass);
            map.put(PrivateDataBaseEnums.database.getValue(),baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }



}
