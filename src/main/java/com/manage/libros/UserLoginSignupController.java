package com.manage.libros;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;

public class UserLoginSignupController {

    public TextField signupUsername;
    public TextField signupAge;
    public TextField signupMail;
    public TextField signupPassword;
    public Label signupError;
    public Button signupButton;
    public TextField loginUsername;
    public TextField loginPassword;
    public Label loginError;
    FXMLLoader fxmlLoader;
    static Connection conn;
    static String username;


    // leads back to home
    public void homepage() throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("libros.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    //resets the fields
    public void resetFields() {
        signupUsername.setText("");
        signupAge.setText("");
        signupMail.setText("");
        signupPassword.setText("");
        signupError.setText("");
        loginUsername.setText("");
        loginPassword.setText("");
        loginError.setText("");
    }


    public void userLoginButton(){
        String serverName = "localhost";
        String mydatabase = "Libros";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
        System.out.println(loginUsername.getText());
        System.out.println(loginPassword.getText());
        try{
           conn = DriverManager.getConnection(url, loginUsername.getText(), loginPassword.getText());
           if(conn.isValid(1)){
               loginError.setText("");
               username = loginUsername.getText();
               BooksUserController.loadBooks(conn);
               fxmlLoader = new FXMLLoader(Main.class.getResource("userPanel.fxml"));
               Main.mainStage.setScene(new Scene(fxmlLoader.load()));
           }else{
               loginError.setText("Invalid credentials or inaccesible database");
           }
       }catch (IOException | SQLException e){
           e.printStackTrace();
        }

    }


    public boolean validate() {
        if (signupUsername.getText().contains(" ") || signupPassword.getText().contains(" ") || signupMail.getText().contains(" ") || signupAge.getText().contains(" ")) {
            signupError.setText("Fields can't contain spaces");
            return false;
        } else if (signupPassword.getLength() < 8) {
            signupError.setText("Password should be longer than 8 chars");
            return false;
        } else if (!signupMail.getText().contains("@") || signupMail.getText().endsWith(Arrays.toString(new String[]{".edu", ".com", ".in", ".org"}))) {
            signupError.setText("Enter valid mail");
            return false;
        } else if (signupMail.getText().isEmpty() || signupAge.getText().isEmpty() || signupUsername.getText().isEmpty() || signupPassword.getText().isEmpty()) {
            signupError.setText("All fields are mandatory");
            return false;
        }
        return true;
    }

    // adds a new user to Users table and generate a table of same name
    public void signUp() {
        String serverName = "localhost";
        String mydatabase = "Libros";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        boolean status = false;
        try {
            if(validate()){
                conn = DriverManager.getConnection(url, "UserManager", "password");

                // query to create user
                String createUAquery = "CREATE USER '" + signupUsername.getText()+"'@'"+serverName + "' IDENTIFIED BY '" + signupPassword.getText()+"'";

                // query to create table of same name
                String createTablequery = "CREATE TABLE " + signupUsername.getText() + "(BookName VARCHAR(20),Author VARCHAR(20),Year INT(4))";

                //query to grant permissions to the new user
                String grantPrivquery = "GRANT INSERT,DELETE, SELECT,UPDATE on "+mydatabase+"."+signupUsername.getText()+" TO "+signupUsername.getText()+"@"+serverName;
                System.out.println(grantPrivquery);
                //query to read original books table
                String grantReadBookquery = "GRANT SELECT on "+mydatabase+".Books"+" TO "+signupUsername.getText()+"@"+serverName;
                System.out.println(grantReadBookquery);

                //updating users table with new user
                PreparedStatement updateUsers = conn.prepareStatement("INSERT INTO Users VALUES(?,?,?,?,?)");

                // creating statements and executing them
                Statement createTable = conn.createStatement();
                createTable.execute(createTablequery);
                Statement createUA = conn.createStatement();
                createUA.execute(createUAquery);
                Statement grantPriv = conn.createStatement();
                grantPriv.execute(grantPrivquery);
                Statement grantRead = conn.createStatement();
                grantRead.execute(grantReadBookquery);

                // settings prepared statement vars
                updateUsers.setString(1, signupUsername.getText());
                updateUsers.setString(2, signupPassword.getText());
                updateUsers.setInt(3, Integer.parseInt(signupAge.getText()));
                updateUsers.setString(4, signupMail.getText());
                updateUsers.setInt(5, 0);
                updateUsers.execute();
                status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (status) {
                signupError.setText("Signup Successful! You can login now.");
            }
            else
                signupError.setText("Already exists");
        }

    }

    // leads to signup panel
    public void goToSignup() throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("userSignup.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    //leads to signin pane;
    public void goToSignin() throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("userLogin.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }


}


