package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.service.businness.AuthenticationService;
import com.upgrad.FoodOrderingApp.service.businness.SignupBusinessService;
import com.upgrad.FoodOrderingApp.service.businness.UpdateCustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.Base64.getEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This class contains all the test cases regarding the customer controller
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignupBusinessService mockCustomerService;

    @MockBean
    private UpdateCustomerService mockUpdateCustomerService;

    @MockBean
    private AuthenticationService mockAuthenticationService;


    // ----------------------------- POST /customer/signup --------------------------------

    //This test case passes when you are able to signup successfully.
    @Test
    public void shouldSignUpForValidRequest() throws Exception {
        final CustomerEntity createdCustomerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        createdCustomerEntity.setUuid(customerId);
        when(mockCustomerService.saveCustomer(any())).thenReturn(createdCustomerEntity);

        mockMvc
                .perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\", \"email_address\":\"abc@email.com\", \"contact_number\":\"9090909090\", \"password\":\"qawsedrf@123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(customerId));
        verify(mockCustomerService, times(1)).saveCustomer(any());
    }

    //This test case passes when you have handled the exception of trying to signup but the request field is empty.
    @Test
    public void shouldNotSignUpForEmptyRequest() throws Exception {
        when(mockCustomerService.saveCustomer(any()))
                .thenThrow(new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled"));
        mockMvc
                .perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\", \"email_address\":\"\", \"contact_number\":\"9090909090\", \"password\":\"qawsedrf@123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("SGR-005"));
        verify(mockCustomerService, times(1)).saveCustomer(any());
    }

    //This test case passes when you have handled the exception of trying to signup with invalid email-id.
    @Test
    public void shouldNotSignUpForInvalidEmailId() throws Exception {
        when(mockCustomerService.saveCustomer(any()))
                .thenThrow(new SignUpRestrictedException("SGR-002", "Invalid email-id format!"));

        mockMvc
                .perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\", \"email_address\":\"abc@1\", \"contact_number\":\"9090909090\", \"password\":\"qawsedrf@123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("SGR-002"));
        verify(mockCustomerService, times(1)).saveCustomer(any());
    }

    //This test case passes when you have handled the exception of trying to signup with invalid contact number.
    @Test
    public void shouldNotSignUpForInvalidContactNo() throws Exception {
        when(mockCustomerService.saveCustomer(any()))
                .thenThrow(new SignUpRestrictedException("SGR-003", "Invalid contact number!"));

        mockMvc
                .perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\", \"email_address\":\"abc@email.com\", \"contact_number\":\"123\", \"password\":\"qawsedrf@123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("SGR-003"));
        verify(mockCustomerService, times(1)).saveCustomer(any());
    }

    //This test case passes when you have handled the exception of trying to signup with invalid password.
    @Test
    public void shouldNotSignUpForInvalidPassword() throws Exception {
        when(mockCustomerService.saveCustomer(any()))
                .thenThrow(new SignUpRestrictedException("SGR-004", "Weak password!"));

        mockMvc
                .perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\", \"email_address\":\"abc@email.com\", \"contact_number\":\"9090909090\", \"password\":\"1\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("SGR-004"));
        verify(mockCustomerService, times(1)).saveCustomer(any());
    }

    //This test case passes when you have handled the exception of trying to signup with a contact number which is
    // already registered.
    @Test
    public void shouldNotSignUpIfTheContactIsAlreadySignedUp() throws Exception {
        when(mockCustomerService.saveCustomer(any()))
                .thenThrow(new SignUpRestrictedException("SGR-001", "Try any other contact number, this contact number has already been taken"));

        mockMvc
                .perform(post("/customer/signup")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\", \"email_address\":\"abc@email.com\", \"contact_number\":\"9090909090\", \"password\":\"qawsedrf@123\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("SGR-001"));
        verify(mockCustomerService, times(1)).saveCustomer(any());
    }

    // ----------------------------- POST /customer/login --------------------------------

    //This test case passes when you are able to login successfully.
    @Test
    public void shouldLoginForValidRequest() throws Exception {

        final CustomerAuthTokenEntity createdCustomerAuthEntity = new CustomerAuthTokenEntity();
        createdCustomerAuthEntity.setAccessToken("accessToken");
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        createdCustomerAuthEntity.setCustomers(customerEntity);

        when(mockAuthenticationService.authenticateCustomer("9090909090", "CorrectPassword"))
                .thenReturn(createdCustomerAuthEntity);

        mockMvc
                .perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Basic " + getEncoder().encodeToString("9090909090:CorrectPassword".getBytes())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(customerId))
                .andExpect(header().exists("access-token"));
        verify(mockAuthenticationService, times(1)).authenticateCustomer("9090909090", "CorrectPassword");
    }

    //This test case passes when you have handled the exception of trying to login with invalid authorization format.
    @Test
    public void shouldNotLoginForInvalidAuthorizationFormat() throws Exception {
        mockMvc
                .perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Basic " + getEncoder().encodeToString(":".getBytes())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value("ATH-003"));
        verify(mockAuthenticationService, times(1)).authenticateCustomer(anyString(), anyString());
    }

    //This test case passes when you have handled the exception of trying to login with a contact number that is not
    // registered yet.
    @Test
    public void shouldNotLoginIfNoDataPresentForGivenMobileNo() throws Exception {
        when(mockAuthenticationService.authenticateCustomer("123", "CorrectPassword"))
                .thenThrow(new AuthenticationFailedException("ATH-001", "This contact number does not exist"));
        mockMvc
                .perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Basic " + getEncoder().encodeToString("123:CorrectPassword".getBytes())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value("ATH-001"));
        verify(mockAuthenticationService, times(1)).authenticateCustomer("123", "CorrectPassword");
    }

    //This test case passes when you have handled the exception of trying to login with incorrect password.
    @Test
    public void shouldNotLoginForWrongPassword() throws Exception {
        when(mockAuthenticationService.authenticateCustomer("9090909090", "IncorrectPassword"))
                .thenThrow(new AuthenticationFailedException("ATH-002", "Password failed"));
        mockMvc
                .perform(post("/customer/login")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Basic " + getEncoder().encodeToString("9090909090:IncorrectPassword".getBytes())))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("code").value("ATH-002"));
        verify(mockAuthenticationService, times(1)).authenticateCustomer("9090909090", "IncorrectPassword");
    }

    // ----------------------------- POST /customer/logout --------------------------------

    //This test case passes when you are able to logout successfully.
    @Test
    public void shouldLogoutForValidRequest() throws Exception {
        final CustomerAuthTokenEntity createdCustomerAuthEntity = new CustomerAuthTokenEntity();
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        createdCustomerAuthEntity.setCustomers(customerEntity);
        when(mockAuthenticationService.logout("access-token")).thenReturn(createdCustomerAuthEntity);

        mockMvc
                .perform(post("/customer/logout")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer access-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(customerId));
        verify(mockAuthenticationService, times(1)).logout("access-token");
    }

    //This test case passes when you have handled the exception of trying to logout without even logging in.
    @Test
    public void shouldNotLogoutWhenCustomerIsNotLoggedIn() throws Exception {
        when(mockAuthenticationService.logout("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));

        mockMvc
                .perform(post("/customer/logout")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));
        verify(mockAuthenticationService, times(1)).logout("auth");
    }

    //This test case passes when you have handled the exception of trying to logout when you have already logged out.
    @Test
    public void shouldNotLogoutIfCustomerIsAlreadyLoggedOut() throws Exception {
        when(mockAuthenticationService.logout("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));

        mockMvc
                .perform(post("/customer/logout")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));
        verify(mockAuthenticationService, times(1)).logout("auth");
    }

    //This test case passes when you have handled the exception of trying to logout while your session is already expired.
    @Test
    public void shouldNotLogoutIfSessionIsExpired() throws Exception {
        when(mockAuthenticationService.logout("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));

        mockMvc
                .perform(post("/customer/logout")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));
        verify(mockAuthenticationService, times(1)).logout("auth");
    }

    // ----------------------------- PUT /customer --------------------------------

    //This test case passes when you are able to update customer details successfully.
    @Test
    public void shouldUpdateCustomerDetails() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setFirstname("firstname");
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);

        when(mockUpdateCustomerService.getCustomer("auth")).thenReturn(customerEntity);

        final CustomerEntity updatedCustomerEntity = new CustomerEntity();
        updatedCustomerEntity.setFirstname("first");
        updatedCustomerEntity.setLastname("last");
        updatedCustomerEntity.setUuid(customerId);
        when(mockUpdateCustomerService.updateCustomer("first","last",customerEntity)).thenReturn(updatedCustomerEntity);
        mockMvc
                .perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(customerId));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(1)).updateCustomer("first","last",customerEntity);
    }

    //This test case passes when you have handled the exception of trying to update user details but the first name
    // field is empty.
    @Test
    public void shouldNotUpdateCustomerDetailsIfFirstNameNotPresentInTheRequest() throws Exception {
        mockMvc
                .perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "auth")
                        .content("{\"first_name\":\"\", \"last_name\":\"last\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("UCR-002"));
        verify(mockUpdateCustomerService, times(0)).getCustomer(anyString());
        verify(mockUpdateCustomerService, times(0)).updateCustomer("","last", any());
    }

    //This test case passes when you have handled the exception of trying to update customer details when the customer
    // is not logged in.
    @Test
    public void shouldNotUpdateCustomerDetailsWhenCustomerIsNotLoggedIn() throws Exception {
        when(mockUpdateCustomerService.getCustomer("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));

        mockMvc
                .perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(0)).updateCustomer("first","last",any());
    }

    //This test case passes when you have handled the exception of trying to update customer details while you are
    // already logged out.
    @Test
    public void shouldUpdateCustomerDetailsIfCustomerIsAlreadyLoggedOut() throws Exception {
        when(mockUpdateCustomerService.getCustomer("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));

        mockMvc
                .perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(0)).updateCustomer("first", "last",any());
    }

    //This test case passes when you have handled the exception of trying to update customer details while your session
    // is already expired.
    @Test
    public void shouldUpdateCustomerDetailsIfSessionIsExpired() throws Exception {
        when(mockUpdateCustomerService.getCustomer("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));

        mockMvc
                .perform(put("/customer")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"first_name\":\"first\", \"last_name\":\"last\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(0)).updateCustomer("first","last",any());
    }

    // ----------------------------- PUT /customer/password --------------------------------

    //This test case passes when you are able to update your password successfully.
    @Test
    public void shouldUpdateCustomerPassword() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);

        when(mockUpdateCustomerService.getCustomer("auth")).thenReturn(customerEntity);
        when(mockUpdateCustomerService.updateCustomerPassword("oldPwd", "newPwd", customerEntity))
                .thenReturn(customerEntity);
        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"old_password\":\"oldPwd\", \"new_password\":\"newPwd\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(customerId));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(1)).updateCustomerPassword("oldPwd", "newPwd", customerEntity);
    }

    //This test case passes when you have handled the exception of trying to update your password but your old password
    // field is empty.
    @Test
    public void shouldNotUpdateCustomerPasswordIfOldPasswordIsEmpty() throws Exception {
        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "auth")
                        .content("{\"old_password\":\"\", \"new_password\":\"newPwd\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("UCR-003"));
        verify(mockUpdateCustomerService, times(0)).getCustomer(anyString());
        verify(mockUpdateCustomerService, times(0)).updateCustomerPassword("","newPwd",any());
    }

    //This test case passes when you have handled the exception of trying to update your password when your new password
    // field is empty
    @Test
    public void shouldNotUpdateCustomerPasswordIfNewPasswordIsEmpty() throws Exception {
        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "auth")
                        .content("{\"old_password\":\"oldPwd\", \"new_password\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("UCR-003"));
        verify(mockUpdateCustomerService, times(0)).getCustomer(anyString());
        verify(mockUpdateCustomerService, times(0)).updateCustomerPassword("oldPwd", "",any());
    }

    //This test case passes when you have handled the exception of trying to update your password but you are not
    // logged in.
    @Test
    public void shouldNotUpdateCustomerPasswordWhenCustomerIsNotLoggedIn() throws Exception {
        when(mockUpdateCustomerService.getCustomer("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));

        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"old_password\":\"oldPwd\", \"new_password\":\"newPwd\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(0)).updateCustomerPassword("oldPwd", "newPwd", any());
    }

    //This test case passes when you have handled the exception of trying to update your password but you are already
    // logged out.
    @Test
    public void shouldUpdateCustomerPasswordIfCustomerIsAlreadyLoggedOut() throws Exception {
        when(mockUpdateCustomerService.getCustomer("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));

        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"old_password\":\"oldPwd\", \"new_password\":\"newPwd\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(0)).updateCustomerPassword("oldPwd","newPwd",any());
    }

    //This test case passes when you have handled the exception of trying to update your password but your session is
    // already expired.
    @Test
    public void shouldUpdateCustomerPasswordIfSessionIsExpired() throws Exception {
        when(mockUpdateCustomerService.getCustomer("auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));

        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"old_password\":\"oldPwd\", \"new_password\":\"newPwd\"}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(0)).updateCustomerPassword("oldPwd","newPwd",any());
    }

    //This test case passes when you have handled the exception of trying to update your password while your new
    // password is weak.
    @Test
    public void shouldNotUpdateCustomerPasswordIfNewPasswordDoesNotFollowRecommendedPasswordFormat() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        when(mockUpdateCustomerService.getCustomer("auth")).thenReturn(customerEntity);
        when(mockUpdateCustomerService.updateCustomerPassword("oldPwd", "newPwd", customerEntity))
                .thenThrow(new UpdateCustomerException("UCR-001", "Weak password!"));
        mockMvc
                .perform(put("/customer/password")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer auth")
                        .content("{\"old_password\":\"oldPwd\", \"new_password\":\"newPwd\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("UCR-001"));
        verify(mockUpdateCustomerService, times(1)).getCustomer("auth");
        verify(mockUpdateCustomerService, times(1)).updateCustomerPassword("oldPwd", "newPwd", customerEntity);
    }
}