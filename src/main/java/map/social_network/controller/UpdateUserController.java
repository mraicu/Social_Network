package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.social_network.domain.entities.User;
import map.social_network.service.UserService;

import java.util.Optional;

public class UpdateUserController {

    UserService userService;

    User user;
    Alert alert = new Alert(Alert.AlertType.NONE);
    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public Button btnUpdateSubmit;

    public void setUserService(UserService service) {
        this.userService=service;
    }

    public void setUser(User user) {
        this.user= user;
    }

    public void onSubmitUpdate(ActionEvent actionEvent) {

        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {

            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Fields cannot be null!");
            alert.show();

        } else {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            Optional<User> res = this.userService.updateUser(user);

            if (res.isEmpty()) {
                Button btnSubmitAdd = (Button) actionEvent.getSource();
                Stage stage = (Stage) btnSubmitAdd.getScene().getWindow();
                stage.close();
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("User already exists!");
                alert.show();
            }

        }
    }


}
