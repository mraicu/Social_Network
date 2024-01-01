package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.RequestService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    private TableColumn<User, Button> columnAddSearch;

    @FXML
    private TableColumn<User, Button> columnSendSearch;

    @FXML
    private Button btnPreviousRequests;

    @FXML
    private Button btnNextRequests;

    @FXML
    private Label labelPageNumber;

    UserService userService;
    FriendshipService friendshipService;
    User user;
    RequestService requestService;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ObservableList<User> userModel = FXCollections.observableArrayList();
    ObservableList<Friendship> friendshipsModel = FXCollections.observableArrayList();

    public void setService(UserService userService, FriendshipService friendshipService, RequestService requestService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;
        this.requestService=requestService;

        initModel();
    }

    public void setUser(User u) {
        this.user = u;
    }

    @FXML
    public void initialize() {
        columnNameSearch.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        tableViewSearchUsers.setItems(userModel);
        setButtonHandlers();
    }

    private void setButtonHandlers() {
        // Set action handler for the sendMsg button
        columnSendSearch.setCellFactory(tc -> new TableCell<>() {
            final Button sendMsgButton = new Button("M");

            {
                sendMsgButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    System.out.println(selectedUser);
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
        //TODO
        columnAddSearch.setCellFactory(tc -> new TableCell<>() {
            final Button addBtnButton = new Button("+");

            {
                addBtnButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());
                    requestService.saveRequest(user, selectedUser);
                });
            }

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(addBtnButton);
                }
            }
        });
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

    public void onPreferencesSearch(MouseEvent mouseEvent) throws IOException {
        FXMLLoader preferencesLoader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(
                new File("src/main/resources/map/social_network/view/preferences-view.fxml")
        );

//        String css = getClass().getResource("map/social_network/view/css/main.css").toExternalForm();

        AnchorPane preferencesAnchorPane = preferencesLoader.load(fileInputStream);


        Stage updateUserStage = new Stage();
        updateUserStage.setTitle("Preferences");
        updateUserStage.setScene(new Scene(preferencesAnchorPane));

//        preferencesAnchorPane.getStylesheets().add(css);

        PreferencesController userController = preferencesLoader.getController();
        userController.setService(userService);

        updateUserStage.show();
    }
}
