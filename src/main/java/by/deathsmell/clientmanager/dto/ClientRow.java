package by.deathsmell.clientmanager.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientRow implements Row {

    private final List<Cell> cells;

    public ClientRow(List<Cell> cells) {
        this.cells = cells;
    }


    public ClientRow(){
        this.cells = new ArrayList<>();
    }

    @Override
    public int getCellCounts() {
        return cells.size();
    }

    @Override
    public List<Cell> getCells() {
        return cells;
    }

    @Override
    public Cell getCell(int index) {
        return cells.get(index);
    }


    @Override
    public void addCell(Cell cell) {
        cells.add(cell);
    }

    @Override
    public Iterator<Cell> iterator() {
        return this.cells.iterator();
    }
}
