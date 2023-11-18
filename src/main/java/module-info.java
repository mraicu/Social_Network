module map.social_network {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens map.social_network to javafx.fxml;
    exports map.social_network;
}