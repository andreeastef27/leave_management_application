package com.example.leave_manager.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private int userId;

    private int departmentId;

    private String name;

    private String surname;

    private String marca;

    private String role;
}
