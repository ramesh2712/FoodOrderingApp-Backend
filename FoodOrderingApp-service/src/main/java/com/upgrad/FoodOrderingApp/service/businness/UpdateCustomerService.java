package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



@Service
public class UpdateCustomerService {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer (final String firstName,final String lastName,final CustomerAuthTokenEntity customerAuthToken) throws UpdateCustomerException {

        if(firstName == null){
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");
        }
        else{

            CustomerEntity customer = customerAuthToken.getCustomers();
            customer.setFirstname(firstName);
            customer.setLastname(lastName);
            return customerDao.updateCustomerDetails(customer);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updatePassword (final String oldPassword, final String newPassword, final CustomerEntity customer) throws UpdateCustomerException {
        if(oldPassword == null || newPassword == null){
            throw new UpdateCustomerException("(UCR-003","No field should be empty");
        }else if(!newPassword.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).+$")){
            throw new UpdateCustomerException("UCR-001","Weak password!");
        }else{
             final String passwordEncrypt = cryptographyProvider.encrypt(oldPassword,customer.getSalt());
             if(!passwordEncrypt.equals(customer.getPassword())){
                 throw new UpdateCustomerException("UCR-004","Incorrect old password!");
             }else{
                 final String newPasswordEncrypt = cryptographyProvider.encrypt(newPassword,customer.getSalt());
                 customer.setPassword(newPasswordEncrypt);
                 CustomerEntity customerUpdate = customerDao.updateCustomerPassword(customer);
                 return customerUpdate;
             }

        }
    }

}
