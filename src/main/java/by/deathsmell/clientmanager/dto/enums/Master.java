package by.deathsmell.clientmanager.dto.enums;

public enum Master {
    PETROVICH("Петрович", "П"),
    NICOLAEVICH("Николаевич", "Н"),
    MITSKEVICH("Мицкевич", "С"),
    YASUNAS("Ясунас", "Я");

    private final String name;
    private final String alias;


    Master(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getMasterName(){
        return this.name;
    }

    public String getAlias(){
        return this.alias;
    }
}
