package com.example.doerfinal2;


import com.example.doerfinal2.controllers.JournalController;
import com.example.doerfinal2.controllers.SignUpController;
import com.example.doerfinal2.dialogs.EncouragementDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.HTMLEditor;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.ResourceBundle;

import static com.example.doerfinal2.controllers.JournalController.mode;
import static com.example.doerfinal2.controllers.JournalController.record;

public class TextEditorController implements Initializable {

    public static String[] images = {"https://www.shihoriobata.com/wp-content/uploads/2021/10/short-motivational-quotes-thumbnail.jpg",
    "https://i.pinimg.com/originals/89/08/87/8908879717f17449a052e2e0bfaad2a7.png",
    "https://i.pinimg.com/originals/38/2a/5b/382a5b2c6959d2e73a9f6c117ac58a54.jpg",
    "https://i.pinimg.com/originals/3e/6b/8a/3e6b8ad4beb868f334374c9c9cf7d374.jpg",
    "https://parade.com/.image/t_share/MTkzMDEwNDU3Mjc4MTYyNjQw/best-inspirational-quote.png",
    "https://quotefancy.com/media/wallpaper/1600x900/6951421-Ana-Claudia-Antunes-Quote-Nobody-can-go-back-and-start-all-over.jpg",
    "https://www.keepinspiring.me/wp-content/uploads/2013/04/encouraging-quotes-7-1024x770.jpg"
};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        if(mode == JournalController.JournalMode.UPDATE){
           htmlEdit.setHtmlText(record.getText());


        }

    }

    private final MongodbUtil util = new MongodbUtil();
    @FXML
    private HTMLEditor htmlEdit;


    public void saveText(ActionEvent actionEvent) {

        if(htmlEdit.getHtmlText().isEmpty()){
            util.showAlert("Text area is empty.");

        }else {

            String normalText = Jsoup.parse(htmlEdit.getHtmlText()).text();



            boolean isNegative = detectNegative(normalText);

            if (isNegative) {
                showEncouragementMessage();
            }

            record.setText(htmlEdit.getHtmlText());
            if (JournalController.mode == JournalController.JournalMode.ADD) {

                util.saveRecord(record);
            } else {

                util.updateRecord(record);
            }

            util.changeScene(actionEvent, "journal.fxml", "Journal Page", SignUpController.Username);
        }
    }

    private boolean detectNegative(String normalText) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.apilayer.com/sentiment/analysis"))
                .header("apikey", "kr2vlVYJuzkGK4H6utP64fWHpA6hiaXy")
                .POST(HttpRequest.BodyPublishers.ofString(normalText))
                .build();

                HttpResponse<String> response = null;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (response != null && response.body().contains("negative")) {
            return true;
        }
        return false;
    }

    private void showEncouragementMessage() {

        Random random = new Random();
         int index = random.nextInt(7);

         EncouragementDialog dialog = new EncouragementDialog(images[index]);
         dialog.show();



    }

    public void goBack(ActionEvent actionEvent) {
        util.changeScene(actionEvent,"journal.fxml","Journal Page" , SignUpController.Username);
    }



}
