package map.social_network;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import map.social_network.controller.LogInController;
import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.repository.Repository;
import map.social_network.repository.database.DatabaseFriendshipRepository;
import map.social_network.repository.database.DatabaseUserRepository;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;

import java.io.IOException;
public class StartApplication extends Application {

    UserService userService;
    FriendshipService friendshipService;

    @Override
    public void start(Stage primaryStage) throws IOException {
        String url = "jdbc:postgresql://localhost:5432/Social_Network";
        String username = "postgres";
        String password = "m1234.=";

        Repository<Long, User> userRepository = new DatabaseUserRepository(url, username, password);
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new DatabaseFriendshipRepository(url, username, password);
        userService = new UserService(userRepository, friendshipRepository);
        friendshipService = new FriendshipService(friendshipRepository, userRepository);

        initView(primaryStage);
        primaryStage.setTitle("Social Network");
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

        logInController.setUserService(userService);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
