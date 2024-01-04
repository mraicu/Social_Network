package map.social_network.controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.MessageService;
import map.social_network.service.UserService;
import map.social_network.utils.events.FriendshipChangeEvent;
import map.social_network.utils.events.UserChangeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController implements Observer<FriendshipChangeEvent> {
    public Label labelPageNumber;
    public TableColumn<User, String> columnName;
    public TableColumn<User, Button> columnSendMsg;
    public TableColumn<User, Button> columnRemove;
    public TableView tableFriends;
    UserService userService;
    User user;
    ObservableList<User> friendshipsModel = FXCollections.observableArrayList();
    Alert alert = new Alert(Alert.AlertType.NONE);
    FriendshipService friendshipService;
    MessageService messageService;
    private int pageIndex = 0;
    private int pageSize = 3;
    SearchUserController searchUserController;

    public void setService(UserService userService, FriendshipService friendshipService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipService.addObserver(this);
        this.messageService = messageService;

        initModel(pageSize);
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    public void initialize() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        tableFriends.setItems(friendshipsModel);
        setButtonHandlers();
    }

    public void initModel(int pageSize) {
        friendshipService.setPageSize(pageSize);
        friendshipService.setUser(user);
//        Iterable<Friendship> friendships = friendshipService.getFriendshipsOnThisPage(pageSize);
        Iterable<Friendship> friendships = friendshipService.findAllService();

        List<User> users = new ArrayList<>();
        for (Friendship f : friendships) {

            if (f.getId().getRight() == user.getId()) {
                users.add(userService.findOneService(f.getId().getLeft()).get());
                friendshipService.setNames(f.getId().getLeft(), f.getId().getRight());
            } else {
                if (f.getId().getLeft() == user.getId())
                    users.add(userService.findOneService(f.getId().getRight()).get());
                friendshipService.setNames(f.getId().getLeft(), f.getId().getRight());
            }
        }

        friendshipsModel.setAll(users);
    }

    private void setButtonHandlers() {
        // Set action handler for the sendMsg button
        columnSendMsg.setCellFactory(tc -> new TableCell<>() {
            final Button sendMsgButton = new Button();

            {
                FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.ENVELOPE);
                iconView.setStyleClass("icon");
                sendMsgButton.setGraphic(iconView);
                sendMsgButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    FXMLLoader onMessageLoader = new FXMLLoader();

                    URL fxmlUrl = getClass().getResource("/map/social_network/view/chat-view.fxml");

                    if (fxmlUrl == null) {
                        throw new RuntimeException("FXML file not found");
                    }

                    onMessageLoader.setLocation(fxmlUrl);

                    AnchorPane onMessageAnchorPane = null;
                    String css = this.getClass().getResource("/map/social_network/view/css/message.css").toExternalForm();

                    try {
                        onMessageAnchorPane = onMessageLoader.load();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Stage messageToStage = new Stage();
                    messageToStage.setTitle("Chat");
                    messageToStage.setScene(new Scene(onMessageAnchorPane));

                    ChatController messageController = onMessageLoader.getController();
                    messageController.setUser(user, selectedUser);
                    messageController.setUserService(userService, messageService);

                    onMessageAnchorPane.getStylesheets().add(css);
                    messageToStage.show();
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(sendMsgButton);
                }
            }
        });

        columnRemove.setCellFactory(tc -> new TableCell<>() {
            final Button removeButton = new Button();

            {
                FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                iconView.setStyleClass("icon");
                removeButton.setGraphic(iconView);
                removeButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    friendshipService.deleteFriendship(new Tuple<>(selectedUser.getId(), user.getId()));
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });
    }

    @Override
    public void update(FriendshipChangeEvent userChangeEvent) {
        initModel(pageSize);
    }

    public void onPreviousBtn(ActionEvent actionEvent) {
        Iterable<Friendship> friendships = friendshipService.getPreviousFriendships();

        List<User> users = new ArrayList<>();
        for (Friendship f : friendships) {
            friendshipService.setNames(f.getId().getLeft(), f.getId().getRight());
            if (f.getId().getRight() != user.getId()) {
                users.add(userService.findOneService(f.getId().getRight()).get());
            } else {
                users.add(userService.findOneService(f.getId().getLeft()).get());
            }
        }

        friendshipsModel.setAll(users);
    }

    public void onNextBtn(ActionEvent actionEvent) {
        Iterable<Friendship> friendships = friendshipService.getNextFriendships();

        List<User> users = new ArrayList<>();
        for (Friendship f : friendships) {
            friendshipService.setNames(f.getId().getLeft(), f.getId().getRight());
            if (f.getId().getRight() != user.getId()) {
                users.add(userService.findOneService(f.getId().getRight()).get());
            } else {
                users.add(userService.findOneService(f.getId().getLeft()).get());
            }
        }

        friendshipsModel.setAll(users);
    }

    public void onPreferencesSearch(MouseEvent mouseEvent) throws IOException {
        FXMLLoader preferencesLoader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(
                new File("src/main/resources/map/social_network/view/preferences-view.fxml")
        );
        AnchorPane preferencesAnchorPane = preferencesLoader.load(fileInputStream);


        Stage updateUserStage = new Stage();
        updateUserStage.setTitle("Preferences");
        updateUserStage.setScene(new Scene(preferencesAnchorPane));

        PreferencesController userController = preferencesLoader.getController();
        userController.setService(userService);
        userController.setFriendsService(this);

        updateUserStage.show();
    }
}
