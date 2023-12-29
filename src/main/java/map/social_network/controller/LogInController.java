package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

public class LogInController {
    public Button btnLogIn;
    public TextField textFieldEmail;
    UserService userService;

    FriendshipService friendshipService;
    Alert alert = new Alert(Alert.AlertType.NONE);

    //TODO de verificat ca exista si parola
    public void onLogIn(ActionEvent actionEvent) throws IOException {
        if (!userService.existsUserByEmail(textFieldEmail.getText()).isEmpty()) {
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
                menuController.setService(userService, friendshipService);

                menuUserStage.show();
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
