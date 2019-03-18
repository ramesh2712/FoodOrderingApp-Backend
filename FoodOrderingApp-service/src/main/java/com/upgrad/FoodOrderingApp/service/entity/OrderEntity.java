package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@NamedQueries(
        {
                @NamedQuery(name = "getOrderByCustomer", query = "select o from OrderEntity o where o.customer =:customer")
        }
)
public class OrderEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "BILL")
    @NotNull
    private BigDecimal bill;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "COUPON_ID")
    private CouponEntity coupon;

    @ManyToOne
    @JoinColumn(name = "PAYMENT_ID")
    private PaymentEntity payment;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID")
    private AddressEntity address;

    @ManyToOne
    @JoinColumn(name = "RESTAURANT_ID")
    @OnDelete(action=OnDeleteAction.CASCADE)
    private RestaurantEntity restaurant;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItemEntity> orderItem;

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

    public BigDecimal getBill() {
        return bill;
    }

    public void setBill(BigDecimal bill) {
        this.bill = bill;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public RestaurantEntity getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantEntity restaurant) {
        this.restaurant = restaurant;
    }

    public List<OrderItemEntity> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItemEntity> orderItem) {
        this.orderItem = orderItem;
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
