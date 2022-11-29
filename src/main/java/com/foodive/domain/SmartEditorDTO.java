package com.foodive.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SmartEditorDTO {
    private MultipartFile Filedata;
    private String callback;
    private String callback_func;
}
