package com.dogukanhan.kayitlar.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "category")
public class Category implements AbstractModel<Long> {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField()
    private String name;

    @ForeignCollectionField
    private ForeignCollection<Product> products;

    public Category() {
    }

    public Category(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ForeignCollection<Product> getProducts() {
        return products;
    }

    public void setProducts(ForeignCollection<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void setId(Long id) {
        this.id= id;
    }

    @Override
    public Long getId() {
        return this.id;
    }
}
