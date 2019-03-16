package com.upgrad.FoodOrderingApp.service.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Table(name="customer")
@NamedQueries(
        {
                @NamedQuery(name = "contactNumber", query = "select u from CustomerEntity u where u.ContactNumber =:number"),
                @NamedQuery(name = "userByContact", query = "select u from CustomerEntity u where u.ContactNumber =:ContactNumber"),



        }
)
public class CustomerEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @NotNull
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "EMAIL")

    @NotNull
    @Size(max = 200)
    private String email;

    @Column(name = "CONTACT_NUMBER")
    @NotNull
    @Size(max = 10)
    private String ContactNumber;

    @Column(name = "PASSWORD")
    @NotNull
   private String password;

    @Column(name = "SALT")
    @NotNull
    private String salt;

   /* @ManyToMany(cascade = CascadeType.ALL,fetch =   FetchType.LAZY)
    @JoinTable(name = "customer_address",
                joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "address_id", referencedColumnName = "id"))
    private List<AddressEntity> address = new ArrayList<AddressEntity>();*/

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
   private List<CustomerAddressEntity> customerAddress;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "customers")
    private List<CustomerAuthTokenEntity> customerAuth = new ArrayList<>();

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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<CustomerAddressEntity> getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(List<CustomerAddressEntity> customerAddress) {
        this.customerAddress = customerAddress;
    }

    public List<CustomerAuthTokenEntity> getCustomerAuth() {
        return customerAuth;
    }

    public void setCustomerAuth(List<CustomerAuthTokenEntity> customerAuth) {
        this.customerAuth = customerAuth;
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
