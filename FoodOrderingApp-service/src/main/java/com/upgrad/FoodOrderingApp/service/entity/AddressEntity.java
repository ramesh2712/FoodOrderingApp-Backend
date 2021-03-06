package com.upgrad.FoodOrderingApp.service.entity;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "address")
@NamedQueries(
        {
                @NamedQuery(name = "getAddress2", query = "select a from AddressEntity a join a.customerAddress ca join ca.customer c join c.customerAuth au  where  au.accessToken=:accessToken"),
                @NamedQuery(name = "getAddressUuid", query = "select a from AddressEntity a where a.uuid=:uuid")
        }
)
public class AddressEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column (name = "FLAT_BUIL_NUMBER")
    private String flatBuilNumber;

    @Column(name = "LOCALITY")
    private String locality;

    @Column(name = "CITY")
    private String city;

    @Column (name = "PINCODE")
    private String pinCode;

    @Column(name = "ACTIVE")
    private Integer active;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "STATE_ID")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private StateEntity state;

    @OneToMany
    List<RestaurantEntity> restaurant = new ArrayList<>();

    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List <CustomerAddressEntity> customerAddress = new ArrayList<>();

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

    public String getFlatBuilNumber() {
        return flatBuilNumber;
    }

    public void setFlatBuilNumber(String flatBuilNumber) {
        this.flatBuilNumber = flatBuilNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public List<CustomerAddressEntity> getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(List<CustomerAddressEntity> customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<RestaurantEntity> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<RestaurantEntity> restaurant) {
        this.restaurant = restaurant;
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
