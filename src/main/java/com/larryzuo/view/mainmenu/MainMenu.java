package com.larryzuo.view.mainmenu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {

    private static MainMenu mainMenu = new MainMenu();

    private VBox myVBox;
    private HBox myHBox;
    private Stage myStage;
    private Scene myScene;
    private Pane rootPane;

    private MainMenu(){};

    public static MainMenu getInstance() {

        return mainMenu;
    }


    public void setMainMenu(Stage primaryStage) {
        primaryStage.setTitle("Restaurant Application");

        Menu startMenu = new Menu("Start");
        Menu quitMenu = new Menu("Quit");

        MenuItem customItem = new MenuItem("Custom");
        MenuItem adminItem = new MenuItem("Admin");
        MenuItem exit = new MenuItem("Exit");

        startMenu.getItems().addAll(customItem,adminItem);
        quitMenu.getItems().add(exit);

        CustAction custAction = new CustAction();
        AdminAction adminAction = new AdminAction();

        customItem.setOnAction(custAction);
        adminItem.setOnAction(new AdminAction());
        quitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(startMenu,quitMenu);

        rootPane = new Pane();
        myVBox = new VBox(2);

        myVBox.getChildren().add(menuBar);

        rootPane.getChildren().addAll(myVBox);
        myScene = new Scene(rootPane,600,400);
        primaryStage.setScene(myScene);
        primaryStage.show();
        myStage = primaryStage;
    }
}
