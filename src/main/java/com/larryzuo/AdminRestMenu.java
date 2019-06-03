package com.larryzuo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.ArrayList;

public class AdminRestMenu {

    Stage stage;
    Scene scene;
    VBox vbox;
    HBox hbox1, hbox2,hboxLabelName;
    String restName = "";
    RadioButton[] menuRadioButton;

    DataBaseConnection dataBaseConnection;


    public AdminRestMenu(String str){

        final String hostName = "jdbc:mysql://35.225.192.66/cs370";
        final String userName = "yuezuo";
        final String passWord = "cs370";

        restName = str;
        vbox = new VBox();
        scene = new Scene(vbox,500,600);
        stage = new Stage();

        Button infoEdit = new Button("Information Edit");
        Button addDish = new Button("Add Dish");
        Button deleteDish = new Button("Delete Dish");
        Button editDish = new Button("Edit Dish");

        ArrayList<String> itemStr = new ArrayList<>();
        ArrayList<String> infoStr = new ArrayList<>();

        dataBaseConnection = new DataBaseConnection();

        Label restLabelName = new Label(restName);

        hbox1 = new HBox(infoEdit);
        hbox2 = new HBox(addDish,deleteDish,editDish);
        hboxLabelName = new HBox(restLabelName);
        hboxLabelName.setAlignment(Pos.CENTER);
        hbox1.setAlignment(Pos.CENTER);
        hbox2.setAlignment(Pos.CENTER);
        hbox1.setSpacing(80);
        hbox2.setSpacing(10);

        Label dish_price = new Label("Dish_Name/Dish_Price");

        vbox.getChildren().addAll(hbox1,hbox2,hboxLabelName);
        vbox.setSpacing(10);

        dataBaseConnection.connectionOpen(hostName,userName,passWord);

        String preparedSQL = "select menu.dish_name, menu.dish_price " +
                "from menu,restaurant,rest_menu " +
                "where menu.dish_id = rest_menu.dish_id and " +
                "rest_menu.rest_id = restaurant.rest_id and " +
                "restaurant.rest_name = ? ";
        String preparedSQL2 = "select rest_address, open_hour, close_hour,close_day " +
                "from restaurant " +
                "where rest_name = ? ";

        try {

            dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);

            dataBaseConnection.PreStmt.setString(1,restName);

            dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

            while(dataBaseConnection.myRs.next()){
                String menuName = dataBaseConnection.myRs.getString("dish_name");
                double price = dataBaseConnection.myRs.getDouble("dish_price");
                itemStr.add(menuName+" ----------- "+price);
            }

            dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL2);

            dataBaseConnection.PreStmt.setString(1,restName);

            dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

            while (dataBaseConnection.myRs.next()){
                String info = dataBaseConnection.myRs.getString("rest_address")+" -------- "+"Open: "+
                        dataBaseConnection.myRs.getString("open_hour")+
                        ", "+"Close: "+dataBaseConnection.myRs.getString("close_hour")+
                        ", "+"Close Day: "+dataBaseConnection.myRs.getString("close_day");
                infoStr.add(info);
            }

            dataBaseConnection.connectionClose();
        }catch (Exception e){
            e.printStackTrace();
        }

        menuRadioButton = new RadioButton[itemStr.size()];
        ToggleGroup tg = new ToggleGroup();

        for (int i=0;i<itemStr.size();i++){

            menuRadioButton[i] = new RadioButton(itemStr.get(i));
            menuRadioButton[i].setToggleGroup(tg);
        }

        HBox infoHBox = new HBox(new Label(infoStr.get(0)));
        infoHBox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(infoHBox,dish_price);
        vbox.getChildren().addAll(menuRadioButton);

        // buttons functions
        addDish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                new AddDish(restName,stage);
            }
        });

        deleteDish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                for(RadioButton rb: menuRadioButton){

                    if(rb.isSelected()){

                        int reply = JOptionPane.showConfirmDialog(null,
                                "Are you sure to delete the dish?");


                        if(reply == JOptionPane.YES_OPTION){

                            String tempStr = "";

                            for(int i=0;i<rb.getText().length();i++){

                                if(rb.getText().charAt(i) == '-'){

                                    if(i<10){
                                        tempStr = rb.getText().substring(0,i);
                                    }
                                    else {
                                        tempStr = rb.getText().substring(0, i - 10);
                                    }
                                }
                            }

                            String dishName = tempStr;

                            int dishID = 0;

                            String preparedSQL = "select dish_id from menu " +
                                    "where dish_name =?";
                            String preparedSQL2 = "delete from rest_menu " +
                                    "where dish_id = ?";
                            String preparedSQL3 = "select rest_id from rest_menu where dish_id = ?";

                            String preparedSQL4 = "delete from menu where dish_id =?";

                            dataBaseConnection.connectionOpen(hostName,userName,passWord);

                            try {
                                dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);
                                dataBaseConnection.PreStmt.setString(1, dishName);
                                dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

                                while (dataBaseConnection.myRs.next()){

                                    dishID = dataBaseConnection.myRs.getInt("dish_id");
                                }

                                dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL2);
                                dataBaseConnection.PreStmt.setInt(1,dishID);
                                dataBaseConnection.PreStmt.executeUpdate();

                                // check the all restaurants are still have the same dish with the deleted dish,
                                // if yes, the deleted dish cannot be deleted from menu table,
                                // if no, the deleted dish should be deleted from menu table.

                                dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL3);
                                dataBaseConnection.PreStmt.setInt(1,dishID);
                                dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

                                // if no restaurant has the same dish
                                // then delete the dish;

                                if(!dataBaseConnection.myRs.next()){

                                    dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL4);

                                    dataBaseConnection.PreStmt.setInt(1,dishID);

                                    dataBaseConnection.PreStmt.executeUpdate();
                                }

                                dataBaseConnection.connectionClose();

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            stage.close();
                            new AdminRestMenu(restName);
                        }

                    }
                }
            }
        });

        editDish.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String dishName;
                double dishPrice;
                for(RadioButton rb: menuRadioButton){

                    if(rb.isSelected()){

                        String sub = "";
                        String sub_price = "";
                        for(int i = 0;i<rb.getText().length();i++){

                            if(rb.getText().charAt(i) == '-'){


                                if(i<10){
                                    sub = rb.getText().substring(0,i);
                                }

                                else {
                                    sub = rb.getText().substring(0, i - 10);
                                }
                                    sub_price = rb.getText().substring(i + 2);

                            }
                        }

                        dishName = sub;
                        dishPrice = Double.parseDouble(sub_price);
                        new EditDish(restName, dishName, dishPrice, stage);

                    }
                }

            }
        });

        infoEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                new EditInformation(restName, stage);
            }
        });

        stage.setScene(scene);
        stage.setTitle("Admin Restaurant Menu");

        stage.show();


    }
}
