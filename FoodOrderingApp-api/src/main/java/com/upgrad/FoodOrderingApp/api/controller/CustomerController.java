package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.LogoutResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.SignupBusinessService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.print.attribute.standard.Media;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CustomerController {
    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.POST,path = "/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup (final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        final CustomerEntity customerEntity =new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstname(signupCustomerRequest.getFirstName());
        customerEntity.setLastname(signupCustomerRequest.getLastName());
        customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
        customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
        customerEntity.setPassword(signupCustomerRequest.getPassword());

        final CustomerEntity createdCustomerEntity= signupBusinessService.signUp(customerEntity);
       SignupCustomerResponse signupCustomerResponse= new SignupCustomerResponse().id(createdCustomerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method=RequestMethod.POST, path="/customer/login", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login (@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {
        String[] decodedArray= authenticationService.authenticate(authorization);

       CustomerAuthTokenEntity customerAuthToken = authenticationService.authenticateCustomer(decodedArray[0],decodedArray[1]);
       CustomerEntity customerEntity = customerAuthToken.getCustomer();
       LoginResponse loginResponse = new LoginResponse().id(customerAuthToken.getUuid()).message("LOGGED IN SUCCESSFULLY").firstName(customerEntity.getFirstname()).lastName(customerEntity.getLastname())
            .contactNumber(customerEntity.getContactNumber()).emailAddress(customerEntity.getEmail());

        HttpHeaders header = new HttpHeaders();
        header.add("access-token",customerAuthToken.getAccessToken());
        return new ResponseEntity<LoginResponse>(loginResponse,header,HttpStatus.OK);

    }

    @RequestMapping(method = RequestMethod.POST , path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout (@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
       CustomerAuthTokenEntity customerAuthToken = authenticationService.tokenAuthenticate(authorization);
       CustomerEntity customer = customerAuthToken.getCustomer();

       LogoutResponse logoutResponse = new LogoutResponse().id(customer.getUuid()).message("LOGGED OUT SUCCESSFULLY");
       return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);


    }
}
