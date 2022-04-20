package com.dogukanhan.kayitlar.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.math.BigDecimal;
import java.sql.Date;

@DatabaseTable(tableName = "sale")
public class Sale implements AbstractModel<Long>  {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "product_id")
    private Product product;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "customer_id")
    private Customer customer;

    @DatabaseField
    private BigDecimal amount;

    @DatabaseField
    private Date date;

    @DatabaseField
    private BigDecimal perCost;

    @DatabaseField
    private BigDecimal cost;

    @ForeignCollectionField
    private ForeignCollection<Income> incomes;

    public Sale() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public ForeignCollection<Income> getIncomes() {
        return incomes;
    }

    public void setIncomes(ForeignCollection<Income> incomes) {
        this.incomes = incomes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getPerCost() {
        return perCost;
    }

    public void setPerCost(BigDecimal perCost) {
        this.perCost = perCost;
    }
}
