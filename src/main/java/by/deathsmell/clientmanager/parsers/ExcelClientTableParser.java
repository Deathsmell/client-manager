package by.deathsmell.clientmanager.parsers;

import by.deathsmell.clientmanager.dto.ClientCell;
import by.deathsmell.clientmanager.dto.ClientRow;
import by.deathsmell.clientmanager.dto.ClientTable;
import by.deathsmell.clientmanager.dto.Table;
import by.deathsmell.clientmanager.dto.enums.TableCollum;
import by.deathsmell.clientmanager.utils.ClientTableUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;


@Service("excel-parser")
public class ExcelClientTableParser implements ClientTableParser<Sheet> {

    @Override
    public Table sheetParseToTable(Sheet sheet) {
        Table table = new ClientTable();
        for (Row currentRowFromExcel : sheet) {
            by.deathsmell.clientmanager.dto.Row newRowClientTable = new ClientRow();
            for (int i = 0; i < TableCollum.values().length; i++) {
                Cell cell = currentRowFromExcel.getCell(i);
                String cellText = "";
                if (cell != null) {
                    cellText = ClientTableUtils.ifNumericTypeReturnString(cell);
                }
                newRowClientTable.addCell(new ClientCell(cellText));
                if (cellText.contains("ИНОГОРОДНИЕ")) {
                    System.out.println("NONRISEDENT: " + cellText);
                    break;
                }
            }
            table.addRow(newRowClientTable);
        }
        return table;
    }
}
