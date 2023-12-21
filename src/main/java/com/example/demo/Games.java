package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
public class Games implements Serializable {
    @Id


    private String Title;
    private String IMG_small;
    private String IMG_big;
    private String Developer;
    private String Publisher;
    private String Link;
    private double Price;
    private int Reviews;
    private int Positive;
    private int Sale;
    private String Date;
    private String Platform;
    private int ElementId;

}