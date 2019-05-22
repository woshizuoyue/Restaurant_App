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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class EditDish {

    Stage stage;
    Scene scene;
    VBox vbox;
    TextField txtDishName;
    TextField txtDishPrice;

    String restName;
    String dishName;
    double dishPrice;

    Connection myConn;
    PreparedStatement PreStmt;


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

        EditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                            "yuezuo", "cs370");

                    PreStmt = myConn.prepareStatement(
                            "update menu set dish_name = ? , dish_price = ? " +
                                    "where dish_name = ?"
                    );

                    PreStmt.setString(1,txtDishName.getText());
                    PreStmt.setString(2,txtDishPrice.getText());
                    PreStmt.setString(3,dishName);

                    PreStmt.executeUpdate();

                    myConn.close();
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
