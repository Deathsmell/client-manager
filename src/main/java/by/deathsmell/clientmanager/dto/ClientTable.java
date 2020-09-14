package by.deathsmell.clientmanager.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientTable implements Table {
    private final List<Row> table;

    public ClientTable(List<Row> table) {
        this.table = table;
    }

    public ClientTable(){
        this.table = new ArrayList<>();
    }

    @Override
    public Iterator<Row> iterator() {
        return this.table.iterator();
    }

    @Override
    public List<Row> getRows() {
        return this.table;
    }

    @Override
    public int getRowsCount() {
        return this.table.size();
    }

    @Override
    public Row getRow(int index) {
        return this.table.get(index);
    }

    @Override
    public void addRow(Row row) {
        this.table.add(row);
    }
}
