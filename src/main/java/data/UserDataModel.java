package data;

import domain.controller.data.UserController;
import domain.model.User;

public class UserDataModel<E extends User> extends DataModel<E> implements UserController<E> {
    private static UserDataModel mInstance = new UserDataModel();

    private UserDataModel() {
        super("data/users/");
    }

    public static UserDataModel getInstance() {
        return mInstance;
    }

    @Override
    public E login(String name, String pass) {
        if (exists(name)) {
            User user = get(name, User.class);
            if (user.getPassword().equals(pass)) return (E) user;
        }
        return null;
    }
}