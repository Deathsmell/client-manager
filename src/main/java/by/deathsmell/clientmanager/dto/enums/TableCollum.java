package by.deathsmell.clientmanager.dto.enums;

import org.apache.poi.ss.usermodel.CellType;

import static org.apache.poi.ss.usermodel.CellType.*;

public enum TableCollum {

    CONTRACT(0, "№", 550, STRING),

    CASHBOX_DATE_ENTER(1, "Дата ввода", 780, STRING),

    VAT(2, "УНП", 950, STRING),

    FIRM_NAME(3, "Имя фирмы", 2245, STRING),

    CASHBOX_HAVE_SKNO(4, "СКНО", 700, STRING),

    CASHBOX_MODEL(5, "Наименование КСА", 1055, STRING),

    CASHBOX_SERIAL_NUMBER(6, "№ КСА", 1000, STRING),

    CASHBOX_VERSION(7, "Версия", 950, STRING),

    CASHBOX_ADDRESS(8, "Адрес", 4150, STRING),

    CASHBOX_DATE_CREATE(9, "Год выпуска", 820, STRING),

    MASTER(10, "Мастер", 750, STRING);


    private int collum;
    private final String name;
    private long cellWeight;
    private CellType cellType;


    TableCollum(int collum, String name, long cellWeight, CellType cellType) {
        this.collum = collum;
        this.name = name;
        this.cellWeight = cellWeight;
        this.cellType = cellType;
    }

    public int getCollum() {
        return collum;
    }

    public long getCellWeight(){
        return cellWeight;
    }

    public void setCollum(int collum) {
        this.collum = collum;
    }

    public void setCellWeight(long cellWeight){
        this.cellWeight = cellWeight;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }

    public String getCollumName() {
        return name;
    }

    public CellType getCellType() {
        return cellType;
    }
}
