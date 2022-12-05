package com.foodive.domain;

import lombok.*;

import java.util.Date;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class PayVO {
    private Long payNo;
    private Long ono;
    @NonNull
    private String payment;
    private Integer state;
    private Date payDate;
}
