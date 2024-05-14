package org.ahmedukamel.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {
    @GetMapping("/api/v1/public/ahmed")
    public String oauth(@RequestParam("token") String token, Authentication authentication) {
        System.out.println(authentication);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return token;
    }

    @Component
    @AllArgsConstructor
    public static class StringToBlaConverter implements Converter<String, Bla> {
        private ObjectMapper objectMapper;

        @SneakyThrows
        @Override
        public Bla convert(String source) {
            return objectMapper.readValue(source, Bla.class);
        }
    }

    //    public record Bla(@NotBlank @NotNull String name) {
//    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bla {
        @NotNull
        @NotBlank
        private String name;
    }

    @PostMapping("/api/v1/public/test")
    public String test(@Valid @ModelAttribute("token") Bla bla) {
        return bla.getName();
    }
}
