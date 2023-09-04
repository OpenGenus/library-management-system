package com.manage.libros;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewBookController {
    public Button newBookCancel;
    public Button newBookAdd;
    public Label newBookError;
    public TextField newBookStocks;
    public TextField newBookYear;
    public TextField newBookAuthor;
    public TextField newBookName;
    public Scene panelScene;
    public static Connection connection;
    public static TableView<Books> booksTable;

    // loads scene for adding book
    void addBook(Connection conn,TableView<Books> adminBooksTable){
        booksTable = adminBooksTable;
        connection = conn;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newBook.fxml"));
            panelScene = Main.getMainStage().getScene();
            Main.mainStage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // updating DB with new book detail
    public void Book2DB() throws SQLException, IOException {
        PreparedStatement newBook = connection.prepareStatement("INSERT INTO Books VALUES(?,?,?,?)");
        newBook.setString(1,newBookName.getText());
        newBook.setString(2,newBookAuthor.getText());
        newBook.setInt(3,Integer.parseInt(newBookYear.getText()));
        newBook.setInt(4,Integer.parseInt(newBookStocks.getText()));
        newBook.execute();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminPanel.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }

    // cancel button back to admin panel
    public void cancelNewBook() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("adminPanel.fxml"));
        Main.mainStage.setScene(new Scene(fxmlLoader.load()));
    }
}
