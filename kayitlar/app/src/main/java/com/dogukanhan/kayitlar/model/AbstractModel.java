package com.dogukanhan.kayitlar.model;

public interface AbstractModel<K> {

    void setId(K k);

    K getId();
}
