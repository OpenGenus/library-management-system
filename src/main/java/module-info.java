module com.manage.lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;
//    requires eu.hansolo.tilesfx;

    opens com.manage.lms to javafx.fxml;
    exports com.manage.lms;
}