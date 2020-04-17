package com.dqchen.database.framework;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dqchen.database.framework.bean.Photo;
import com.dqchen.database.framework.bean.User;
import com.dqchen.database.framework.sqlite.BaseDao;
import com.dqchen.database.framework.sqlite.BaseDaoFactory;
import com.dqchen.database.framework.sqlite.BaseDaoImpA;
import com.dqchen.database.framework.sqlite.IBaseDao;
import com.dqchen.database.framework.sub_sqlite.BaseDaoSubFactory;
import com.dqchen.database.framework.sub_sqlite.PhotoDao;
import com.dqchen.database.framework.sub_sqlite.UserDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
//        BaseDaoFactory baseDaoFactory = BaseDaoFactory.getInstance(this);
//        baseDao = baseDaoFactory.getBaseDao(User.class);

    }

    public void clickInsert(View view) {
//        User user = new User();
//        user.setId(123);
//        user.setName("dqchen");
//        user.setPassword("123123");
//        user.setStatus(1);
//        baseDao.insert(user);
//        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();

        IBaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(BaseDaoImpA.class,User.class);
        userDao.insert(new User(1,"adasdads","dasd",1));
        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();
    }

    public void clickUpdate(View view) {
        BaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User where = new User();
        where.setId(1);
        User entity = new User();
        entity.setName("dqchen");
        userDao.update(entity,where);
        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();
    }

    public void clickDelete(View view) {
        BaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User entity = new User();
        entity.setName("dqchen");
        userDao.delete(entity);
        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();
    }

    public void clickSelect(View view) {
        BaseDao<User> userDao = BaseDaoFactory.getInstance().getBaseDao(User.class);
        User entity = new User();
        List<User> userList = userDao.query(entity);
        for (User user : userList) {
            Log.i("dqchen-->","query-->"+user.toString());
        }
        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();
    }

    public void clickLogin(View view) {
        UserDao userDao = BaseDaoFactory.getInstance().getBaseDao(UserDao.class,User.class);
        User entity = new User();
        entity.setName("张三"+ i++);
        entity.setId(i);
        entity.setPassword("123456");
        userDao.insert(entity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clickSubInsert(View view) {
        Photo photo = new Photo();
        photo.setPath("data/data/my.jpg");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String date = simpleDateFormat.format(new Date());
        photo.setDate(date);
        PhotoDao photoDao = BaseDaoSubFactory.getInstance().getBaseDao(PhotoDao.class,Photo.class);
        photoDao.insert(photo);
        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();
    }

    public void write(View view) {
    }

    public void update(View view) {
    }

    public static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    /**
     * 验证读取sd卡的权限
     *
     * @param activity
     */
    public boolean verifyStoragePermissions(Activity activity) {
/*******below android 6.0*******/
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else {
            return true;
        }

    }


    /**
      * 请求权限回调
      *
      * @param requestCode
      * @param permissions
      * @param grantResults
      */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"授权成功",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"授权失败,请去设置打开权限",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
