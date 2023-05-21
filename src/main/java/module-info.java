module com.example.doerfinal {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.jfoenix;





    opens com.example.doerfinal2.models to javafx.fxml;
    exports com.example.doerfinal2.models;



    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires java.desktop;
    requires org.slf4j;
    requires org.burningwave.core;
    requires okhttp;
    requires org.jsoup;
    requires java.net.http;


    opens com.example.doerfinal2 to javafx.fxml;
    exports com.example.doerfinal2;
    exports com.example.doerfinal2.controllers;
    opens com.example.doerfinal2.controllers to javafx.fxml;
    exports com.example.doerfinal2.dialogs;
    opens com.example.doerfinal2.dialogs to javafx.fxml;
}