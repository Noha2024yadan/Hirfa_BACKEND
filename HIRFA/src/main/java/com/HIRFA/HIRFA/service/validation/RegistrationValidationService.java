package com.HIRFA.HIRFA.service.validation;

import com.HIRFA.HIRFA.exception.ValidationException;
import org.springframework.stereotype.Service;

@Service
public class RegistrationValidationService {

    public void validateRegistration(Object dto) {
        // This can be extended with more validation rules
        if (dto instanceof com.HIRFA.HIRFA.dto.ClientRegistrationDto) {
            validateClientRegistration((com.HIRFA.HIRFA.dto.ClientRegistrationDto) dto);
        } else if (dto instanceof com.HIRFA.HIRFA.dto.DesignerRegistrationDto) {
            validateDesignerRegistration((com.HIRFA.HIRFA.dto.DesignerRegistrationDto) dto);
        } else if (dto instanceof com.HIRFA.HIRFA.dto.CooperativeRegistrationDto) {
            validateCooperativeRegistration((com.HIRFA.HIRFA.dto.CooperativeRegistrationDto) dto);
        }
    }

    private void validateClientRegistration(com.HIRFA.HIRFA.dto.ClientRegistrationDto dto) {
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            throw new ValidationException("Password and confirmation do not match");
        }
    }

    private void validateDesignerRegistration(com.HIRFA.HIRFA.dto.DesignerRegistrationDto dto) {
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            throw new ValidationException("Password and confirmation do not match");
        }
    }

    private void validateCooperativeRegistration(com.HIRFA.HIRFA.dto.CooperativeRegistrationDto dto) {
        if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
            throw new ValidationException("Password and confirmation do not match");
        }
    }
}
