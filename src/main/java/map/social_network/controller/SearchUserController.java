package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SearchUserController implements Observer<UserChangeEvent> {
    @FXML
    private TextField textFieldSearch;

    @FXML
    private TableView<User> tableViewSearchUsers;

    @FXML
    private TableColumn<User, String> columnNameSearch;

    @FXML
    private TableColumn columnAddSearch;

    @FXML
    private TableColumn columnSendSearch;

    @FXML
    private Button btnPreviousRequests;

    @FXML
    private Button btnNextRequests;

    @FXML
    private Label labelPageNumber;

    UserService userService;
    FriendshipService friendshipService;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ObservableList<User> userModel = FXCollections.observableArrayList();
    ObservableList<Friendship> friendshipsModel = FXCollections.observableArrayList();

    public void setService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;

        initModel();
    }

    @FXML
    public void initialize() {
        columnNameSearch.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        columnSendSearch.setCellValueFactory(new PropertyValueFactory<User, Button>("sendMsg"));
        columnAddSearch.setCellValueFactory(new PropertyValueFactory<User, Button>("addBtn"));

        tableViewSearchUsers.setItems(userModel);
    }

    private void initModel() {
        Iterable<User> users = userService.findAllService();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        userModel.setAll(userList);
    }

    public void onSearchIcon(MouseEvent mouseEvent) {
        String nameTo = textFieldSearch.getText();
        List<User> users;
        if (nameTo.isEmpty()) {
            users = (List<User>) userService.findAllService();
        } else {

            users = userService.filterFrendshipByString(nameTo);
            userModel.setAll(users);
        }

    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel();
    }
}
