package com.example.doerfinal2.dialogs;


import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.TaskModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.LocalDate;


public class TaskDialog extends Dialog<TaskModel> implements CustomDialog {


    public static TaskModel eventModel;
    public static TaskDialogController controller = new TaskDialogController();
    private final MongodbUtil util =new MongodbUtil();
    public String mode;


    public TaskDialog(TaskModel newEvent, String mode) {
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
        return util.getContent("addTask.fxml", "event");

    }

    @Override
    public void getDialogUi() {

        Pane pane = getContent();
        getDialogPane().setContent(pane);
        controller.getCompleted_checkBox().setDisable(true);

        DialogPane dialogPane = getDialogPane();
        Button buttonOK = util.setDialogButtons(dialogPane);


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
                        showAlertDialog("Wrong time format.Should be(eg.22:00,9:00)");
                        return false;

                    }

                    //here should warn if event for the same date and start time already exists
                    ObservableList<TaskModel> existingEvents;
                    existingEvents = getEventsOnDate(controller.dateField.getValue());
                    for(TaskModel existingEvent : existingEvents){
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
    public ObservableList<TaskModel> getEventsOnDate(LocalDate date){
        return util.fetchEventsOnDate(date);

    }


    @Override
    public void setResultConverter() {
        javafx.util.Callback<ButtonType, TaskModel> bookResultConverter = buttonType -> {
            if(buttonType == ButtonType.OK){
                if(controller.getEventType().get() != TaskModel.COMPLETED ) {
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

    @Override
    public void setPropertyBindings() {
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

        if(eventModel.getPriority().get() == TaskModel.STANDARD){
            controller.standardEventButton.fire();
        } else if (eventModel.getPriority().get() == TaskModel.IMPORTANT) {
            controller.importantEventButton.fire();
        } else if (eventModel.getPriority().get() == TaskModel.URGENT) {
            controller.urgentEventButton.fire();
        }else {
            controller.clearSelected();
        }


        controller.getEventType().bindBidirectional(eventModel.getPriority());

    }



}