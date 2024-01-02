package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.RequestService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class RequestsController implements Observer<UserChangeEvent> {
    public TableView tableViewRequests;
    public TableColumn<Request, String> columnNameRequests;
    public TableColumn<Request, Button> columnConfirmRequests;
    public TableColumn<Request, Button> columnRemoveRequests;
    public Label labelPageNumber;

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
//        this.requestService.addObserver(this);

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
//        Iterable<Request> requests = requestService.findByUserId(userTo.getId());
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
                    setGraphic(rejectBtn);
                }
            }
        });

        // Set action handler for the addBtn button
        columnConfirmRequests.setCellFactory(tc -> new TableCell<>() {
            final Button approveBtn = new Button("+");

            {
                approveBtn.setOnAction(event -> {

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
    @Override
    public void update(UserChangeEvent userChangeEvent) {

    }

    public void onSettings(MouseEvent mouseEvent) {
    }
}
