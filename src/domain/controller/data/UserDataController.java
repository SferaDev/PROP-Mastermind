package domain.controller.data;

import domain.model.User;
import domain.model.exceptions.UserNotFoundException;

/**
 * The interface User data controller.
 *
 * @param <E> the type parameter
 * @author Alexis Rico Carreto
 */
public interface UserDataController<E extends User> extends DataController<E> {
    /**
     * Logs a new user
     *
     * @param name the user name
     * @param pass the password
     * @return true if the user can be logged, false otherwise
     */
    boolean login(String name, String pass) throws UserNotFoundException;
}
