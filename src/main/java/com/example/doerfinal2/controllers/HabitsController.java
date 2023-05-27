package com.example.doerfinal2.controllers;


import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.HabitModel;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;




import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class HabitsController implements Initializable {

    @FXML
    private TableView<HabitModel> week1Table;
    @FXML
    private TableView<HabitModel>  week2Table;
    @FXML
    private TableView<HabitModel>  week4Table;
    @FXML
    private TableView<HabitModel>  week3Table;


    private final MongodbUtil util = new MongodbUtil();
    @FXML
    private Tab week4_tab;
    @FXML
    private Tab week3_tab;
    @FXML
    private Tab week2_tab;
    @FXML
    private Tab week1_tab;
    @FXML
    private JFXButton finish_btn;




    ObservableList<HabitModel> habitModelsObservableList = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        finish_btn.setTooltip(new Tooltip("Click if you want to start tracking a new set of habits"));


        habitModelsObservableList.clear();
        habitModelsObservableList.addAll(util.getHabitsForWeek(1));
        changeTableCells(week1Table);
        week1Table.setItems(habitModelsObservableList);



        week1_tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                habitModelsObservableList.clear();
                habitModelsObservableList.addAll(util.getHabitsForWeek(1));
                changeTableCells(week1Table);
                week1Table.setItems(habitModelsObservableList);
            }
        });




        week2_tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                habitModelsObservableList.clear();
                habitModelsObservableList.addAll(util.getHabitsForWeek(2));
                changeTableCells(week2Table);
                week2Table.setItems(habitModelsObservableList);
            }
        });




        week3_tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                habitModelsObservableList.clear();
                habitModelsObservableList.addAll(util.getHabitsForWeek(3));
                changeTableCells(week3Table);
                week3Table.setItems(habitModelsObservableList);
            }
        });




        week4_tab.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue){
                habitModelsObservableList.clear();
                habitModelsObservableList.addAll(util.getHabitsForWeek(4));
                changeTableCells(week4Table);
                week4Table.setItems(habitModelsObservableList);
            }
        });


    }




    public void changeTableCells(TableView<HabitModel> tableView) {




        TableColumn<HabitModel, ?> habit_col = tableView.getColumns().get(0);
        TableColumn<HabitModel, Double> progress_col = (TableColumn<HabitModel, Double>) tableView.getColumns().get(8);
        TableColumn<HabitModel, Boolean> mon_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(1);
        TableColumn<HabitModel, Boolean> tue_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(2);
        TableColumn<HabitModel, Boolean> wed_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(3);
        TableColumn<HabitModel, Boolean> thu_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(4);
        TableColumn<HabitModel, Boolean> fri_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(5);
        TableColumn<HabitModel, Boolean> sat_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(6);
        TableColumn<HabitModel, Boolean> sun_col = (TableColumn<HabitModel, Boolean>) tableView.getColumns().get(7);


        habit_col.setCellValueFactory(new PropertyValueFactory<>("title"));


        mon_col.setCellValueFactory(new PropertyValueFactory<>("mondaySelected"));
        adjustCellFactory(mon_col,"mon");


        tue_col.setCellValueFactory(new PropertyValueFactory<>("tuesdaySelected"));
        adjustCellFactory(tue_col,"tue");


        wed_col.setCellValueFactory(new PropertyValueFactory<>("wednesdaySelected"));
        adjustCellFactory(wed_col,"wed");


        thu_col.setCellValueFactory(new PropertyValueFactory<>("thursdaySelected"));
        adjustCellFactory(thu_col,"thu");


        fri_col.setCellValueFactory(new PropertyValueFactory<>("fridaySelected"));
        adjustCellFactory(fri_col,"fri");


        sat_col.setCellValueFactory(new PropertyValueFactory<>("saturdaySelected"));
        adjustCellFactory(sat_col,"sat");


        sun_col.setCellValueFactory(new PropertyValueFactory<>("sundaySelected"));
        adjustCellFactory(sun_col,"sun");




        progress_col.setCellValueFactory(
                new PropertyValueFactory<>("progress")
        );
        progress_col.setCellFactory(
                ProgressBarTableCell.forTableColumn()
        );

    }






    public void adjustCellFactory(TableColumn<HabitModel, Boolean> col, String key){
        switch(key){
            case "mon":
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setMondaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setMondaySelected(false);


                                    }


                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });
                break;


            case "tue":
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setTuesdaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setTuesdaySelected(false);


                                    }
                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });


                break;


            case "wed":
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setWednesdaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setWednesdaySelected(false);


                                    }
                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });


                break;


            case "thu":
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setThursdaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setThursdaySelected(false);


                                    }
                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });


                break;


            case "fri":
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setFridaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setFridaySelected(false);


                                    }
                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });


                break;


            case "sat":
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setSaturdaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setSaturdaySelected(false);


                                    }
                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });




                break;


            default:
                col.setCellFactory(column -> new CheckBoxTableCell<>() {
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);


                        if (!empty && item != null) {
                            // Get the index of the row
                            int rowIndex = getIndex();


                            // Access the corresponding HabitModel object based on the index
                            HabitModel habit = getTableView().getItems().get(rowIndex);


                            if (habit != null) {
                                CheckBox checkBox = new CheckBox();
                                checkBox.setSelected(item);


                                // Add listener to update the boolean property when checkbox state changes
                                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {




                                    if (newValue) {
                                        habit.setProgress(habit.getProgress() + 0.14285714);
                                        habit.setSundaySelected(true);


                                    } else {
                                        habit.setProgress(habit.getProgress() - 0.14285714);
                                        habit.setSundaySelected(false);


                                    }
                                    util.updateHabit(habit, key);
                                });


                                setGraphic(checkBox);


                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                });




        }


    }




    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"mainPage.fxml","Main Page", SignUpController.Username);
    }




    public void seeChart(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"barChart.fxml","Track your progress", null);
    }


    public void restartTracking(ActionEvent actionEvent) {




        Optional<ButtonType> isConfirmed = util.showConfirmationDialog("Confirmation", "Delete confirmation", "Are you sure you want to delete selected item?");
        if (isConfirmed.get() == ButtonType.OK) {


            util.deleteAllHabits();


            util.changeScene(actionEvent, "habitsFirstVisit.fxml", "Habits",null);
        }




    }
}











