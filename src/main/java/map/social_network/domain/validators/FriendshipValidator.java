package map.social_network.domain.validators;

import map.social_network.domain.entities.Friendship;

public class FriendshipValidator implements Validator<Friendship> {

    @Override
    public void validate(Friendship friendship) throws ValidationException {
        if (friendship == null) {
            throw new ValidationException("Entity is null");
        }
        if (friendship.getId().getLeft().equals(friendship.getId().getRight())) {
            throw new ValidationException("A user cannot be friend with himself");
        }
    }
}
