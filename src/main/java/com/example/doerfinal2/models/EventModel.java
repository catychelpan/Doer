package com.example.doerfinal2.models;

import javafx.beans.property.*;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class EventModel {

    private ObjectId user_id;

    private StringProperty title;
    private StringProperty purpose;

    private IntegerProperty priority;

    private StringProperty description;

    private ObjectProperty<LocalDate> date;

    private StringProperty startTime;

    private StringProperty endTime;



    public static final String[] MONTHS = { "January", "February", "March", "April",
            "May", "June", "July", "August", "September", "October", "November",
            "December" };

    public static final int COMPLETED = 0;
    public static final int STANDARD = 1;
    public static final int IMPORTANT = 2;
    public static final int URGENT = 3;

    public EventModel(String title, int priority, String description,
                         LocalDate date, String startTime , String endTime, String purpose) {


        this.title =  new SimpleStringProperty(title);
        this.priority = new SimpleIntegerProperty(priority);
        this.description = new SimpleStringProperty(description);
        this.date = new SimpleObjectProperty<>(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.purpose = new SimpleStringProperty(purpose);

    }

    public EventModel() {

    }

    public StringProperty getTitle() {
        return title;
    }
    public String getTitleString(){
        return title.get();

    }

    public void setTitle(String title) {
        this.title = new SimpleStringProperty(title);
    }

    public IntegerProperty getPriority() {
        return priority;
    }

    public int getPriorityInt() {
        return priority.get();
    }


    public void setPriority(int priority) {
        this.priority = new SimpleIntegerProperty(priority);
    }

    public StringProperty getDescription() {
        return description;
    }
    public String getDescriptionString() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public ObjectProperty<LocalDate> getDate() {
        return  date;
    }

    public void setDate(LocalDate date) {
        this.date = new SimpleObjectProperty<>(date);
    }

    public StringProperty getStartTime() {
        return startTime;
    }

    public String getStartTimeString() {
        return startTime.get();
    }

    public boolean setStartTime(String startTime) {
        String [] arr = startTime.split(":");
        if(Integer.parseInt(arr[0]) < 1 || Integer.parseInt(arr[0] ) > 24){
            return false;
        }else{
            this.startTime = new SimpleStringProperty(startTime);
            return true;
        }

    }

    public StringProperty getEndTime() {
        return endTime;
    }
    public String getEndTimeString() {
        return endTime.get();
    }

    public void setEndTime(String endTime) {
        this.endTime = new SimpleStringProperty(endTime);
    }
    public ObjectId getUser_id() {
        return user_id;
    }

    public void setUser_id(ObjectId user_id) {
        this.user_id = user_id;
    }

    public StringProperty getPurpose() {

        return purpose;
    }
    public String getPurposeString() {

        return  purpose.get();
    }


    public void setPurpose(String purpose) {
        this.purpose = new SimpleStringProperty(purpose);
    }


}
