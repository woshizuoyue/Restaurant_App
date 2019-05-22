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

import javax.swing.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AddDish {

    Stage stage;
    Scene scene;
    VBox vbox;
    String restName;
    int tempDishID;
    int restID;

    TextField txtDishID;
    TextField txtDishName;
    TextField txtDishPrice;

    Connection myConn;
    PreparedStatement PreStmt;
    Statement myStmt;
    ResultSet myRs;

    public AddDish(String str, final Stage adminMenuStage){

        restName = str;

        Label labelDishID = new Label("Dish ID: ");
        txtDishID = new TextField();
        txtDishID.setPrefWidth(100);
        Button checkButton = new Button("Check");
        HBox hboxDishID = new HBox(labelDishID,txtDishID,checkButton);
        hboxDishID.setSpacing(20);
        hboxDishID.setAlignment(Pos.CENTER);

        Label labelDishName = new Label("Dish Name: ");
        txtDishName = new TextField();
        HBox hboxDishName = new HBox(labelDishName,txtDishName);
        hboxDishName.setAlignment(Pos.CENTER);

        Label labelDishPrice = new Label("Dish Price: ");
        txtDishPrice = new TextField();
        HBox hboxDishPrice = new HBox(labelDishPrice,txtDishPrice);
        hboxDishPrice.setAlignment(Pos.CENTER);

        Button create = new Button("Create");
        HBox hboxCreateButton = new HBox(create);
        hboxCreateButton.setAlignment(Pos.CENTER);

        vbox = new VBox(hboxDishID,hboxDishName,hboxDishPrice,hboxCreateButton);
        vbox.setSpacing(20);

        // get data insert into database;

        create.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                            "yuezuo", "cs370");

                    PreStmt = myConn.prepareStatement(

                            "insert into menu( dish_id, dish_name, dish_price) " +
                                    "values(?,?,?)"
                    );

                    tempDishID = Integer.parseInt(txtDishID.getText());
                    PreStmt.setInt(1,tempDishID);
                    PreStmt.setString(2,txtDishName.getText());
                    PreStmt.setString(3,txtDishPrice.getText());

                    PreStmt.executeUpdate();

                    // add rest_id and dish_id into rest_menu table;

                    PreStmt = myConn.prepareStatement(

                            "select rest_id from restaurant " +
                                    "where rest_name like ?"
                    );

                    String tempRestName = restName.split("\\(")[0];

                    PreStmt.setString(1,tempRestName+"%");

                    myRs = PreStmt.executeQuery();

                    while (myRs.next()) {

                        restID = myRs.getInt("rest_id");


                        PreStmt = myConn.prepareStatement(

                                "insert into rest_menu " +
                                        "(rest_id, dish_id) " +
                                        "values(?,?)"
                        );

                        PreStmt.setInt(1, restID);
                        PreStmt.setInt(2, tempDishID);
                        PreStmt.executeUpdate();
                    }

                    myConn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                stage.close();
                adminMenuStage.close();

                new AdminRestMenu(restName);

            }
        });

        checkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Set<Integer> tempSet = new HashSet<>();

                try{
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                            "yuezuo","cs370");

                    myStmt = myConn.createStatement();

                    myRs = myStmt.executeQuery("select dish_id from menu ");

                    while (myRs.next()){

                        tempSet.add(myRs.getInt("dish_id"));
                    }

                    myConn.close();
                }catch (Exception e){

                    e.printStackTrace();
                }

                if(tempSet.contains(Integer.parseInt(txtDishID.getText()))){

                    JOptionPane.showMessageDialog(null,"There is the same dish_id in the list" +
                            "please change a new dish_id");
                }
                else{

                    JOptionPane.showMessageDialog(null, "No same dish_id in the list, " +
                            "it is works!");
                }

            }
        });



        scene = new Scene(vbox,300,300);
        stage = new Stage();
        stage.setTitle("Dish Creation");
        stage.setScene(scene);
        stage.show();

    }


}
