package by.deathsmell.clientmanager.service;

import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.dto.Table;

import java.util.List;

public interface ClientListCreator {

    List<Client> createClients(Table table);
}
