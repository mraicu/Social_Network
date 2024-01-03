package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import map.social_network.domain.entities.User;
import map.social_network.service.FriendshipService;
import map.social_network.service.MessageService;
import map.social_network.service.RequestService;
import map.social_network.service.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class LogInController {
    public Button btnLogIn;
    public TextField textFieldEmail;
    public PasswordField textFieldPassword;
    UserService userService;
    FriendshipService friendshipService;
    RequestService requestService;

    MessageService messageService;
    Alert alert = new Alert(Alert.AlertType.NONE);
    User user;

    //TODO de verificat ca exista si parola
    public void onLogIn(ActionEvent actionEvent) throws Exception {

        User userByEmail = userService.existsUserByEmail(textFieldEmail.getText());
        User userByPassword = userService.existsUserByPassword(textFieldPassword.getText());

        if (userByEmail == null) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Incorrect email!");
            alert.show();
            return;
        }
        if (userByPassword == null) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Incorrect password!");
            alert.show();
            return;
        }
        if (userByEmail != null && userByPassword != null) {
            FXMLLoader menuUserLoader = new FXMLLoader();

            FileInputStream fileInputStream = new FileInputStream(
                    new File("src/main/resources/map/social_network/view/menu-view.fxml")
            );

            try {
                BorderPane menuAnchorPane = menuUserLoader.load(fileInputStream);

                String css = this.getClass().getResource("/map/social_network/view/css/main.css").toExternalForm();
                menuAnchorPane.getStylesheets().add(css);

                Stage menuUserStage = new Stage();
                menuUserStage.setTitle("Menu");
                menuUserStage.setScene(new Scene(menuAnchorPane));

                MenuController menuController = menuUserLoader.getController();
                menuController.setService(userService, friendshipService, requestService, messageService);
                menuController.setUser(userByEmail);

                menuUserStage.show();
                textFieldEmail.setText(null);
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Register first!");
            alert.show();
        }
    }


    public void onRegister(MouseEvent mouseEvent) throws IOException {
        FXMLLoader registerUserLoader = new FXMLLoader();
        FileInputStream fileInputStream = new FileInputStream(
                new File("src/main/resources/map/social_network/view/register-view.fxml")
        );

        AnchorPane addUserAnchorPane = registerUserLoader.load(fileInputStream);

        Stage registerUserStage = new Stage();
        registerUserStage.setTitle("Add User");
        registerUserStage.setScene(new Scene(addUserAnchorPane));

        RegisterController addUserController = registerUserLoader.getController();
        addUserController.setService(userService);


        registerUserStage.show();
    }

    public void setUserService(UserService userService, FriendshipService friendshipService, RequestService requestService, MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.requestService = requestService;
        this.messageService = messageService;
    }


}
