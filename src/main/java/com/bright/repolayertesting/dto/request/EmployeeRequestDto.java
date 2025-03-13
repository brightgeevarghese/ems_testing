package com.bright.repolayertesting.dto.request;

public record EmployeeRequestDto(
        String firstName,
        String lastName,
        String email,
        String departmentCode
) {
}
