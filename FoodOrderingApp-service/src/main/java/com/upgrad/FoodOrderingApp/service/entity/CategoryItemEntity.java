package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_ITEM")
public class CategoryItemEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ITEM_ID")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private ItemEntity item;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CATEGORY_ID")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private ItemEntity categoryItem;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemEntity getItem() {
        return item;
    }

    public void setItem(ItemEntity item) {
        this.item = item;
    }

    public ItemEntity getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(ItemEntity categoryItem) {
        this.categoryItem = categoryItem;
    }

    @Override
    public boolean equals(Object obj) {
        return new EqualsBuilder().append(this, obj).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this).hashCode();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
