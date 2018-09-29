package Model;

public class User {
    private String username;
    private Integer idUser;
    private String email;
    private String password;

    public User(String username, Integer idUser, String email, String password){
        this.username = username;
        this.idUser = idUser;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public User () {}

    public User(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.idUser = user.getIdUser();
    }


}
