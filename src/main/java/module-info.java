module com.cgvsu.rasterizationfxapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens fxapp to javafx.fxml;
    exports fxapp;
}