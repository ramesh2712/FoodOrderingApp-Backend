package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderBusinessService {

    @Autowired
    private OrderDao orderDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public CouponEntity getCouponByName (final String couponName) throws CouponNotFoundException {
        if(couponName == null){
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }else{
            CouponEntity coupon = orderDao.getCouponName(couponName);
            if(coupon == null){
                throw  new CouponNotFoundException("CPF-001","No coupon by this name");
            }
            return coupon;
        }
    }

    public CouponEntity getCouponId(final String uuid) throws CouponNotFoundException {
       CouponEntity coupon =  orderDao.getCouponById(uuid);
       if(coupon == null){
           throw new CouponNotFoundException("CPF-002","No coupon by this id");
       }
       return coupon;
    }

    public AddressEntity getAddressById (final String uuid) throws AddressNotFoundException {
        AddressEntity address = orderDao.getAddressById(uuid);

        if(address == null){
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        return address;
    }

    public PaymentEntity getPaymentById (final String uuid) throws PaymentMethodNotFoundException {
        PaymentEntity payment = orderDao.getPaymentId(uuid);
        if(payment == null){
            throw new PaymentMethodNotFoundException("PNF-002","(No payment method found by this id");
        }
        return payment;

    }

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

    public ItemEntity getItemById(final String uuid) throws ItemNotFoundException {
       ItemEntity item = orderDao.getItem(uuid);
       if(item == null){
           throw new ItemNotFoundException("INF-003","No item by this id exist");
       }
       return item;
    }
}
