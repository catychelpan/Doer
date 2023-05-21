package com.example.doerfinal2.controllers;

import com.example.doerfinal2.MongodbUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.net.URL;
import java.util.ResourceBundle;


public class LogInController implements Initializable  {

    private static Document user;
    @FXML
    private Button btn_login;
    @FXML
    private Button btn_signup;
    @FXML
    private TextField tf_username;
    @FXML
    private PasswordField tf_password;

    public static MongodbUtil util = new MongodbUtil();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btn_login.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                String username = tf_username.getText();
                String password = tf_password.getText();
                logInUser(username,password,event);


            }
        });
        btn_signup.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                util.changeScene(event, "sign-up.fxml","Sign Up", null);

            }
        });

    }

    public static void logInUser(String username, String password , ActionEvent event){
        //user from a database with the same username
        user = util.findUserByUsername(username);


        if(user == null){
            util.showAlert("Wrong username.Try again");
        }else{

            //here should decrypt password received as a hash to be a plain String
            String encryptedPassword = user.getString("password");
            String salt = user.getString("salt");
            boolean verified = util.verifyPassword(password,encryptedPassword,salt);



            if(verified){
                SignUpController.user_id =  user.get("_id", ObjectId.class);
                SignUpController.Username = username;
                util.changeScene(event, "mainPage.fxml","Main Page",username);
            }else {
               util.showAlert("Wrong password.Try again");
            }
        }



    }



}
