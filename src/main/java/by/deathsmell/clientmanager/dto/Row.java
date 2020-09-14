package by.deathsmell.clientmanager.dto;

import java.util.List;

public interface Row extends Iterable<Cell> {
    int getCellCounts();
    List<Cell> getCells();
    Cell getCell(int position);
    void addCell(Cell cell);
}
