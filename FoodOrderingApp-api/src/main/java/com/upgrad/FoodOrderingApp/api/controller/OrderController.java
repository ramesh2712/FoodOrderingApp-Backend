package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.OrderBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
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

    // Save Order endpoint ....

    @RequestMapping(method = RequestMethod.POST, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder (@RequestBody(required = false) final SaveOrderRequest saveOrderRequest,
                                                        @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, AddressNotFoundException, PaymentMethodNotFoundException, RestaurantNotFoundException, CouponNotFoundException, ItemNotFoundException {

       // Check for User Authorization ...
        final CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
        final OrderEntity orders = new OrderEntity();
        orders.setUuid(UUID.randomUUID().toString());
        // Set Bill ...
        orders.setBill(saveOrderRequest.getBill());

        // Set Coupon  ...
        final String couponUuid = saveOrderRequest.getCouponId().toString();
        final CouponEntity coupon = orderBusinessService.getCouponId(couponUuid);
        orders.setCoupon(coupon);

        // Set Address and check for valid user ...
        final String addressUuid = saveOrderRequest.getAddressId();
        final CustomerEntity customerEntity = customerAuthToken.getCustomers();
        AddressEntity address = orderBusinessService.getAddressById(addressUuid, customerEntity);
        orders.setAddress(address);

        // Set Payment method ....
        final PaymentEntity payment = orderBusinessService.getPaymentById(saveOrderRequest.getPaymentId().toString());
        orders.setPayment(payment);

        // Set Customer ...
        orders.setCustomer(customerEntity);

        // Set discount ...
        final BigDecimal discount = saveOrderRequest.getDiscount();
        orders.setDiscount(discount);

        // Set Restaurant Detail ....
        final String restaurantUuid = saveOrderRequest.getRestaurantId().toString();
        RestaurantEntity restaurant = orderBusinessService.getRestaurantById(restaurantUuid);
        orders.setRestaurant(restaurant);

        // Set Date of Order ....
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        orders.setDate(date);

        // Set Item for Order ....
        final OrderItemEntity orderItemEntity = new OrderItemEntity();
        final List<ItemQuantity> itemQuantityList = saveOrderRequest.getItemQuantities();
        List<OrderItemEntity> itemOrderEntities = new ArrayList<>();
        for(ItemQuantity q : itemQuantityList){
            final String itemUuid = q.getItemId().toString();
            final ItemEntity item = orderBusinessService.getItemById(itemUuid);
            orderItemEntity.setItem(item);
            orderItemEntity.setPrice(q.getPrice());
            orderItemEntity.setQuantity(q.getQuantity());
            orderItemEntity.setOrder(orders);
            itemOrderEntities.add(orderItemEntity);
        }

        orders.setOrderItem(itemOrderEntities);
        orderBusinessService.saveOrder(orders);

        final SaveOrderResponse saveOrderResponse = new SaveOrderResponse();
        saveOrderResponse.id(orders.getUuid()).status("ORDER SUCCESSFULLY PLACED");
        return new ResponseEntity<SaveOrderResponse>(saveOrderResponse,HttpStatus.OK);
    }

    // Get Past Orders of User endpoint .....

    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderList>> getPastOrders(@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException{
       // Check for Valid User ...
        final CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
        final CustomerEntity customerEntity = customerAuthToken.getCustomers();
        final List<OrderEntity> orderEntityList = orderBusinessService.getOrderListByCustomer(customerEntity);

        List<OrderList> orderLists = new ArrayList<>();
        for(OrderEntity orderEntity : orderEntityList){
            final OrderList orderList = new OrderList();

            // Set UUID and Bill ...
            orderList.id(UUID.fromString(orderEntity.getUuid()));
            orderList.bill(orderEntity.getBill());

            // Set Coupon ...
            final OrderListCoupon orderListCoupon = new OrderListCoupon();
            final CouponEntity couponEntity = orderEntity.getCoupon();
            orderListCoupon.id(UUID.fromString(couponEntity.getUuid()));
            orderListCoupon.couponName(couponEntity.getCouponName());
            orderListCoupon.percent(couponEntity.getPercent());
            orderList.coupon(orderListCoupon);

            // Set discount ...
            orderList.discount(orderEntity.getDiscount());

            // Set Date ...
            orderList.date(orderEntity.getDate().toString());

            // Set Payment ...
            final OrderListPayment orderListPayment = new OrderListPayment();
            final PaymentEntity paymentEntity = orderEntity.getPayment();
            orderListPayment.id(UUID.fromString(paymentEntity.getUuid()));
            orderListPayment.paymentName(paymentEntity.getPaymentName());
            orderList.payment(orderListPayment);

            // Set Customer ...
            final OrderListCustomer orderListCustomer = new OrderListCustomer();
            orderListCustomer.id(UUID.fromString(customerEntity.getUuid()));
            orderListCustomer.firstName(customerEntity.getFirstname());
            orderListCustomer.lastName(customerEntity.getLastname());
            orderListCustomer.emailAddress(customerEntity.getEmail());
            orderListCustomer.contactNumber(customerEntity.getContactNumber());
            orderList.customer(orderListCustomer);

            // Set Address ....
            final OrderListAddress orderListAddress = new OrderListAddress();
            final AddressEntity addressEntity = orderEntity.getAddress();
            orderListAddress.id(UUID.fromString(addressEntity.getUuid()));
            orderListAddress.flatBuildingName(addressEntity.getFlatBuilNumber());
            orderListAddress.locality(addressEntity.getLocality());
            orderListAddress.city(addressEntity.getCity());
            orderListAddress.pincode(addressEntity.getPinCode());

            // Set State into Address ...
            final StateEntity stateEntity = addressEntity.getState();
            final OrderListAddressState orderListAddressState = new OrderListAddressState();
            orderListAddressState.id(UUID.fromString(stateEntity.getUuid()));
            orderListAddressState.setStateName(stateEntity.getStateName());
            orderListAddress.state(orderListAddressState);
            orderList.address(orderListAddress);

            // Set Item ....
            final List<ItemQuantityResponse> itemQuantityResponses = new ArrayList<>();
            final List<OrderItemEntity> orderItemEntityList = orderEntity.getOrderItem();
            for (OrderItemEntity orderItemEntity : orderItemEntityList) {

                final ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
                final ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();

                // Set itemQuantityResponseItem ...
                final ItemEntity itemEntity = orderItemEntity.getItem();
                itemQuantityResponseItem.id(UUID.fromString(itemEntity.getUuid()));
                itemQuantityResponseItem.itemName(itemEntity.getItenName());
                itemQuantityResponseItem.itemPrice(itemEntity.getPrice());

                if(itemEntity.getType().equals("0")){
                    itemQuantityResponseItem.type(ItemQuantityResponseItem.TypeEnum.VEG);
                }else{
                    itemQuantityResponseItem.type(ItemQuantityResponseItem.TypeEnum.NON_VEG);
                }
                // Set itemQuantityResponses ...
                itemQuantityResponse.item(itemQuantityResponseItem);
                itemQuantityResponse.quantity(orderItemEntity.getQuantity());
                itemQuantityResponse.price(orderItemEntity.getPrice());
                itemQuantityResponses.add(itemQuantityResponse);

                orderList.itemQuantities(itemQuantityResponses);
            }
            orderLists.add(orderList);
        }

        return new ResponseEntity<List<OrderList>>(orderLists,HttpStatus.OK);
    }

}
