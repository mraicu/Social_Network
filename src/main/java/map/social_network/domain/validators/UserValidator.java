package map.social_network.domain.validators;

import map.social_network.domain.entities.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User is null");
        }
        if (user.getFirstName() == null) {
            throw new ValidationException("First name is null.");
        }

    }
}
