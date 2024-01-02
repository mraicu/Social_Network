package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.social_network.domain.entities.User;
import map.social_network.service.UserService;

import java.util.Optional;

public class RegisterController {
    public Button btnRegisterUser;
    public TextField textFieldLastName;
    public PasswordField textFieldPassword;
    public TextField textFieldFirstName;
    public TextField textFieldEmail;

    UserService userService;

    User user;

    Alert alert = new Alert(Alert.AlertType.NONE);

    public void onRegisterUser(ActionEvent actionEvent) {
        String firstName = textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String email = textFieldEmail.getText();
        String password = textFieldPassword.getText();

        if (firstName.isEmpty() || lastName.isEmpty()) {

            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Fields cannot be null!");
            alert.show();

        } else {
            Optional<User> res = userService.saveUser(firstName, lastName, email, password);
            if (!res.isEmpty() && userService.existsUserByEmail(res.get().getEmail()).isEmpty()) {
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

    public void setService(UserService userService) {
        this.userService = userService;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
