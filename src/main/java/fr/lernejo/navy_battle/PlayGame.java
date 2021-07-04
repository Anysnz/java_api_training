package fr.lernejo.navy_battle;

import javax.swing.*;
import java.util.UUID;

public class PlayGame {

    private final UUID id;

    private final BoardGame localMap;

    private final BoardGame remoteMap;

    public PlayGame() {
        super();
        this.id = UUID.randomUUID();
        this.localMap = new BoardGame(true);
        this.remoteMap = new BoardGame(false);
    }

    public PlayGame(UUID id, BoardGame localMap, BoardGame remoteMap) {
        this.id = id;
        this.localMap = localMap;
        this.remoteMap = remoteMap;
    }

    public UUID getId() {
        return id;
    }

    public BoardGame getLocalMap() {
        return localMap;
    }

    public BoardGame getRemoteMap() {
        return remoteMap;
    }
}
