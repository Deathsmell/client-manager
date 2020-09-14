package by.deathsmell.clientmanager.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Cashbox {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String model;
    private String serialNumber;
    private String version;
    private boolean skno;
    private String dateCreate;
    private String  dateEnter;
    private String address;
    private String master;
    private boolean nonresident;

}
