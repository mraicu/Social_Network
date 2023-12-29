package map.social_network.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

public class RequestsController implements Observer<UserChangeEvent>
{
    public TableView tableViewRequests;
    public TableColumn columnNameRequests;
    public TableColumn columnConfirmRequests;
    public TableColumn columnRemoveRequests;
    public Button btnNextRequests;
    public Button btnPreviousRequests;
    public Label labelPageNumber;

    UserService userService;

    FriendshipService friendshipService;

    public void setService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;
    }
    @Override
    public void update(UserChangeEvent userChangeEvent) {
        
    }

    public void onSettings(MouseEvent mouseEvent) {
    }
}
