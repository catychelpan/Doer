package com.example.doerfinal2.dialogs;


import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.EventModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.LocalDate;


public class EventDialog  extends Dialog<EventModel> {


    public static EventModel eventModel;
    public static eventDialogController controller = new eventDialogController();
    private final MongodbUtil util =new MongodbUtil();
    public String mode;


    public EventDialog(EventModel newEvent, String mode) {
        super();
        this.mode = mode;
        this.setTitle("Add event");
        eventModel = newEvent;
        getDialogUi();
        controller.initDataEvent(eventModel);
        //before it shows dialog so it stays as completed
        setPropertyBindings();
        setResultConverter();





    }

    public Pane getContent() {
        return util.getContent("addEvent.fxml","event");

    }

    private void getDialogUi() {

        Pane pane = getContent();
        getDialogPane().setContent(pane);
        controller.getCompleted_checkBox().setDisable(true);



        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button buttonOK = (Button) getDialogPane().lookupButton(ButtonType.OK);
        buttonOK.setStyle("-fx-background-color: #A0B5B1;-fx-font-size: 14.0;-fx-border-radius : 5 ;-fx-background-radius: 5");
        Button buttonCancel = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        buttonCancel.setStyle("-fx-background-color: #FFFFFF;-fx-border-color: #000000;-fx-font-size: 14.0;-fx-border-radius : 5 ;-fx-background-radius: 5");



        buttonOK.addEventFilter(javafx.event.ActionEvent.ACTION, new EventHandler<>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!validateDialog()) {
                    actionEvent.consume();
                }
            }

            private boolean validateDialog() {

                if (controller.titleField.getText().isEmpty() || controller.getEventType().get() == -1 || controller.getPurpose() == null || controller.dateField.getValue() == null
                        || controller.startTime_tf.getText().isEmpty() || controller.endTime_tf.getText().isEmpty()) {

                    showAlertDialog("Not all the fields were filled.");
                    return false;
                }else if(mode.equals("add")){
                    String pattern = "^((1[0-9]|2[0-3])|[1-9]):[0-5][0-9]$";
                    if(!controller.startTime_tf.getText().matches(pattern) || !controller.endTime_tf.getText().matches(pattern)){
                        showAlertDialog("Check your time input. It should be in right format(eg.22:00,9:00)");
                        return false;

                    }

                    //here should warn if event for the same date and start time already exists
                    ObservableList<EventModel> existingEvents;
                    existingEvents = getEventsOnDate(controller.dateField.getValue());
                    for(EventModel existingEvent : existingEvents){
                        if(existingEvent.getStartTimeString().equals(controller.startTime_tf.getText())){
                            showAlertDialog("You already have event on this date and starting time.");
                            return false;
                        }
                    }
                }

                return true;

            }
        });
    }

    public void showAlertDialog(String message){
        util.showAlert(message);
    }
    public ObservableList<EventModel> getEventsOnDate(LocalDate date){
        return util.fetchEventsOnDate(date);

    }


    private void setResultConverter() {
        javafx.util.Callback<ButtonType, EventModel> bookResultConverter = buttonType -> {
            if(buttonType == ButtonType.OK){
                if(controller.getEventType().get() != EventModel.COMPLETED ) {
                    eventModel.setPriority(controller.getEventType().get());
                }else{
                    eventModel.setPriority(0);
                    util.deleteEvent(eventModel);
                }
                return eventModel;
            }else{
                return null;
            }
        };
        setResultConverter(bookResultConverter);

    }

    private void setPropertyBindings() {
        //in brackets, we should have string property

        controller.titleField.textProperty().bindBidirectional(eventModel.getTitle());
        //not sure
        controller.dateField.valueProperty().bindBidirectional(eventModel.getDate());
        controller.endTime_tf.textProperty().bindBidirectional(eventModel.getEndTime());
        controller.startTime_tf.textProperty().bindBidirectional(eventModel.getStartTime());
        controller.description_ta.textProperty().bindBidirectional(eventModel.getDescription());

        bindPriority();

        StringProperty selectedValueProperty = new SimpleStringProperty();
        selectedValueProperty.bindBidirectional(eventModel.getPurpose());
        selectedValueProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (Toggle radioButton : controller.getPurposeProperty_group().getToggles()) {
                    if (radioButton.getUserData().toString().equals(newValue)) {
                        radioButton.setSelected(true);
                        break;
                    }
                }
            }
        });

        controller.getPurposeProperty_group().selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                selectedValueProperty.set(((RadioButton) newToggle).getText());
            }
        });


    }
    private void bindPriority() {

        if(eventModel.getPriority().get() == EventModel.STANDARD){
            controller.standardEventButton.fire();
        } else if (eventModel.getPriority().get() == EventModel.IMPORTANT) {
            controller.importantEventButton.fire();
        } else if (eventModel.getPriority().get() == EventModel.URGENT) {
            controller.urgentEventButton.fire();
        }else {
            controller.clearSelected();
        }




        controller.getEventType().bindBidirectional(eventModel.getPriority());

    }



}