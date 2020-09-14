package by.deathsmell.clientmanager.dto;

public class ClientCell implements Cell {
    private String cell;

    public ClientCell(String cell) {
        this.cell = cell;
    }

    public ClientCell(){
        this.cell = "";
    }

    @Override
    public String getText() {
        return this.cell;
    }

    @Override
    public void setText() {
        this.cell = cell;
    }
}
