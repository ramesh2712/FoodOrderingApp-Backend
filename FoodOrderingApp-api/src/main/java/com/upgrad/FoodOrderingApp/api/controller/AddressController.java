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

    @RequestMapping(method = RequestMethod.POST, path = "/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes =  MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress (final SaveAddressRequest saveAddressRequest,
                                                            @RequestHeader("authorization") final String authToken) throws AuthorizationFailedException, SaveAddressException, AddressNotFoundException {
       CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(authToken);
       CustomerEntity customer = customerAuthToken.getCustomers();
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setFlatBuilNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setPinCode(saveAddressRequest.getPincode());
        addressEntity.setUuid(UUID.randomUUID().toString());
        StateEntity state = addressBusinessService.getState(saveAddressRequest.getStateUuid());
        addressEntity.setState(state);
         AddressEntity createAddress = addressBusinessService.customerAddressSave(addressEntity);
        CustomerAddressEntity customerAddress = new CustomerAddressEntity();
        customerAddress.setAddress(addressEntity);
        customerAddress.setCustomer(customer);
        CustomerAddressEntity customerAddressPersist = addressBusinessService.saveCustomerAddress(customerAddress);

        final SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(createAddress.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse,HttpStatus.OK);
    }

  /* @RequestMapping(method = RequestMethod.GET, path = "/address/customer" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getSavedAddress (@RequestHeader("authorization") final String accessToken) throws AuthorizationFailedException {
           CustomerAuthTokenEntity customerAuthToken = authenticationService.authCustomerToken(accessToken);
           CustomerEntity customer = customerAuthToken.getCustomers();
           List<AddressEntity> address = addressBusinessService.getCustomerAddress(accessToken);

           AddressList addressList = new AddressList();
           List<AddressList> arrayList = new ArrayList<>();
           final AddressListResponse addressListResponse = new AddressListResponse();
            final AddressListState addressListState = new AddressListState();

           for(AddressEntity adrss : address){

               addressList.setId(UUID.fromString(adrss.getUuid()));
               addressList.setFlatBuildingName(adrss.getFlatBuilNumber());
               addressList.setLocality(adrss.getLocality());
               addressList.setCity(adrss.getCity());
               addressList.setPincode(adrss.getPinCode());
               addressListState.setId(UUID.fromString(adrss.getState().getUuid()));
               addressListState.setStateName(adrss.getState().getStateName());
               addressList.setState(addressListState);
               arrayList.add(addressList);
               addressListResponse.addresses(arrayList);
           }
       return new ResponseEntity<AddressListResponse>(addressListResponse,HttpStatus.OK);
    }*/

    @RequestMapping(method = RequestMethod.GET, path = "/address/{address_id}" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteCustomerAddress(@PathVariable("address_id") final String uUid,
                                                                       @RequestHeader("authorization") final String accessToken) throws AddressNotFoundException, UpdateCustomerException {
        AddressEntity address = addressBusinessService.getAddressByUuid(uUid);
        UUID addUuid = UUID.fromString(address.getUuid());
        final  DeleteAddressResponse deleteAddressResponse =  new DeleteAddressResponse().id(addUuid).status("ADDRESS DELETED SUCCESSFULLY");
        return new ResponseEntity<DeleteAddressResponse>(deleteAddressResponse,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states" , produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates(){
        List<StateEntity>  listState = addressBusinessService.getStates();

        StatesList statesList = new StatesList();
        final StatesListResponse statesListResponse = new StatesListResponse();
        for(StateEntity s : listState){
            statesList.setId(UUID.fromString(s.getUuid()));
            statesList.setStateName(s.getStateName());
            statesListResponse.addStatesItem(statesList);

        }
        return new ResponseEntity<StatesListResponse>(statesListResponse,HttpStatus.OK);

    }


}
