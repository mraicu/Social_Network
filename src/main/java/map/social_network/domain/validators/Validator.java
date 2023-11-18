package map.social_network.domain.validators;

/**
 * Validator class.
 *
 * @param <T> : type T must have in validations.
 */
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
