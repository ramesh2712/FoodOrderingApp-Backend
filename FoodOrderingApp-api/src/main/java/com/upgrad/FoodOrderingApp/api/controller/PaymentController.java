package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class PaymentController {
    @Autowired
    private PaymentBusinessService paymentBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/payment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<PaymentResponse>> getPaymentMethods (){

       List<PaymentEntity> paymentEntities = paymentBusinessService.getPaymentsMethod();

       List<PaymentResponse> paymentResponseList = new ArrayList<>();
       for (PaymentEntity p : paymentEntities){
           PaymentResponse paymentResponse = new PaymentResponse();
          paymentResponse.setId(UUID.fromString(p.getUuid()));
          paymentResponse.setPaymentName(p.getPaymentName());
          paymentResponseList.add(paymentResponse);

       }

       return new ResponseEntity<List<PaymentResponse>>(paymentResponseList,HttpStatus.OK);

    }
}
