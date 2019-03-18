package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
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
}
