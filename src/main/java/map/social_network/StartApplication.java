package map.social_network;

import javafx.application.Application;
import javafx.stage.Stage;
import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.repository.Repository;
import map.social_network.repository.database.DatabaseFriendshipRepository;
import map.social_network.repository.database.DatabaseUserRepository;
import map.social_network.service.UserService;

import java.io.IOException;
import java.sql.SQLException;

public class StartApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {

        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String username = "postgres";
        String password = "m1234.=";

        Repository<Long, User> userRepository = new DatabaseUserRepository(url, username, password);
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new DatabaseFriendshipRepository(url, username, password);
        UserService userService = new UserService(userRepository, friendshipRepository);

        initView(stage);
        stage.setWidth(800);
        stage.show();
    }

    private void initView(Stage primaryStage) throws IOException {


    }

    public static void main(String[] args) {
        launch();
    }
}