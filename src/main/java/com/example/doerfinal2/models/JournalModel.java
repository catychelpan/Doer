package com.example.doerfinal2.models;



import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.bson.types.ObjectId;

import java.time.LocalDate;

public class JournalModel {
    private ObjectId user_idJournal;
    private StringProperty title;
    private StringProperty text;
    private LocalDate date;

    public JournalModel(String title, String text, LocalDate date) {
        this.title = new SimpleStringProperty(title);
        this.text = new SimpleStringProperty(text);
        this.date = date;
    }

    public ObjectId getUser_idJournal() {
        return user_idJournal;
    }

    public void setUser_idJournal(ObjectId user_idJournal) {
        this.user_idJournal = user_idJournal;
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

    public String getText() {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String info) {
        text = new SimpleStringProperty(info);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
