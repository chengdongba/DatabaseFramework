package com.dqchen.database.framework.bean;

import com.dqchen.database.framework.annotation.DbField;
import com.dqchen.database.framework.annotation.DbTable;

@DbTable("tb_user")
public class User {
    @DbField("_id")
    private int id;
    @DbField("name")
    private String name;
    @DbField("password")
    private String password;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }

}
