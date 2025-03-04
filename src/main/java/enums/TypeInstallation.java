package enums;

public enum TypeInstallation {
    STADE(1),
    SALLE_GYM(2),
    TERRAIN(3),
    PISCINE(4);

    private final int value;

    TypeInstallation(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
