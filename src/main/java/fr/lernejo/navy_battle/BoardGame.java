package fr.lernejo.navy_battle;

import fr.lernejo.navy_battle.BoatPositioning;
import fr.lernejo.navy_battle.SetFire;
import fr.lernejo.navy_battle.GridCell;
import java.util.*;


public class BoardGame {
    private final Integer[] BOATS = {5, 4, 3, 3, 2};
    private final GridCell[][] map = new GridCell[10][10];
    private final List<List<Hook>> boats = new ArrayList<>();
    private final List<Hook> positionsToTest = new ArrayList<>();
    private final List<Hook> lightHitPositions = new ArrayList<>();

    public BoardGame(boolean fill) {
        for (GridCell[] gameCells : map) {
            Arrays.fill(gameCells, GridCell.EMPTY);
        }
        if (fill) {
            buildMap();
        }
        for (int i = 0; i < getWidth(); i++) {
            for (int j = i % 2; j < getHeight(); j += 2) { lightHitPositions.add(new Hook(i, j)); }
        }
    }
    public int getHeight() {
        return map[0].length;
    }
    public int getWidth() {
        return map.length;
    }
    private void buildMap() {
        var random = new Random();
        var boats = new ArrayList<>(Arrays.asList(BOATS));
        Collections.shuffle(boats);
        while (!boats.isEmpty()) {
            int boat = boats.get(0);int x = Math.abs(random.nextInt()) % getWidth();int y = Math.abs(random.nextInt()) % getHeight();
            var Position = random.nextBoolean() ? BoatPositioning.HORIZONTAL : BoatPositioning.VERTICAL;
            if (!canFit(boat, x, y, Position))
                continue;
            addBoat(boat, x, y, Position);boats.remove(0);
        }
    }
    private boolean canFit(int length, int x, int y, BoatPositioning Position) {
        if (x >= getWidth() || y >= getHeight() || getCell(x, y) != GridCell.EMPTY)
            return false;
        if (length == 0)
            return true;
        return switch (Position) {
            case HORIZONTAL -> canFit(length - 1, x + 1, y, Position);
            case VERTICAL -> canFit(length - 1, x, y + 1, Position);
        };
    }
    public GridCell getCell(Hook coordinates) {
        return getCell(coordinates.getX(), coordinates.getY());
    }
    public GridCell getCell(int x, int y) {
        if (x >= 10 || y >= 10)
            throw new RuntimeException("Invalidate coordinates!");
        return map[x][y];
    }
    public void setCell(Hook coordinates, GridCell newStatus) {
        map[coordinates.getX()][coordinates.getY()] = newStatus;
        if (newStatus == GridCell.SUCCESSFUL_FIRE) {
            positionsToTest.addAll(List.of(coordinates.plus(-1, 0), coordinates.plus(0, -1), coordinates.plus(1, 0), coordinates.plus(0, 1)));
        }
    }
    public void addBoat(int length, int x, int y, BoatPositioning orientation) {
        var coordinates = new ArrayList<Hook>();
        while (length > 0) {
            map[x][y] = GridCell.BOAT;length--;coordinates.add(new Hook(x, y));
            switch (orientation) {
                case HORIZONTAL -> x++;
                case VERTICAL -> y++;
            }
        }
        boats.add(coordinates);
    }
    public boolean hasShipLeft() {
        for (var row : map) {
            if (Arrays.stream(row).anyMatch(s -> s == GridCell.BOAT)) return true;
        }
        return false;
    }
    public Hook getNextPlaceToHit() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) {
                if (getCell(i, j) == GridCell.EMPTY)
                    return new Hook(i, j);
            }
        }
        throw new RuntimeException("Error");
    }
    public SetFire hit(Hook coordinates) {
        if (getCell(coordinates) != GridCell.BOAT) return SetFire.MISS;
        var first = boats.stream().filter(s -> s.contains(coordinates)).findFirst();
        assert (first.isPresent());first.get().remove(coordinates);setCell(coordinates, GridCell.SUCCESSFUL_FIRE);
        return first.get().isEmpty() ? SetFire.SUNK : SetFire.HIT;
    }
    private Hook fireAroundSuccessfulHit() {
        while (positionsToTest.size() > 0) { var c = positionsToTest.remove(0);if (getCell(c) == GridCell.EMPTY) return c; }
        return null;
    }

    private Hook lightHit() {
        while (lightHitPositions.size() > 0) {var c = lightHitPositions.remove(0); if (getCell(c) == GridCell.EMPTY) return c; }
        return null;
    }
    private Hook bruteForceHit() {
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight(); j++) { if (getCell(i, j) == GridCell.EMPTY) return new Hook(i, j); }
        }
        throw new RuntimeException("Brute force!");
    }

}
