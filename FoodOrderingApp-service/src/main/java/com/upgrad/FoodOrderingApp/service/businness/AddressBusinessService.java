package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
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
    public List<AddressEntity> getCustomerAddress (final String accessToken){
         return addressDao.getddress(accessToken);
    }


  @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressEntity saveCustomerAddress (CustomerAddressEntity customerAddress){
        return addressDao.customerAddress(customerAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity getAddressByUuid (final String uUid) throws AddressNotFoundException, UpdateCustomerException {
     AddressEntity address = addressDao.getAddByuUid(uUid);
      if(uUid == null){
         throw new UpdateCustomerException("UAR-005","Address id can not be empty");
     }else if(address == null){
         throw new AddressNotFoundException("(ANF-003","No address by this id");
     }else{
          address.setActive(0);
          return addressDao.deleteAddress (address);
     }
    }

    public List<StateEntity> getStates(){
        return addressDao.getAllStates();
    }


}
