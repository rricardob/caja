package modelo;

public class Usuario {

    private int id;
    private String userName;
    private String passwordHash;;
    private String fullName;
    
    public Usuario(){}

    public Usuario(int id, String userName, String passwordHash, String fullName) {
        this.id = id;
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    


}
