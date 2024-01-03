package map.social_network;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.social_network.controller.LogInController;
import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.Message;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.repository.Repository;
import map.social_network.repository.database.DatabaseFriendshipRepository;
import map.social_network.repository.database.DatabaseMessageRepository;
import map.social_network.repository.database.DatabaseRequestRepository;
import map.social_network.repository.database.DatabaseUserRepository;
import map.social_network.repository.paging.PagingRepository;
import map.social_network.service.FriendshipService;
import map.social_network.service.MessageService;
import map.social_network.service.RequestService;
import map.social_network.service.UserService;

import java.io.IOException;

public class StartApplication extends Application {

    UserService userService;
    FriendshipService friendshipService;
    RequestService requestService;
    MessageService messageService;

    @Override
    public void start(Stage primaryStage) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/Social_Network";
        String username = "postgres";
        String password = "m1234.=";

        Repository<Long, User> userRepository = new DatabaseUserRepository(url, username, password);
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new DatabaseFriendshipRepository(url, username, password);
        Repository<Long, Request> requestRepository = new DatabaseRequestRepository(url, username, password);

        PagingRepository<Tuple<Long, Long>, Friendship> pagingFriendshipRepository = new DatabaseFriendshipRepository(url, username, password);
        PagingRepository<Long, Request> pagingRequestRepository = new DatabaseRequestRepository(url, username, password);
        PagingRepository<Long, User> pagingUserRepository = new DatabaseUserRepository(url, username, password);
        Repository<Long, Message> messageRepository = new DatabaseMessageRepository(url, username, password);
        userService = new UserService(userRepository, friendshipRepository, pagingUserRepository);
        friendshipService = new FriendshipService(friendshipRepository, userRepository, pagingFriendshipRepository);
        requestService = new RequestService(userRepository, requestRepository, pagingRequestRepository);
        messageService = new MessageService(userRepository, messageRepository);

        initView(primaryStage);
        primaryStage.setTitle("Log In");
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader();
        mainLoader.setLocation(getClass().getResource("view/login-view.fxml"));

        AnchorPane mainAnchorPane = mainLoader.load();
        primaryStage.setScene(new Scene(mainAnchorPane));

        String css = getClass().getResource("view/css/main.css").toExternalForm();

        mainAnchorPane.getStylesheets().add(css);

        LogInController logInController = mainLoader.getController();

        logInController.setUserService(userService, friendshipService, requestService, messageService);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
