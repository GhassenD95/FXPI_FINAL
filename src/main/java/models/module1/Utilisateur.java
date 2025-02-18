package models.module1;

import enums.Role;
import enums.Status;

import java.util.Date;

public class Utilisateur {
    private int id;
    private String prenom;
    private String nom;
    private Role role;
    private Date birthday;
    private String tel;
    private String adresse;
    private Status status;
    private String image_url;
    private String email;
    private String mdp_hash;

    public Utilisateur(String prenom, String nom, Role role, String tel, Date birthday, String adresse, Status status, String image_url, String email, String mdp_hash) {
        this.prenom = prenom;
        this.nom = nom;
        this.role = role;
        this.tel = tel;
        this.birthday = birthday;
        this.adresse = adresse;
        this.status = status;
        this.image_url = image_url;
        this.email = email;
        this.mdp_hash = mdp_hash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public String getMdp_hash() {
        return mdp_hash;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMdp_hash(String mdp_hash) {
        this.mdp_hash = mdp_hash;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", role=" + role +
                ", birthday=" + birthday +
                ", tel='" + tel + '\'' +
                ", adresse='" + adresse + '\'' +
                ", status=" + status +
                ", image_url='" + image_url + '\'' +
                ", email='" + email + '\'' +
                ", mdp_hash='" + mdp_hash + '\'' +
                '}';
    }
}
