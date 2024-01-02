package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController implements Observer<UserChangeEvent> {
    public Label labelPageNumber;
    public TableColumn<Friendship, String> columnName;
    public TableColumn<Friendship, Button> columnSendMsg;
    public TableColumn<Friendship, Button> columnRemove;
    public TableView tableFriends;
    UserService userService;
    User user;
    ObservableList<User> friendshipsModel = FXCollections.observableArrayList();
    Alert alert = new Alert(Alert.AlertType.NONE);
    FriendshipService friendshipService;

    private int pageIndex = 0;

    private int pageSize = 3;

    public void setService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;
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

    private void initModel(int pageSize) {
        friendshipService.setPageSize(pageSize);
        friendshipService.setUser(user);
        Iterable<Friendship> friendships = friendshipService.getFriendshipsOnThisPage(pageSize);
//        Iterable<Friendship> friendships = friendshipService.findAllService();
        List<Friendship> friendshipsList = StreamSupport.stream(friendships.spliterator(), false)
                .collect(Collectors.toList());

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

    private void setButtonHandlers() {
        // Set action handler for the sendMsg button
        columnSendMsg.setCellFactory(tc -> new TableCell<>() {
            final Button sendMsgButton = new Button("M");

            {
                sendMsgButton.setOnAction(event -> {
//                    User selectedUser = getTableView().getItems().get(getIndex());
//                    System.out.println(selectedUser);
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

        // Set action handler for the addBtn button
        columnRemove.setCellFactory(tc -> new TableCell<>() {
            final Button removeButton = new Button("x");

            {
                removeButton.setOnAction(event -> {

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
    public void update(UserChangeEvent userChangeEvent) {

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
}
