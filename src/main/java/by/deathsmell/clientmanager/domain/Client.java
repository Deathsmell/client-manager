package by.deathsmell.clientmanager.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String contract;
    private String name;
    private String vat;

    @OneToMany
    private List<Cashbox> cashboxes = new ArrayList<>();
}
