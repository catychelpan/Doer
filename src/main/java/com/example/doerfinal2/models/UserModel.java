package com.example.doerfinal2.models;
import com.example.doerfinal2.MongodbUtil;

public class UserModel {
    private String username;
    private String password;
    private MongodbUtil util = new MongodbUtil();

    private String confPassword;
    public UserModel(){
        this.username = null;
        this.password = null;
    }
    public UserModel(String username, String password){
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length() < 6){
            util.showAlert("Password should be at least 6 characters.");

        }else{
            this.password = password;
        }
    }
    public String getConfPassword() {return confPassword;}

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }
}
