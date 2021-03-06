package com.chen.settings.domain;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:38
 */
public class DicType {
    private String code;
    private String name;
    private String description;

    @Override
    public String toString() {
        return "DicType{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public DicType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public DicType() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
