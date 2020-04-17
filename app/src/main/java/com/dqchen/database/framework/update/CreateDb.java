package com.dqchen.database.framework.update;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建数据库脚本
 */
public class CreateDb {
    //数据库表名
    private String name;
    //创建表的sql语句集合
    private List<String> sqlCreates;

    public CreateDb (Element ele){
        name = ele.getAttribute("name");
        {
            NodeList sqlList = ele.getElementsByTagName("sql_createTable");
            sqlCreates = new ArrayList<>();
            for (int i = 0; i < sqlList.getLength(); i++) {
                Node item = sqlList.item(i);
                String textContent = item.getTextContent();
                sqlCreates.add(textContent);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSqlCreates() {
        return sqlCreates;
    }

    public void setSqlCreates(List<String> sqlCreates) {
        this.sqlCreates = sqlCreates;
    }
}
