package com.dogukanhan.kayitlar.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.math.BigDecimal;

@DatabaseTable(tableName = "customer")
public class Customer implements Serializable, AbstractModel<Long> {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String surname;

    @DatabaseField
    private String address;

    @DatabaseField
    private String phone;

    @DatabaseField
    private String email;

    @DatabaseField
    private BigDecimal currentIncome;

    @ForeignCollectionField
    private ForeignCollection<Sale> sales;

    public Customer(){
        currentIncome=new BigDecimal(0);
    }

    public Customer(String name, String surname,BigDecimal currentIncome) {
        this.name=name;
        this.surname=surname;
        this.currentIncome=currentIncome;
        this.id =0L;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ForeignCollection<Sale> getSales() {
        return sales;
    }

    public void setSales(ForeignCollection<Sale> sales) {
        this.sales = sales;
    }


    public BigDecimal getCurrentIncome() {
        return currentIncome;
    }

    public void setCurrentIncome(BigDecimal currentIncome) {
        this.currentIncome = currentIncome;
    }
}
