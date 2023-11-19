package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.service.UserService;

import java.util.Optional;

public class AddUserController {
    UserService userService;

    public TextField textFieldFirstName;
    public TextField textFieldLastName;

    public void setUserService(UserService service) {
        this.userService = service;
    }

    public void onSubmitAdd(ActionEvent actionEvent) {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();

        Optional<User> res = userService.saveUser(firstName, lastName);

        if (!res.isEmpty()) {
            Button btnSubmitAdd = (Button) actionEvent.getSource();
            Stage stage = (Stage) btnSubmitAdd.getScene().getWindow();
            stage.close();
        } else {
            //
        }
    }
}
