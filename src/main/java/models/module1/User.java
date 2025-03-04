package models.module1;
import enums.Role;

public class User {
    private int id;
    private String prenom;
    private String nom;
    private Role role;
    private String birthday;
    private String tel;
    private String adresse;
    private String status;
    private String imageUrl;
    private String email;
    private String mdpHash;

    public User(int id, String prenom, String nom, Role role, String birthday, String tel, String adresse, String status, String imageUrl, String email, String mdpHash) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.birthday = birthday;
        this.tel = tel;
        this.adresse = adresse;
        this.status = status;
        this.imageUrl = imageUrl;
        this.email = email;
        this.mdpHash = mdpHash;
    }

    public User(int id, String text, String text1, String role, String text2, String text3, String adresse, String text4, String text5) {
    }

    public static Role valueOf(String role) {
        return Role.valueOf(role);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getTel() { return tel; }
    public void setTel(String tel) { this.tel = tel; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMdpHash() { return mdpHash; }
    public void setMdpHash(String mdpHash) { this.mdpHash = mdpHash; }
}