package com.larryzuo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Stack;

public class Main extends Application {

    private VBox myVBox;
    private HBox myHBox;
    private Stage myStage;
    private Scene myScene;
    private Pane rootPane;

    private Connection myConn;
    private Statement myStmt;
    private ResultSet myRs;
    private PreparedStatement PreStmt;

    private Stack<VBox> myStack = new Stack<>();
    private Stack<VBox> sortStack = new Stack<>();

    HBox hboxSortButton;
    HBox hboxSortByLocation;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Restaurant Application");

        Menu startMenu = new Menu("Start");
        Menu quitMenu = new Menu("Quit");

        MenuItem customItem = new MenuItem("Custom");
        MenuItem adminItem = new MenuItem("Admin");
        MenuItem exit = new MenuItem("Exit");

        startMenu.getItems().addAll(customItem,adminItem);
        quitMenu.getItems().add(exit);

        customItem.setOnAction(new CustAction());
        adminItem.setOnAction(new AdminAction());
        quitMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Platform.exit();
            }
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(startMenu,quitMenu);

        rootPane = new Pane();
        myVBox = new VBox(2);

        myVBox.getChildren().addAll(menuBar);

        rootPane.getChildren().addAll(myVBox);

        myScene = new Scene(rootPane,600,400);
        primaryStage.setScene(myScene);
        primaryStage.show();
        myStage = primaryStage;
    }

    // custom window;
    class CustAction implements EventHandler<ActionEvent> {

        private RadioButton[] radioButtons;
        private ToggleGroup radioGroup;
        private RestMenu rest_Menu;

        @Override
        public void handle(ActionEvent event){

            myStack.push(myVBox);
            myVBox = new VBox();
            myVBox.setPadding(new Insets(10,10,10,10));
            myVBox.setStyle("-fx-border-color: gray");
            myHBox = new HBox(3);
            rootPane = new Pane();


            //Buttons group;
            final Button cust_back = new Button("Back");
            cust_back.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {

                    myVBox = myStack.pop();
                    rootPane = new Pane();
                    rootPane.getChildren().addAll(myVBox);

                    myScene = new Scene(rootPane,600,400);
                    myStage.setScene(myScene);
                    myStage.show();
                }
            });

            // sort button action!!!;
            final Button sortByNameAsc = new Button("Sort By Name Asc");
            final Button sortByNameDesc = new Button("Sort By Name Desc");
            final Button submit = new Button("Submit");

            // sort by location;

            Label xCoord = new Label("X Coordinate: ");
            final TextField txtXCoord = new TextField();
            txtXCoord.setPrefWidth(50);
            HBox hboxXCoord = new HBox(xCoord, txtXCoord);
            hboxXCoord.setSpacing(5);
            hboxXCoord.setAlignment(Pos.CENTER);

            Label yCoord = new Label("Y Coordinate: ");
            final TextField txtYCoord = new TextField();
            txtYCoord.setPrefWidth(50);
            HBox hboxYCoord = new HBox(yCoord, txtYCoord);
            hboxYCoord.setSpacing(5);
            hboxYCoord.setAlignment(Pos.CENTER);


            Button sortByLocation = new Button("Sort By Location");
            hboxSortButton = new HBox(sortByLocation);
            hboxSortButton.setSpacing(5);
            hboxSortButton.setAlignment(Pos.CENTER);

            hboxSortByLocation = new HBox(hboxXCoord,hboxYCoord);

            hboxSortByLocation.setSpacing(5);
            hboxSortByLocation.setAlignment(Pos.CENTER);



            // rest_data in radio button; connection to the database;

            String holdStr;
            final ArrayList<String> radStr = new ArrayList<>();

            try{
                myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                        "yuezuo","cs370");
                myStmt = myConn.createStatement();

                myRs = myStmt.executeQuery("select * from restaurant");

                while(myRs.next()){

                    holdStr = myRs.getString("rest_name");

                    radStr.add(holdStr);
                }

                radioButtons = new RadioButton[radStr.size()];

                myConn.close();

            }catch (Exception e){
                e.printStackTrace();
            }

            radioGroup = new ToggleGroup();

            for(int i=0;i<radStr.size();i++)
            {
                radioButtons[i] = new RadioButton(radStr.get(i));
                radioButtons[i].setToggleGroup(radioGroup);
            }

            myVBox.setSpacing(2+radStr.size());

            // Buttons actionListener.
            submit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    for (RadioButton rb: radioButtons)
                    {
                        if(rb.isSelected())
                        {
                            rest_Menu = new RestMenu(rb.getText());
                        }
                    }

                }
            });

            sortByNameAsc.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    sortStack.push(myVBox);


                    ArrayList<String> tempStr = new ArrayList<>();

                    try{
                        myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                                "yuezuo","cs370");

                        myStmt = myConn.createStatement();

                        myRs = myStmt.executeQuery("select rest_name from restaurant " +
                                "group by rest_name " +
                                "order by rest_name ");

                        while (myRs.next()){

                            tempStr.add(myRs.getString("rest_name"));

                        }

                        radioButtons = new RadioButton[tempStr.size()];

                        myConn.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    radioGroup = new ToggleGroup();
                    for(int i=0;i<tempStr.size();i++)
                    {
                        radioButtons[i] = new RadioButton(tempStr.get(i));
                        radioButtons[i].setToggleGroup(radioGroup);
                    }

                    myHBox = new HBox(3);
                    myVBox = new VBox();
                    myVBox.setSpacing(2+tempStr.size());
                    rootPane = new Pane();
                    myVBox.setPadding(new Insets(10,10,10,10));
                    myVBox.setStyle("-fx-border-color: gray");

                    myHBox.getChildren().addAll(cust_back,sortByNameAsc,sortByNameDesc);
                    myVBox.getChildren().add(myHBox);
                    myVBox.getChildren().add(hboxSortByLocation);
                    myVBox.getChildren().add(hboxSortButton);
                    myVBox.getChildren().addAll(radioButtons);
                    myVBox.getChildren().add(submit);

                    rootPane.getChildren().add(myVBox);

                    myScene = new Scene(rootPane,400,600);

                    myStage.setScene(myScene);
                    myStage.show();


                }
            });

            sortByNameDesc.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    sortStack.push(myVBox);


                    ArrayList<String> tempStr = new ArrayList<>();

                    try{
                        myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120:3306/cs370",
                                "yuezuo","cs370");

                        myStmt = myConn.createStatement();

                        myRs = myStmt.executeQuery("select rest_name from restaurant " +
                                "group by rest_name " +
                                "order by rest_name desc ");

                        while (myRs.next()){

                            tempStr.add(myRs.getString("rest_name"));

                        }

                        radioButtons = new RadioButton[tempStr.size()];

                        myConn.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    radioGroup = new ToggleGroup();
                    for(int i=0;i<tempStr.size();i++)
                    {
                        radioButtons[i] = new RadioButton(tempStr.get(i));
                        radioButtons[i].setToggleGroup(radioGroup);
                    }

                    myHBox = new HBox(3);
                    myVBox = new VBox();
                    myVBox.setSpacing(2+tempStr.size());
                    rootPane = new Pane();
                    myVBox.setPadding(new Insets(10,10,10,10));
                    myVBox.setStyle("-fx-border-color: gray");

                    myHBox.getChildren().addAll(cust_back,sortByNameAsc,sortByNameDesc);
                    myVBox.getChildren().add(myHBox);
                    myVBox.getChildren().add(hboxSortByLocation);
                    myVBox.getChildren().add(hboxSortButton);
                    myVBox.getChildren().addAll(radioButtons);
                    myVBox.getChildren().add(submit);

                    rootPane.getChildren().add(myVBox);

                    myScene = new Scene(rootPane,400,600);

                    myStage.setScene(myScene);
                    myStage.show();

                }
            });

            sortByLocation.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    int xTarget = Integer.parseInt(txtXCoord.getText());
                    int yTarget = Integer.parseInt(txtYCoord.getText());
                    int tempXAdd;
                    int tempYAdd;
                    double rest_dist;

                    ArrayList<String> tempStr = new ArrayList<>();


                    try{
                        myConn = DriverManager.getConnection("jdbc:mysql://52.14.102.120/cs370",
                                "yuezuo","cs370");

                        myStmt = myConn.createStatement();

                        myRs = myStmt.executeQuery("select rest_address from restaurant");

                        while(myRs.next()) {

                            String strAddress = myRs.getString("rest_address");
                            String tempStrX = strAddress.split("-")[0];
                            String tempStrY = strAddress.split("-")[1].split("S")[0];

                            tempXAdd = Integer.parseInt(tempStrX);
                            tempYAdd = Integer.parseInt(tempStrY);

                            int restID = Integer.parseInt(tempStrX + tempStrY);

                            rest_dist = Math.sqrt((tempXAdd - xTarget) * (tempXAdd - xTarget) +
                                    (tempYAdd - yTarget) * (tempYAdd - yTarget));

                            PreStmt = myConn.prepareStatement(
                                    "update restaurant " +
                                            "set rest_dist = ? " +
                                            "where rest_id = ? "
                            );


                            PreStmt.setDouble(1,rest_dist);
                            PreStmt.setInt(2,restID);

                            PreStmt.executeUpdate();

                        }

                        myStmt = myConn.createStatement();

                        myRs = myStmt.executeQuery(

                                "select rest_name from restaurant " +
                                        "order by rest_dist "
                        );

                        while (myRs.next()){
                            tempStr.add(myRs.getString("rest_name"));
                        }

                        radioButtons = new RadioButton[tempStr.size()];

                        myConn.close();

                    }catch (Exception e){

                        e.printStackTrace();
                    }

                    radioGroup = new ToggleGroup();
                    for(int i=0;i<tempStr.size();i++)
                    {
                        radioButtons[i] = new RadioButton(tempStr.get(i));
                        radioButtons[i].setToggleGroup(radioGroup);
                    }

                    myHBox = new HBox(3);
                    myVBox = new VBox();
                    myVBox.setSpacing(2+tempStr.size());
                    rootPane = new Pane();
                    myVBox.setPadding(new Insets(10,10,10,10));
                    myVBox.setStyle("-fx-border-color: gray");

                    myHBox.getChildren().addAll(cust_back,sortByNameAsc,sortByNameDesc);
                    myVBox.getChildren().add(myHBox);
                    myVBox.getChildren().add(hboxSortByLocation);
                    myVBox.getChildren().add(hboxSortButton);
                    myVBox.getChildren().addAll(radioButtons);
                    myVBox.getChildren().add(submit);

                    rootPane.getChildren().add(myVBox);

                    myScene = new Scene(rootPane,400,600);

                    myStage.setScene(myScene);
                    myStage.show();

                }
            });




            myHBox.getChildren().addAll(cust_back,sortByNameAsc,sortByNameDesc);
            myVBox.getChildren().add(myHBox);
            myVBox.getChildren().add(hboxSortByLocation);
            myVBox.getChildren().add(hboxSortButton);
            myVBox.getChildren().addAll(radioButtons);
            myVBox.getChildren().add(submit);

            rootPane.getChildren().add(myVBox);

            myScene = new Scene(rootPane,400,600);

            myStage.setScene(myScene);
            myStage.show();

        }

    }

    // admin window;
    class AdminAction implements EventHandler<ActionEvent>{

        String userName = "";
        String passWord = "";
        @Override
        public void handle(ActionEvent event){

            createLogin();

        }

        void createLogin(){

            final Stage stage = new Stage();
            VBox vbox = new VBox(4);
            HBox hbox1 = new HBox(2);
            HBox hbox2 = new HBox(2);
            HBox hbox3 = new HBox(6);

            Label hint = new Label("Enter the username and password");
            final TextField uField = new TextField();
            final PasswordField pField = new PasswordField();
            uField.setPromptText("Enter the username");
            pField.setPromptText("Enter the password");

            Button submit = new Button("submit");
            Button cancel = new Button("cancel");

            submit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {


                    userName = uField.getText();
                    passWord = pField.getText();

                    if(userName.equals("yuezuo")&&passWord.equals("cs370")){

                        stage.close();

                        AdminRest adRest = new AdminRest();
                    }
                    else{

                        JOptionPane.showMessageDialog(null,"Incorrect username or password," +
                                " please try again.");
                        stage.close();
                    }

                }
            });
            cancel.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });

            hbox1.getChildren().addAll(new Label("username"),uField);
            hbox2.getChildren().addAll(new Label("password"),pField);
            hbox3.getChildren().addAll(submit,cancel);
            hbox1.setAlignment(Pos.CENTER);
            hbox2.setAlignment(Pos.CENTER);
            hbox3.setAlignment(Pos.CENTER);

            vbox.getChildren().addAll(hint,hbox1,hbox2,hbox3);
            vbox.setSpacing(10);

            stage.setScene(new Scene(vbox,300,150));
            stage.show();

        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
