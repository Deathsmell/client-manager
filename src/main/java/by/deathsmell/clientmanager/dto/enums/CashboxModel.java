package by.deathsmell.clientmanager.dto.enums;

public enum CashboxModel {
    OKA_MK("ОКА МК"),
    OKA_MK_TRAVEL("ОКА МК тр. Вар."),
    OKA_102K("ОКА 102К"),
    TITAN_A("Титан-А"),
    TITAN_PLUS("Титан-Плюс"),
    DATEKC_MP01("Датекс MP01"),
    DATEKC_HTC("Датекс-НТС"),
    BM8119("ВМ 8119"),
    ORION("Орион 100Ф"),
    MERCURIY("Меркурий НТС-180Ф"),
    POS_SYSTEM("Check Way POS"),
    MINICA_HTC("Миника НТС"),
    MINICA_MIK("Миника МИК"),
    CP412FP("СП412ФР"),
    CP811FP("СП811ФР"),
    WINCOR("WINCOR NIXDORF"),
    SHTRIH_FR_K("Штьрих ФР-К"),
    EKR_HTC("ЭКР НТС"),
    TEC_ST_B20("TEC ST-B20"),
    GEPARD_HTC("ФР Гепард НТС-МФ"),
    UNKNOWN("");

    private final String nameModel;

    CashboxModel(String nameModel) {
        this.nameModel = nameModel;
    }

    public String model() {
        return this.nameModel;
    }

}
