package com.example.testing.ui;

public class profile {
    private String name;
    private String email;



    public profile(String name, String email, boolean permission) {
        this.name = name;
        this.email = email;
        this.permission = permission;
    }

    private boolean permission;

    public profile() {
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }


}
