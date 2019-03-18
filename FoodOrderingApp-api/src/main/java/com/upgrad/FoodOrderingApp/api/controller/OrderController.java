package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantity;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderResponse;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.OrderBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/")
public class OrderController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OrderBusinessService orderBusinessService;

    //Get Coupon by Coupon Name endpoint ....

   @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName (@PathVariable("coupon_name") final String couponName,
                                                                        @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, CouponNotFoundException {
       final CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
       final CouponEntity coupon = orderBusinessService.getCouponByName(couponName);
       final CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
       couponDetailsResponse.id(UUID.fromString(coupon.getUuid()));
       couponDetailsResponse.couponName(coupon.getCouponName());
       couponDetailsResponse.percent(coupon.getPercent());

      return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder (final SaveOrderRequest saveOrderRequest,
                                                        @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, CouponNotFoundException, ItemNotFoundException {

        CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
        OrderEntity orders = new OrderEntity();
        orders.setUuid(UUID.randomUUID().toString());
        orders.setBill(saveOrderRequest.getBill());

        CouponEntity coupon = orderBusinessService.getCouponId(saveOrderRequest.getCouponId().toString());
        orders.setCoupon(coupon);

        AddressEntity address = orderBusinessService.getAddressById(saveOrderRequest.getAddressId());
        orders.setAddress(address);

        PaymentEntity payment = orderBusinessService.getPaymentById(saveOrderRequest.getPaymentId().toString());
        orders.setPayment(payment);

        orders.setCustomer(customerAuthToken.getCustomers());

        orders.setDiscount(saveOrderRequest.getDiscount());

        RestaurantEntity restaurant = orderBusinessService.getRestaurantById (saveOrderRequest.getRestaurantId().toString());
        orders.setRestaurant(restaurant);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        orders.setDate(date);

        OrderItemEntity orderItemEntity = new OrderItemEntity();
        List<ItemQuantity> itemQuantityList = saveOrderRequest.getItemQuantities();
        List<OrderItemEntity> itemOrderEntities = new ArrayList<>();
        for(ItemQuantity q : itemQuantityList){
            ItemEntity item = orderBusinessService.getItemById(q.getItemId().toString());
            orderItemEntity.setItem(item);
            orderItemEntity.setPrice(q.getPrice());
            orderItemEntity.setQuantity(q.getQuantity());
            orderItemEntity.setOrder(orders);
            itemOrderEntities.add(orderItemEntity);
        }

        orders.setOrderItem(itemOrderEntities);
        orderBusinessService.saveOrder(orders);

        SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        saveOrderResponse.id(orders.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse,HttpStatus.OK);
    }


}
