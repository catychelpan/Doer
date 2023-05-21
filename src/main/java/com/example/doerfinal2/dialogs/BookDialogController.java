package com.example.doerfinal2.dialogs;


import com.example.doerfinal2.controllers.BooksController;
import com.example.doerfinal2.models.BookModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class BookDialogController implements Initializable {
    public BookModel bookModel;

    public  String selectedFilePath;
    public  String statusOption;
    public  String rate;
    @FXML
    private   JFXTextField titleField;

    @FXML
    private  JFXTextField author;
    @FXML
    public JFXComboBox<String> status_option;
    @FXML
    private  JFXRadioButton one;
    private final String[] status = {"In process", "Finished" , "Favorite", "In plans", "Dropped"};
    @FXML
    private  JFXButton addFile_btn;
    @FXML
    public   Label addedFile;
    @FXML
    private  JFXRadioButton two;
    ToggleGroup radio_btn = new ToggleGroup();
    @FXML
    private  JFXRadioButton three;
    @FXML
    private  JFXRadioButton four;
    @FXML
    private  JFXRadioButton five;



    public BookDialogController(){


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        one.setUserData("1");
        one.setToggleGroup(radio_btn);
       two.setUserData("2");
        two.setToggleGroup(radio_btn);
        three.setUserData("3");



        three.setToggleGroup(radio_btn);
        four.setUserData("4");
        four.setToggleGroup(radio_btn);
        five.setUserData("5");
        five.setToggleGroup(radio_btn);


        addFile_btn.setOnAction(this::addFile);
        status_option.getItems().addAll(status);
        status_option.setOnAction(this::getStatus);



        radio_btn.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {


            if (radio_btn.getSelectedToggle() != null) {


                rate = radio_btn.getSelectedToggle().getUserData().toString();




            }

        });


    }

    public void addFile (ActionEvent actionEvent){


        FileChooser.ExtensionFilter ef = new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.pdf", "*.docx");
        FileChooser.ExtensionFilter ef2 = new FileChooser.ExtensionFilter("All Files", "*.*");

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(ef, ef2);
        fileChooser.setTitle("Open My Files");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            addedFile.setText(selectedFile.getPath());
            addFile_btn.setDisable(true);
            selectedFilePath = selectedFile.getPath();


        }


    }
    public void deleteFile (ActionEvent actionEvent){
        addedFile.setText("Added File");
        addFile_btn.setDisable(false);
    }




    private void getStatus (ActionEvent actionEvent){
        statusOption = status_option.getValue();
    }


    public  JFXTextField getTitleField() {
        return titleField;
    }


    public void setTitleField(JFXTextField title) {
        titleField = title;
    }
    public  JFXTextField getAuthorField() {
        return author;
    }


    public void setAuthorField(JFXTextField aut) {
        author = aut;
    }


    public void initData(BookModel bookModel) {

        if(BooksController.mode.equals(BooksController.DialogMode.UPDATE.toString())){
            titleField.setText(bookModel.getTitle());
            titleField.setDisable(true);
            author.setText(bookModel.getAuthor());
            author.setDisable(true);
        }
        titleField.setText(bookModel.getTitle());
        author.setText(bookModel.getAuthor());
        titleField.setText(bookModel.getTitle());
        author.setText(bookModel.getAuthor());
        status_option.valueProperty().setValue(bookModel.getStatus());


        for(Toggle toggles : radio_btn.getToggles()){
            if(toggles.getUserData().equals(bookModel.getRate())){
                toggles.setSelected(true);
                break;
            }
        }

        addedFile.setText(bookModel.getFilePath());
        selectedFilePath = addedFile.getText();
        statusOption = status_option.getValue();


    }


    public void setModel(BookModel book) {
        bookModel = book;
    }
}



