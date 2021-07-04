package fr.lernejo.navy_battle;

import java.util.Arrays;

public enum SetFire {
    MISS("miss"), HIT("hit"), SUNK("sunk");

    private final String apiString;

    SetFire(String res) {
        this.apiString = res;
    }

    public static SetFire fromAPI(String value) {
        var res = Arrays.stream(SetFire.values()).filter(f -> f.apiString.equals(value)).findFirst();

        if (res.isEmpty())
            throw new RuntimeException("Invalid value!");

        return res.get();
    }

}
