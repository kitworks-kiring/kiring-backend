package io.dodn.springboot.storage.db.matzip.entity;

import io.dodn.springboot.storage.db.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    protected Category() {
    }

    public Category(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
