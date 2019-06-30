package com.example.sber.domain;

import javax.persistence.*;
import java.sql.Date;

import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private String login="";
    private String name="";
    private String surname="";
    private String address="";
    private Date birth=Date.valueOf("2000-01-01");

    @Lob
    private byte [] photo=null;

    @Lob
    private String info="";

    public User() {
    }

    public User(String login, String name, String surname, String address) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.address = address;
    }
}
