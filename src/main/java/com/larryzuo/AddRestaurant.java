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

    DataBaseConnection dataBaseConnection;
    String hostName = "jdbc:mysql://35.225.192.66/cs370";
    String userName = "yuezuo";
    String passWord = "cs370";

    public AddRestaurant(final Stage adminStage){

        dataBaseConnection = new DataBaseConnection();

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

                dataBaseConnection.connectionOpen(hostName,userName,passWord);

                String preparedSQL = "insert into restaurant (rest_id, rest_name, rest_address, open_hour, " +
                        "close_hour, close_day) " +
                        "values(?,?,?,?,?,?)";

                try{

                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);
                    int tempID = Integer.parseInt(txtRestXCoord.getText()+txtRestYCoord.getText());
                    String dash = "-";
                    String street = "St";
                    String temp_rest_address = txtRestXCoord.getText()+dash+txtRestYCoord.getText()+street;

                    dataBaseConnection.PreStmt.setInt(1, tempID);
                    dataBaseConnection.PreStmt.setString(2,txtRestName.getText());
                    dataBaseConnection.PreStmt.setString(3, temp_rest_address);
                    dataBaseConnection.PreStmt.setString(4,txtOpenHour.getText());
                    dataBaseConnection.PreStmt.setString(5,txtCloseHour.getText());
                    dataBaseConnection.PreStmt.setString(6,txtCloseDay.getText());

                    dataBaseConnection.PreStmt.executeUpdate();

                    dataBaseConnection.connectionClose();

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
                    dataBaseConnection.connectionOpen(hostName,userName,passWord);

                    String sql = "select rest_id from restaurant ";

                    dataBaseConnection.myStmt = dataBaseConnection.myConn.createStatement();

                    dataBaseConnection.myRs = dataBaseConnection.myStmt.executeQuery(sql);

                    while(dataBaseConnection.myRs.next()){

                        tempSet.add(dataBaseConnection.myRs.getInt("rest_id"));
                    }

                    dataBaseConnection.connectionClose();
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
                    dataBaseConnection.connectionOpen(hostName,userName,passWord);

                    String preparedSQL = "insert into restaurant (rest_id, rest_name, rest_address, open_hour, " +
                            "close_day) values(?,?,?,?,?,?)";

                    String preparedSQL2 = "select rest_id from restaurant " +
                            "where rest_name = ?";

                    String preparedSQL3 = "select dish_id from rest_menu " +
                    "where rest_id = ?";

                    String preparedSQL4 = "insert into rest_menu (rest_id, dish_id) " +
                            "values(?,?)";


                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);
                    int tempID = Integer.parseInt(txtRestXCoord.getText()+txtRestYCoord.getText());
                    String dash = "-";
                    String street = "St";
                    String temp_rest_address = txtRestXCoord.getText()+dash+txtRestYCoord.getText()+street;
                    dataBaseConnection.PreStmt.setInt(1, tempID);
                    dataBaseConnection.PreStmt.setString(2,txtRestName.getText());
                    dataBaseConnection.PreStmt.setString(3, temp_rest_address);
                    dataBaseConnection.PreStmt.setString(4,txtOpenHour.getText());
                    dataBaseConnection.PreStmt.setString(5,txtCloseHour.getText());
                    dataBaseConnection.PreStmt.setString(6,txtCloseDay.getText());

                    dataBaseConnection.PreStmt.executeUpdate();

                    // check each dish under the selectedStoreName
                    // and assgin dishes to a new store

                    int selectedStoreID = 0;
                    int tempDishID = 0;

                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL2);

                    dataBaseConnection.PreStmt.setString(1,selectStoreName);

                    dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

                    while (dataBaseConnection.myRs.next()){

                        selectedStoreID = dataBaseConnection.myRs.getInt("rest_id");

                    }

                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL3);

                    dataBaseConnection.PreStmt.setInt(1,selectedStoreID);

                    dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

                    while(dataBaseConnection.myRs.next()){

                        tempDishID = dataBaseConnection.myRs.getInt("dish_id");

                        dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL4);

                        dataBaseConnection.PreStmt.setInt(1,tempID);
                        dataBaseConnection.PreStmt.setInt(2,tempDishID);

                        dataBaseConnection.PreStmt.executeUpdate();

                    }

                    dataBaseConnection.connectionClose();
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

                String preparedSQL5 = "select rest_id from restaurant";

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
                    dataBaseConnection.connectionOpen(hostName,userName,passWord);

                    dataBaseConnection.myStmt = dataBaseConnection.myConn.createStatement();

                    dataBaseConnection.myRs = dataBaseConnection.myStmt.executeQuery(preparedSQL5);

                    while(dataBaseConnection.myRs.next()){

                        tempSet.add(dataBaseConnection.myRs.getInt("rest_id"));
                    }

                    dataBaseConnection.connectionClose();
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
