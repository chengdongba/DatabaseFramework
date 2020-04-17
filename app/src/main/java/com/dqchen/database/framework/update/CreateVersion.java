package com.dqchen.database.framework.update;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库升级创建表脚本
 */
public class CreateVersion {
    private String version;
    private List<CreateDb> createDbs;

    public CreateVersion(Element ele){
        version = ele.getAttribute("version");
        NodeList elements = ele.getElementsByTagName("createDb");
        createDbs = new ArrayList<>();
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            CreateDb createDb = new CreateDb(element);
            createDbs.add(createDb);
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<CreateDb> getCreateDbs() {
        return createDbs;
    }

    public void setCreateDbs(List<CreateDb> createDbs) {
        this.createDbs = createDbs;
    }
}
