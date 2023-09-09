package com.manage.lms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class BooksAdminController implements Initializable {

    @FXML
    private TableView<Books> adminBooksTable;
    static ObservableList<Books> booksObservableList;
    @FXML
    public TableColumn<Books, String> bname;
    @FXML
    public TableColumn<Books, String> bauthor;
    @FXML
    public TableColumn<Books, Integer> byear;
    @FXML
    public TableColumn<Books, Integer> bstocks;
    public static Connection connection;
    private static ObservableList<Books> bookList;
    public static FXMLLoader fxmlLoader;
    public BooksAdminController() {
        bookList = FXCollections.observableArrayList();
    }

    // loads books from mysql db
    public static void loadBooks(Connection conn) {
            String getDataQuery = "SELECT * FROM Books";
            connection = conn;
            booksObservableList = FXCollections.observableArrayList();
            try{
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery(getDataQuery);
                while(rs.next()){
                    Books book = new Books(rs.getString("Book Name"),rs.getString("Author"),rs.getInt("Year"),rs.getInt("Stocks"));
                    booksObservableList.add(book);
                    System.out.println(rs.getString("Book Name"));
                    System.out.println(rs.getString("Author"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
    }

    // Closes the connection and returns to home
    public void logoutAdmin(){
        try {
            connection.close();
            fxmlLoader = new FXMLLoader(Main.class.getResource("lms.fxml"));
            Main.getMainStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //inserts the book
    @FXML
    void insertBook() {
        NewBookController nbc = new NewBookController();
        nbc.addBook(connection,adminBooksTable);
        updateTable();      // broken, doesnt update UI
    }


    //---------------WIP-----------------//
    public void updateTable(){
        bname.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
        bauthor.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
        byear.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
        bstocks.setCellValueFactory(new PropertyValueFactory<Books,Integer>("stocks"));
        bookList = booksObservableList;
        adminBooksTable.setItems(bookList);
    }
    //-----------------------------------//


    // initializes the table
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
                bname.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
                bauthor.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
                byear.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
                bstocks.setCellValueFactory(new PropertyValueFactory<Books,Integer>("stocks"));
                adminBooksTable.setItems(booksObservableList);
    }
}
