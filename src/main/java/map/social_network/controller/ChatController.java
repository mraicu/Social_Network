package map.social_network.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import map.social_network.domain.entities.Message;
import map.social_network.domain.entities.User;
import map.social_network.observer.Observer;
import map.social_network.service.MessageService;
import map.social_network.service.UserService;
import map.social_network.utils.events.MessageChangeEvent;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController implements Observer<MessageChangeEvent> {
    public TextField textFieldTextMessage;
    public Label labelName;
    public TableColumn columnFrom;
    public TableColumn columnMessage;
    public TableColumn columnDate;
    public TableView tableViewMessage;
    public TableColumn columnReply;
    UserService userService;
    MessageService messageSevice;
    User userTo, userFrom;
    Alert alert = new Alert(Alert.AlertType.NONE);
    ObservableList<Message> messageModel = FXCollections.observableArrayList();

    public void setUser(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;

    }

    public void setUserService(UserService userService, MessageService messageSevice) {
        this.messageSevice = messageSevice;
        this.userService = userService;
        this.messageSevice.addObserver(this);

        initModel();
    }

    private void initModel() {
        Iterable<Message> messages = messageSevice.findByUser(userFrom.getId(), userTo.getId());

        List<Message> messageList = StreamSupport.stream(messages.spliterator(), false)
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
        messageModel.setAll(messageList);
    }

    @FXML
    public void initialize() {
        columnFrom.setCellValueFactory(new PropertyValueFactory<>("fromName"));
        columnMessage.setCellValueFactory(new PropertyValueFactory<>("text"));

        columnMessage.setCellFactory(column -> new TableCell<Message, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);

                    if (getTableView().getItems().get(getIndex()).getFrom().getId() == userFrom.getId()) {
                        setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
                    } else {
                        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                    }
                }
            }
        });
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnReply.setCellValueFactory(new PropertyValueFactory<Message, Long>("replyId"));
        columnReply.setCellFactory(column -> new TableCell<Message, Long>() {
            @Override
            protected void updateItem(Long replyId, boolean empty) {
                super.updateItem(replyId, empty);

                if (empty || replyId == 0) {
                    setText(null);
                } else {
                    Message replyMessage = messageSevice.findOneService(replyId).get();
                    setText(replyMessage.getText());
                }
            }
        });


        tableViewMessage.setItems(messageModel);
    }

    @FXML
    public Message selectMessageFromTable() {
        ObservableList selectedItems =
                tableViewMessage.getSelectionModel().getSelectedItems();
        if (!selectedItems.isEmpty()) {
            Message message = (Message) selectedItems.getFirst();
            return message;
        }
        return null;
    }

    public void onSendButton(ActionEvent actionEvent) {
        Message message = selectMessageFromTable();
        Long replyId;
        if (message != null) {
            replyId = message.getId();
        } else {
            replyId = null;
        }

        String text = textFieldTextMessage.getText();

        if (text.isEmpty()) {

            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Field cannot be null!");
            alert.show();

        } else {
            Instant instant = Instant.now();
            Timestamp date = Timestamp.from(instant);
            messageSevice.saveMessage(text, userFrom, userTo, replyId, date);
            textFieldTextMessage.setText(null);

        }

    }


    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        initModel();
    }
}
