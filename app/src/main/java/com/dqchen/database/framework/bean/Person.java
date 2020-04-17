package com.dqchen.database.framework.bean;

import com.dqchen.database.framework.annotation.DbField;
import com.dqchen.database.framework.annotation.DbTable;

@DbTable("tb_person")
public class Person {
    @DbField("name")
    String name;
    @DbField("age")
    Integer age;
}
