package map.social_network.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import map.social_network.service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController implements Initializable {

    public BorderPane borderPaneMenu;
    UserService userService;

    private BorderPane bp = new BorderPane();

    public void onSearchWindow(MouseEvent mouseEvent) throws IOException {
     loadPage("search-user-view.fxml");
    }

    public void onHomeWindow(MouseEvent mouseEvent) throws IOException {
        loadPage("home-view.fxml");
    }

    public void onRequestsWindow(MouseEvent mouseEvent) {
    }

    public void onFriendsWindow(MouseEvent mouseEvent) {
    }

    public void onLogOutWindow(MouseEvent mouseEvent) {
    }

    public void set(UserService userService) {
        this.userService = userService;
    }

    private void loadPage(String page) throws IOException {
        Parent root = null;

        try {
            String resourcePath = "/map/social_network/view/" + page;
            root = FXMLLoader.load(getClass().getResource(resourcePath));
        } catch (IOException e) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, e);
        }
        borderPaneMenu.setCenter(null);
        borderPaneMenu.setCenter(root);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
