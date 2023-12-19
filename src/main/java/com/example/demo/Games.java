package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Games implements Serializable {
    @Id

    private int Indeks;
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

    public int getIndeks() {
        return Indeks;
    }

    public void setIndeks(int Indeks) {
        this.Indeks = Indeks;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getIMG_small() {
        return IMG_small;
    }

    public void setIMG_small(String IMG) {
        this.IMG_small = IMG;
    }

    public String getIMG_big() {
        return IMG_big;
    }

    public void setIMG_big(String IMG) {
        this.IMG_big = IMG;
    }

    public String getDeveloper() {
        return Developer;
    }

    public void setDeveloper(String Developer) {
        this.Developer = Developer;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String Publisher) {
        this.Publisher = Publisher;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String Link) {
        this.Link = Link;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int getReviews() {
        return Reviews;
    }

    public void setReviews(int Reviews) {
        this.Reviews = Reviews;
    }

    public int getPositive() {
        return Positive;
    }

    public void setPositive(int Positive) {
        this.Positive = Positive;
    }

    public int getSale() {
        return Sale;
    }

    public void setSale(int Sale) {
        this.Sale = Sale;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getPlatform() {
        return Platform;
    }

    public void setPlatform(String Platform) {
        this.Platform = Platform;
    }

    public int getElementId() {
        return ElementId;
    }
    public void setElementId(int ElementId) {
        this.ElementId = ElementId;
    }
}