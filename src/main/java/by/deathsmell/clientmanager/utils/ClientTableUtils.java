package by.deathsmell.clientmanager.utils;

import by.deathsmell.clientmanager.dto.Cell;
import by.deathsmell.clientmanager.dto.Row;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.List;

import static by.deathsmell.clientmanager.dto.enums.TableCollum.values;

public class ClientTableUtils {

    public static boolean isEmptyRow(Row row) {
        return (row == null || !hasRowContainCell(row));
    }

    public static boolean isEmptyRow(XWPFTableRow row) {
        for (int currentCellIndex = 0; currentCellIndex < values().length; currentCellIndex++) {
            XWPFTableCell currentCell = row.getCell(currentCellIndex);
            if (currentCell != null && StringUtils.hasText(String.valueOf(currentCell))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isEmptyRow(org.apache.poi.ss.usermodel.Row row) {
        for (int currentCellIndex = 0; currentCellIndex < values().length; currentCellIndex++) {
            org.apache.poi.ss.usermodel.Cell currentCell = row.getCell(currentCellIndex);
            if (currentCell != null && StringUtils.hasText(String.valueOf(currentCell))) {
                return false;
            }
        }
        return true;
    }


    public static boolean hasRowContainCell(Row row) {
        for (int currentCellIndex = 0; currentCellIndex < values().length; currentCellIndex++) {
            Cell currentCell = row.getCell(currentCellIndex);
            if (currentCell != null && StringUtils.hasText(currentCell.getText())) {
                return true;
            }
        }
        return false;
    }

    public static String ifNumericTypeReturnString(org.apache.poi.ss.usermodel.Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return "";
        }
    }

    public static void setFormatAndOrientation(XWPFDocument document,
                                               org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation.Enum orientation,
                                               DocumentSizeFormat format) {
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
        pgSz.setOrient(orientation);
        if (STPageOrientation.LANDSCAPE.equals(orientation)) {
            pgSz.setW(BigInteger.valueOf(format.y * 20));
            pgSz.setH(BigInteger.valueOf(format.x * 20));
        } else {
            pgSz.setW(BigInteger.valueOf(format.x * 20));
            pgSz.setH(BigInteger.valueOf(format.y * 20));
        }


    }

    public enum DocumentSizeFormat {
        A4(595, 842);
        public final int x;
        public final int y;

        DocumentSizeFormat(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void mergeAllCellsHorizontal(XWPFTable table, int rowIndex) {
        XWPFTableRow row = table.getRow(rowIndex);
        List<XWPFTableCell> tableCells = row.getTableCells();
        mergeCellsHorizontal(table, row, 0, tableCells.size() - 1);
    }

    public static void mergeAllCellsHorizontal(XWPFTableRow row) {
        List<XWPFTableCell> tableCells = row.getTableCells();
        mergeCellsHorizontal(null, row, 0, tableCells.size() - 1);
    }

    public static void mergeCellsHorizontal(XWPFTableRow row,int startColl, int endColl) {
        List<XWPFTableCell> tableCells = row.getTableCells();
        mergeCellsHorizontal(null, row, startColl, endColl);
    }

    public static void mergeCellsHorizontal(XWPFTable table, XWPFTableRow row, int startCol, int endCol) {
        for (int cellIndex = startCol; cellIndex <= endCol; cellIndex++) {
            XWPFTableCell cell = row.getCell(cellIndex);
            if (cellIndex == startCol) {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
            }
        }
    }

    public static void setDefFontAndWriteText(XWPFTableCell cell, String text) {
        for (int i = 0; i < cell.getParagraphs().size(); i++) {
            cell.removeParagraph(i);
        }
        XWPFRun run = cell.addParagraph().createRun();
        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
        run.setFontSize(8);
        run.setFontFamily("Times New Roman");
        run.setText(text);
    }

    public static void setTableAlign(XWPFTable table, ParagraphAlignment align) {
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        CTJc jc = (tblPr.isSetJc() ? tblPr.getJc() : tblPr.addNewJc());
        STJc.Enum en = STJc.Enum.forInt(align.getValue());
        jc.setVal(en);
    }

    private static <T extends ICell> XWPFRun setTableCellRunAndWriteText(String text,
                                                                         T cell,
                                                                         String frontFamily,
                                                                         int fontSize) {
        XWPFRun run = null;
        if (cell instanceof XWPFTableCell) {
            List<XWPFParagraph> paragraphs = ((XWPFTableCell) cell).getParagraphs();
            if (paragraphs.size() == 0) {
                run = ((XWPFTableCell) cell).addParagraph().createRun();
            } else {
                for (XWPFParagraph paragraph : paragraphs) {
                    if (paragraph.getRuns().size() == 0) {
                        run = paragraph.createRun();
                        break;
                    } else {
                        run = ((XWPFTableCell) cell).addParagraph().createRun();
                    }
                }
            }
        }

        if (run == null) throw new RuntimeException();
        run.setFontSize(fontSize);
        run.setFontFamily(frontFamily);
        run.setText(text);

        return run;
    }
}
