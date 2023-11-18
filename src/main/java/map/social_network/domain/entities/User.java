package map.social_network.domain.entities;

import map.social_network.domain.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends Entity<Long> {

    private String firstName;
    private String lastName;
    private List<User> friends=new ArrayList<>();
    private static long count = 1;
    private Long id ;

    public User(String firstName, String lastName) {
        this.id=Long.valueOf(count);
        this.firstName = firstName;
        this.lastName = lastName;
        setCount(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<User> getFriends() {
        return friends;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Add a friend
     *
     * @param newFriend new friend to add.
     */
    public boolean addFriend(User newFriend) {
        return this.friends.add(newFriend);
    }

    /**
     * Delete a friend from list.
     *
     * @param friend: friend to delete
     */
    public User deleteFriend(User friend) {
        for (int i = 0; i < this.friends.size(); i++) {
            if (this.friends.get(i) == friend) {
                return friends.remove(i);
            }
        }
        return null;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        return "Utilizator{" +
                "id='" + id + '\'' +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getFriends());
    }

    /**
     * Setting the current count on the next maximum id.
     * This should be called specifically after every setID call and User instantiation.
     *
     * @param id id of the current entity instance
     */
    public void setCount(long id) {
        if (id >= count) count = id + 1;
    }

    /**
     * Resetting count to 1.
     * This should be use only for tests.
     */
    public void resetCount() {
        count = 1;
    }
}