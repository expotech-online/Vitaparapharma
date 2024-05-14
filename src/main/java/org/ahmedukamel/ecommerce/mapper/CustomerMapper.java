package org.ahmedukamel.ecommerce.mapper;

import org.ahmedukamel.ecommerce.dto.CustomerDto;
import org.ahmedukamel.ecommerce.dto.CustomerDtoV2;
import org.ahmedukamel.ecommerce.dto.request.RegistrationRequest;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.CustomerDetail;
import org.ahmedukamel.ecommerce.model.Review;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.ahmedukamel.ecommerce.model.enumeration.Role;
import org.ahmedukamel.ecommerce.util.EntityDetailsUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

public class CustomerMapper {
    public static CustomerDto toResponse(Customer customer, Integer languageId) {
        CustomerDetail customerDetail = EntityDetailsUtils.supplyCustomerDetail(customer, languageId);
        CustomerDto response = new CustomerDto();
        response.setCustomerId(customer.getCustomerId());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setFirstName(customerDetail.getFirstName());
        response.setLastName(customerDetail.getLastName());
        response.setRole(customer.getRole().name());
        response.setEnabled(customer.isEnabled());
        response.setLocked(!customer.isAccountNonLocked());
        return response;
    }

    public static List<CustomerDto> toResponse(Collection<Customer> items, Integer languageId) {
        return items.stream().map(item -> CustomerMapper.toResponse(item, languageId)).toList();
    }

    public static Customer toCustomer(PasswordEncoder passwordEncoder, RegistrationRequest request) {
        Customer customer = new Customer();
        customer.setEmail(request.getEmail().toLowerCase().strip());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setPhone(request.getPhone().strip());
        customer.setProvider(Provider.NONE);
        customer.setRole(Role.CUSTOMER);
        customer.setEnabled(false);
        return customer;
    }

    public static CustomerDtoV2 toResponseV2(Customer customer, Integer languageId) {
        int reports = customer.getReviews()
                .stream()
                .map(Review::getReports)
                .mapToInt(Collection::size)
                .sum();

        CustomerDto dto = toResponse(customer, languageId);
        CustomerDtoV2 response = new CustomerDtoV2();
        BeanUtils.copyProperties(dto, response);
        response.setHasPicture(StringUtils.hasLength(customer.getPicture()));
        response.setPicture(getPicture(customer));
        response.setMale(customer.isMale());
        response.setDateOfBirth(getDateOfBirth(customer));
        response.setRegistration(customer.getRegistration().toString());
        response.setOrders(customer.getOrders().size());
        response.setCreatedReports(customer.getReports().size());
        response.setReports(reports);
        return response;
    }

    public static List<CustomerDtoV2> toResponseV2(Collection<Customer> items, Integer languageId) {
        return items.stream().map(item -> CustomerMapper.toResponseV2(item, languageId)).toList();
    }

    private static String getDateOfBirth(Customer customer) {
        return customer.getDateOfBirth() == null ? "" : customer.getDateOfBirth().toString();
    }

    private static String getPicture(Customer customer) {
        return StringUtils.hasLength(customer.getPicture()) ?
                "https://api.vitaparapharma.com/api/v1/public/profile/" + customer.getPicture() : null;
    }
}