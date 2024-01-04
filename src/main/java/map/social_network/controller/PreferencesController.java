package map.social_network.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import map.social_network.domain.entities.User;
import map.social_network.observer.NotifyStatus;
import map.social_network.observer.Observable;
import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

import java.util.ArrayList;
import java.util.List;

public class PreferencesController implements Observable<UserChangeEvent> {
    public TextField textFieldNumElements;
    private UserService userService;
    private List<Observer<UserChangeEvent>> userObservers = new ArrayList<>();

    private SearchUserController searchUserController;
    private RequestsController requestsController;
    private FriendsController friendsController;

    public void setService(UserService userService) {
        this.userService = userService;
    }

    public void setSearchService(SearchUserController searchUserController) {
        this.searchUserController = searchUserController;
    }

    public void setRequestService(RequestsController requestsController) {
        this.requestsController = requestsController;
    }

    public void setFriendsService(FriendsController friendsController) {
        this.friendsController = friendsController;
    }

    public void onNumOfElements(ActionEvent actionEvent) {
        int numElems = Integer.parseInt(textFieldNumElements.getText());
        Stage stage = (Stage) textFieldNumElements.getScene().getWindow();
        stage.close();
        userService.setPageSize(numElems);
        userService.setPage(0);
        searchUserController.initModel(numElems);
        requestsController.initModel(numElems);
        friendsController.initModel(numElems);
    }

    @Override
    public void addObserver(Observer<UserChangeEvent> e) {
        userObservers.add(e);
    }

    @Override
    public void removeObserver(Observer<UserChangeEvent> e) {
    }

    @Override
    public void notifyObservers(UserChangeEvent t) {
        userObservers.stream().forEach(o -> o.update(t));
    }
}
