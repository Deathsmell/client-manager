package by.deathsmell.clientmanager.service;

import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.dto.Table;

import java.io.IOException;
import java.util.List;

public interface TableReaderAndCreator {
    Table read(String path);
    void create(List<Client> cells, String path) throws IOException;
}
