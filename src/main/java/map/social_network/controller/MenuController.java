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
import map.social_network.service.MessageService;
import map.social_network.service.RequestService;
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
    public Button btnMessage;

    UserService userService;
    FriendshipService friendshipService;
    RequestService requestService;
    MessageService messageService;
    User user;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ObservableList<User> userModel = FXCollections.observableArrayList();
    ObservableList<Friendship> friendshipsModel = FXCollections.observableArrayList();

    SearchUserController searchUserController;

    HomeController homeController;

    RequestsController requestsController;

    FriendsController friendsController;

    AccountController accountController;

    MultipleMessageController multipleMessageController;

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

    @FXML
    public void onMessageWindow(MouseEvent mouseEvent) {
        loadPage("multiple-message-view.fxml");
    }

    public void setService(UserService userService, FriendshipService friendshipService, RequestService requestService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
    }


    public void setUser(User u) {
        this.user = u;
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

        if (Objects.equals(page, "search-user-view.fxml")) {
            searchUserController = loader.getController();
            searchUserController.setUser(user);
            searchUserController.setService(userService, friendshipService, requestService, messageService);
        }

        if (homeController == null && Objects.equals(page, "home-view.fxml")) {
            homeController = loader.getController();
            homeController.setService(userService, friendshipService);
        }

        if (Objects.equals(page, "requests-view.fxml")) {
            requestsController = loader.getController();
            requestsController.setUser(user);
            requestsController.setService(userService, requestService, friendshipService);
        }

        if (Objects.equals(page, "friends-view.fxml")) {
            friendsController = loader.getController();
            friendsController.setUser(user);
            friendsController.setService(userService, friendshipService, messageService);
        }

        if (Objects.equals(page, "account-view.fxml")) {
            accountController = loader.getController();
            accountController.setUser(user);
            accountController.setService(userService, friendshipService);
        }
        if (Objects.equals(page, "multiple-message-view.fxml")) {
            multipleMessageController = loader.getController();
            multipleMessageController.setUser(user);
            multipleMessageController.setService(userService, messageService);
        }

        borderPaneMenu.setCenter(null);
        borderPaneMenu.setCenter(searchUserAnchorPane);
    }


}
