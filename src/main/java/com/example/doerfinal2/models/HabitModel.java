package com.example.doerfinal2.models;

import javafx.beans.property.*;
import org.bson.types.ObjectId;

public class HabitModel {

    private ObjectId user_idHabits;
    private final StringProperty title;
    private final IntegerProperty weekIndex;
    private final BooleanProperty mondaySelected;


    public HabitModel(String title, Integer weekIndex, Boolean mondaySelected, Boolean tuesdaySelected, Boolean wednesdaySelected, Boolean thursdaySelected, Boolean fridaySelected, Boolean saturdaySelected, Boolean sundaySelected, Double progress) {
        this.title = new SimpleStringProperty(title);
        this.weekIndex = new SimpleIntegerProperty(weekIndex);
        this.mondaySelected = new SimpleBooleanProperty(mondaySelected);
        this.tuesdaySelected = new SimpleBooleanProperty(tuesdaySelected);
        this.wednesdaySelected = new SimpleBooleanProperty(wednesdaySelected);
        this.thursdaySelected = new SimpleBooleanProperty(thursdaySelected);
        this.fridaySelected = new SimpleBooleanProperty(fridaySelected);
        this.saturdaySelected = new SimpleBooleanProperty(saturdaySelected);
        this.sundaySelected = new SimpleBooleanProperty(sundaySelected);
        this.progress = new SimpleDoubleProperty(progress);
    }

    private final BooleanProperty tuesdaySelected;
    private final BooleanProperty wednesdaySelected;
    private final BooleanProperty thursdaySelected;
    private final BooleanProperty fridaySelected;
    private final BooleanProperty saturdaySelected;
    private final BooleanProperty sundaySelected;
    private final DoubleProperty progress;


    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getWeekIndex() {
        return weekIndex.get();
    }

    public IntegerProperty weekIndexProperty() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex.set(weekIndex);
    }

    public boolean isMondaySelected() {
        return mondaySelected.get();
    }

    public BooleanProperty mondaySelectedProperty() {
        return mondaySelected;
    }

    public void setMondaySelected(boolean mondaySelected) {
        this.mondaySelected.set(mondaySelected);
    }

    public boolean isTuesdaySelected() {
        return tuesdaySelected.get();
    }

    public BooleanProperty tuesdaySelectedProperty() {
        return tuesdaySelected;
    }

    public void setTuesdaySelected(boolean tuesdaySelected) {
        this.tuesdaySelected.set(tuesdaySelected);
    }

    public boolean isWednesdaySelected() {
        return wednesdaySelected.get();
    }

    public BooleanProperty wednesdaySelectedProperty() {
        return wednesdaySelected;
    }

    public void setWednesdaySelected(boolean wednesdaySelected) {
        this.wednesdaySelected.set(wednesdaySelected);
    }

    public boolean isThursdaySelected() {
        return thursdaySelected.get();
    }

    public BooleanProperty thursdaySelectedProperty() {
        return thursdaySelected;
    }

    public void setThursdaySelected(boolean thursdaySelected) {
        this.thursdaySelected.set(thursdaySelected);
    }

    public boolean isFridaySelected() {
        return fridaySelected.get();
    }

    public BooleanProperty fridaySelectedProperty() {
        return fridaySelected;
    }

    public void setFridaySelected(boolean fridaySelected) {
        this.fridaySelected.set(fridaySelected);
    }

    public boolean isSaturdaySelected() {
        return saturdaySelected.get();
    }

    public BooleanProperty saturdaySelectedProperty() {
        return saturdaySelected;
    }

    public void setSaturdaySelected(boolean saturdaySelected) {
        this.saturdaySelected.set(saturdaySelected);
    }

    public boolean isSundaySelected() {
        return sundaySelected.get();
    }

    public BooleanProperty sundaySelectedProperty() {
        return sundaySelected;
    }

    public void setSundaySelected(boolean sundaySelected) {
        this.sundaySelected.set(sundaySelected);
    }

    public double getProgress() {
        return progress.get();
    }

    public DoubleProperty progressProperty() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress.set(progress);
    }


    public ObjectId getUser_idHabits() {
        return user_idHabits;
    }

    public void setUser_idHabits(ObjectId user_idHabits) {
        this.user_idHabits = user_idHabits;
    }


}
