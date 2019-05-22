package com.larryzuo;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.*;
import java.sql.*;
import java.util.ArrayList;

public class RestMenu {

    Stage menuStage;
    Scene menuScene;
    Pane menuPane;
    VBox menuVBox;

    String restName;

    Connection myConn;
    PreparedStatement myPreStmt;
    ResultSet myRs;

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

        try {
            myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370", "yuezuo", "cs370");

            myPreStmt = myConn.prepareStatement(

                    "select menu.dish_name, menu.dish_price " +
                    "from menu,restaurant,rest_menu " +
                    "where menu.dish_id = rest_menu.dish_id and " +
                    "rest_menu.rest_id = restaurant.rest_id and " +
                            "restaurant.rest_name = ? ");

            myPreStmt.setString(1,restName);

            myRs = myPreStmt.executeQuery();

            while (myRs.next()){

               String menuName = myRs.getString("dish_name");
               double price = myRs.getDouble("dish_price");

               itemStr.add(menuName+" ----------- "+price);
            }

            myPreStmt = myConn.prepareStatement(

                    "select rest_address, open_hour, close_hour, close_day " +
                            "from restaurant " +
                            "where rest_name = ? "
            );

            myPreStmt.setString(1,restName);

            myRs = myPreStmt.executeQuery();

            while(myRs.next()){

                String address = myRs.getString("rest_address");
                String openHour = myRs.getString("open_hour");
                String closeHour = myRs.getString("close_hour");
                String closeDay = myRs.getString("close_day");

                String info = address+" ------ "+"Open: "+openHour+", "+"Close: "+closeHour+", "+"Close Day: "+closeDay;
                infoStr.add(info);
            }

            myConn.close();
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
