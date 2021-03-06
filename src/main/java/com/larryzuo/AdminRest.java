package com.larryzuo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class AdminRest {

    Stage adminStage;
    Scene adminScene;
    VBox adminVbox;

    DataBaseConnection dataBaseConnection;
    RadioButton[] adminRadioButton;

    public AdminRest(){

        adminVbox = new VBox();

        createRestList();
        adminScene = new Scene(adminVbox,400,600);
        adminStage = new Stage();
        adminStage.setTitle("Administration");

        adminStage.setScene(adminScene);
        adminStage.show();
    }

    void createRestList(){

        final String hostName = "jdbc:mysql://35.225.192.66/cs370";
        final String userName = "yuezuo";
        final String passWord = "cs370";

        Button add = new Button("Add");
        Button delete = new Button("Delete");
        Button submit = new Button("Submit");
        Button refresh = new Button("Refresh");
        Button copy = new Button("Copy");


        HBox hbox1 = new HBox(add,delete,copy,refresh);
        hbox1.setSpacing(10);
        HBox hbox2 = new HBox(submit);
        hbox1.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);
        VBox vbox1 = new VBox(hbox1);
        vbox1.setSpacing(10);
        VBox vbox2 = new VBox(hbox2);

        ArrayList<String> restList;

        dataBaseConnection = new DataBaseConnection();

        dataBaseConnection.connectionOpen(hostName,userName,passWord);

        String sql = "select * from restaurant";

        restList = dataBaseConnection.getResultSet(sql,"rest_name");

        adminRadioButton = new RadioButton[restList.size()];

        dataBaseConnection.connectionClose();

        ToggleGroup tg = new ToggleGroup();

        for(int i=0;i<restList.size();i++){

            adminRadioButton[i] = new RadioButton(restList.get(i));
            adminRadioButton[i].setToggleGroup(tg);
        }

        vbox1.getChildren().addAll(adminRadioButton);

        // button group function;
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for(RadioButton rb: adminRadioButton){

                    if(rb.isSelected()){

                        new AdminRestMenu(rb.getText());
                    }
                }
            }
        });

        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new AddRestaurant(adminStage);
            }
        });

        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for(RadioButton rb: adminRadioButton){

                    if(rb.isSelected()){
                        int reply = JOptionPane.showConfirmDialog(null,"Are you sure to delete "
                                + rb.getText()+" restaurant?");

                        if(reply == JOptionPane.YES_OPTION){

                            try {

                                dataBaseConnection.connectionOpen(hostName,userName,passWord);

                                String preparedSQL = "select menu.dish_name, menu.dish_price " +
                                        "from menu,restaurant,rest_menu " +
                                        "where menu.dish_id = rest_menu.dish_id and " +
                                        "rest_menu.rest_id = restaurant.rest_id and " +
                                        "restaurant.rest_name = ? ";

                                String preparedSQL2 ="delete from restaurant " +
                                        "where rest_name = ?";

                                dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);

                                dataBaseConnection.PreStmt.setString(1,rb.getText());

                                dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

                                if(dataBaseConnection.myRs.next()){

                                    // need edit;
                                    JOptionPane.showMessageDialog(null,"There are dishes inside " +
                                            "delete all dishes first");
                                }

                                else{

                                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL2);

                                    dataBaseConnection.PreStmt.setString(1, rb.getText());

                                    dataBaseConnection.PreStmt.executeUpdate();

                                }

                                dataBaseConnection.connectionClose();
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    }
                }

                adminStage.close();
                new AdminRest();

            }
        });

        copy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for(RadioButton rb: adminRadioButton){

                    if(rb.isSelected()){

                        new AddRestaurant(rb.getText(),adminStage);

                    }
                }


            }
        });

        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                adminStage.close();

                new AdminRest();
            }
        });

        adminVbox.getChildren().addAll(vbox1,vbox2);

    }
}
