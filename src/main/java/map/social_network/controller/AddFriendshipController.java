package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.social_network.domain.entities.Friendship;
import map.social_network.service.FriendshipService;

import java.util.Optional;

import static java.lang.Long.parseLong;

public class AddFriendshipController {

    public Button btnSubmitAdd;
    public TextField textFieldIdUser1;
    public TextField textFieldIdUser2;
    FriendshipService friendshipService;


    public void setFriendshipService(FriendshipService service) {
        this.friendshipService = service;
    }

    public void onSubmitAdd(ActionEvent actionEvent) {
        String id1 = textFieldIdUser1 .getText();
        String id2 = textFieldIdUser2.getText();

        Optional<Friendship> res = friendshipService.saveFriendship(parseLong(id1),parseLong(id2));

        if (!res.isEmpty()) {
            Button btnSubmitAdd = (Button) actionEvent.getSource();
            Stage stage = (Stage) btnSubmitAdd.getScene().getWindow();
            stage.close();
        } else {
            //
        }
    }
}
