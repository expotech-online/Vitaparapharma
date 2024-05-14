package org.ahmedukamel.ecommerce.service.profile;

import lombok.RequiredArgsConstructor;
import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.repository.CustomerRepository;
import org.ahmedukamel.ecommerce.service.file.FileService;
import org.ahmedukamel.ecommerce.util.ImageDirectoryUtils;
import org.ahmedukamel.ecommerce.util.RepositoryUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.ahmedukamel.ecommerce.util.SecurityContextUtils.*;

@Service
@RequiredArgsConstructor
public class ProfileImageServiceImpl implements ProfileImageService {
    final FileService fileService;
    final CustomerRepository customerRepository;

    @Override
    public byte[] getImage(String imageName) throws IOException {
        byte[] image = fileService.get(imageName, ImageDirectoryUtils.PROFILE_DIRECTORY_PATH);
        if (image == null) {
            Resource resource = new ClassPathResource("static/images/no-profile-picture.png");
            image = resource.getContentAsByteArray();
        }
        return image;
    }

    @Override
    public ApiResponse uploadImage(MultipartFile image) throws IOException {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());

        if (StringUtils.hasLength(customer.getPicture())) {
            fileService.delete(customer.getPicture(), ImageDirectoryUtils.PROFILE_DIRECTORY_PATH);
        }

        String imageName = fileService.save(image, ImageDirectoryUtils.PROFILE_DIRECTORY_PATH);
        customer.setPicture(imageName);
        customerRepository.save(customer);

        return new ApiResponse(true, "Successful upload image");
    }

    @Override
    public ApiResponse deleteImage() throws IOException {
        Customer customer = RepositoryUtils.getCustomer(customerRepository, getEmail(), getProvider());

        if (StringUtils.hasLength(customer.getPicture())) {
            fileService.delete(customer.getPicture(), ImageDirectoryUtils.PROFILE_DIRECTORY_PATH);

            customer.setPicture(null);
            customerRepository.save(customer);

            return new ApiResponse(true, "Successful delete image");
        }

        throw new RuntimeException("No profile image found");
    }
}