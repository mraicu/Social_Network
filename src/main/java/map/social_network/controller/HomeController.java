package map.social_network.controller;

import map.social_network.observer.Observer;
import map.social_network.service.FriendshipService;
import map.social_network.service.UserService;
import map.social_network.utils.events.UserChangeEvent;

public class HomeController implements Observer<UserChangeEvent> {
    UserService userService;

    FriendshipService friendshipService;

    public void setService(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.userService.addObserver(this);
        this.friendshipService = friendshipService;
    }

    @Override
    public void update(UserChangeEvent userChangeEvent) {

    }
}
