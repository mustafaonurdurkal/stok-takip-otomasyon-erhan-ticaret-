package com.dogukanhan.kayitlar.model;

import androidx.annotation.NonNull;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;

@DatabaseTable(tableName = "product")
public class Product implements AbstractModel<Long> {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String brand;

    @DatabaseField
    private String name;


    @DatabaseField
    private String barcode;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "category_id")
    private Category category;

    @ForeignCollectionField
    private ForeignCollection<Purchase> purchases;

    @ForeignCollectionField
    private ForeignCollection<Sale> sales;

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public ForeignCollection<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(ForeignCollection<Purchase> purchases) {
        this.purchases = purchases;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ForeignCollection<Sale> getSales() {
        return sales;
    }

    public void setSales(ForeignCollection<Sale> sales) {
        this.sales = sales;
    }




    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
