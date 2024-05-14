package org.ahmedukamel.ecommerce.util;

import org.ahmedukamel.ecommerce.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static ResponseEntity<ApiResponse> acceptedResponse(ApiResponse response) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public static ResponseEntity<ApiResponse> forbiddenResponse(ApiResponse response) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(response);
    }

    public static ResponseEntity<ApiResponse> badRequestResponse(ApiResponse response) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
