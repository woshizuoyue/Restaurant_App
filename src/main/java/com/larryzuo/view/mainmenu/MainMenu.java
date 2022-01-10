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

    private VBox vBox;
    private HBox hBox;
    private Stage stage;
    private Scene scene;
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
        adminItem.setOnAction(adminAction);
        quitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(startMenu,quitMenu);

        rootPane = new Pane();
        vBox = new VBox(2);

        vBox.getChildren().add(menuBar);

        rootPane.getChildren().addAll(vBox);
        scene = new Scene(rootPane,600,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        stage = primaryStage;
    }

    public VBox getvBox() {
        return vBox;
    }

    public void setvBox(VBox vBox) {
        this.vBox = vBox;
    }

    public HBox gethBox() {
        return hBox;
    }

    public void sethBox(HBox hBox) {
        this.hBox = hBox;
    }

    public Pane getRootPane() {
        return rootPane;
    }

    public void setRootPane(Pane rootPane) {
        this.rootPane = rootPane;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
