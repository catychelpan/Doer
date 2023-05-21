package com.example.doerfinal2.models;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.bson.types.ObjectId;


public class BookModel {
    private ObjectId user_idBooks;
    private StringProperty title;
    private StringProperty author;

    public BookModel(StringProperty title, StringProperty author, StringProperty status, StringProperty rate, StringProperty filePath) {
        this.title = title;
        this.author = author;
        Status = status;
        Rate = rate;
        this.filePath = filePath;
    }

    private StringProperty Status;
    private StringProperty Rate;
    private StringProperty filePath;


    //Create a constructor to accept string value and assign it to our "property
    // fields"(do not forget to change it in all save methods in util and others)
    public BookModel(String title, String author, String status, String rate, String filePath ){
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.Status = new SimpleStringProperty(status);
        this.Rate = new SimpleStringProperty(rate);
        this.filePath = new SimpleStringProperty(filePath);
    }

    public BookModel() {}


   

    public void setUser_idBooks(ObjectId user_id) {
        this.user_idBooks = user_id;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getStatus() {
        return Status.get();
    }

    public StringProperty statusProperty() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status.set(status);
    }

    public String getRate() {
        return Rate.get();
    }

    public StringProperty rateProperty() {
        return Rate;
    }

    public void setRate(String rate) {
        this.Rate.set(rate);
    }

    public String getFilePath() {
        return filePath.get();
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public Object getUser_idBooks() {
        return user_idBooks;
    }
}
