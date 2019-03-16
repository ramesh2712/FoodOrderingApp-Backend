package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.SignupBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.UpdateCustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class CustomerController {
    @Autowired
    private SignupBusinessService signupBusinessService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UpdateCustomerService updateCustomerService;


    // signup Endpoint ...

    @RequestMapping(method = RequestMethod.POST,path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

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

    // login endpoint ...

    @RequestMapping(method=RequestMethod.POST, path="/customer/login", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login (@RequestHeader("authorization") final String authorization) throws AuthenticationFailedException {

        // Check for correct format for credentials ...
        String[] decodedArray= authenticationService.authenticate(authorization);

        // Get Login Response If login is successfull
       CustomerAuthTokenEntity customerAuthToken = authenticationService.authenticateCustomer(decodedArray[0],decodedArray[1]);
       CustomerEntity customerEntity = customerAuthToken.getCustomers();
       LoginResponse loginResponse = new LoginResponse().id(customerAuthToken.getUuid())
               .message("LOGGED IN SUCCESSFULLY")
               .firstName(customerEntity.getFirstname())
               .lastName(customerEntity.getLastname())
               .contactNumber(customerEntity.getContactNumber())
               .emailAddress(customerEntity.getEmail());

       // List of Exposed Headers ...
        List<String> header = new ArrayList<>();
        header.add("access-token");

        // Set Headers ...
        HttpHeaders headers = new HttpHeaders();
        headers.add("access-token",customerAuthToken.getAccessToken());
        headers.setAccessControlExposeHeaders(header);

        return new ResponseEntity<LoginResponse>(loginResponse,headers,HttpStatus.OK);
    }

    // logout endpoint ....
    @RequestMapping(method = RequestMethod.POST , path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {

       CustomerAuthTokenEntity customerAuthToken = authenticationService.tokenAuthenticate(authorization);
       CustomerEntity customer = customerAuthToken.getCustomers();
       LogoutResponse logoutResponse = new LogoutResponse().id(customer.getUuid()).message("LOGGED OUT SUCCESSFULLY");
       return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);

    }

    // Update endpoint ....
    @RequestMapping(method = RequestMethod.PUT , path = "/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(@RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest,
                                                         @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UpdateCustomerException {

        final String authorizationToken = authorization.split("Bearer ")[1];
        final String firstName = updateCustomerRequest.getFirstName();
        final String lastName = updateCustomerRequest.getLastName();

       CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(authorizationToken);
       CustomerEntity customer = updateCustomerService.updateCustomer(firstName,lastName,customerAuthToken);
       UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(customer.getUuid())
               .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY")
               .firstName(customer.getFirstname())
               .lastName(customer.getLastname());
       return new ResponseEntity<UpdateCustomerResponse>(updateCustomerResponse,HttpStatus.OK);
    }

    // Change Password endpoint ...
    @RequestMapping(method = RequestMethod.PUT , path = "/customer/password", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword (@RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest,
                                                                  @RequestHeader("authorization") final String authToken) throws AuthorizationFailedException, UpdateCustomerException {
       CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(authToken);
        final String oldPassword = updatePasswordRequest.getOldPassword();
        final String newPassword = updatePasswordRequest.getNewPassword();
        CustomerEntity customerUpdatedPassword = updateCustomerService.updatePassword(oldPassword,newPassword,customerAuthToken.getCustomers());
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(customerUpdatedPassword.getUuid())
                .status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);

    }
}
