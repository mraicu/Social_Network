package map.social_network.controller;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

public class AccountController implements Observer<UserChangeEvent> {
    public Label labelNameAccount;
    public Label labelEmailAccount;
    public Label btnDelete;
    public Label btnLogOut;

    UserService userService;

    FriendshipService friendshipService;

    User user;

    public void setService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;
    }

    public void setUser(User u) {
        this.user = u;
        this.labelEmailAccount.setText(user.getEmail());
        this.labelNameAccount.setText(user.getFullName());
    }


    @Override
    public void update(UserChangeEvent userChangeEvent) {

    }

    public void onLogOut(MouseEvent mouseEvent) {
        Stage stage = (Stage) btnLogOut.getScene().getWindow();
        stage.close();
    }

    public void onDeleteAccount(MouseEvent mouseEvent) {
        userService.deleteUser(user.getId());
        Stage stage = (Stage) btnDelete.getScene().getWindow();
        stage.close();
    }
}
