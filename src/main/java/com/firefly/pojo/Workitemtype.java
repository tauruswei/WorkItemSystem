package com.firefly.pojo;

import javax.persistence.*;

public class Workitemtype {
    private String type;

    @Column(name = "typeCode")
    private String typecode;

    /**
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return typeCode
     */
    public String getTypecode() {
        return typecode;
    }

    /**
     * @param typecode
     */
    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }
}