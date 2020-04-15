package com.dqchen.database.framework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.dqchen.database.framework.bean.User;
import com.dqchen.database.framework.sqlite.BaseDao;
import com.dqchen.database.framework.sqlite.BaseDaoFactory;

public class MainActivity extends AppCompatActivity {

    private BaseDao<User> baseDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        BaseDaoFactory baseDaoFactory = BaseDaoFactory.getInstance(this);
        baseDao = baseDaoFactory.getBaseDao(User.class);

    }

    public void clickInsert(View view) {
        User user = new User();
        user.setId(123);
        user.setName("dqchen");
        user.setPassword("123123");
        user.setStatus(1);
        baseDao.insert(user);
        Toast.makeText(this,"执行成功",Toast.LENGTH_SHORT).show();
    }

    public void clickUpdate(View view) {
    }

    public void clickDelete(View view) {
    }

    public void clickSelect(View view) {
    }

    public void clickLogin(View view) {
    }

    public void clickSubInsert(View view) {
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
