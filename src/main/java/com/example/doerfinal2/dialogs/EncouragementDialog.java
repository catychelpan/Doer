package com.example.doerfinal2.dialogs;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;



public class EncouragementDialog extends Dialog<ButtonType> {

    private String imageResource;
    public EncouragementDialog(String imagePath){
        super();
        imageResource = imagePath;
        this.setTitle("Relax");
        buildUI();
    }

    private void buildUI() {
        VBox container = new VBox();
        container.setPadding(new Insets(10,10,10,10));
        container.setAlignment(Pos.CENTER);
        Image image = new Image(imageResource);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(300);
        imageView.setPreserveRatio(true);
        container.getChildren().add(imageView);

        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        Button buttonCancel = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        buttonCancel.setId("CancelButton");
        buttonCancel.setText("OK");


        getDialogPane().setContent(container);

    }

}
