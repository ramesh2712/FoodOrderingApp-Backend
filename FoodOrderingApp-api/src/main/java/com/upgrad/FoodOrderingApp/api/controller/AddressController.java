package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import io.swagger.models.auth.In;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;



@RestController
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private AddressBusinessService addressBusinessService;

    // Save Address Endpoint .....

    @RequestMapping(method = RequestMethod.POST, path = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes =  MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestBody(required = false) final SaveAddressRequest saveAddressRequest,
                                                            @RequestHeader("authorization") final String authToken) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {

       CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(authToken);
       CustomerEntity customer = customerAuthToken.getCustomers();

       AddressEntity addressEntity = new AddressEntity();
       addressEntity.setCity(saveAddressRequest.getCity());
       addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
       addressEntity.setLocality(saveAddressRequest.getLocality());
       addressEntity.setPinCode(saveAddressRequest.getPincode());
       addressEntity.setUuid(UUID.randomUUID().toString());
       addressEntity.setActive(1);
       StateEntity state = addressBusinessService.getState(saveAddressRequest.getStateUuid());
       addressEntity.setState(state);

       AddressEntity createAddress = addressBusinessService.customerAddressSave(addressEntity);
       CustomerAddressEntity customerAddressPersist = addressBusinessService.saveCustomerAddress(customer,createAddress);

       final SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(createAddress.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
       return new ResponseEntity<SaveAddressResponse>(saveAddressResponse,HttpStatus.OK);
    }

    // Get All Saved Addresses endpoint
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getSavedAddress (@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {
           CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
           CustomerEntity customer = customerAuthToken.getCustomers();

           List<AddressEntity> address = addressBusinessService.getCustomerAddressList(accessToken);

           final AddressListResponse addressListResponse = new AddressListResponse();

           for(AddressEntity object : address){
               final AddressList addressList = new AddressList();
               final AddressListState addressListState = new AddressListState();

               addressList.setId(UUID.fromString(object.getUuid()));
               addressList.setFlatBuildingName(object.getFlatBuilNumber());
               addressList.setLocality(object.getLocality());
               addressList.setCity(object.getCity());
               addressList.setPincode(object.getPinCode());
               addressListState.setId(UUID.fromString(object.getState().getUuid()));
               addressListState.setStateName(object.getState().getStateName());
               addressList.setState(addressListState);
               addressListResponse.addAddressesItem(addressList);
           }
       return new ResponseEntity<AddressListResponse>(addressListResponse,HttpStatus.OK);
    }

    //Delete Saved Address endpoint ...

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteCustomerAddress(@PathVariable("address_id") final String uuid,
                                                                       @RequestHeader("authorization") final String accessToken) throws AddressNotFoundException, UpdateCustomerException , AuthorizationFailedException{

        CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
        CustomerEntity customer = customerAuthToken.getCustomers();

        AddressEntity address = addressBusinessService.deleteAddress(uuid ,customer);
        UUID addUuid = UUID.fromString(address.getUuid());
        final DeleteAddressResponse deleteAddressResponse =  new DeleteAddressResponse().id(addUuid).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates(){
        List<StateEntity>  listState = addressBusinessService.getStates();

        final StatesListResponse statesListResponse = new StatesListResponse();
        for(StateEntity s : listState){
            StatesList statesList = new StatesList();
            statesList.setId(UUID.fromString(s.getUuid()));
            statesList.setStateName(s.getStateName());
            statesListResponse.addStatesItem(statesList);
        }
        return new ResponseEntity<StatesListResponse>(statesListResponse,HttpStatus.OK);
    }
}
