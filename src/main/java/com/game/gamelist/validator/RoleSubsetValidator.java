package com.game.gamelist.validator;

import com.game.gamelist.entity.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleSubsetValidator implements ConstraintValidator<RoleSubset, Set<Role>> {
    private Set<String> acceptedValues;

    @Override
    public void initialize(RoleSubset constraint) {
        acceptedValues = Arrays.stream(constraint.anyOf())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(Set<Role> roles, ConstraintValidatorContext context) {
        return roles == null || roles.stream()
                .map(Enum::name)
                .allMatch(acceptedValues::contains);
    }
}
