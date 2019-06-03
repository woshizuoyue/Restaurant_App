package com.larryzuo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditDish {

    Stage stage;
    Scene scene;
    VBox vbox;
    TextField txtDishName;
    TextField txtDishPrice;

    String restName;
    String dishName;
    double dishPrice;

    DataBaseConnection dataBaseConnection;
    String hostName = "jdbc:mysql://35.225.192.66/cs370";
    String userName = "yuezuo";
    String passWord = "cs370";


    public EditDish(String rn, String dn, double dp, final Stage adminStage){

        restName = rn;
        dishName = dn;
        dishPrice = dp;

        Label dishNameLabel = new Label("Dish Name: ");
        txtDishName = new TextField(dishName);
        HBox hboxDishName = new HBox(dishNameLabel,txtDishName);
        hboxDishName.setAlignment(Pos.CENTER);

        Label dishPriceLabel = new Label("Dish Price: ");
        txtDishPrice = new TextField(String.valueOf(dishPrice));
        HBox hboxDishPrice = new HBox(dishPriceLabel, txtDishPrice);
        hboxDishPrice.setAlignment(Pos.CENTER);

        Button EditButton = new Button("Edit");
        HBox hboxEditButton = new HBox(EditButton);
        hboxEditButton.setAlignment(Pos.CENTER);

        dataBaseConnection = new DataBaseConnection();

        dataBaseConnection.connectionOpen(hostName,userName,passWord);

        final String preparedSQL = "update menu set dish_name =? , dish_price = ? " +
                "where dish_name = ?";

        EditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);

                    dataBaseConnection.PreStmt.setString(1,txtDishName.getText());
                    dataBaseConnection.PreStmt.setString(2,txtDishPrice.getText());
                    dataBaseConnection.PreStmt.setString(3,dishName);

                    dataBaseConnection.PreStmt.executeUpdate();

                    dataBaseConnection.connectionClose();
                }catch (Exception e){
                    e.printStackTrace();
                }

                stage.close();
                adminStage.close();
                new AdminRestMenu(restName);
            }
        });

        vbox = new VBox(hboxDishName, hboxDishPrice,hboxEditButton);
        vbox.setSpacing(20);

        scene = new Scene(vbox,300,150);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit Dish");
        stage.show();

    }
}
