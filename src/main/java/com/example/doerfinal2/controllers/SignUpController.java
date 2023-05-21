package com.example.doerfinal2.controllers;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public static ObjectId user_id;
    public static String Username;

    @FXML
    private Button btn_signup;
    @FXML
    private PasswordField tf_password;
    @FXML
    private Button btn_login;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_confPassword;
    private final MongodbUtil util = new MongodbUtil();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                if(!tf_password.getText().trim().isEmpty() && !tf_username.getText().trim().isEmpty()){
                    if( util.findUserByUsername(tf_username.getText()) == null) {
                        if (tf_password.getText().equals(tf_confPassword.getText())) {
                            UserModel user = new UserModel();
                            user.setUsername(tf_username.getText());
                            user.setPassword(tf_password.getText());

                            if(user.getPassword() != null){
                                user_id = util.saveUser(user);
                                Username = user.getUsername();
                                util.changeScene(event, "mainPage.fxml", "Home", user.getUsername());
                            }



                        } else {
                            util.showAlert("Passwords are not the same.");
                        }
                    }else{
                        util.showAlert("This username already exists.");
                    }

                }else{
                    util.showAlert("Fill all fields.");
                }


            }
        });
        btn_login.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                util.changeScene(event, "log-in.fxml","Log In",null);

            }
        });
    }


}
