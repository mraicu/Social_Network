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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.MessageService;
import map.social_network.service.RequestService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
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
    private Label labelPageNumber;

    UserService userService;
    FriendshipService friendshipService;

    MessageService messageSevice;
    User user;
    RequestService requestService;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ObservableList<User> userModel = FXCollections.observableArrayList();
    private int pageIndex = 0;

    private int pageSize = 3;

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setService(UserService userService, FriendshipService friendshipService, RequestService requestService, MessageService messageService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageSevice = messageService;

        initModel(this.pageSize);
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
                    messageController.setUserService(userService, messageSevice);

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

        // Set action handler for the addBtn button
        columnAddSearch.setCellFactory(tc -> new TableCell<>() {
            final Button addBtnButton = new Button();

            {
                FontAwesomeIconView iconView = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
                iconView.setStyleClass("icon");
                addBtnButton.setGraphic(iconView);
                addBtnButton.setOnAction(event -> {
                    User selectedUser = getTableView().getItems().get(getIndex());

                    List<Request> requests = requestService.findByUserId(selectedUser.getId());

                    boolean crtUserExistsInRequests = requests.stream()
                            .anyMatch(request -> request.getFrom().equals(user) || request.getTo().equals(user));

                    if (!crtUserExistsInRequests) {
                        requestService.saveRequest(user, selectedUser);
                    } else {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setContentText("Request already sent!");
                        alert.show();
                    }
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

    public void initModel(int pageSize) {
        userService.setPageSize(pageSize);
        Iterable<User> users = userService.getUsesOnThisPage(pageIndex);

        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        labelPageNumber.setText(String.valueOf(userService.getPage()));
        userModel.setAll(userList);
    }

    public void onSearchIcon(MouseEvent mouseEvent) {
        String nameTo = textFieldSearch.getText();
        List<User> users;
        if (nameTo.isEmpty()) {
            users = (List<User>) userService.findAllService();
        } else {
            users = userService.filterFrendshipByString(nameTo);
        }
        userModel.setAll(users);
        labelPageNumber.setText(String.valueOf(0));
        userService.setPage(0);
        this.pageIndex = 0;
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {
        initModel(this.pageSize);
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

        PreferencesController prefController = preferencesLoader.getController();
        prefController.setService(userService);
        prefController.setSearchService(this);

        updateUserStage.show();
        labelPageNumber.setText("0");
    }


    public void onPreviousBtn(ActionEvent actionEvent) {
        Iterable<User> users = userService.getPreviousUsers();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        userModel.setAll(userList);

        labelPageNumber.setText(String.valueOf(userService.getPage()));
    }

    public void onNextBtn(ActionEvent actionEvent) {
        Iterable<User> users = userService.getNextUsers();
        List<User> userList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        labelPageNumber.setText(String.valueOf(userService.getPage()));
        userModel.setAll(userList);
    }
}
