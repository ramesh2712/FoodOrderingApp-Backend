package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category")

@NamedQueries(
        {
                @NamedQuery(name = "getCategory", query = "select c from CategoryEntity c order By c.categoryName")
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
