package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class Filters implements Serializable {
    @Id

    private String ByTitle;
    private String ByDeveloper;
    private String ByPublisher;
    private double ByPriceMin;
    private double ByPriceMax;
    private int ByPositive;
    private int Page;

    public String getByTitle() {
        return ByTitle;
    }
    public void setByTitle(String Title) {
        this.ByTitle = Title;
    }

    public String getByDeveloper() {
        return ByDeveloper;
    }
    public void setByDeveloper(String Developer) {
        this.ByDeveloper = Developer;
    }

    public String getByPublisher() {
        return ByPublisher;
    }
    public void setByPublisher(String Publisher) {
        this.ByPublisher = Publisher;
    }

    public double getByPriceMin() {
        return ByPriceMin;
    }
    public void setByPriceMin(double Price)   {
        this.ByPriceMin = Price;
    }

    public double getByPriceMax() {
        return ByPriceMax;
    }
    public void setByPriceMax(double Price) {
        this.ByPriceMax = Price;
    }

    public int getByPositive() {
        return ByPositive;
    }
    public void setByPositive(int Positive) {
        this.ByPositive = Positive;
    }

    public int getPage() {
        return Page;
    }
    public void setPage(int Page) {
        this.Page = Page;
    }


    @Override
    public String toString() {
        return "Filters{" +
                "Title=" + ByTitle +
                ", Developer=" + ByDeveloper +
                ", Publisher=" + ByPublisher +
                ", PriceMin=" + ByPriceMin +
                ", PriceMax=" + ByPriceMax +
                ", Positive=" + ByPositive +
                ", Positive=" + Page +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Filters filter = (Filters) o;
        return Objects.equals(ByTitle, filter.ByTitle) && Objects.equals(ByDeveloper, filter.ByDeveloper)
                && Objects.equals(ByPublisher, filter.ByPublisher) && Objects.equals(ByPositive, filter.ByPositive)
                && Objects.equals(ByPriceMin, filter.ByPriceMin) && Objects.equals(ByPriceMax, filter.ByPriceMax);
    }
}