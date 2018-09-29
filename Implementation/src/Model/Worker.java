package Model;

public class Worker extends User{
    public Worker() {}

    public Worker(String username, Integer idUser, String email, String password) {
        super(username, idUser, email, password);
    }

    public Worker(User user){
        super(user.getUsername(), user.getIdUser(), user.getEmail(), user.getPassword());
    }
}
