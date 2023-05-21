package com.example.doerfinal2;
import com.example.doerfinal2.controllers.MainPageController;
import com.example.doerfinal2.controllers.SignUpController;
import com.example.doerfinal2.dialogs.BookDialog;
import com.example.doerfinal2.dialogs.EventDialog;
import com.example.doerfinal2.models.*;
import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;


import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.*;

import static com.mongodb.client.model.Sorts.ascending;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class MongodbUtil {

    private static MongoCollection<Document> collectionOfUsers;
    private static MongoCollection<Document> collectionOfEvents;
    private static MongoCollection<Document> collectionOfBooks;
    private static MongoCollection<Document> collectionOfRecords;
    private static MongoCollection<Document> collectionOfHabits;
    private MongoClient mongoClient;


    public MongodbUtil() {

        ConnectionString connectionString = new ConnectionString("mongodb+srv://admin:QdMfmdMBj9AxKXbN@cluster0.xdbbu7t.mongodb.net/?retryWrites=true&w=majority");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString)
                .serverApi(ServerApi.builder().version(ServerApiVersion.V1).build()).build();
        MongoDatabase database;
         mongoClient = MongoClients.create(settings);

            database = mongoClient.getDatabase("database");

        collectionOfUsers = database.getCollection("users");
        collectionOfEvents = database.getCollection("events");
        collectionOfBooks = database.getCollection("books");
        collectionOfRecords = database.getCollection("records");
        collectionOfHabits = database.getCollection("habits");

    }

    public  void closeDBClient(){
        mongoClient.close();
    }

    public ObjectId saveUser(UserModel user){

        //should receive password as a plain String and encrypt it

        String originalPassword = user.getPassword();
         String salt = generateSaltValue(30);
        String encryptedPassword = encryptPassword(originalPassword,salt);

        Document document = new Document("username",user.getUsername())
                .append("password", encryptedPassword).append("salt",salt);


        collectionOfUsers.insertOne(document);


        return document.get("_id" , ObjectId.class);

    }

    public String generateSaltValue(int length){
        Random random = new SecureRandom();
        String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        StringBuilder finalValue = new StringBuilder(length);

        for(int i = 0; i < length; i++){
            finalValue.append(characters.charAt(random.nextInt(characters.length())));
        }



        return new String(finalValue);
    }
    public byte[] generateHashValue(char[]originalPassword, byte[] salt){
        int iterations = 10000;
        int keyLength = 256;

        PBEKeySpec spec = new PBEKeySpec(originalPassword, salt, iterations, keyLength);
        Arrays.fill(originalPassword, Character.MIN_VALUE);


        try{
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return secretKeyFactory.generateSecret(spec).getEncoded();

        }catch (NoSuchAlgorithmException | InvalidKeySpecException e){
            throw new AssertionError("Error when hashing a password: " + e.getMessage(), e);

        }finally {
            spec.clearPassword();
        }

    }

    public String encryptPassword(String originalPassword , String salt){
        String encrypted = null;
        byte[] securePassword = generateHashValue(originalPassword.toCharArray(), salt.getBytes());
        encrypted = Base64.getEncoder().encodeToString(securePassword);


        return encrypted;
    }

    public boolean verifyPassword(String retrievedPassword, String encrypted, String salt){

        //generate new encrypted password with the same salt

        String newEncrypted = encryptPassword(retrievedPassword,salt);


        return newEncrypted.equalsIgnoreCase(encrypted);

    }


    public  void saveEvent(EventModel event){
        Document document = new Document("user_id", SignUpController.user_id).append("title",event.getTitleString()).append("priority", event.getPriorityInt()).append("description", event.getDescriptionString())
                .append("date", event.getDate().get().toString()).append("startTime", convertToNumber(event.getStartTimeString())).append("endTime",event.getEndTimeString())
                .append("purpose",event.getPurposeString());

        collectionOfEvents.insertOne(document);

    }

    public int convertToNumber(String str){
        String [] arr = str.split(":");
        String num = arr[0] + arr[1];
        return Integer.parseInt(num);
    }
    public String convertBackToString(int num) {
        String str = String.valueOf(num);

        String timeString;
        if(str.length() == 3){
            timeString = str.charAt(0) + ":" + str.substring(1,3);
        }else{
            timeString = str.substring(0,2) + ":" + str.substring(2,4);
        }
        return timeString;
    }
    public void saveBook(BookModel book){
        Document document = new Document("user_idBooks",SignUpController.user_id)
                .append("title", book.getTitle()).append("author", book.getAuthor())
                .append("status", book.getStatus()).append("rate", book.getRate()).append("filePath",book.getFilePath());

        collectionOfBooks.insertOne(document);

    }
    public void updateBook(BookModel newBook){
        newBook.setUser_idBooks(SignUpController.user_id);
        Bson updates = Updates.combine(
                Updates.set("title", newBook.getTitle()),
                Updates.set("author", newBook.getAuthor()),
                Updates.set("status",newBook.getStatus()),
                Updates.set("rate" , newBook.getRate()),
                Updates.set("filePath" , newBook.getFilePath()));

        Bson query = and(eq("user_idBooks" , newBook.getUser_idBooks()),eq("title",newBook.getTitle()),
                eq("author",newBook.getAuthor()));



        collectionOfBooks.updateOne(query,updates);

    }


    public  void deleteBook(BookModel book){
        book.setUser_idBooks(SignUpController.user_id);
        Bson query = and(eq("user_idBooks" , book.getUser_idBooks()),eq("title",book.getTitle()),
                eq("author",book.getAuthor()));
        collectionOfBooks.deleteOne(query);


    }
    public void saveRecord(JournalModel record){
        Document document = new Document("user_idJournal",SignUpController.user_id)
                .append("title", record.getTitle()).append("text", record.getText())
                .append("date", record.getDate().toString());

        collectionOfRecords.insertOne(document);

    }

    public void saveHabit(HabitModel habitModel) {
        Document document = new Document("user_idHabits",SignUpController.user_id)
                .append("title", habitModel.getTitle()).append("weekIndex", habitModel.getWeekIndex())
                .append("mondaySelected", habitModel.isMondaySelected()).append("tuesdaySelected",habitModel.isTuesdaySelected())
                .append("wednesdaySelected", habitModel.isWednesdaySelected()).append("thursdaySelected", habitModel.isThursdaySelected())
                .append("fridaySelected", habitModel.isFridaySelected()).append("saturdaySelected",habitModel.isSaturdaySelected())
                .append("sundaySelected", habitModel.isSundaySelected()).append("progress", habitModel.getProgress());

        collectionOfHabits.insertOne(document);

    }
    public void updateRecord(JournalModel record){
        record.setUser_idJournal(SignUpController.user_id);
        Bson updates = Updates.combine(
                Updates.set("title", record.getTitle()),
                Updates.set("text", record.getText()),
                Updates.set("date",LocalDate.now().toString()));


        Bson query = and(eq("user_idJournal" , record.getUser_idJournal()),eq("title",record.getTitle()),
                eq("date",record.getDate().toString()));



        collectionOfRecords.updateOne(query,updates);

    }

    public void updateEvent(EventModel event, EventModel oldEvent) {
        event.setUser_id(SignUpController.user_id);

        Bson updates = Updates.combine(
                Updates.set("title", event.getTitleString()),
                Updates.set("purpose", event.getPurposeString()),
                Updates.set("priority",event.getPriorityInt()),
                Updates.set("date" , event.getDate().get().toString()),
                Updates.set("startTime" , convertToNumber(event.getStartTimeString())),
                Updates.set("endTime", event.getEndTimeString()),
                Updates.set("description", event.getDescriptionString()));

        //here it is not validated in the right way because fields already been changed

        Bson query = and(eq("user_id" , event.getUser_id()),eq("title",oldEvent.getTitleString()),
                eq("date",oldEvent.getDate().get().toString()),eq("startTime",convertToNumber(oldEvent.getStartTimeString())));
        collectionOfEvents.updateOne(query,updates);


    }

    public void updateHabit(HabitModel habit, String key) {
        habit.setUser_idHabits(SignUpController.user_id);
        Bson updates = null;

        switch(key){
            case "mon":
                 updates = Updates.combine(
                        Updates.set("mondaySelected",habit.isMondaySelected()),
                        Updates.set("progress",habit.getProgress()));
                 break;

            case "tue":
                 updates = Updates.combine(
                        Updates.set("tuesdaySelected",habit.isTuesdaySelected()),
                        Updates.set("progress",habit.getProgress()));
                break;

            case "wed":
                updates = Updates.combine(
                        Updates.set("wednesdaySelected",habit.isWednesdaySelected()),
                        Updates.set("progress",habit.getProgress()));
                break;

            case "thu":
                updates = Updates.combine(
                        Updates.set("thursdaySelected",habit.isThursdaySelected()),
                        Updates.set("progress",habit.getProgress()));
                break;

            case "fri":
                updates = Updates.combine(
                        Updates.set("fridaySelected",habit.isFridaySelected()),
                        Updates.set("progress",habit.getProgress()));
                break;

            case "sat":
                updates = Updates.combine(
                        Updates.set("saturdaySelected",habit.isSaturdaySelected()),
                        Updates.set("progress",habit.getProgress()));
                break;

            default:
                updates = Updates.combine(
                        Updates.set("sundaySelected",habit.isSundaySelected()),
                        Updates.set("progress",habit.getProgress()));

        }

        Bson query = and(eq("user_idHabits" , habit.getUser_idHabits()),eq("title",habit.getTitle()),eq("weekIndex",habit.getWeekIndex()));

        collectionOfHabits.updateOne(query,updates);




    }

    public  void deleteRecord(JournalModel record){
        record.setUser_idJournal(SignUpController.user_id);
        Bson query = and(eq("user_idJournal" , record.getUser_idJournal()),eq("title",record.getTitle()),
                eq("text",record.getText()));
        collectionOfRecords.deleteOne(query);


    }

    public void deleteEvent(EventModel eventModel) {
        eventModel.setUser_id(SignUpController.user_id);
        Bson query = and(eq("user_id" , eventModel.getUser_id()),eq("title",eventModel.getTitleString()),
                eq("date",eventModel.getDate().get().toString()),eq("startTime",convertToNumber(eventModel.getStartTimeString())));
        collectionOfEvents.deleteOne(query);

    }

    public  ArrayList<BookModel> fetchBooks(){
        ArrayList<BookModel> usersBooks = new ArrayList<>();
        List<Document> list = collectionOfBooks.find(eq("user_idBooks", SignUpController.user_id)).into(new ArrayList<>());
        for (Document book : list) {
            String title = book.getString("title");
            String author = book.getString("author");
            String status = book.getString("status");
            String rate = book.getString("rate");
            String filePath = book.getString("filePath");

            BookModel newItem = new BookModel(title,author,status,rate,filePath);
            usersBooks.add(newItem);


        }
        return usersBooks;
    }

    public ArrayList<JournalModel> fetchRecords() {
        ArrayList<JournalModel> userRecords = new ArrayList<>();
        List<Document> list = collectionOfRecords.find(eq("user_idJournal", SignUpController.user_id)).into(new ArrayList<>());
        for (Document record : list) {
            String title = record.getString("title");
            String text = record.getString("text");
            LocalDate date = LocalDate.parse(record.getString("date"));


           JournalModel newRecord = new JournalModel(title,text,date);
            userRecords.add(newRecord);


        }
        return userRecords;

    }

    public JournalModel getTextByTitleAndDate(String title, String date) {
       Document document = collectionOfRecords.find(and(eq("title", title),(eq("date", date)))).first();
        if (document != null) {
            return new JournalModel(document.getString("title"),document.getString("text"), LocalDate.parse(document.getString("date")));
        }
        return null;


    }


    public Document findUserByUsername(String username){


        return collectionOfUsers.find(eq("username", username)).first();


    }


    public ObservableList<EventModel> fetchEventsOnDate(LocalDate date){

        ObservableList<EventModel> specificEvents = FXCollections.observableArrayList();
        //sort by time
        List<Document> list = collectionOfEvents.find(and(eq("user_id", SignUpController.user_id),eq("date", date.toString()))).sort(ascending("startTime")).into(new ArrayList<>());
        for (Document event : list) {
            specificEvents.add(new EventModel(event.getString("title"),
                    (event.getInteger("priority")), event.getString("description"), LocalDate.parse(event.getString("date")),
                    convertBackToString(event.getInteger("startTime")),event.getString("endTime"), event.getString("purpose")));



        }
        //here should return sorted based on time
        return specificEvents;
    }

    public ObservableList<HabitModel> getHabitsForWeek(int i) {
        ObservableList<HabitModel> habitsOnWeek = FXCollections.observableArrayList();
        List<Document> list = collectionOfHabits.find(and(eq("user_idHabits", SignUpController.user_id),eq("weekIndex", i))).into(new ArrayList<>());
        for (Document habit : list) {
            habitsOnWeek.add(new HabitModel(habit.getString("title"),habit.getInteger("weekIndex"),habit.getBoolean("mondaySelected"),
                    habit.getBoolean("tuesdaySelected"),habit.getBoolean("wednesdaySelected"),habit.getBoolean("thursdaySelected"),
                    habit.getBoolean("fridaySelected"),habit.getBoolean("saturdaySelected"),habit.getBoolean("sundaySelected"),habit.getDouble("progress")));
        }
        return habitsOnWeek;
    }

    public ArrayList<HabitModel> getHabitsByTitle(String title) {
        ArrayList<HabitModel> habitsByTitle = new ArrayList<>();
        List<Document> list = collectionOfHabits.find(and(eq("user_idHabits", SignUpController.user_id),eq("title", title))).into(new ArrayList<>());
        for (Document habit : list) {
            habitsByTitle.add(new HabitModel(habit.getString("title"),habit.getInteger("weekIndex"),habit.getBoolean("mondaySelected"),
                    habit.getBoolean("tuesdaySelected"),habit.getBoolean("wednesdaySelected"),habit.getBoolean("thursdaySelected"),
                    habit.getBoolean("fridaySelected"),habit.getBoolean("saturdaySelected"),habit.getBoolean("sundaySelected"),habit.getDouble("progress")));
        }
        return habitsByTitle;
    }

    public  void changeScene(ActionEvent event, String fxmlFile, String title, String username) {
        Parent root = null;
        ScrollPane sp = new ScrollPane();

        if (username != null) {


            try {
                FXMLLoader fxmlLoader = new FXMLLoader(LaunchDoer.class.getResource(fxmlFile));
                root = fxmlLoader.load();

                MainPageController mainPage = fxmlLoader.getController();
                mainPage.setInfo(username);

            } catch (Exception ex) {
                System.out.println(ex.getMessage());

            }
        }else {

            try {

                FXMLLoader fxmlLoader = new FXMLLoader(LaunchDoer.class.getResource(fxmlFile));
                root = fxmlLoader.load();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        //sp.setContent(root);
        Scene scene = new Scene(root, 600, 400);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle(title);
        window.setScene(scene);
        window.show();

    }
    public  void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
    public  Optional<ButtonType> showConfirmationDialog(String title, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.setHeaderText(headerText);

        return alert.showAndWait();
    }

    public void setPriorityColor(EventModel event, HBox hBox){
        int priority = event.getPriorityInt();
        switch (priority){
            case 1 : hBox.setStyle("-fx-background-color: #8FC1D1");
                break;
            case 2 : hBox.setStyle("-fx-background-color:  #8D9F87 ");
                break;
            default: hBox.setStyle("-fx-background-color: #E9A971");
        }
    }
    public HBox createEventPane(EventModel eventItem) {
        Label titleLabel = new Label(eventItem.getTitleString());
        titleLabel.setStyle("-fx-font-size: 13");
        Label timeLabel = new Label(eventItem.getStartTimeString() + "-" + eventItem.getEndTimeString());
        timeLabel.setStyle("-fx-font-size: 13");



        HBox eventPane = new HBox();
        AnchorPane apLeft = new AnchorPane();
        HBox.setHgrow(apLeft, Priority.ALWAYS);
        AnchorPane apRight = new AnchorPane();

        eventPane.getChildren().addAll(apLeft,apRight);
        apLeft.getChildren().add(titleLabel);
        apRight.getChildren().add(timeLabel);

        eventPane.setPrefWidth(460);
        eventPane.setPadding(new Insets(5,5,5,5));

        setPriorityColor(eventItem,eventPane);

        return eventPane;
    }

    public  Pane getContent(String resource, String key) {


        FXMLLoader loader = new FXMLLoader(MongodbUtil.class.getResource(resource));


        try {
            Pane content;
            if(key.equals("book")){
                content = loader.load();
                BookDialog.controller = loader.getController();
                BookDialog.controller.setModel(BookDialog.bookModel);
                return content;


            }else if(key.equals("event")){
                content = loader.load();
                EventDialog.controller = loader.getController();
                EventDialog.controller.setModel(EventDialog.eventModel);

                return content;

            }

        } catch (IOException ex) {
            ex.printStackTrace();
            return new Pane();
        }
        return new Pane();

    }


    public void deleteAllHabits() {
        BasicDBObject document = new BasicDBObject();
        collectionOfHabits.deleteMany(document);
    }
}