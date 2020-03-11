package com.tinshine.tmall.pojo;

public class User {
    private Integer id;

    private String name;

    private String password;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getAnonymousName() {
        if(name == null)
            return null;
        if(name.length() == 1)
            return "*";
        StringBuilder ret = new StringBuilder();
        ret.append(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            ret.append("*");
        }
        return ret.toString();
    }
}