package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


@Service
public class UpdateCustomerService {
    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(final String firstName,final String lastName,final CustomerEntity customerEntity) throws UpdateCustomerException {

        if(firstName.length() == 0) {
            throw new UpdateCustomerException("UCR-002","First name field should not be empty");
        } else {
            customerEntity.setFirstname(firstName);
            customerEntity.setLastname(lastName);
            return customerDao.updateCustomerDetails(customerEntity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(final String oldPassword, final String newPassword, final CustomerEntity customer) throws UpdateCustomerException {
        // Check for different password validations .....
        if(oldPassword.length() == 0 || newPassword.length() == 0) {
            throw new UpdateCustomerException("UCR-003","No field should be empty");
        } else if(!newPassword.matches("^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).+$") || newPassword.length() < 8) {
            throw new UpdateCustomerException("UCR-001","Weak password!");
        } else {
             final String passwordEncrypt = cryptographyProvider.encrypt(oldPassword,customer.getSalt());
             if(!passwordEncrypt.equals(customer.getPassword())) {
                 throw new UpdateCustomerException("UCR-004","Incorrect old password!");
             } else {
                 final String newPasswordEncrypt = cryptographyProvider.encrypt(newPassword,customer.getSalt());
                 customer.setPassword(newPasswordEncrypt);
                 CustomerEntity customerUpdate = customerDao.updateCustomerPassword(customer);
                 return customerUpdate;
             }
        }
    }

    public CustomerEntity getCustomer(final String accessToken) throws AuthorizationFailedException {
        CustomerAuthTokenEntity customerAuthToken =  customerDao.getAuthToken(accessToken);
        ZonedDateTime now = ZonedDateTime.now();
        if(customerAuthToken == null) {
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        } else if(customerAuthToken.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002","Customer is logged out. Log in again to access this endpoint.");
        } else if(now.isAfter(customerAuthToken.getExpiresAt())) {
            throw new AuthorizationFailedException("ATHR-003","Your session is expired. Log in again to access this endpoint.");
        } else {
            return customerAuthToken.getCustomers();
        }
    }
}
