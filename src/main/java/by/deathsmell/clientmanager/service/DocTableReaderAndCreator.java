package by.deathsmell.clientmanager.service;

import by.deathsmell.clientmanager.configuration.ProjectProperties;
import by.deathsmell.clientmanager.domain.Cashbox;
import by.deathsmell.clientmanager.domain.Client;
import by.deathsmell.clientmanager.dto.enums.TableCollum;
import by.deathsmell.clientmanager.dto.Table;
import by.deathsmell.clientmanager.parsers.ClientTableParser;
import by.deathsmell.clientmanager.utils.ClientTableUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import static by.deathsmell.clientmanager.dto.enums.TableCollum.*;

@Slf4j
@Service("doc")
public class DocTableReaderAndCreator implements TableReaderAndCreator {

    final ClientListCreator clientListCreator;

    @Qualifier("docx-parser")
    final ClientTableParser<XWPFTable> parser;

    private final String fileName;

    private final String defaultPath;

    @Autowired
    public DocTableReaderAndCreator(ClientListCreator clientListCreator,
                                    ClientTableParser<XWPFTable> parser,
                                    ProjectProperties projectProperties) {
        this.clientListCreator = clientListCreator;
        this.parser = parser;
        this.fileName = projectProperties.getDoc();
        this.defaultPath = projectProperties.getPath();

    }


    @Override
    public Table read(String path) {
        path = path.isEmpty() ? defaultPath + fileName : path;
        Table clientTable = null;
        try {
            FileInputStream fis = new FileInputStream(path);
            XWPFDocument docx = new XWPFDocument(OPCPackage.open(fis));
            Iterator<IBodyElement> bodyElementIterator = docx.getBodyElementsIterator();
            while (bodyElementIterator.hasNext()) {
                IBodyElement element = bodyElementIterator.next();
                if ("TABLE".equalsIgnoreCase(element.getElementType().name())) {
                    List<XWPFTable> tableList = element.getBody().getTables();
                    if (tableList.size() > 1) throw new RuntimeException("В файле не должно быть больше одной таблицы");
                    for (XWPFTable table : tableList) {
                        clientTable = parser.sheetParseToTable(table);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return clientTable;
    }


    @Override
    public void create(List<Client> clients, String path) throws IOException {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File(defaultPath + fileName));

        ClientTableUtils.setFormatAndOrientation(document,
                STPageOrientation.LANDSCAPE,
                ClientTableUtils.DocumentSizeFormat.A4);

        XWPFTable table = document.createTable(1, TableCollum.values().length);

        ClientTableUtils.setTableAlign(table, ParagraphAlignment.CENTER);

        setNameColumnHeaders(table);

        boolean nonresident = false;
        for (Client client : clients) {
            for (Cashbox cashbox : client.getCashboxes()) {
                XWPFTableRow newTableRow = table.createRow();
                newTableRow.setHeight(400);
                if (!nonresident && cashbox.isNonresident()) {
                    log.debug("Create nonresident head cell");
                    nonresident = true;
                    createNonresidentHeadRow(newTableRow);
                    newTableRow = table.createRow();
                }
                writeClientDataInRow(client, cashbox, newTableRow);
            }
        }


        document.write(out);
        out.close();
    }


    private void createNonresidentHeadRow(XWPFTableRow newTableRow) {
        ClientTableUtils.mergeAllCellsHorizontal(newTableRow);
        ClientTableUtils.setDefFontAndWriteText(newTableRow.getCell(0), "ИНОГОРОДНИЕ");
    }

    private void setNameColumnHeaders(XWPFTable table) {
        TableCollum[] values = values();
        table.getCTTbl().addNewTblGrid();
        for (TableCollum value : values) {
            CTTblGridCol ctTblGridCol = table.getCTTbl().getTblGrid().addNewGridCol();
            ctTblGridCol.setW(BigInteger.valueOf(value.getCellWeight()));
            XWPFTableCell cell = table.getRow(0).getCell(value.getCollum());
            ClientTableUtils.setDefFontAndWriteText(cell, value.getCollumName());
        }
    }


    private void writeClientDataInRow(Client client, Cashbox cashbox, XWPFTableRow newTableRow) {

        XWPFTableCell contractCell = newTableRow.getCell(CONTRACT.getCollum());
        ClientTableUtils.setDefFontAndWriteText(contractCell, String.valueOf(client.getContract()));

        XWPFTableCell cashboxDateEnterCell = newTableRow.getCell(CASHBOX_DATE_ENTER.getCollum());
        ClientTableUtils.setDefFontAndWriteText(cashboxDateEnterCell, cashbox.getDateEnter());

        XWPFTableCell vatCell = newTableRow.getCell(VAT.getCollum());
        ClientTableUtils.setDefFontAndWriteText(vatCell, client.getVat());

        XWPFTableCell firmNameCell = newTableRow.getCell(FIRM_NAME.getCollum());
        ClientTableUtils.setDefFontAndWriteText(firmNameCell, client.getName());

        XWPFTableCell sknoCell = newTableRow.getCell(CASHBOX_HAVE_SKNO.getCollum());
        ClientTableUtils.setDefFontAndWriteText(sknoCell, cashbox.isSkno() ? "СКНО" : "");

        XWPFTableCell cashboxModelCell = newTableRow.getCell(CASHBOX_MODEL.getCollum());
        ClientTableUtils.setDefFontAndWriteText(cashboxModelCell, cashbox.getModel());

        XWPFTableCell cashboxSerialNumberCell = newTableRow.getCell(CASHBOX_SERIAL_NUMBER.getCollum());
        ClientTableUtils.setDefFontAndWriteText(cashboxSerialNumberCell, cashbox.getSerialNumber());

        XWPFTableCell cashboxVersionCell = newTableRow.getCell(CASHBOX_VERSION.getCollum());
        ClientTableUtils.setDefFontAndWriteText(cashboxVersionCell, cashbox.getVersion());

        XWPFTableCell cashboxAddressCell = newTableRow.getCell(CASHBOX_ADDRESS.getCollum());
        ClientTableUtils.setDefFontAndWriteText(cashboxAddressCell, cashbox.getAddress());

        XWPFTableCell cashboxDataCreate = newTableRow.getCell(CASHBOX_DATE_CREATE.getCollum());
        ClientTableUtils.setDefFontAndWriteText(cashboxDataCreate, cashbox.getDateCreate());

        XWPFTableCell masterCell = newTableRow.getCell(MASTER.getCollum());
        ClientTableUtils.setDefFontAndWriteText(masterCell, cashbox.getMaster());

    }


}
