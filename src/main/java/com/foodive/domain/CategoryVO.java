package com.foodive.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CategoryVO {
    private Long cno;
    private String code;
    private String name;
    private Integer state;

    private Date regDate;
    private Date modDate;
    private Date dropDate;

    @JsonProperty("hCode")
    private String hCode;
    @JsonProperty("eName")
    private String eName;
}
