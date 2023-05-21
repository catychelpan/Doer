package com.example.doerfinal2.controllers;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.HabitModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BarChartController implements Initializable {
    @FXML
    private AnchorPane anchorPane;

    private BarChart<String,Number> barChart;

    private final MongodbUtil util = new MongodbUtil();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Habits");
        yAxis.setLabel("Days");
        yAxis.setTickUnit(1);

        barChart = new BarChart<>(xAxis,yAxis);

        barChart.setTitle("All habits during 4 weeks");

        XYChart.Series series = new XYChart.Series();

        barChart.setLegendVisible(false);

        //1 - habit title  2 - amount of days from method
        //for loop for 10 repeat

        ArrayList<HabitModel> chartModels = new ArrayList<>();

        ObservableList<HabitModel> habitsOnWeek = util.getHabitsForWeek(1);

        for(HabitModel model: habitsOnWeek ){

            chartModels.clear();
            chartModels = util.getHabitsByTitle(model.getTitle());
            int days  = getNumberOfFinishedDays( chartModels);
            series.getData().add(new XYChart.Data<>(model.getTitle(),days));

        }

//        barChart.getStyleClass().add("custom-bar-chart");
//        barChart.getStylesheets().add(getClass().getResource("BarChartStyle.css").toExternalForm());

        barChart.setStyle("-fx-bar-fill: #A0B5B1;");



        barChart.getData().add(series);
        anchorPane.setPadding(new Insets(5,5,5,5));
        anchorPane.getChildren().add(barChart);
        AnchorPane.setTopAnchor(barChart, 0.0);
        AnchorPane.setLeftAnchor(barChart, 0.0);
        AnchorPane.setRightAnchor(barChart, 0.0);



    }

    private int getNumberOfFinishedDays(ArrayList<HabitModel> chartModels) {

        int days = 0;

        for (HabitModel habit : chartModels){


            if(habit.isMondaySelected()){
                days++;
            }
            if(habit.isTuesdaySelected()){
                days++;
            }
            if(habit.isThursdaySelected()){
                days++;
            }
            if(habit.isWednesdaySelected()){
                days++;
            }
            if(habit.isThursdaySelected()){
                days++;
            }
            if(habit.isFridaySelected()){
                days++;
            }
            if(habit.isSaturdaySelected()){
                days++;
            }
            if(habit.isSundaySelected()){
                days++;
            }



        }
        return days;
    }


    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent, "habits.fxml","Habit Tracker", null);
    }
}
