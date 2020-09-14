package by.deathsmell.clientmanager;

import by.deathsmell.clientmanager.dto.enums.TableCollum;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import static by.deathsmell.clientmanager.dto.enums.TableCollum.values;

@SpringBootTest
class ClientManagerApplicationTests {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        createCustomDox();
//        readCustomDox();
    }

    private static void readCustomDox() throws IOException, InvalidFormatException {
        FileInputStream fis = new FileInputStream("/home/deathsmell/Documents/Clients3.docx");
        XWPFDocument xdoc1 = new XWPFDocument(OPCPackage.open(fis));
        List<CTTblGridCol> gridColList = null;
        System.out.println(xdoc1.getDocument().toString());
        for (XWPFTable table : xdoc1.getTables()) {
            CTTbl ctTbl = table.getCTTbl();
            CTTblGrid tblGrid = ctTbl.getTblGrid();
            gridColList = tblGrid.getGridColList();
            int sum = 0;
            for (CTTblGridCol ctTblGridCol : gridColList) {
                BigInteger w = ctTblGridCol.getW();
                System.out.println(w);
                sum += w.intValue();
            }
            System.out.println(sum);
        }
    }
//        List<XWPFTable> tables = xdoc.getTables();
//        for (XWPFTable table : tables) {
//            if (table == null) continue;
//            List<CTTblGridCol> gridColList = table.getCTTbl().getTblGrid().getTblGridChange().getTblGrid().getGridColList();
//            for (CTTblGridCol ctTblGridCol : gridColList) {
//                if (ctTblGridCol != null){
//                    System.out.println(ctTblGridCol.getW());
//                }
//            }
//        }


    private static void createCustomDox() throws IOException {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(new File("/home/deathsmell/Documents/Clients3.docx"));

        CTDocument1 document1 = document.getDocument();
        CTBody body = document1.getBody();
        if (!body.isSetSectPr()) {
            body.addNewSectPr();
        }
        CTSectPr section = body.getSectPr();
        if (!section.isSetPgSz()) {
            section.addNewPgSz();
        }
        CTPageSz pgSz = section.getPgSz();
        pgSz.setOrient(STPageOrientation.LANDSCAPE);
        pgSz.setW(BigInteger.valueOf(842 * 20));
        pgSz.setH(BigInteger.valueOf(595 * 20));

        XWPFTable table = document.createTable(1, values().length - 2);


        table.getCTTbl().addNewTblGrid();

        TableCollum[] values = values();
        for (int i = 1; i < values.length - 1; i++) {
            CTTblGridCol ctTblGridCol = table.getCTTbl().getTblGrid().addNewGridCol();
            ctTblGridCol.setW(BigInteger.valueOf(values[i].getCellWeight()));
        }

        XWPFTableCell cell = table.getRow(0).getCell(0);
        setTableCellRunAndWriteText(cell.addParagraph(),"FEFE");

        System.out.println(table.getCTTbl().getTblGrid().sizeOfGridColArray() + " size table");
//        CTTblGrid ctTblGrid = ctTbl.addNewTblGrid();
//
//        TableCollum[] values = values();
//        int sum = 0;
//        for (int i = 1; i < values.length - let; i++) {
//            ctTbl.getTblGrid().addNewGridCol().setW(BigInteger.valueOf(sum += 500));
//            XWPFTableCell cell = table.getRow(0).getCell(values[i].getCollum() - 1);
//            setTableCellRunAndWriteText(cell.addParagraph(), values[i].getCollumName());
//        }
//        XWPFTableRow newrow = table.createRow();
//        for (int i = 1; i < values().length - let; i++) {
//            XWPFTableCell cell = newrow.getCell(i);
//            setTableCellRunAndWriteText(cell.addParagraph(), "B" + i);
//        }


        document.write(out);
        out.close();
    }

    private static XWPFRun setTableCellRunAndWriteText(XWPFParagraph paragraph, String text) {
        paragraph.setFirstLineIndent(20);
        XWPFRun run = paragraph.createRun();
        run.setFontSize(8);
        run.setFontFamily("Times New Roman");
        run.setText(text);
//        run.addBreak();
        return run;
    }

}
