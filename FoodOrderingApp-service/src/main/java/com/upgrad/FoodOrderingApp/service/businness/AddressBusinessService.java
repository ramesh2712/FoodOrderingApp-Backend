package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressBusinessService {

    @Autowired
    private AddressDao addressDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity customerAddressSave (final AddressEntity addressEntity) throws SaveAddressException, AddressNotFoundException {
        if((addressEntity.getCity() == null) || (addressEntity.getFlatBuilNumber() == null) || (addressEntity.getLocality() == null)
                || (addressEntity.getPinCode() == null)){
            throw new SaveAddressException("SAR-001","No field can be empty");
        } else if (addressEntity.getPinCode().matches("[0-9]+") && addressEntity.getPinCode().length()!= 6){
            throw new SaveAddressException("SAR-002","Invalid pincode");
        } else{
            return addressDao.createAddress(addressEntity);
        }

    }

    public StateEntity getState (final String stateId) throws AddressNotFoundException {
       StateEntity stateInformation = addressDao.getStateByUuid(stateId);
        if(stateInformation == null){
            throw new AddressNotFoundException("ANF-002","No state by this id");
        }else{
            return stateInformation;
        }
    }
}
