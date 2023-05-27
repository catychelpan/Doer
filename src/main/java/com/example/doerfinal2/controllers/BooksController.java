package com.example.doerfinal2.controllers;


import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.dialogs.BookDialog;
import com.example.doerfinal2.models.BookModel;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;




public class BooksController implements Initializable {
    @FXML
    private Button update_btn;
    private final MongodbUtil util = new MongodbUtil();
    public static String mode;


    public enum DialogMode {ADD, UPDATE}



    @FXML
    private TableView<BookModel> tableView;
    @FXML
    private TableColumn<BookModel, String> title_col;
    @FXML
    private TableColumn<BookModel, String> author_col;
    @FXML
    private TableColumn<BookModel, String> status_col;
    @FXML
    private TableColumn<BookModel, Integer> rate_col;
    @FXML
    private TableColumn<BookModel, Hyperlink> notes_col;
    @FXML
    private JFXButton addBook_btn;
    @FXML
    private TextField keywordField;

    public ObservableList<BookModel> bookModelObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        TableColumn<BookModel, ?> tableColumn = tableView.getColumns().get(0);
        TableColumn<BookModel, ?> tableColumn1 = tableView.getColumns().get(4);

        tableColumn.getStyleClass().add("leftmost-column-header");
        tableColumn1.getStyleClass().add("rightmost-column-header");



        bookModelObservableList.addAll(util.fetchBooks());


        title_col.setCellValueFactory(new PropertyValueFactory<>("title"));
        author_col.setCellValueFactory(new PropertyValueFactory<>("author"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("Status"));
        rate_col.setCellValueFactory(new PropertyValueFactory<>("Rate"));
        //for notes column we need to change property for HyperLink
        notes_col.setCellValueFactory(cellData -> {

            //cellData.getValue() will return the observable object from the particular row

            String filePath = cellData.getValue().getFilePath();
            Hyperlink link = new Hyperlink("Notes");

            //for this hyperlink we need to set the action of redirecting to the file by filepath
            // (or opening another window with text area to just display the contents of a file)

            link.setOnAction(event -> {
                try {
                    File file = new File(filePath);
                    if (file.exists()) {
                        java.awt.Desktop.getDesktop().open(file);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return new SimpleObjectProperty<>(link);
        });
        //fill the table with our objects from the observable collection
        tableView.setItems(bookModelObservableList);
        //when we type in the text filed the listener takes it as a new value  and in our filtered list we
        // check by row if columns contain this "new value". If there is a match, TableView displays the matching rows
        //because, if there is a matching it returns true, and then the FilteredList filters itself. Doing so will trigger the binding
        //made between sorted list and TableView. It triggers the TableView to refresh its contents.




        FilteredList<BookModel> filteredData = new FilteredList<>(bookModelObservableList, b -> true);
        // addListener to capture any changes in search text field
        keywordField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate(bookModel -> {
                if (newValue.isEmpty() || newValue == null) {
                    return true;
                }
                String searchKeyword = newValue.toLowerCase();
                if (bookModel.getTitle().toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                } else if (bookModel.getAuthor().toLowerCase().indexOf(searchKeyword) != -1) {
                    return true;
                } else return bookModel.getStatus().toLowerCase().indexOf(searchKeyword) != -1;




            });
        });
        SortedList<BookModel> sortedData = new SortedList<>(filteredData);
        //we will bind this sorted list to our table of books(so they will be in sync)
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        //applying filtered and sorted data to the table view(all is loaded during the initialization)
        tableView.setItems(sortedData);
    }


    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"mainPage.fxml","Main Page" , SignUpController.Username);
    }


    public void deleteBook(ActionEvent actionEvent) {
        Optional<ButtonType> isConfirmed = util.showConfirmationDialog("Confirmation", "Delete confirmation", "Are you sure you want to delete selected item?");
        if (isConfirmed.get() == ButtonType.OK) {
            int selected = tableView.getSelectionModel().getSelectedIndex();
            BookModel newBook = tableView.getSelectionModel().getSelectedItem();
            bookModelObservableList.remove(selected);
            util.deleteBook(newBook);




        }
    }








    public void updateBook(ActionEvent actionEvent) {
        addNewBook(actionEvent);
    }




    public void addNewBook(ActionEvent actionEvent) {




        BookModel newBook;
        //DialogMode mode;
        BookModel deletedBook = null;
        String dialogTitle = "";
        if (actionEvent.getSource().equals(addBook_btn)) {
            mode = DialogMode.ADD.toString();
            dialogTitle = "Add a book";
            newBook = new BookModel("","","","","");
        } else if (actionEvent.getSource().equals(update_btn)) {
            mode = DialogMode.UPDATE.toString();
            dialogTitle = "Update a book";
            newBook = tableView.getSelectionModel().getSelectedItem();








        } else {
            return;
        }





        Dialog<BookModel> bookDialog = new BookDialog(newBook);
        bookDialog.setTitle(dialogTitle);
        Optional<BookModel> result = bookDialog.showAndWait();




        try {
            if(result.isPresent() ){
                BookModel addedBook = result.get();
                if(mode.equals(DialogMode.ADD.toString())){
                    bookModelObservableList.add(addedBook);
                    tableView.refresh();
                    util.saveBook(addedBook);
                }else{
                    for(BookModel oldBook : bookModelObservableList){
                        if (oldBook.getTitle().equals(addedBook.getTitle()) && oldBook.getAuthor().equals(addedBook.getAuthor())){
                            deletedBook = oldBook;
                        }
                    }
                    //need to update the tableview
                    bookModelObservableList.remove(deletedBook);
                    bookModelObservableList.add(addedBook);
                    tableView.refresh();
                    util.updateBook(addedBook);
                }




            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }



}





