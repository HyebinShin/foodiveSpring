package com.foodive.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DatePageDTO {
    private Integer dateNumber;
    private Integer state;

    private Integer page;
    private String id;
}
