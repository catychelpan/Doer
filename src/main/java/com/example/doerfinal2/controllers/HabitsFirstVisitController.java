package com.example.doerfinal2.controllers;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.HabitModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class HabitsFirstVisitController implements Initializable {
    @FXML
    private JFXTextField habit1;
    @FXML
    private JFXTextField habit2;
    @FXML
    private JFXTextField habit3;
    @FXML
    private JFXTextField habit4;
    @FXML
    private JFXTextField habit5;
    @FXML
    private JFXTextField habit6;
    @FXML
    private JFXTextField habit7;
    @FXML
    private JFXTextField habit8;
    @FXML
    private JFXButton ok_btn;

    private final MongodbUtil util = new MongodbUtil();
    @FXML
    private JFXTextField habit9;
    @FXML
    private JFXTextField habit10;


    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"mainPage.fxml","Home Page",null);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ok_btn.addEventFilter(javafx.event.ActionEvent.ACTION, new EventHandler<>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!validate()) {
                    actionEvent.consume();

                }else{
                    for(int i = 1; i < 5; i++) {
                        util.saveHabit(new HabitModel(habit1.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit2.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit3.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit4.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit5.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit6.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit7.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit8.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit9.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                        util.saveHabit(new HabitModel(habit10.getText(), i, false,false,
                                false,false,false,false,
                                false ,0.0));
                    }
                    util.changeScene(actionEvent,"habits.fxml","Habit Tracker",null);
                }
            }

            private boolean validate() {

                if (habit1.getText().isEmpty() || habit2.getText().isEmpty() || habit3.getText().isEmpty() || habit4.getText().isEmpty()
                        || habit5.getText().isEmpty() || habit6.getText().isEmpty() || habit7.getText().isEmpty() || habit8.getText().isEmpty()
                ||habit9.getText().isEmpty() || habit10.getText().isEmpty()) {
                    util.showAlert("Not all the fields were filled.");
                    return false;
                }

                return true;

            }
        });;
    }


}
