package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.social_network.domain.entities.Friendship;
import map.social_network.service.FriendshipService;

import java.util.Optional;

import static java.lang.Long.parseLong;

public class AddFriendshipController {

    public Button btnSubmitAdd;

    Alert alert = new Alert(Alert.AlertType.NONE);
    public TextField textFieldIdUser1;
    public TextField textFieldIdUser2;
    FriendshipService friendshipService;


    public void setFriendshipService(FriendshipService service) {
        this.friendshipService = service;
    }

    public void onSubmitAdd(ActionEvent actionEvent) {
        String id1 = textFieldIdUser1 .getText();
        String id2 = textFieldIdUser2.getText();

        if (id1.isEmpty() || id2.isEmpty()) {

            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Fields cannot be null!");
            alert.show();

        } else {

            Optional<Friendship> res = friendshipService.saveFriendship(parseLong(id1), parseLong(id2));

            if (!res.isEmpty()) {
                Button btnSubmitAdd = (Button) actionEvent.getSource();
                Stage stage = (Stage) btnSubmitAdd.getScene().getWindow();
                stage.close();
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Friendship already exists!");
                alert.show();
            }
        }
    }
}
