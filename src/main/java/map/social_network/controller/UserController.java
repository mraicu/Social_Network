package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class UserController implements Observer<UserChangeEvent> {
    public TableColumn tableColumnFriendshipDate;
    public TableColumn tableColumnFriend2Name;
    public TableColumn tableColumnFriend1Name;
    UserService userService;

    FriendshipService friendshipService;

    ObservableList<User> userModel = FXCollections.observableArrayList();
    ObservableList<Friendship> friendshipsModel = FXCollections.observableArrayList();

    @FXML
    public TableView tableViewUserList;

    @FXML
    public TableView tableViewFriendship;

    @FXML
    TableColumn<User, String> tableColumnFirstName;

    @FXML
    TableColumn<User, String> tableColumnLastName;
//    @FXML
//    TableColumn<User, String> tableColumnNumFriends;

    public void setUserService(UserService service, FriendshipService friendshipService) {
        this.userService = service;
        this.userService.addObserver(this);
        this.friendshipService=friendshipService;
        initModel();
    }

    @FXML
    public void initialize(){

        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));


        tableColumnFriendshipDate.setCellValueFactory(new PropertyValueFactory<Friendship, String>("date"));
        tableColumnFriend1Name.setCellValueFactory(new PropertyValueFactory<Friendship, String>("date"));
        tableColumnFriend2Name.setCellValueFactory(new PropertyValueFactory<Friendship, String>("date"));

        tableViewUserList.setItems(userModel);
        tableViewFriendship.setItems(friendshipsModel);
    }

    private void initModel(){
        Iterable<User> users = userService.findAllService();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        userModel.setAll(userList);

//        Iterable<Friendship> friendships = friendshipService.findAllService();
//        List<Friendship> friendshipsList = StreamSupport.stream(friendships.spliterator(), false)
//                .collect(Collectors.toList());
//        friendshipsModel.setAll(friendshipsList);
    }

    @FXML
    public void onAddUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader addUserLoader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(
                new File("src/main/resources/map/social_network/view/add-user-view.fxml")
        );

        AnchorPane addUserAnchorPane = addUserLoader.load(fileInputStream);

        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add User");
        addUserStage.setScene(new Scene(addUserAnchorPane));

        AddUserController addUserController = addUserLoader.getController();
        addUserController.setUserService(userService);

        addUserStage.show();
    }

    @FXML
    public void onDeleteUser(ActionEvent actionEvent) {
    }

    @FXML
    public void onUpdateUser(ActionEvent actionEvent) {
    }

    @FXML
    public void onFilterUser(ActionEvent actionEvent) {
    }

    @FXML
    public void onAddFriendship(ActionEvent actionEvent) throws IOException {
        FXMLLoader addUserLoader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(
                new File("src/main/resources/map/social_network/view/add-friendship-view.fxml")
        );

        AnchorPane addUserAnchorPane = addUserLoader.load(fileInputStream);

        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add Friendship");
        addUserStage.setScene(new Scene(addUserAnchorPane));

        AddUserController addUserController = addUserLoader.getController();
        addUserController.setUserService(userService);

        addUserStage.show();
    }

    @FXML
    public void onDeleteFriendship(ActionEvent actionEvent) {
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }
}
