package by.deathsmell.clientmanager.dto;

import java.util.List;

public interface Table extends Iterable<Row> {
    List<Row> getRows();
    int getRowsCount();
    Row getRow(int index);
    void addRow(Row row);
}
