package com.foodive.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateInfo {
    private String duplicateParam;
    private String duplicateCase;
}
