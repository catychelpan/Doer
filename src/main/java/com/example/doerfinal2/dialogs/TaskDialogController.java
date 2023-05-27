package com.example.doerfinal2.dialogs;


import com.example.doerfinal2.models.TaskModel;
import com.jfoenix.controls.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskDialogController implements Initializable {
    @FXML
    public JFXTextField titleField;
    @FXML
    public  JFXButton standardEventButton;
    @FXML
    public  JFXButton importantEventButton;
    @FXML
    public  JFXButton urgentEventButton;
    @FXML
    public JFXTextField startTime_tf;
    @FXML
    public JFXTextField endTime_tf;
    @FXML
    public JFXTextArea description_ta;
    @FXML
    public DatePicker dateField;
    @FXML
    private JFXRadioButton morningPurpose;
    @FXML
    private JFXRadioButton forGoalsPurpose;
    @FXML
    private JFXRadioButton creativePurpose;
    @FXML
    private JFXRadioButton relaxPurpose;
    @FXML
    private  JFXCheckBox completed_checkBox;
    private IntegerProperty eventType;
    private String purpose;
    ToggleGroup purpose_toggleGroup = new ToggleGroup();
    public TaskModel eventModel;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPriorityListeners();
        morningPurpose.setUserData("Morning");
        morningPurpose.setToggleGroup(purpose_toggleGroup);
        forGoalsPurpose.setUserData("For Goals");
        forGoalsPurpose.setToggleGroup(purpose_toggleGroup);
        creativePurpose.setUserData("Creative");
        creativePurpose.setToggleGroup(purpose_toggleGroup);
        relaxPurpose.setUserData("Relax");
        relaxPurpose.setToggleGroup(purpose_toggleGroup);


        purpose_toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {


            if (purpose_toggleGroup.getSelectedToggle() != null) {


                purpose = purpose_toggleGroup.getSelectedToggle().getUserData().toString();

            }


        });

    }

    private void addPriorityListeners() {
        standardEventButton.setOnAction(event -> {
            clearSelected();
            eventType =  new SimpleIntegerProperty(TaskModel.STANDARD);
            standardEventButton.setStyle("-fx-background-color :  #8FC1D1 ; -fx-background-radius:15 ;");
            completed_checkBox.setSelected(false);
        });

        importantEventButton.setOnAction(event -> {
            clearSelected();
            eventType =  new SimpleIntegerProperty(TaskModel.IMPORTANT);
            importantEventButton.setStyle("-fx-background-color :   #8D9F87 ; -fx-background-radius:15 ;");
            completed_checkBox.setSelected(false);
        });

        urgentEventButton.setOnAction(event -> {
            clearSelected();
            eventType =  new SimpleIntegerProperty(TaskModel.URGENT);
           urgentEventButton.setStyle("-fx-background-color :   #E9A971 ; -fx-background-radius:15 ;");
            completed_checkBox.setSelected(false);
        });


        completed_checkBox.setOnAction(actionEvent -> {
            clearSelected();
            eventType = new SimpleIntegerProperty(0);
        });


    }

    public  void clearSelected() {
        eventType = new SimpleIntegerProperty(-1);
        standardEventButton
                .setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15; ");
        importantEventButton
                .setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15;");
        urgentEventButton
                .setStyle("-fx-background-color : #BDC6CC ; -fx-background-radius:15;");

    }

    public IntegerProperty getEventType() {
        return eventType;
    }

    public void setEventType(IntegerProperty eventType) {
        this.eventType = eventType;
    }

    public void initDataEvent(TaskModel event) {

        titleField.setText(event.getTitleString());
        description_ta.setText(event.getDescriptionString());
        dateField.setValue(event.getDate().get());
        startTime_tf.setText(event.getStartTimeString());
        endTime_tf.setText(event.getEndTimeString());

        if(event.getPriority().get() == TaskModel.STANDARD){
            standardEventButton.fire();
        } else if (event.getPriority().get() == TaskModel.IMPORTANT) {
            importantEventButton.fire();
        } else if (event.getPriority().get() == TaskModel.URGENT) {
            urgentEventButton.fire();
        }else {
           clearSelected();
        }



        for(Toggle toggles : purpose_toggleGroup.getToggles()){
            if(toggles.getUserData().equals(event.getPurposeString())){
                toggles.setSelected(true);
                break;
            }
        }

    }


    public void setModel(TaskModel event) {
        eventModel = event;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public JFXRadioButton getRelaxPurpose() {
        return relaxPurpose;
    }

    public void setRelaxPurpose(JFXRadioButton relaxPurpose) {
        this.relaxPurpose = relaxPurpose;
    }

    public JFXRadioButton getCreativePurpose() {
        return creativePurpose;
    }

    public void setCreativePurpose(JFXRadioButton creativePurpose) {
        this.creativePurpose = creativePurpose;
    }

    public JFXRadioButton getForGoalsPurpose() {
        return forGoalsPurpose;
    }

    public void setForGoalsPurpose(JFXRadioButton forGoalsPurpose) {
        this.forGoalsPurpose = forGoalsPurpose;
    }

    public ToggleGroup getPurposeProperty_group() {
        return purpose_toggleGroup;
    }

    public JFXCheckBox getCompleted_checkBox() {
        return completed_checkBox;
    }

    public void setCompleted_checkBox(JFXCheckBox completed_checkBox) {
        this.completed_checkBox = completed_checkBox;
    }

    public JFXRadioButton getMorningPurpose() {
        return morningPurpose;
    }

    public void setMorningPurpose(JFXRadioButton morningPurpose) {
        this.morningPurpose = morningPurpose;
    }


}
