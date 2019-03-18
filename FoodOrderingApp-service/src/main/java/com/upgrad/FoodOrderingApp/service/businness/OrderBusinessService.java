package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderBusinessService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private AddressDao addressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByName (final String couponName) throws CouponNotFoundException {
        if(couponName.length() == 0){
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }else{
            CouponEntity coupon = orderDao.getCouponName(couponName);
            if(coupon == null){
                throw  new CouponNotFoundException("CPF-001","No coupon by this name");
            }
            return coupon;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponId(final String uuid) throws CouponNotFoundException {
       CouponEntity coupon =  orderDao.getCouponById(uuid);
       if(coupon == null){
           throw new CouponNotFoundException("CPF-002","No coupon by this id");
       }
       return coupon;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressById(final String uuid , final CustomerEntity customerEntity) throws AddressNotFoundException , AuthorizationFailedException {
        AddressEntity addressEntity = orderDao.getAddressById(uuid);

        if(addressEntity == null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }

        // Check for Valid AccessToken .....
        final CustomerAddressEntity customerAddressEntity = addressDao.getCustomerByAddressId(addressEntity);
        final CustomerEntity customerEntity1 = customerAddressEntity.getCustomer();

        if(customerEntity1.getId() != customerEntity.getId()){
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

        return addressEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentEntity getPaymentById (final String uuid) throws PaymentMethodNotFoundException {
        PaymentEntity payment = orderDao.getPaymentId(uuid);
        if(payment == null){
            throw new PaymentMethodNotFoundException("PNF-002","No payment method found by this id");
        }
        return payment;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity getRestaurantById(final String uuid) throws RestaurantNotFoundException {
        RestaurantEntity restaurant = orderDao.getRestaurantId(uuid);
        if(restaurant == null) {
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }
        return restaurant;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder (final OrderEntity orderEntity){
       return orderDao.saveOrders(orderEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ItemEntity getItemById(final String uuid) throws ItemNotFoundException {
       final ItemEntity item = orderDao.getItem(uuid);
       if(item == null){
           throw new ItemNotFoundException("INF-003","No item by this id exist");
       }
       return item;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderEntity> getOrderListByCustomer(final CustomerEntity customerEntity){
        final List<OrderEntity> orderEntityList = orderDao.getOrderByCustomer(customerEntity);
        return orderEntityList;
    }
}
