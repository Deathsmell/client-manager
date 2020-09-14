package by.deathsmell.clientmanager.parsers;

import by.deathsmell.clientmanager.dto.ClientCell;
import by.deathsmell.clientmanager.dto.ClientRow;
import by.deathsmell.clientmanager.dto.ClientTable;
import by.deathsmell.clientmanager.dto.Table;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.stereotype.Service;

@Service("docx-parser")
public class DocxClientTableParser implements ClientTableParser<XWPFTable> {

    @Override
    public Table sheetParseToTable(XWPFTable xwpfTable) {
        Table table = new ClientTable();
        for (XWPFTableRow currentRowFromDocx : xwpfTable.getRows()) {
            by.deathsmell.clientmanager.dto.Row newRowClientTable = new ClientRow();
            for (XWPFTableCell cell : currentRowFromDocx.getTableCells()) {
                String cellText = cell.getText();
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
