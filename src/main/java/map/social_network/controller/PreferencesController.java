package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import map.social_network.domain.entities.User;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;

public class PreferencesController {
    public TextField textFieldNumElements;

    private UserService userService;
    public void setService(UserService userService) {
        this.userService = userService;
    }
    public void onNumOfElements(ActionEvent actionEvent) {
    }
}
