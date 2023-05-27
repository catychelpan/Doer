package com.example.doerfinal2.controllers;



import com.example.doerfinal2.dialogs.AllEventsDialog;
import com.example.doerfinal2.dialogs.TaskDialog;
import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.TaskModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import javafx.beans.property.BooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.ResourceBundle;

public class TasksViewController implements Initializable {
    @FXML
    private Label monthYear_label;
    @FXML
    private Label monday_label;
    @FXML
    private Label tuesday_label;
    @FXML
    private Label wednesday_label;
    @FXML
    private Label thursday_label;
    @FXML
    private Label friday_label;
    @FXML
    private Label saturday_label;
    @FXML
    private Label sunday_label;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private JFXCheckBox standard_checkBox;
    @FXML
    private JFXCheckBox important_checkBox;
    @FXML
    private JFXCheckBox urgent_checkBox;

    @FXML
    private JFXButton addEvent_btn;
    @FXML
    private HBox EventPane;
    private LocalDate selectedDate;


    private BooleanProperty standardFilterProperty;
    private BooleanProperty importantFilterProperty;
    private BooleanProperty urgentFilterProperty;



    private final MongodbUtil util = new MongodbUtil();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initLeftPane();



        refreshCalendar(LocalDate.now());
        selectedDate = LocalDate.now();


        

    }

    private void initLeftPane() {

        datePicker.setOnAction(event -> {
            selectedDate = datePicker.getValue();
            refreshCalendar(selectedDate);
        });

        standardFilterProperty = standard_checkBox.selectedProperty();
        importantFilterProperty = important_checkBox.selectedProperty();
        urgentFilterProperty = urgent_checkBox.selectedProperty();



        standard_checkBox.setStyle(
                "-jfx-checked-color : #8FC1D1; -jfx-unchecked-color : #FFFFFF ;");
        important_checkBox.setStyle(
                "-jfx-checked-color: #8D9F87; -jfx-unchecked-color : #FFFFFF ;");
        urgent_checkBox.setStyle(
                "-jfx-checked-color : #E9A971; -jfx-unchecked-color : #FFFFFF ;");



        standard_checkBox.fire();
        important_checkBox.fire();
        urgent_checkBox.fire();

    }

    public void addNewEvent(ActionEvent actionEvent) {
        //here should open a dialog and wait for the answer(EventModel) to add it
        // to database and to the VBox container as a new mini event pane
        TaskModel newEvent;

        String dialogTitle = "Add new event";
        newEvent = new TaskModel("", -1, "", LocalDate.now(), "", "", "");


        Dialog<TaskModel> eventDialog = new TaskDialog(newEvent, "add");




        eventDialog.setTitle(dialogTitle);
        Optional<TaskModel> result = eventDialog.showAndWait();

        //based on the returned result ad it to the database and refresh calendar
        try {
            if (result.isPresent()) {
                TaskModel addedEvent = result.get();
                if(addedEvent.getPriorityInt() != 0){
                    saveEvent(addedEvent);
                }
                    refreshCalendar(addedEvent.getDate().get());
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saveEvent(TaskModel eventToSave){
        util.saveEvent(eventToSave);
    }
    public void updateEvent(TaskModel eventToUpdate, TaskModel oldEvent){
        util.updateEvent(eventToUpdate, oldEvent);
    }

    private void showAll(LocalDate date, String purpose) {
        //open a dialog to show all the events for this particular date

        ObservableList<TaskModel> allEvents = getEventsOnDate(date);
        allEvents.removeIf(event -> !event.getPurposeString().equals(purpose));

        Dialog<ButtonType> allEventsDialog = new AllEventsDialog(allEvents);


        Optional<ButtonType> clicked_btn = allEventsDialog.showAndWait();
        if(clicked_btn.get() == ButtonType.CANCEL){
            allEventsDialog.close();
            refreshCalendar(date);
        }


    }

    public void showDetails(TaskModel event, String key) {
        //open a dialog to show detailed info about event(reuse the dialog to add
        // events and do bindings between dialog fields and chosen EventModel)

         TaskModel old = new TaskModel(event.getTitleString(),event.getPriorityInt(), event.getDescriptionString(),
                 event.getDate().get(), event.getStartTimeString(), event.getEndTimeString(),event.getPurposeString());

        String dialogTitle = "Edit event";






        Dialog<TaskModel> eventDialog = new TaskDialog(event, "update");

        TaskDialog.controller.getCompleted_checkBox().setDisable(false);

        eventDialog.setTitle(dialogTitle);

        Optional<TaskModel> result = eventDialog.showAndWait();



        //based on the returned result ad it to the database and refresh calendar
        try {
            if (result.isPresent()) {
                TaskModel addedEvent = result.get();



                updateEvent(addedEvent, old);



                if(key.equals("first dialog")) {
                    refreshCalendar(addedEvent.getDate().get());
                }else if(key.equals("showAll dialog")){
                  //AllEventsDialog allDialog = new AllEventsDialog(getEventsOnDate(old.getDate().get()));
                    // allDialog.refreshDialogView(old);



                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void refreshCalendar(LocalDate selectedDate) {
        clearEvents();
        LocalDate monday = selectedDate;
        this.selectedDate = selectedDate;
        updateDate();

        while (monday.getDayOfWeek() != DayOfWeek.MONDAY) {
            monday = monday.minusDays(1);
        }
        Queue<LocalDate> stack = new LinkedList<>();
        monday = changeDateLabel(monday_label,monday,stack);
        monday = changeDateLabel(tuesday_label,monday,stack);
        monday = changeDateLabel(wednesday_label,monday,stack);
        monday = changeDateLabel(thursday_label,monday,stack);
        monday = changeDateLabel(friday_label,monday,stack);
        monday = changeDateLabel(saturday_label,monday,stack);
        monday = changeDateLabel(sunday_label,monday,stack);

        addEvents(stack);



    }
    private LocalDate changeDateLabel(Label label ,LocalDate start,Queue<LocalDate> stack) {
        label.setText(String.valueOf(start.getDayOfMonth()));
        stack.add(start);
        start = start.plusDays(1);
        return start;
    }

    public ObservableList<TaskModel> getEventsOnDate(LocalDate date){
        return util.fetchEventsOnDate(date);
    }



    private void addEvents(Queue<LocalDate> stack) {
        int column = 0;

        while (stack.size() > 0) {
            LocalDate date = stack.remove();

            ObservableList<TaskModel> eventList = getEventsOnDate(date);

            int morning = 0;
            int goals = 0;
            int creative = 0;
            int relax = 0;

            for (TaskModel eventModel : eventList) {
                int eventPriority = eventModel.getPriorityInt();
                Label eventBox = createEventPane(eventModel);
                String purpose = eventModel.getPurposeString().toLowerCase();
                switch(purpose){
                    case "morning":
                        morning++;
                        break;
                    case "for goals":
                        goals++;
                        break;
                    case "creative":
                        creative++;
                        break;
                    default:
                        relax++;

                }
                if((morning == 3 && purpose.equals("morning")) || (goals == 3 && purpose.equals("for goals"))
                        || (creative == 3 && purpose.equals("creative"))|| (relax == 3 && purpose.equals("relax"))){

                    eventBox.setText("...");
                    eventBox.setOnMouseClicked(mouseEvent -> showAll(eventModel.getDate().get(), eventModel.getPurposeString()));

                }else if(morning < 3 && purpose.equals("morning") || goals < 3 && purpose.equals("for goals")
                        || creative < 3 && purpose.equals("creative") || relax < 3 && purpose.equals("relax")){
                    eventBox.setText(eventModel.getTitleString());
                    eventBox.setOnMouseClicked(mouseEvent -> showDetails(eventModel, "first dialog"));

                }else{
                    break;
                }


                if (eventPriority == TaskModel.STANDARD) {
                    eventBox.visibleProperty()
                            .bind(getStandardFilterPropertyProperty());
                    eventBox.managedProperty()
                            .bind(getStandardFilterPropertyProperty());
                } else if (eventPriority == TaskModel.IMPORTANT) {
                    eventBox.visibleProperty()
                            .bind(getImportantFilterPropertyProperty());
                    eventBox.managedProperty()
                            .bind(getImportantFilterPropertyProperty());
                } else {
                    eventBox.visibleProperty()
                            .bind(getUrgentFilterPropertyProperty());
                    eventBox.managedProperty()
                            .bind(getUrgentFilterPropertyProperty());
                }


                int row ;

                switch(purpose){
                    case "morning":
                        row = 2;
                        break;
                    case "for goals":
                        row = 3;
                        break;
                    case "creative":
                        row = 4;
                        break;
                    default:
                        row =5;
                }
                //adding event pane to the particular cell in our week calendar


                VBox cell = (VBox)((VBox) EventPane.getChildren().get(column)).getChildren().get(row);
                cell.getChildren().add(eventBox);





            }
            column++;



        }


    }

    private Label createEventPane(TaskModel event) {
        Label box = new Label();
        box.setMinHeight(18);
        box.setPrefHeight(18);
        box.setMaxHeight(18);
        box.setMinWidth(56);
        box.setMaxWidth(56);
        box.setPrefWidth(56);

        box.setStyle("-fx-font-size: 9");


        int priority = event.getPriorityInt();


            if (priority == TaskModel.STANDARD) {
                box.setStyle("-fx-background-color:  #8FC1D1");
            } else if (priority == TaskModel.IMPORTANT) {
                box.setStyle("-fx-background-color:   #8D9F87");
            } else {
                box.setStyle("-fx-background-color: #E9A971");
            }


        box.setTooltip(new Tooltip(event.getTitleString() + "\n" + event.getStartTimeString() + "-" + event.getEndTimeString()));

        return box;


    }


    @FXML
    private void moveBack() {

            selectedDate = selectedDate.minusWeeks(1);
            refreshCalendar(selectedDate);

        updateDate();
    }

    @FXML
    private void moveForward() {

            selectedDate = selectedDate.plusWeeks(1);
            refreshCalendar(selectedDate);

        updateDate();
    }

    private void updateDate() {
        monthYear_label.setText(TaskModel.MONTHS[selectedDate.getMonthValue() - 1] + " "
                + selectedDate.getYear());
    }



    private void clearEvents() {

        for(int i = 0; i < 7 ; i++){
            for(int j = 2; j < 6; j++){
                //null pointer exception
                VBox cell = (VBox)((VBox) EventPane.getChildren().get(i)).getChildren().get(j);
                cell.getChildren().clear();
            }

        }

    }

    public void selectToday(ActionEvent actionEvent) {
        selectedDate = LocalDate.now();
        datePicker.setValue(selectedDate);
        refreshCalendar(selectedDate);
        updateDate();

    }


    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }




    public BooleanProperty getUrgentFilterPropertyProperty() {
        return urgentFilterProperty;
    }

    public void setUrgentFilterProperty(boolean urgentFilterProperty) {
        this.urgentFilterProperty.set(urgentFilterProperty);
    }



    public BooleanProperty getImportantFilterPropertyProperty() {
        return importantFilterProperty;
    }

    public void setImportantFilterProperty(boolean importantFilterProperty) {
        this.importantFilterProperty.set(importantFilterProperty);
    }



    public BooleanProperty getStandardFilterPropertyProperty() {
        return standardFilterProperty;
    }

    public void setStandardFilterProperty(boolean standardFilterProperty) {
        this.standardFilterProperty.set(standardFilterProperty);
    }


    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"mainPage.fxml","Main Page", SignUpController.Username);
    }
}
