package by.deathsmell.clientmanager.service;


import by.deathsmell.clientmanager.configuration.ProjectProperties;
import by.deathsmell.clientmanager.domain.Cashbox;
import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.dto.enums.TableCollum;
import by.deathsmell.clientmanager.dto.Table;
import by.deathsmell.clientmanager.parsers.ClientTableParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static by.deathsmell.clientmanager.dto.enums.TableCollum.*;

@Slf4j
@Service("excel")
public class ExcelTableReaderAndCreator implements TableReaderAndCreator {

    private final ClientTableParser<Sheet> tableParser;

    private final String fileName;

    private final String defaultPath;

    private int manyClients = 0;

    @Autowired
    public ExcelTableReaderAndCreator(ClientTableParser<Sheet> tableParser, ProjectProperties projectProperties) {
        this.tableParser = tableParser;
        this.fileName = projectProperties.getExcel();
        this.defaultPath = projectProperties.getPath();
    }

    @Override
    public Table read(String path) {
        Table table = null;
        try {
            path = path.isEmpty() ? defaultPath + fileName : path;
            FileInputStream excelFile = new FileInputStream(new File(path));
            Workbook workbook = new HSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            table = tableParser.sheetParseToTable(datatypeSheet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }


    @Override
    public void create(List<Client> clients, String path) {

        path = path.isEmpty() ? defaultPath + fileName : path;
        File file = new File(path);

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Список клиентов");
        Row row = sheet.createRow(manyClients);

        for (TableCollum collum : values()) {
            Cell newCell = row.createCell(collum.getCollum());
            newCell.setCellValue(collum.getCollumName());
        }

        boolean nonresident = false;
        for (Client client : clients) {

            for (Cashbox cashbox : client.getCashboxes()) {

                if (!nonresident && cashbox.isNonresident()) {
                    nonresident = true;
                    row = sheet.createRow(++manyClients);
                    row.createCell(0).setCellValue("ИНОГОРОДНИЕ");
                    CellRangeAddress region = new CellRangeAddress(manyClients,
                            manyClients,
                            0,
                            values().length);
                    sheet.addMergedRegion(region);
                }

                row = sheet.createRow(++manyClients);

                row.createCell(CONTRACT.getCollum()).setCellValue(client.getContract());

                row.createCell(CASHBOX_DATE_ENTER.getCollum()).setCellValue(cashbox.getDateEnter());

                row.createCell(VAT.getCollum()).setCellValue(client.getVat());

                row.createCell(FIRM_NAME.getCollum()).setCellValue(client.getName());

                row.createCell(CASHBOX_HAVE_SKNO.getCollum()).setCellValue(cashbox.isSkno() ? "СКНО" : "");

                row.createCell(CASHBOX_MODEL.getCollum()).setCellValue(cashbox.getModel());

                row.createCell(CASHBOX_SERIAL_NUMBER.getCollum()).setCellValue(cashbox.getSerialNumber());

                row.createCell(CASHBOX_VERSION.getCollum()).setCellValue(cashbox.getVersion());

                row.createCell(CASHBOX_ADDRESS.getCollum()).setCellValue(cashbox.getAddress());

                row.createCell(MASTER.getCollum()).setCellValue(cashbox.getMaster());

                row.createCell(CASHBOX_DATE_CREATE.getCollum()).setCellValue(cashbox.getDateCreate());


            }
        }

        for (int i = 0; i < values().length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            workbook.write(new FileOutputStream(file));
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

