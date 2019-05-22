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

import java.sql.*;

public class EditInformation {

    Stage stage;
    Scene scene;
    VBox vbox;
    TextField txtRestName;
    TextField txtOpenHour;
    TextField txtCloseHour;
    TextField txtCloseDay;

    String restName;
    String openHour;
    String closeHour;
    String closeDay;

    Connection myConn;
    PreparedStatement PreStmt;
    ResultSet myRs;

    public EditInformation(String rn, final Stage adminStage){

        restName = rn;

        try{
            myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                    "yuezuo","cs370");

            PreStmt = myConn.prepareStatement(

                    "select open_hour, close_hour,close_day " +
                            "from restaurant " +
                            "where rest_name = ?"
            );

            PreStmt.setString(1,restName);

            myRs = PreStmt.executeQuery();

            while (myRs.next()){

                openHour = myRs.getString("open_hour");
                closeHour = myRs.getString("close_hour");
                closeDay = myRs.getString("close_day");
            }

            myConn.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        Label restNameLabel = new Label("Restaurant Name: ");
        txtRestName = new TextField(restName);
        HBox hboxRestName = new HBox(restNameLabel,txtRestName);
        hboxRestName.setAlignment(Pos.CENTER);

        Label openHourLabel = new Label("Open Hour: ");
        txtOpenHour = new TextField(openHour);
        HBox hboxOpenHour = new HBox(openHourLabel, txtOpenHour);
        hboxOpenHour.setAlignment(Pos.CENTER);

        Label closeHourLabel = new Label("Close Hour: ");
        txtCloseHour = new TextField(closeHour);
        HBox hboxCloseHour = new HBox(closeHourLabel, txtCloseHour);
        hboxCloseHour.setAlignment(Pos.CENTER);

        Label closeDayLabel = new Label("Close Day: ");
        txtCloseDay = new TextField(closeDay);
        HBox hboxCloseDay = new HBox(closeDayLabel, txtCloseDay);
        hboxCloseDay.setAlignment(Pos.CENTER);


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

                            "update restaurant set rest_name = ?, " +
                                    "open_hour = ?, close_hour = ?, close_day = ? " +
                                    "where rest_name = ?"
                    );

                    PreStmt.setString(1,txtRestName.getText());
                    PreStmt.setString(2,txtOpenHour.getText());
                    PreStmt.setString(3,txtCloseHour.getText());
                    PreStmt.setString(4,txtCloseDay.getText());
                    PreStmt.setString(5,restName);

                    PreStmt.executeUpdate();

                    myConn.close();
                }catch (Exception e){

                    e.printStackTrace();
                }

                stage.close();
                adminStage.close();

                new AdminRestMenu(txtRestName.getText());

            }
        });

        vbox = new VBox(hboxRestName,hboxOpenHour,hboxCloseHour,hboxCloseDay, hboxEditButton);
        vbox.setSpacing(20);

        scene = new Scene(vbox, 400,300);
        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Information Edition");
        stage.show();

    }
}
