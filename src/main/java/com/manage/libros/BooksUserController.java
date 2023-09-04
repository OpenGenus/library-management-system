package com.manage.libros;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class BooksUserController implements Initializable {
    public static Connection connection;
    public static FXMLLoader fxmlLoader;
    @FXML
    public TableColumn<Books, String> bnameUser;
    @FXML
    public TableColumn<Books, String> bauthorUser;
    @FXML
    public TableColumn<Books, Integer> byearUser;
    @FXML
    public TableColumn<Books, Integer> bstocksUser;
    public TableView<Books> userAllTables;
    static ObservableList<Books> booksObservableList;

    private static ObservableList<Books> issuedList;
    public Button issueButton;
    public Button returnButton;

    private static Books selectedBook;
    private static Books selectedIssuedBook;
    public TableColumn<Books, String> issuedName;
    public TableView<Books> issuedTable;
    public Label usernameID;


    // loads books and set to table to show available books
    //------------------WIP-----------------//
    public static void loadBooks(Connection conn) {
        String getDataQuery = "SELECT * FROM Books";
        String getIssuedQuery = "SELECT * FROM "+UserLoginSignupController.username;
        connection = conn;
        booksObservableList = FXCollections.observableArrayList();
        issuedList = FXCollections.observableArrayList();
        try{
            Statement all = connection.createStatement();
            Statement issued = connection.createStatement();
            ResultSet rs_all = all.executeQuery(getDataQuery);
            ResultSet rs_issued = issued.executeQuery(getIssuedQuery);
            while(rs_all.next()){
                Books book = new Books(rs_all.getString("Book Name"),rs_all.getString("Author"),rs_all.getInt("Year"),rs_all.getInt("Stocks"));
                booksObservableList.add(book);
            }
            while(rs_issued.next()){
                Books book = new Books(rs_issued.getString("BookName"),rs_issued.getString("Author"),rs_issued.getInt("Year"),0);
                issuedList.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    ////////////////////////

    // closes connection and loads home scene
    public void logoutUser() {
        try {
            connection.close();
            fxmlLoader = new FXMLLoader(Main.class.getResource("libros.fxml"));
            Main.getMainStage().setScene(new Scene(fxmlLoader.load()));
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void issueBook() throws SQLException {
            String issueQuery = "INSERT INTO "+UserLoginSignupController.username+" VALUES(?,?,?)";
            PreparedStatement updateIssued = connection.prepareStatement(issueQuery);
            updateIssued.setString(1,selectedBook.getTitle());
            updateIssued.setString(2,selectedBook.getAuthor());
            updateIssued.setInt(3,selectedBook.getYear());
            updateIssued.execute();

    }

    public void returnBook() throws SQLException {
        String returnQuery = "DELETE FROM " + UserLoginSignupController.username + " WHERE `Author`=`" + selectedIssuedBook.getAuthor() + "`";
        Statement returnBook = connection.createStatement();
        returnBook.execute(returnQuery);
    }


    // supposed to initialize table to show available books
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userAllTables.setRowFactory(tv->{
            TableRow<Books> row = new TableRow<>();
            row.setOnMouseClicked((event)->{
                selectedBook = row.getItem();
                issueButton.setDisable(selectedBook == null);
                loadBooks(connection);
            });
            return row;
        });
        issuedTable.setRowFactory(iv->{
            TableRow<Books> row = new TableRow<>();
            row.setOnMouseClicked((event)->{
                selectedIssuedBook = row.getItem();
                returnButton.setDisable(selectedIssuedBook == null);
                loadBooks(connection);
            });
            return row;
        });
        usernameID.setText(UserLoginSignupController.username);
        bnameUser.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
        bauthorUser.setCellValueFactory(new PropertyValueFactory<Books,String>("author"));
        byearUser.setCellValueFactory(new PropertyValueFactory<Books,Integer>("year"));
        bstocksUser.setCellValueFactory(new PropertyValueFactory<Books,Integer>("stocks"));
        userAllTables.setItems(booksObservableList);
        issuedName.setCellValueFactory(new PropertyValueFactory<Books,String>("title"));
        issuedTable.setItems(issuedList);
    }
}
