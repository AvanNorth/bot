package orm;

import java.util.List;

public class UserService {

    private UserDao usersDao = new UserDao();

    public UserService() {}

    public DataUser findUser(int id) {
        return usersDao.findById(id);
    }

    public void saveUser(DataUser user) {
        usersDao.save(user);
    }

    public void deleteUser(DataUser user) {
        usersDao.delete(user);
    }

    public void updateUser(DataUser user) {
        usersDao.update(user);
    }

    public List<DataUser> findAllUsers() {
        return usersDao.findAll();
    }

}