package com.larryzuo;

import java.sql.*;
import java.util.ArrayList;

public class DataBaseConnection {

    public Connection myConn;
    private Statement myStmt;
    public PreparedStatement PreStmt;
    private ResultSet myRs;

    void connectionOpen(String hostName, String userName, String passWord){

        try {

            myConn = DriverManager.getConnection(hostName,userName,passWord);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    ArrayList<String> getResultSet(String sqlCommand, String columnName){

        ArrayList<String> tempStrList = new ArrayList<>();

        try {
            myStmt = myConn.createStatement();

            myRs = myStmt.executeQuery(sqlCommand);

            while(myRs.next()){
                tempStrList.add(myRs.getString(columnName));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return tempStrList;

    }

    void connectionClose(){

        try {
            myConn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
