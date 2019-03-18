package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressBusinessService {

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private OrderDao orderDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity customerAddressSave (final AddressEntity addressEntity) throws SaveAddressException, AddressNotFoundException {
        if((addressEntity.getCity().length() == 0) ||
                (addressEntity.getFlatBuilNumber().length() == 0) ||
                (addressEntity.getLocality().length() == 0) ||
                (addressEntity.getPinCode().length() == 0)) {
            throw new SaveAddressException("SAR-001","No field can be empty");
        } else if (addressEntity.getPinCode().matches("[0-9]+") && addressEntity.getPinCode().length()!= 6){
            throw new SaveAddressException("SAR-002","Invalid pincode");
        } else{
            return addressDao.createAddress(addressEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public StateEntity getState (final String stateId) throws AddressNotFoundException {
       StateEntity stateInformation = addressDao.getStateByUuid(stateId);
        if(stateInformation == null){
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }else{
            return stateInformation;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddress(final CustomerEntity customerEntity, final AddressEntity addressEntity){
        CustomerAddressEntity customerAddress = new CustomerAddressEntity();
        customerAddress.setAddress(addressEntity);
        customerAddress.setCustomer(customerEntity);
        return addressDao.customerAddress(customerAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AddressEntity> getCustomerAddressList(final String accessToken){
        return addressDao.getAddressList(accessToken);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity deleteAddress(final String uuid , CustomerEntity customerEntity) throws AddressNotFoundException, UpdateCustomerException ,AuthorizationFailedException {

        AddressEntity addressEntity = addressDao.getAddressByuUid(uuid);
        if (uuid == null) {
             throw new UpdateCustomerException("UAR-005","Address id can not be empty");
        } else if (addressEntity == null){
             throw new AddressNotFoundException("ANF-003","No address by this id");
        }
        // Check for Valid AccessToken .....
        final CustomerAddressEntity customerAddressEntity = addressDao.getCustomerByAddressId(addressEntity);
        final CustomerEntity customerEntity1 = customerAddressEntity.getCustomer();

        if(customerEntity1.getId() != customerEntity.getId()){
            throw new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }

        final List<OrderEntity> orderEntityList = orderDao.getOrderByAddress(addressEntity);
        if (orderEntityList == null){
            addressDao.deleteAddress(addressEntity);
            return addressEntity;
        }
        else {
            addressEntity.setActive(0);
            return addressDao.updateAddress(addressEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<StateEntity> getStates(){
        return addressDao.getAllStates();
    }

}
