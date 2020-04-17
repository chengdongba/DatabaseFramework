package com.dqchen.database.framework.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 通过单例工厂让调用层获取数据库操作的实例
 * 创建数据库
 */
public class BaseDaoFactory {

    private static final BaseDaoFactory ourInstance = new BaseDaoFactory();

    //保留所有的dao层,实现单例
    protected Map<String,BaseDao> map = Collections.synchronizedMap(new HashMap<String, BaseDao>());

    //数据库路径
    private String sqliteDateBasePath;
    //数据库操作引用
    private SQLiteDatabase sqLiteDatabase;

    public static BaseDaoFactory getInstance(){
        return ourInstance;
    }

//    public static BaseDaoFactory getInstance(Context context) {
//        if (ourInstance==null){
//            synchronized (BaseDaoFactory.class){
//                if (ourInstance==null){
//                    ourInstance = new BaseDaoFactory(context);
//                }
//            }
//        }
//        return ourInstance;
//    }

    private BaseDaoFactory() {
        //建议写入SD卡
//        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"update");
        File file = new File(Environment.getExternalStorageDirectory(),"update");
        if (!file.exists()){
            file.mkdirs();
        }
//        sqliteDateBasePath = file.getAbsolutePath()+"/user.db";
        sqliteDateBasePath = "data/data/com.dqchen.database.framework/user.db";
        Log.d("dqchen-->","path: "+sqliteDateBasePath);
        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(sqliteDateBasePath,null);
    }

    public synchronized <T> BaseDao<T> getBaseDao(Class<T> entityClass){
        BaseDao<T> baseDao = null;
        try {
            baseDao = BaseDao.class.newInstance();
            baseDao.init(sqLiteDatabase,entityClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return baseDao;
    }

    public synchronized <T extends BaseDao<M>,M> T getBaseDao(Class<T> daoClass,Class<M> entityClass){
        BaseDao baseDao = null;
        if (map.get(daoClass.getSimpleName())!=null){
            return (T) map.get(daoClass.getSimpleName());
        }
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(sqLiteDatabase,entityClass);
            map.put(daoClass.getSimpleName(),baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }
}
