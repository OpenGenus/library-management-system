module com.manage.libros.libros {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
//    requires eu.hansolo.tilesfx;

    opens com.manage.libros to javafx.fxml;
    exports com.manage.libros;
}