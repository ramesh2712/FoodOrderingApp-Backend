package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurant")

@NamedQueries(
        {
                @NamedQuery(name = "getRestByName",query = "select r from RestaurantEntity r where r.restaurantName LIKE :restName"),
                @NamedQuery(name = "getRestById", query = "select r from RestaurantEntity r where r.uuid=:uuid"),
                @NamedQuery(name = "getAllRest", query = "select r from RestaurantEntity r")
        }
)


public class RestaurantEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "RESTAURANT_NAME")
    @NotNull
    private String restaurantName;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    @Column(name = "CUSTOMER_RATING")
    @NotNull
    private BigDecimal customer_rating;

    @Column(name = "AVERAGE_PRICE_FOR_TWO")
    @NotNull
    private Integer averagePriceForTwo;

    @Column(name = "NUMBER_OF_CUSTOMERS_RATED")
    @NotNull
    private Integer noCustomersRated;

   @ManyToOne
   @JoinColumn(name = "ADDRESS_ID")
   private AddressEntity restAddress;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "restaurant")
   List<RestaurantCategoryEntity> restaurantCategory = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public BigDecimal getCustomer_rating() {
        return customer_rating;
    }

    public void setCustomer_rating(BigDecimal customer_rating) {
        this.customer_rating = customer_rating;
    }

    public Integer getAveragePriceForTwo() {
        return averagePriceForTwo;
    }

    public void setAveragePriceForTwo(Integer averagePriceForTwo) {
        this.averagePriceForTwo = averagePriceForTwo;
    }

    public Integer getNoCustomersRated() {
        return noCustomersRated;
    }

    public void setNoCustomersRated(Integer noCustomersRated) {
        this.noCustomersRated = noCustomersRated;
    }

    public AddressEntity getRestAddress() {
        return restAddress;
    }

    public void setRestAddress(AddressEntity restAddress) {
        this.restAddress = restAddress;
    }

    public List<RestaurantCategoryEntity> getRestaurantCategory() {
        return restaurantCategory;
    }

    public void setRestaurantCategory(List<RestaurantCategoryEntity> restaurantCategory) {
        this.restaurantCategory = restaurantCategory;
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
