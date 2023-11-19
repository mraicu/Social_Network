package map.social_network.domain.entities;

import map.social_network.domain.Entity;
import map.social_network.domain.Tuple;

import java.time.LocalDateTime;

public class Friendship extends Entity<Tuple<Long, Long>> {

    LocalDateTime date;

    private String leftName, rightName;

    public String getLeftName() {
        return leftName;
    }

    public String getRightName() {
        return rightName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public Friendship(Long id1, Long id2) {

        this.id = new Tuple<>(id1, id2);
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }



    public void setDate(LocalDateTime date) {
        this.date = date;
    }

}
