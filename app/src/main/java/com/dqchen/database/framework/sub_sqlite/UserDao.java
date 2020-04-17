package com.dqchen.database.framework.sub_sqlite;

import com.dqchen.database.framework.bean.User;
import com.dqchen.database.framework.sqlite.BaseDao;

import java.util.List;

public class UserDao extends BaseDao<User> {

    @Override
    public long insert(User entity) {
        List<User> userList = query(new User());
        if (userList!=null){
            for (User user : userList) {
                User where = new User();
                where.setId(user.getId());
                user.setStatus(0);
                update(user,where);
            }
        }
        entity.setStatus(1);
        return super.insert(entity);
    }

    /**
     * @return 当前登入的用户
     */
    protected User getCurrentUser(){
        User currentUser = null;
        User where = new User();
        where.setStatus(1);
        List<User> userList = query(where);
        if (userList!=null && userList.size()!=0){
            currentUser = userList.get(0);
        }
        return currentUser;
    }
}
