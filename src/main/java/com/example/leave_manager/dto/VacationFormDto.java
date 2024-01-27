package com.example.leave_manager.dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VacationFormDto {
    private int formId;

    private Date startDate;

    private Date stopDate;

    private int userId;

    private int vacationId;
}
