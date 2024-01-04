package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import map.social_network.domain.entities.User;
import map.social_network.service.MessageService;
import map.social_network.service.UserService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MultipleMessageController {
    public TextField textFieldToMultipleMessage;
    public TextArea textAreaMultipleMessage;
    Alert alert = new Alert(Alert.AlertType.NONE);
    UserService userService;
    MessageService messageService;
    User userFrom;

    public void setUser(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setService(UserService userService, MessageService messageSevice) {
        this.messageService = messageSevice;
        this.userService = userService;
    }

    public void onSendMultipleMessage(ActionEvent actionEvent) {
        String emails = textFieldToMultipleMessage.getText();
        String text = textAreaMultipleMessage.getText();
        Long replyId = null;

        if (emails.isEmpty()) {

            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Fields cannot be null!");
            alert.show();

        } else {
            List<String> allEmails = List.of(emails.split(","));
            List<User> userTo = new ArrayList<>();
            for (String email : allEmails) {
                User userto = userService.existsUserByEmail(email);
                if (userto == null) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setContentText(userto + " not found.");
                    alert.show();
                } else {
                    if (userto.getId() != userFrom.getId()) {
                        userTo.add(userto);
                    }
                }
            }
            for (User user : userTo) {
                Instant instant = Instant.now();
                Timestamp date = Timestamp.from(instant);
                messageService.saveMessage(text, userFrom, user, replyId, date);
            }
            textAreaMultipleMessage.setText(null);
            textFieldToMultipleMessage.setText(null);
        }
    }
}
