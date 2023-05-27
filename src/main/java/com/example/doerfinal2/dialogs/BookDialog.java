package com.example.doerfinal2.dialogs;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.models.BookModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;


public class BookDialog extends Dialog<BookModel> implements CustomDialog  {

    public static BookModel bookModel;
    public static BookDialogController controller = new BookDialogController();
    private final MongodbUtil util = new MongodbUtil();
    public BookDialog(BookModel book) {
        super();


        bookModel = book;
        getDialogUi();
        controller.initData(bookModel);
        setPropertyBindings();
        setResultConverter();

    }


    public  Pane getContent() {


        return util.getContent("addBook.fxml", "book");



    }



    @Override
    public void setPropertyBindings() {

        controller.getTitleField().textProperty().bindBidirectional(bookModel.titleProperty());
        controller.getAuthorField().textProperty().bindBidirectional(bookModel.authorProperty());
        controller.addedFile.textProperty().bindBidirectional(bookModel.filePathProperty());
        controller.status_option.valueProperty().bindBidirectional(bookModel.statusProperty());

        StringProperty selectedValueProperty = new SimpleStringProperty();
        selectedValueProperty.bindBidirectional(bookModel.rateProperty());

        selectedValueProperty.addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                for (Toggle radioButton : controller.radio_btn.getToggles()) {
                    if (radioButton.getUserData().toString().equals(newValue)) {
                        radioButton.setSelected(true);
                        break;
                    }
                }
            }
        });

        controller.radio_btn.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                selectedValueProperty.set(((RadioButton) newToggle).getText());
            }
        });


    }
    @Override
    public void setResultConverter() {
        javafx.util.Callback<ButtonType,BookModel> bookResultConverter = buttonType -> {
            if(buttonType == ButtonType.OK){
                return bookModel;
            }else{
                return null;
            }
        };
        setResultConverter(bookResultConverter);
    }




    @Override
    public void getDialogUi() {

        Pane pane = getContent();
        getDialogPane().setContent(pane);

        DialogPane dialogPane = getDialogPane();

        Button buttonOK = util.setDialogButtons(dialogPane);


        buttonOK.addEventFilter(javafx.event.ActionEvent.ACTION, new EventHandler<>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                if (!validateDialog()) {
                    actionEvent.consume();

                }
            }

            private boolean validateDialog() {



                    if (controller.getTitleField().getText().isEmpty() || controller.getAuthorField().getText().isEmpty() || controller.statusOption.isEmpty() ) {
                        util.showAlert("Not all the fields were filled.");
                        return false;
                    }
                    return true;

            }
        });
    }
}








