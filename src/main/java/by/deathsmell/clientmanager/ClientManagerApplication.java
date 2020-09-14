package by.deathsmell.clientmanager;

import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.repository.CashboxRepository;
import by.deathsmell.clientmanager.repository.ClientRepository;
import by.deathsmell.clientmanager.service.ClientListCreator;
import by.deathsmell.clientmanager.service.InvalidTypeChanger;
import by.deathsmell.clientmanager.service.TableReaderAndCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ClientManagerApplication implements CommandLineRunner {

    final TableReaderAndCreator excelClientTableReaderAndCreator;
    final TableReaderAndCreator docClientTableReaderAndCreator;
    final ClientListCreator clientListCreator;
    final ClientRepository clientRepository;
    final CashboxRepository cashboxRepository;
    final InvalidTypeChanger invalidTypeChanger;

    public ClientManagerApplication(@Qualifier("excel") TableReaderAndCreator excelClientTableReaderAndCreator,
                                    @Qualifier("doc") TableReaderAndCreator docClientTableReaderAndCreator,
                                    ClientListCreator clientListCreator,
                                    ClientRepository clientRepository,
                                    CashboxRepository cashboxRepository,
                                    InvalidTypeChanger invalidTypeChanger) {
        this.excelClientTableReaderAndCreator = excelClientTableReaderAndCreator;
        this.docClientTableReaderAndCreator = docClientTableReaderAndCreator;
        this.clientListCreator = clientListCreator;
        this.clientRepository = clientRepository;
        this.cashboxRepository = cashboxRepository;
        this.invalidTypeChanger = invalidTypeChanger;
    }


    public static void main(String[] args) {
        SpringApplication.run(ClientManagerApplication.class, args).close();
    }


    @Override
    public void run(String... args) throws Exception {
        // empty
    }

}
