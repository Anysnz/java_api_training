package fr.lernejo.navy_battle;

public enum GridCell {
    EMPTY("."),
    MISSED_FIRE("-"),
    SUCCESSFUL_FIRE("X"),
    BOAT("B");

    private final String letter;

    GridCell(String letter) {
        this.letter = letter;
    }

    public String getLetter() {
        return letter;
    }
}
