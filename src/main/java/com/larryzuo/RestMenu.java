package com.larryzuo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.util.ArrayList;

public class RestMenu {

    Stage menuStage;
    Scene menuScene;
    Pane menuPane;
    VBox menuVBox;

    String restName;
    DataBaseConnection dataBaseConnection;

    final String hostName = "jdbc:mysql://35.225.192.66/cs370";
    final String userName = "yuezuo";
    final String passWord = "cs370";

    public RestMenu(String str){

        restName = str;
        menuPane = new Pane();

        createMenu();
        menuScene = new Scene(menuPane,450,600);
        menuStage = new Stage();
        menuStage.setTitle(restName);
        menuStage.setScene(menuScene);

        menuStage.show();
    }

    void createMenu(){

        Label menuNameLabel = new Label(restName);
        HBox tempHBox = new HBox(menuNameLabel);
        tempHBox.setAlignment(Pos.CENTER);
        Label menuNamewithPrice = new Label("Dish_Name/Dish_Price");
        ArrayList<String> itemStr = new ArrayList<>();
        ArrayList<String> infoStr = new ArrayList<>();

        dataBaseConnection = new DataBaseConnection();
        dataBaseConnection.connectionOpen(hostName,userName,passWord);
        String preparedSQL = "select menu.dish_name, menu.dish_price " +
                "from menu, restaurant, rest_menu " +
                "where menu.dish_id = rest_menu.dish_id and " +
                "rest_menu.rest_id = restaurant.rest_id and " +
                "restaurant.rest_name = ? ";
        String preparedSQL2 = "select rest_address, open_hour, close_hour, close_day " +
                "from restaurant where rest_name = ? ";

        // need to refactoring;
        try {
            dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL);

            dataBaseConnection.PreStmt.setString(1,restName);

            dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

            while (dataBaseConnection.myRs.next()){

               String menuName = dataBaseConnection.myRs.getString("dish_name");
               double price = dataBaseConnection.myRs.getDouble("dish_price");

               itemStr.add(menuName+" ----------- "+price);
            }

            dataBaseConnection.PreStmt = dataBaseConnection.myConn.prepareStatement(preparedSQL2);

            dataBaseConnection.PreStmt.setString(1,restName);

            dataBaseConnection.myRs = dataBaseConnection.PreStmt.executeQuery();

            while(dataBaseConnection.myRs.next()){

                String address = dataBaseConnection.myRs.getString("rest_address");
                String openHour = dataBaseConnection.myRs.getString("open_hour");
                String closeHour = dataBaseConnection.myRs.getString("close_hour");
                String closeDay = dataBaseConnection.myRs.getString("close_day");

                String info = address+" ------ "+"Open: "+openHour+", "+"Close: "+closeHour+", "+"Close Day: "+closeDay;
                infoStr.add(info);
            }

            dataBaseConnection.connectionClose();
        }catch (Exception e){

            e.printStackTrace();
        }


        menuVBox = new VBox();

        Label[] item = new Label[itemStr.size()];
        Label infoLabel = new Label(infoStr.get(0));

        for(int i=0;i<itemStr.size();i++){

            item[i] = new Label(itemStr.get(i));
        }

        HBox tempHBoxInfo = new HBox(infoLabel);
        tempHBoxInfo.setAlignment(Pos.CENTER);
        menuVBox.getChildren().addAll(tempHBox,tempHBoxInfo,menuNamewithPrice);
        menuVBox.getChildren().addAll(item);
        menuVBox.setSpacing(10);

        menuPane.getChildren().add(menuVBox);

    }
}
