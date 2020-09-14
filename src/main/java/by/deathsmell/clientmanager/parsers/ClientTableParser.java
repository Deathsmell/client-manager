package by.deathsmell.clientmanager.parsers;

import by.deathsmell.clientmanager.dto.Table;

public interface ClientTableParser<T> {
    Table sheetParseToTable(T table);
}
