package Model;

public class Manager extends User {
    public Manager() {}

    public Manager(String username, Integer idUser, String email, String password) {
        super(username, idUser, email, password);
    }

    public Manager(User user){
        super(user.getUsername(), user.getIdUser(), user.getEmail(), user.getPassword());
    }
}
