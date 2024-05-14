package org.ahmedukamel.ecommerce.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.request.AddressRequest;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Address;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.service.AddressService;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    final CustomerRepository customerRepository;
    final MessageSourceUtils messageSourceUtils;

    @Override
    public ApiResponse addAddress(Object object) {
        AddressRequest request = (AddressRequest) object;
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Validating
        ValidationUtils.validateAddressesNumber(customer, messageSourceUtils);
        // Processing
        Address address = null; // AddressMapper.fromResponse(request);
        address.setCustomer(customer);
        customer.getAddresses().add(address);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.add.address");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse deleteAddress(Integer addressId) {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Validating
        Address address = ValidationUtils.validateGetCustomerAddress(customer, addressId, messageSourceUtils);
        ValidationUtils.validateActiveAddress(address);
        // Processing
        address.setActive(false);
        customerRepository.save(customer);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.delete.address");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getAddresses() {
        // Querying
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        // Processing
        List<Address> activeAdds = customer.getAddresses().stream().filter(Address::getActive).toList();
//        List<AddressRequest> addressResponseList = AddressMapper.toResponse(activeAdds);
        // Response
        String message = messageSourceUtils.getMessage("operation.successful.get.address");
//        return new ApiResponse(true, message, Map.of("addresses", addressResponseList));
        return null;
    }
}