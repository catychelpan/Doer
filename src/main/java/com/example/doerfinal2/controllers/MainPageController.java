package com.example.doerfinal2.controllers;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.EventModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    @FXML
    private ScrollPane todayEvents;
    @FXML
    private Button btn_logout;

    @FXML
    private Label user;

    private final MongodbUtil util = new MongodbUtil();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTodayEvents(LocalDate.now());
        btn_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                util.changeScene(event, "log-in.fxml", "Log-in",null);
                util.closeDBClient();
                //here close the client of database

            }
        });

    }

    public void displayTodayEvents(LocalDate now) {
        ObservableList<EventModel> todayList = util.fetchEventsOnDate(now);

        for(EventModel model : todayList){
            HBox eventPane = util.createEventPane(model);


            EventsViewController controller = new EventsViewController();
            eventPane.setOnMouseClicked(mouseEvent -> {
                controller.showDetails(model, "second dialog");
                refreshTodayView();

            });
            VBox events = (VBox) todayEvents.getContent();
            events.getChildren().add(eventPane);
        }

    }

    public void refreshTodayView() {
        VBox events = (VBox) todayEvents.getContent();
        events.getChildren().clear();
        displayTodayEvents(LocalDate.now());

    }

    public void setInfo(String username){
        user.setText(username);

    }

    public void goToEvents(ActionEvent actionEvent) {
        util.changeScene(actionEvent, "events.fxml", "Events",null);

    }

    public void goToBooks(ActionEvent actionEvent) {
        util.changeScene(actionEvent, "books.fxml", "Books",null);
    }

    public void goToJournal(ActionEvent actionEvent) {
        util.changeScene(actionEvent, "journal.fxml", "Journal",null);
    }

    public void goToHabits(ActionEvent actionEvent) {

        if(util.getHabitsForWeek(1).isEmpty()){
            util.changeScene(actionEvent, "habitsFirstVisit.fxml", "Habits",null);
        }else{
            util.changeScene(actionEvent, "habits.fxml", "Habits Tracker",null);
        }

    }
}
