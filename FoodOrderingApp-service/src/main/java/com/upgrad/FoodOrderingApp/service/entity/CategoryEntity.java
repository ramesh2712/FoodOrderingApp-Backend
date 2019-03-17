package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")

@NamedQueries(
        {
                @NamedQuery(name = "getCategory", query = "select c from CategoryEntity c order By c.categoryName"),
                @NamedQuery(name = "getCategoryById", query = "select c from CategoryEntity c where c.uuid=:uuid"),
                @NamedQuery(name = "getAllCategories", query = "select c from CategoryEntity c")
        }
)
public class CategoryEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column (name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    List<RestaurantCategoryEntity> categoryRestauant = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categoryItem")
    List<CategoryItemEntity> categoryItemList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getuUid() {
        return uuid;
    }

    public void setuUid(String uUid) {
        this.uuid = uUid;
    }

    public String getCategory_name() {
        return categoryName;
    }

    public void setCategory_name(String category_name) {
        this.categoryName = category_name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<RestaurantCategoryEntity> getCategoryRestauant() {
        return categoryRestauant;
    }

    public void setCategoryRestauant(List<RestaurantCategoryEntity> categoryRestauant) {
        this.categoryRestauant = categoryRestauant;
    }

    public List<CategoryItemEntity> getCategoryItemList() {
        return categoryItemList;
    }

    public void setCategoryItemList(List<CategoryItemEntity> categoryItemList) {
        this.categoryItemList = categoryItemList;
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
