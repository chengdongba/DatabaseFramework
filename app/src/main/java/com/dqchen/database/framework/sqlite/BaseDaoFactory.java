package com.dqchen.database.framework.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * 通过单例工厂让调用层获取数据库操作的实例
 * 创建数据库
 */
public class BaseDaoFactory {

    private static BaseDaoFactory ourInstance = null;

    //数据库路径
    private String sqliteDateBasePath;
    //数据库操作引用
    private SQLiteDatabase sqLiteDatabase;

    public static BaseDaoFactory getInstance(Context context) {
        if (ourInstance==null){
            synchronized (BaseDaoFactory.class){
                if (ourInstance==null){
                    ourInstance = new BaseDaoFactory(context);
                }
            }
        }
        return ourInstance;
    }

    private BaseDaoFactory(Context context) {
        //建议写入SD卡
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"update");
        if (!file.exists()){
            file.mkdirs();
        }
        sqliteDateBasePath = file.getAbsolutePath();
        sqliteDateBasePath = "data/data/com.dqchen.database.framework/user.db";
        Log.d("dqchen","path: "+sqliteDateBasePath);
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
}
