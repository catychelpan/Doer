package com.example.doerfinal2.controllers;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.TextEditorController;
import com.example.doerfinal2.models.JournalModel;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;



public class JournalController implements Initializable {

    public static JournalModel record ;



    public enum JournalMode {ADD, UPDATE}
    public static JournalMode mode;
    @FXML
    private JFXDatePicker datePicker;
    @FXML
    private TableView<JournalModel> tableView;
    @FXML
    private JFXButton goBack_btn;
    @FXML
    private JFXTextField textField;
    @FXML
    private TableColumn<JournalModel, LocalDate> date_col;
    @FXML
    private TableColumn<JournalModel,String> record_col;
    private final MongodbUtil util = new MongodbUtil();
    @FXML
    private WebView webView;
    ObservableList<JournalModel> journalModelObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn<JournalModel, ?> tableColumn = tableView.getColumns().get(0);
        TableColumn<JournalModel, ?> tableColumn1 = tableView.getColumns().get(1);

        tableColumn.getStyleClass().add("leftmost-column-header");
        tableColumn1.getStyleClass().add("rightmost-column-header");

        record = new JournalModel("","",LocalDate.now());

        journalModelObservableList.addAll(util.fetchRecords());


        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        date_col.setCellFactory(tc -> new TableCell<JournalModel, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        record_col.setCellValueFactory(new PropertyValueFactory<>("title"));

        tableView.setItems(journalModelObservableList);


    }

    public void deleteRecord(ActionEvent actionEvent) {
        Optional<ButtonType> isConfirmed = util.showConfirmationDialog("Confirmation", "Delete confirmation", "Are you sure you want to delete selected item?");
        if (isConfirmed.get() == ButtonType.OK) {
            int selected = tableView.getSelectionModel().getSelectedIndex();
            getSelected();
            journalModelObservableList.remove(selected);
            util.deleteRecord(record);
        }
    }

    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"mainPage.fxml","Main Page", SignUpController.Username);
    }

    private void convertHTMLToNormalText() {
        WebEngine engine = webView.getEngine();
        engine.loadContent(record.getText(),"text/html");

    }

    public void addNewRecord(ActionEvent actionEvent) {
        mode = JournalMode.ADD;
        //open new scene with html editor
        String titleField = textField.getText();
        LocalDate dateField = datePicker.getValue();
        if(titleField == null || dateField == null){
            util.showAlert("Please, enter data to all fields");
            return;
        }
        record.setTitle(titleField);
        record.setDate(dateField);
        util.changeScene(actionEvent,"textEditor.fxml","Text Editor" , null);
        //changes don't show to the tableview
        journalModelObservableList.add(record);
        tableView.getItems().add(record);
        tableView.refresh();

    }

    public void editRecord(ActionEvent actionEvent) {
        mode = JournalMode.UPDATE;
        JournalModel deletedRecord = null;

        getSelected();

        //JournalModel letter = new JournalModel(selected.getTitle(), selected.getText(), selected.getDate());


        //here doesn't show saved text to html editor
        TextEditorController controller = new TextEditorController();

        util.changeScene(actionEvent,"textEditor.fxml","Text Editor" , null);
        for(JournalModel oldRecord : journalModelObservableList){
            if (oldRecord.getTitle().equals(record.getTitle()) && oldRecord.getDate().equals(record.getDate())){
                deletedRecord = oldRecord;
            }
        }
        //need to update the tableview
        journalModelObservableList.remove(deletedRecord);
        journalModelObservableList.add(record);
        tableView.refresh();


    }
    public void getSelected() {
        JournalModel selectedRecord = tableView.getSelectionModel().getSelectedItem();
        String title = selectedRecord.getTitle();
        LocalDate date = selectedRecord.getDate();


        record.setTitle(title);
        record.setDate(date);
        record.setText( util.getTextByTitleAndDate(title,date.toString()).getText());


    }

    public void showText(ActionEvent actionEvent) {
        getSelected();

            convertHTMLToNormalText();
        }
    }

