package com.manage.lms;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class AdminLoginController {
    public TextField adminUsername;
    public TextField adminPasword;
    public Label signinError;
    public static Connection conn;

    boolean connected = false;
    public static FXMLLoader fxmlLoader;


    // reset button handler
    public void resetFields() {
        adminUsername.clear();
        adminPasword.clear();
        signinError.setText("");
    }

    // admin login button handler
    public void adminLoginButton() throws IOException {
        String serverName = "localhost";
        String mydatabase = "lms";
        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

        try {
            conn = DriverManager.getConnection(url,adminUsername.getText(),adminPasword.getText());
            connected = conn.isValid(1);
        } catch (SQLException ignored) {
        } finally {
            if(connected){
                signinError.setText("");
                BooksAdminController.loadBooks(conn);
                fxmlLoader = new  FXMLLoader(Main.class.getResource("adminPanel.fxml"));
                Main.getMainStage().setScene(new Scene(fxmlLoader.load()));

            }else{
                signinError.setText("Invalid Credentials or DB Inaccessible");
            }

        }

    }
    public void homepage() throws IOException {
        fxmlLoader = new FXMLLoader(Main.class.getResource("lms.fxml"));
        Main.getMainStage().setScene(new Scene(fxmlLoader.load()));
    }
}
