module map.social_network {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;


    opens map.social_network to javafx.fxml;
    opens map.social_network.controller to javafx.fxml;
    opens map.social_network.domain.entities to javafx.base;

    exports map.social_network;
}