package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;

import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;


import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {

    public BorderPane borderPaneMenu;
    public Button btnHome;
    public Button btnSearch;
    public Button btnRequests;
    public Button btnFriends;
    public Button btnAccount;
    public TextField textFieldSearch;

    UserService userService;
    FriendshipService friendshipService;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ObservableList<User> userModel = FXCollections.observableArrayList();
    ObservableList<Friendship> friendshipsModel = FXCollections.observableArrayList();

    SearchUserController searchUserController;

    HomeController homeController;

    RequestsController requestsController;

    FriendsController friendsController;

    AccountController accountController;

    @FXML
    public void onSearchWindow(MouseEvent mouseEvent) throws IOException {
        loadPage("search-user-view.fxml");
    }

    @FXML
    public void onHomeWindow(MouseEvent mouseEvent) throws IOException {
        loadPage("home-view.fxml");
    }

    @FXML
    public void onRequestsWindow(MouseEvent mouseEvent) throws IOException {
        loadPage("requests-view.fxml");
    }

    @FXML
    public void onFriendsWindow(MouseEvent mouseEvent) throws IOException {
        loadPage("friends-view.fxml");
    }

    @FXML
    public void onAccountWindow(MouseEvent mouseEvent) throws IOException {
        loadPage("account-view.fxml");
    }

    public void setService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }


    private void loadPage(String page) {
        String resourcePath = "/map/social_network/view/" + page;
        AnchorPane searchUserAnchorPane = null;
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource(resourcePath));
            searchUserAnchorPane = loader.load();

        } catch (IOException e) {
            Logger.getLogger(MenuController.class.getName()).log(Level.SEVERE, null, e);
        }

        if (searchUserController == null && Objects.equals(page, "search-user-view.fxml")) {
            searchUserController = loader.getController();
            searchUserController.setService(userService, friendshipService);
        }

        if (homeController == null && Objects.equals(page, "home-view.fxml")) {
            homeController = loader.getController();
            homeController.setService(userService, friendshipService);
        }

        if (requestsController == null && Objects.equals(page, "requests-view.fxml")) {
            requestsController = loader.getController();
            requestsController.setService(userService, friendshipService);
        }

        if (friendsController == null && Objects.equals(page, "friends-view.fxml")) {
            friendsController = loader.getController();
            friendsController.setService(userService, friendshipService);
        }

        if (accountController == null && Objects.equals(page, "account-view.fxml")) {
            accountController = loader.getController();
            accountController.setService(userService, friendshipService);
        }

        borderPaneMenu.setCenter(null);
        borderPaneMenu.setCenter(searchUserAnchorPane);
    }
}
