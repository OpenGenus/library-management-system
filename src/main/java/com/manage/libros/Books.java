package com.manage.libros;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


//Books data model class
public class Books {
    SimpleStringProperty title;
    SimpleStringProperty author;
    SimpleIntegerProperty year;
    SimpleIntegerProperty stocks;

    Books(String title,String author,int year,int stocks){
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.year = new SimpleIntegerProperty(year);
        this.stocks = new SimpleIntegerProperty(stocks);
    }


    ////////////////////////////getters////////////////////////////////////
    //value return
    public String getAuthor() {
        return author.get();
    }

    public String getTitle() {
        return title.get();
    }

    public int getStocks() {
        return stocks.get();
    }

    public int getYear() {
        return year.get();
    }


}
