package com.larryzuo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class AddRestaurant {
    Stage stage;
    Scene scene;
    VBox vbox;

    TextField txtRestXCoord;
    TextField txtRestName;
    TextField txtRestYCoord;
    TextField txtOpenHour;
    TextField txtCloseHour;
    TextField txtCloseDay;

    Connection myConn;
    PreparedStatement PreStmt;
    Statement myStmt;
    ResultSet myRs;

    public AddRestaurant(final Stage adminStage){

        Label labelRestXCoord = new Label("X coordinate: ");
        txtRestXCoord = new TextField();
        txtRestXCoord.setPrefWidth(80);
        Label labelRestYCoord = new Label("Y coordinate: ");
        txtRestYCoord = new TextField();
        txtRestYCoord.setPrefWidth(80);
        Button checkButton = new Button("check");
        HBox hboxXYCoord = new HBox(labelRestXCoord, txtRestXCoord,labelRestYCoord,txtRestYCoord,checkButton);
        hboxXYCoord.setSpacing(10);
        hboxXYCoord.setAlignment(Pos.CENTER);

        Label labelRestName = new Label("Restaurant Name: ");
        txtRestName = new TextField();
        HBox hboxRestName = new HBox(labelRestName,txtRestName);
        hboxRestName.setAlignment(Pos.CENTER);


        Label labelOpenHour = new Label("Open Hour: ");
        txtOpenHour = new TextField();
        HBox hboxOpenHour = new HBox(labelOpenHour,txtOpenHour);
        hboxOpenHour.setAlignment(Pos.CENTER);

        Label labelCloseHour = new Label("Close Hour: ");
        txtCloseHour = new TextField();
        HBox hboxCloseHour = new HBox(labelCloseHour,txtCloseHour);
        hboxCloseHour.setAlignment(Pos.CENTER);

        Label labelCloseDay = new Label("Close day: ");
        txtCloseDay = new TextField();
        HBox hboxCloseDay = new HBox(labelCloseDay,txtCloseDay);
        hboxCloseDay.setAlignment(Pos.CENTER);


        Button createButton = new Button("Create");
        HBox hboxButton = new HBox(createButton);
        hboxButton.setAlignment(Pos.CENTER);

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370","yuezuo","cs370");

                    PreStmt = myConn.prepareStatement(

                            "insert into restaurant (rest_id, rest_name, rest_address, open_hour, close_hour,close_day) " +
                                    "values (?,?,?,?,?,?)"
                    );
                    int tempID = Integer.parseInt(txtRestXCoord.getText()+txtRestYCoord.getText());
                    String dash = "-";
                    String street = "St";
                    String temp_rest_address = txtRestXCoord.getText()+dash+txtRestYCoord.getText()+street;
                    PreStmt.setInt(1, tempID);
                    PreStmt.setString(2,txtRestName.getText());
                    PreStmt.setString(3, temp_rest_address);
                    PreStmt.setString(4,txtOpenHour.getText());
                    PreStmt.setString(5,txtCloseHour.getText());
                    PreStmt.setString(6,txtCloseDay.getText());

                    PreStmt.executeUpdate();

                }catch (Exception e){
                    e.printStackTrace();
                }

                stage.close();
                adminStage.close();

                new AdminRest();
            }
        });

        checkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String xCoordStr ="";
                String yCoordStr ="";
                Set<Integer> tempSet = new HashSet<>();
                int restID;

                if(txtRestXCoord.getText().charAt(0) == '0'){
                    xCoordStr = txtRestXCoord.getText().substring(1);
                }
                else{
                    xCoordStr = txtRestXCoord.getText();
                }

                if(txtRestYCoord.getText().charAt(0) == '0'){
                    yCoordStr = txtRestYCoord.getText().substring(1);
                }
                else{
                    yCoordStr = txtRestYCoord.getText();
                }

                restID = Integer.parseInt(xCoordStr+yCoordStr);

                // add all rest_id from database to hashset,
                // and check the new rest_id is in the hashset or not,
                // if it is exist in hashset then pop up the warning message

                try{
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                            "yuezuo","cs370");

                    myStmt = myConn.createStatement();

                    myRs = myStmt.executeQuery(

                            "select rest_id from restaurant "
                    );

                    while(myRs.next()){

                        tempSet.add(myRs.getInt("rest_id"));
                    }

                    myConn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(tempSet.contains(restID)) JOptionPane.showMessageDialog(null,
                        "It already have the same address in other restaurant, change the address");

                else{

                    JOptionPane.showMessageDialog(null, "there is no same address in the list," +
                            "it is works!");
                }

            }
        });

        vbox = new VBox(hboxRestName,hboxXYCoord,hboxOpenHour,hboxCloseHour,hboxCloseDay, hboxButton);
        vbox.setSpacing(20);
        scene = new Scene(vbox,500,300);
        stage = new Stage();
        stage.setTitle("Create Restaurant");
        stage.setScene(scene);
        stage.show();


    }

    public AddRestaurant(final String selectStoreName, final Stage adminStage){

        Label labelRestXCoord = new Label("X coordinate: ");
        txtRestXCoord = new TextField();
        txtRestXCoord.setPrefWidth(80);
        Label labelRestYCoord = new Label("Y coordinate: ");
        txtRestYCoord = new TextField();
        txtRestYCoord.setPrefWidth(80);
        Button checkButton = new Button("check");
        HBox hboxXYCoord = new HBox(labelRestXCoord, txtRestXCoord,labelRestYCoord,txtRestYCoord,checkButton);
        hboxXYCoord.setSpacing(10);
        hboxXYCoord.setAlignment(Pos.CENTER);

        Label labelRestName = new Label("Restaurant Name: ");
        txtRestName = new TextField();
        HBox hboxRestName = new HBox(labelRestName,txtRestName);
        hboxRestName.setAlignment(Pos.CENTER);


        Label labelOpenHour = new Label("Open Hour: ");
        txtOpenHour = new TextField();
        HBox hboxOpenHour = new HBox(labelOpenHour,txtOpenHour);
        hboxOpenHour.setAlignment(Pos.CENTER);

        Label labelCloseHour = new Label("Close Hour: ");
        txtCloseHour = new TextField();
        HBox hboxCloseHour = new HBox(labelCloseHour,txtCloseHour);
        hboxCloseHour.setAlignment(Pos.CENTER);

        Label labelCloseDay = new Label("Close day: ");
        txtCloseDay = new TextField();
        HBox hboxCloseDay = new HBox(labelCloseDay,txtCloseDay);
        hboxCloseDay.setAlignment(Pos.CENTER);


        Button createButton = new Button("Create");
        HBox hboxButton = new HBox(createButton);
        hboxButton.setAlignment(Pos.CENTER);

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                            "yuezuo","cs370");

                    PreStmt = myConn.prepareStatement(

                            "insert into restaurant (rest_id, rest_name, rest_address, open_hour, close_hour,close_day) " +
                                    "values (?,?,?,?,?,?)"
                    );
                    int tempID = Integer.parseInt(txtRestXCoord.getText()+txtRestYCoord.getText());
                    String dash = "-";
                    String street = "St";
                    String temp_rest_address = txtRestXCoord.getText()+dash+txtRestYCoord.getText()+street;
                    PreStmt.setInt(1, tempID);
                    PreStmt.setString(2,txtRestName.getText());
                    PreStmt.setString(3, temp_rest_address);
                    PreStmt.setString(4,txtOpenHour.getText());
                    PreStmt.setString(5,txtCloseHour.getText());
                    PreStmt.setString(6,txtCloseDay.getText());

                    PreStmt.executeUpdate();

                    // check each dish under the selectedStoreName
                    // and assgin dishes to a new store

                    int selectedStoreID = 0;
                    int tempDishID = 0;
                    PreStmt = myConn.prepareStatement(
                            "select rest_id from restaurant " +
                                    "where rest_name = ? "
                    );

                    PreStmt.setString(1,selectStoreName);

                    myRs = PreStmt.executeQuery();

                    while (myRs.next()){

                        selectedStoreID = myRs.getInt("rest_id");

                    }

                    PreStmt = myConn.prepareStatement(
                            "select dish_id from rest_menu " +
                                    "where rest_id = ? "
                    );

                    PreStmt.setInt(1,selectedStoreID);

                    myRs = PreStmt.executeQuery();

                    while(myRs.next()){

                        tempDishID = myRs.getInt("dish_id");

                        PreStmt = myConn.prepareStatement(
                                "insert into rest_menu (rest_id, dish_id) " +
                                        "values(?,?)"
                        );

                        PreStmt.setInt(1,tempID);
                        PreStmt.setInt(2,tempDishID);

                        PreStmt.executeUpdate();

                    }

                    myConn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                stage.close();

                adminStage.close();

                new AdminRest();
            }
        });

        checkButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String xCoordStr ="";
                String yCoordStr ="";
                Set<Integer> tempSet = new HashSet<>();
                int restID;

                if(txtRestXCoord.getText().charAt(0) == '0'){
                    xCoordStr = txtRestXCoord.getText().substring(1);
                }
                else{
                    xCoordStr = txtRestXCoord.getText();
                }

                if(txtRestYCoord.getText().charAt(0) == '0'){
                    yCoordStr = txtRestYCoord.getText().substring(1);
                }
                else{
                    yCoordStr = txtRestYCoord.getText();
                }

                restID = Integer.parseInt(xCoordStr+yCoordStr);

                // add all rest_id from database to hashset,
                // and check the new rest_id is in the hashset or not,
                // if it is exist in hashset then pop up the warning message

                try{
                    myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                            "yuezuo","cs370");

                    myStmt = myConn.createStatement();

                    myRs = myStmt.executeQuery(

                            "select rest_id from restaurant "
                    );

                    while(myRs.next()){

                        tempSet.add(myRs.getInt("rest_id"));
                    }

                    myConn.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(tempSet.contains(restID)) JOptionPane.showMessageDialog(null,
                        "It already have the same address in other restaurant, change the address");

                else{

                    JOptionPane.showMessageDialog(null, "there is no same address in the list," +
                            "it is works!");
                }

            }
        });

        vbox = new VBox(hboxRestName,hboxXYCoord,hboxOpenHour,hboxCloseHour,hboxCloseDay, hboxButton);
        vbox.setSpacing(20);
        scene = new Scene(vbox,500,300);
        stage = new Stage();
        stage.setTitle("Create Restaurant");
        stage.setScene(scene);
        stage.show();


    }


}
