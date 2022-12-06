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
    private Integer state; // 0: 무통장 입금 전 , 1: 결제 완료, 2: 결제 취소
    private Date payDate;
}
