package map.social_network.controller;

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
import map.social_network.domain.StatusRequest;
import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.RequestService;
import map.social_network.service.UserService;
import map.social_network.utils.events.RequestChangeEvent;
import map.social_network.utils.events.UserChangeEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestsController implements Observer<RequestChangeEvent> {
    public TableView tableViewRequests;
    public TableColumn<Request, String> columnNameRequests;
    public TableColumn<Request, Button> columnConfirmRequests;
    public TableColumn<Request, Button> columnRemoveRequests;
    public Label labelPageNumber;
    public Button btnPreviousRequests;

    UserService userService;
    RequestService requestService;
    FriendshipService friendshipService;
    User user;
    Alert alert = new Alert(Alert.AlertType.NONE);

    private int pageIndex = 0;

    private int pageSize = 3;
    ObservableList<Request> requestModel = FXCollections.observableArrayList();

    public void setService(UserService service, RequestService requestService, FriendshipService friendshipService) {
        this.userService = service;
        this.requestService = requestService;
        this.friendshipService = friendshipService;
        this.requestService.addObserver(this);

        initModel(this.pageSize);
    }

    public void setUser(User u) {
        this.user = u;
    }

    @FXML
    public void initialize() {
        columnNameRequests.setCellValueFactory(new PropertyValueFactory<>("FromName"));

        tableViewRequests.setItems(requestModel);
        setButtonHandlers();
    }

    private void initModel(int pageSize) {
        requestService.setPageSize(pageSize);
        requestService.setUser(user);
        Iterable<Request> requests = requestService.getUsesOnThisPage(pageIndex);
        List<Request> requestList = StreamSupport.stream(requests.spliterator(), false)
                .collect(Collectors.toList());
        requestModel.setAll(requestList);
    }

    private void setButtonHandlers() {
        // Set action handler for the sendMsg button
        columnRemoveRequests.setCellFactory(tc -> new TableCell<>() {
            final Button rejectBtn = new Button("x");

            {
                rejectBtn.setOnAction(event -> {
                    Request request = getTableView().getItems().get(getIndex());
                    if (request != null && request.getStatus() == StatusRequest.PENDING) {
//                        friendshipService.deleteFriendship(new Tuple<>(request.getFrom().getId(), request.getTo().getId()));
                        request.setStatus(StatusRequest.REJECTED);
                        requestService.updateRequest(request);
                    } else {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setContentText("Unchecked item!");
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
                    setGraphic(rejectBtn);
                }
            }
        });

        // Set action handler for the addBtn button
        columnConfirmRequests.setCellFactory(tc -> new TableCell<>() {
            final Button approveBtn = new Button("+");

            {
                approveBtn.setOnAction(event -> {
                    Request request = getTableView().getItems().get(getIndex());
                    if (request != null && request.getStatus() == StatusRequest.PENDING) {
                        friendshipService.saveFriendship(request.getFrom().getId(), request.getTo().getId());
                        request.setStatus(StatusRequest.APPROVED);
                        requestService.updateRequest(request);
                    } else {
                        alert.setAlertType(Alert.AlertType.WARNING);
                        alert.setContentText("");
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
                    setGraphic(approveBtn);
                }
            }
        });
    }

    public void onPreviousBtn(ActionEvent actionEvent) {
        Iterable<Request> requests = requestService.getPreviousRequests();
        List<Request> requestList = StreamSupport.stream(requests.spliterator(), false)
                .collect(Collectors.toList());
        requestModel.setAll(requestList);

    }

    public void onNextBtn(ActionEvent actionEvent) {
        Iterable<Request> requests = requestService.getNextRequests();
        List<Request> requestList = StreamSupport.stream(requests.spliterator(), false)
                .collect(Collectors.toList());
        requestModel.setAll(requestList);
    }

    public void onSettings(MouseEvent mouseEvent) {

    }

    @Override
    public void update(RequestChangeEvent requestChangeEvent) {
        initModel(pageSize);
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

        updateUserStage.show();
    }
}
