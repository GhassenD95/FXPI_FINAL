package enums;

public enum Role {
    admin,
    manager,
    coach,
    athlete;

    public String getRoleName(){
        return name();
    }
}
