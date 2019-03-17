package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class SignupBusinessService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {

        // Check for contact number is exist or not ...
        // Check for Field value whether they are empty or not .....
        // Check for Email-Address in correct-format ....
        // Check for contact-number in correct-format ....
        // Check for Valid Password in correct-format ...

        if(customerDao.contactCheck(customerEntity) != null) {
           throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        } else if(customerEntity.getFirstname().length() == 0 ||
                  customerEntity.getPassword().length() == 0 ||
                  customerEntity.getContactNumber().length() == 0 ||
                  customerEntity.getEmail().length() == 0) {
           throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        } else if(!(customerEntity.getEmail().matches("\\w+?@\\w+?\\x2E.+"))) {

            throw new SignUpRestrictedException("SGR-002","Invalid email-id format!");
        } else if (!customerEntity.getContactNumber().matches("[0-9]+") ||
                customerEntity.getContactNumber().length()!= 10) {
            throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
        } else if(!customerEntity.getPassword().matches("^(?=.*[A-Z])(?=.*[!@#$%^&*])(?=.*\\d).+$") ||
               customerEntity.getPassword().length() < 8) {
            throw new SignUpRestrictedException("SGR-004","Weak password!");
        } else {
           // Save Customer details ....
           String[] encryptedText = cryptographyProvider.encrypt(customerEntity.getPassword());
           customerEntity.setSalt(encryptedText[0]);
           customerEntity.setPassword(encryptedText[1]);
           return customerDao.createCustomer(customerEntity);
        }
    }
}
