package com.dqchen.database.framework.bean;

import com.dqchen.database.framework.annotation.DbField;
import com.dqchen.database.framework.annotation.DbTable;

@DbTable("tb_photo")
public class Photo {
    @DbField("path")
    private String path;
    @DbField("date")
    private String date;

    public Photo(String path, String date) {
        this.path = path;
        this.date = date;
    }

    public Photo() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "path='" + path + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
