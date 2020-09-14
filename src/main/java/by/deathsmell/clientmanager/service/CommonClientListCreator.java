package by.deathsmell.clientmanager.service;

import by.deathsmell.clientmanager.domain.Cashbox;
import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.dto.Cell;
import by.deathsmell.clientmanager.dto.Row;
import by.deathsmell.clientmanager.dto.Table;
import by.deathsmell.clientmanager.utils.ClientTableUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static by.deathsmell.clientmanager.dto.enums.TableCollum.*;

@Slf4j
@Service
@Scope("prototype")
public class CommonClientListCreator implements ClientListCreator {

    private Cell contract;
    private Cell cashboxDateEnter;
    private Cell vat;
    private Cell firmName;
    private Cell cashboxHaveSkno;
    private Cell cashboxModel;
    private Cell cashboxSerialNumber;
    private Cell cashboxVersion;
    private Cell cashboxAddress;
    private Cell cashboxDateCreate;
    private Cell master;
    private String lastContract = "";
    private String lastFirmName = "";
    private String lastVat = "";
    private Client client = null;
    boolean nonresident = false;


    @Override
    public List<Client> createClients(Table table) {

        List<Client> clients = new ArrayList<>();


        boolean firstRow = true;
        for (Row row : table) {
            if (firstRow) {
                firstRow = false;
                continue;
            }

            if (ClientTableUtils.isEmptyRow(row)) continue;

            Cell firstCell = row.getCell(0);
            if (firstCell.getText().contains("ИНОГОРОДНИЕ")) {
                nonresident = true;
                continue;
            }


            setEntityInformationFields(row);

            createClient(clients);

            Cashbox cashbox = createCashbox();

            if (client != null)
                client.getCashboxes().add(cashbox);

        }
        return clients;
    }

    private void createClient(List<Client> clients) {
        String contractCellValue = contract.getText();
        String firmNameStringCellValue = firmName.getText();
        String vatStringCellValue = vat.getText();

        if (    (!contractCellValue.equals(lastContract) || contractCellValue.isEmpty())
                && (!firmNameStringCellValue.equals(lastFirmName) || firmNameStringCellValue.isEmpty())
                && (!vatStringCellValue.equals(lastVat) || vatStringCellValue.isEmpty())) {

            if (client != null && !client.getName().isEmpty() && client.getContract() != null) {
                clients.add(client);
            }

            client = new Client();
            client.setContract(contractCellValue);
            lastContract = contractCellValue;
            client.setName(firmNameStringCellValue);
            lastFirmName = firmNameStringCellValue;
            client.setVat(vatStringCellValue);
            lastVat = vatStringCellValue;

        }
    }

    private Cashbox createCashbox() {
        Cashbox cashbox = new Cashbox();

        cashbox.setNonresident(nonresident);
        if (cashboxHaveSkno != null)
            cashbox.setSkno(!cashboxHaveSkno.getText().isBlank());
        if (cashboxAddress != null)
            cashbox.setAddress(cashboxAddress.getText().strip());
        if (cashboxModel != null)
            cashbox.setModel(cashboxModel.getText().strip());
        if (cashboxSerialNumber != null)
            cashbox.setSerialNumber(cashboxSerialNumber.getText().strip());
        if (cashboxVersion != null)
            cashbox.setVersion(cashboxVersion.getText().strip());
        if (cashboxDateCreate != null)
            cashbox.setDateCreate(cashboxDateCreate.getText().strip());
        if (cashboxDateEnter != null)
            cashbox.setDateEnter(cashboxDateEnter.getText().strip());
        if (master != null)
            cashbox.setMaster(master.getText().strip());

        return cashbox;
    }

    private void setEntityInformationFields(Row currentRow) {
        this.contract = currentRow.getCell(CONTRACT.getCollum());
        this.cashboxDateEnter = currentRow.getCell(CASHBOX_DATE_ENTER.getCollum());
        this.vat = currentRow.getCell(VAT.getCollum());
        this.firmName = currentRow.getCell(FIRM_NAME.getCollum());
        this.cashboxHaveSkno = currentRow.getCell(CASHBOX_HAVE_SKNO.getCollum());
        this.cashboxModel = currentRow.getCell(CASHBOX_MODEL.getCollum());
        this.cashboxSerialNumber = currentRow.getCell(CASHBOX_SERIAL_NUMBER.getCollum());
        this.cashboxVersion = currentRow.getCell(CASHBOX_VERSION.getCollum());
        this.cashboxAddress = currentRow.getCell(CASHBOX_ADDRESS.getCollum());
        this.cashboxDateCreate = currentRow.getCell(CASHBOX_DATE_CREATE.getCollum());
        this.master = currentRow.getCell(MASTER.getCollum());
    }

}
