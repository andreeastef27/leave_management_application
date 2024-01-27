package com.example.leave_manager.dto;

import lombok.*;

@Data
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
    private int departmentId;

    private String departmentName;
}
