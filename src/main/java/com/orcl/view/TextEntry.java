package com.orcl.view;

/**
 * Created by IntelliJ IDEA.
 * User: agaidai
 * Date: 2/1/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextEntry {
    protected long id;
    protected String name;
    protected String value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
