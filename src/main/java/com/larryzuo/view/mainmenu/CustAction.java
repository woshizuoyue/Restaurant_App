package com.larryzuo.view.mainmenu;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

public class CustAction implements EventHandler {

    private Stack<VBox> myStack = new Stack<>();
    private Stack<VBox> sortStack = new Stack<>();

    private MainMenu mainMenu;

    @Override
    public void handle(Event event) {

        mainMenu = MainMenu.getInstance();
        VBox custVBox = mainMenu.getvBox();
        HBox custHBox = mainMenu.gethBox();
        Pane custRootPane = mainMenu.getRootPane();
        Scene custScene = mainMenu.getScene();
        Stage custStage = mainMenu.getStage();

        myStack.push(custVBox);

        custVBox = new VBox();
        custHBox = new HBox(3);
        custRootPane = new Pane();

        custVBox.setPadding(new Insets(10,10,10,10));
        custVBox.setStyle("-fx-border-color: gray");

        custVBox.getChildren().add(custHBox);
        custRootPane.getChildren().add(custVBox);
        custScene = new Scene(custRootPane, 400, 600);
        custStage.setScene(custScene);
        custStage.show();
    }
}
