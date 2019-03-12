package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity authenticateCustomer (final String username, final String password) throws AuthenticationFailedException {
        CustomerEntity customerEntity=  customerDao.getUserByContact(username);
        if(customerEntity == null){
            throw new AuthenticationFailedException("ATH-001","This contact number has not been registered!");
        }
        final  String encryptedPassword = cryptographyProvider.encrypt(password,customerEntity.getSalt());
        if(encryptedPassword.equals(customerEntity.getPassword())){
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthTokenEntity customerAuthToken = new CustomerAuthTokenEntity();
            customerAuthToken.setCustomer(customerEntity);
            final  ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);
            customerAuthToken.setUuid(UUID.randomUUID().toString());
            customerAuthToken.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(),now,expiresAt));
            customerAuthToken.setLoginAt(now);
            customerAuthToken.setExpiresAt(expiresAt);
            customerDao.createAuthToken(customerAuthToken);
            return customerAuthToken;

        }else{
            throw new AuthenticationFailedException("ATH-002","Invalid Credentials");
        }
    }

    public String[] authenticate (final String authorization) throws AuthenticationFailedException {
        String[] decodedArray;
        byte[] decode;
        try{
            decode =  Base64.getDecoder().decode(authorization.split("Basic ")[1]);
            String decodedText = new String(decode);
            decodedArray = decodedText.split(":");
            return decodedArray;

        }catch(Exception e){
            throw new AuthenticationFailedException("ATH-003","Incorrect format of decoded customer name and password");

        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity tokenAuthenticate(final String authorization) throws AuthorizationFailedException {
        CustomerAuthTokenEntity customerAuthToken =  customerDao.getAuthToken(authorization);
        ZonedDateTime now = ZonedDateTime.now();
        if(customerAuthToken == null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");
        }else if(customerAuthToken.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-001","Customer is not Logged in.");

        }else if(now.isAfter(customerAuthToken.getExpiresAt())){
            throw new AuthorizationFailedException("ATHR-003","(Your session is expired. Log in again to access this endpoint.");
        }else{

            customerAuthToken.setLoginAt(now);
           CustomerAuthTokenEntity updateToken =  customerDao.updateAuthToken(customerAuthToken);
           return updateToken;
        }
    }
}
