package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.OrderBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OrderBy;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class OrderController {
    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private OrderBusinessService orderBusinessService;

   @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCouponByCouponName (@PathVariable("coupon_name") final String couponName,
                                                                        @RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException, CouponNotFoundException {
       CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
       CouponEntity coupon = orderBusinessService.getCouponByName(couponName);

       CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse();
      couponDetailsResponse.id(UUID.fromString(coupon.getUuid()));
      couponDetailsResponse.couponName(coupon.getCouponName());
      couponDetailsResponse.percent(coupon.getPercent());

      return new ResponseEntity<CouponDetailsResponse>(couponDetailsResponse,HttpStatus.OK);

    }
}
