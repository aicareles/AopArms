package cn.com.superLei.aoparms.common;

/**
 * description $desc$
 * created by jerry on 2019/6/4.
 */
public enum  ResourceType {
    JAR("jar"),
    FILE("file"),

    CLASS_FILE(".class");

    private String typeString;

    private ResourceType(String type) {
        this.typeString = type;
    }

    public String getTypeString() {
        return this.typeString;
    }
}
