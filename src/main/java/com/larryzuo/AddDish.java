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
import java.util.ArrayList;
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

    DataBaseConnection dataBaseConnection;

    public AddDish(String str, final Stage adminMenuStage){

        final String hostName = "jdbc:mysql://35.225.192.66/cs370";
        final String userName = "yuezuo";
        final String passWord = "cs370";

        dataBaseConnection = new DataBaseConnection();

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

                String preparedSQL = "insert into menu( dish_id, dish_name, dish_price) " +
                        "values(?,?,?)";
                String preparedSQL2 = "select rest_id from restaurant " +
                        "where rest_name like ?";
                String preparedSQL3 = "insert into rest_menu " +
                        "(rest_id, dish_id) " +
                        "values(?,?)";

                dataBaseConnection.connectionOpen(hostName,userName,passWord);

                try {

                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);

                    tempDishID = Integer.parseInt(txtDishID.getText());

                    dataBaseConnection.PreStmt.setInt(1,tempDishID);
                    dataBaseConnection.PreStmt.setString(2,txtDishName.getText());
                    dataBaseConnection.PreStmt.setString(3,txtDishPrice.getText());

                    dataBaseConnection.PreStmt.executeUpdate();

                    // add rest_id and dish_id into rest_menu table;

                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL2);

                    String tempRestName = restName.split("\\(")[0];

                    dataBaseConnection.PreStmt.setString(1,tempRestName+"%");

                    dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

                    while (dataBaseConnection.myRs.next()){

                        restID = dataBaseConnection.myRs.getInt("rest_id");

                        dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL3);
                        dataBaseConnection.PreStmt.setInt(1, restID);
                        dataBaseConnection.PreStmt.setInt(2, tempDishID);
                        dataBaseConnection.PreStmt.executeUpdate();
                    }
                    dataBaseConnection.connectionClose();
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

                ArrayList<Integer> tempList;

                String sql = "select dish_id from menu ";

                dataBaseConnection.connectionOpen(hostName,userName,passWord);

                tempList = dataBaseConnection.getIntegerResultSet(sql,"dish_id");

                while (!tempList.isEmpty()){

                    tempSet.add(tempList.remove(0));
                }

                dataBaseConnection.connectionClose();

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
