package com.example.doerfinal2.dialogs;

import com.example.doerfinal2.MongodbUtil;
import com.example.doerfinal2.controllers.EventsViewController;
import com.example.doerfinal2.models.EventModel;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class AllEventsDialog extends Dialog<ButtonType> {

    private ObservableList<EventModel> allEvents;
    private MongodbUtil util = new MongodbUtil();

    private ScrollPane scrollPane = new ScrollPane();
    private EventsViewController controller = new EventsViewController();
    public AllEventsDialog(ObservableList<EventModel> allEvents) {
        super();
        this.setTitle("All Events");
        this.allEvents = allEvents;
        getDialogUi();

    }




    private void getDialogUi() {

        VBox pane = new VBox();


        pane.setPadding(new Insets(8,8,8,8));
        pane.setSpacing(6);
        VBox labelPanel = new VBox();
        labelPanel.setPrefWidth(460);
        labelPanel.setSpacing(3);
        labelPanel.alignmentProperty().setValue(Pos.TOP_CENTER);
        labelPanel.setPadding(new Insets(4,0,4,0));

        Label dateLabel = new Label(allEvents.get(0).getDate().get().toString());
        dateLabel.setStyle("-fx-font-size: 15");
        Label purposeLabel = new Label(allEvents.get(0).getPurposeString());
        purposeLabel.setStyle("-fx-font-size: 14");

        labelPanel.getChildren().addAll(dateLabel,purposeLabel);

        getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
        Button buttonOK = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        buttonOK.setStyle("-fx-font-size: 14.0;" +
                "    -fx-font-family: \"System\";" +
                "    -fx-background-color:#FFFFFF;" +
                "    -fx-border-color: #000000;" +
                "    -fx-border-radius : 5 ;" +
                "    -fx-background-radius: 5;");

        buttonOK.setText("OK");

        VBox eventPaneContainer = new VBox();
        eventPaneContainer.setSpacing(3);
        eventPaneContainer.setPadding(new Insets(5,5,5,5));

        pane.getChildren().addAll(labelPanel,eventPaneContainer);
        scrollPane.setContent(pane);

        addEventPane();
    }


    public void refreshDialogView(EventModel event) {

        //problem is here
        String purpose = event.getPurposeString();
        allEvents = util.fetchEventsOnDate(event.getDate().get());
        allEvents.removeIf(eventModel -> !eventModel.getPurposeString().equals(purpose));

        clearEventPane();
        addEventPane();


    }

    public void addEventPane() {

        EventModel reference = allEvents.get(0);
        EventModel oldEvent = new EventModel(reference.getTitleString(),reference.getPriorityInt(), reference.getDescriptionString(),
                reference.getDate().get(), reference.getStartTimeString(), reference.getEndTimeString(), reference.getPurposeString());


        for(EventModel eventItem : allEvents){
            HBox eventPane = createEventPane(eventItem);
            EventsViewController controller = new EventsViewController();
            eventPane.setOnMouseClicked(mouseEvent -> {
                controller.showDetails(eventItem, "showAll dialog");
                refreshDialogView(oldEvent);

            });
            returnEventsContainer().getChildren().add(eventPane);


        }


        getDialogPane().setContent(scrollPane);
    }

    public VBox returnEventsContainer(){
        VBox content = (VBox) scrollPane.getContent();
        return (VBox) content.getChildren().get(1);

    }
    public void clearEventPane() {
        returnEventsContainer().getChildren().clear();


    }

    public HBox createEventPane(EventModel eventItem) {
       return util.createEventPane(eventItem);




    }

    public void setPaneColor(EventModel eventItem, HBox eventPane){
        util.setPriorityColor(eventItem, eventPane);
    }

}
