package org.ahmedukamel.ecommerce.service.impl;

import org.ahmedukamel.ecommerce.dto.request.AddressRequest;
import org.ahmedukamel.ecommerce.dto.response.AddressResponse;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.mapper.AddressMapper;
import org.ahmedukamel.ecommerce.model.Address;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.Region;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.repository.RegionRepository;
import org.ahmedukamel.ecommerce.util.MessageSourceUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.ahmedukamel.ecommerce.util.ValidationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getEmail;
import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.getProvider;

@Service
public class AddressServiceImplV2 extends AddressServiceImpl {
    final RegionRepository regionRepository;

    public AddressServiceImplV2(CustomerRepository customerRepository,
                                MessageSourceUtils messageSourceUtils,
                                RegionRepository regionRepository) {
        super(customerRepository, messageSourceUtils);
        this.regionRepository = regionRepository;
    }

    @Override
    public ApiResponse addAddress(Object object) {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        ValidationUtils.validateAddressesNumber(customer, messageSourceUtils);
        AddressRequest request = (AddressRequest) object;
        Region region = RepositoryUtils.getRegion(regionRepository, request.getRegionId());
        Address address = new Address();
        address.setDescription(request.getDescription());
        address.setZipCode(request.getZipCode());
        address.setActive(true);
        address.setCustomer(customer);
        address.setRegion(region);
        customer.getAddresses().add(address);
        customerRepository.save(customer);
        String message = messageSourceUtils.getMessage("operation.successful.add.address");
        return new ApiResponse(true, message);
    }

    @Override
    public ApiResponse getAddresses() {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());
        List<Address> activeAdds = customer.getAddresses().stream().filter(Address::getActive).toList();
        List<AddressResponse> addressResponseList = AddressMapper.toResponse(activeAdds);
        String message = messageSourceUtils.getMessage("operation.successful.get.address");
        return new ApiResponse(true, message, Map.of("addresses", addressResponseList));
    }
}
