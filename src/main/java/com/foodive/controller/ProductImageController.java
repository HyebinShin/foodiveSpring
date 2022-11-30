package com.foodive.controller;

import com.foodive.domain.AttachFileDTO;
import com.foodive.domain.ProductImageVO;
import com.foodive.domain.SmartEditorDTO;
import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j
public class ProductImageController {

    private static final String uploadFolder = "/resources/foodive";

    private String getFolder() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String str = simpleDateFormat.format(date);

        return str.replace("-", File.separator);
    }

    private boolean checkImageType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());

            return contentType.startsWith("image");
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return false;
    }

    @PostMapping(
            value = "/uploadAjaxAction",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseEntity<List<ProductImageVO>> uploadAjaxPost(MultipartFile[] uploadFile, HttpServletRequest request) {
        log.info("upload ajax post");

        List<ProductImageVO> list = new ArrayList<>();
        String imgUploadFolder = request.getSession().getServletContext().getRealPath(uploadFolder)+File.separator+"img";

        // make folder
        String uploadFolderPath = getFolder();
        File uploadPath = new File(imgUploadFolder, uploadFolderPath);
        log.info("uploadPath: "+uploadPath);

        if(!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            log.info("upload file name: "+multipartFile.getOriginalFilename());
            log.info("upload file size: "+multipartFile.getSize());

            ProductImageVO imageVO = new ProductImageVO();

            String uploadFileName = multipartFile.getOriginalFilename();

            //UUID
            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);

                imageVO.setUuid(uuid.toString());
                imageVO.setUploadPath(uploadFolderPath);

                // check image type file
                if (checkImageType(saveFile)) {
                    imageVO.setFileType(true);

                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
                    thumbnail.close();
                }

                list.add(imageVO);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping(
            value = "/smartEditorAction",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String smartEditorAction(SmartEditorDTO smartEditorDTO, HttpServletRequest request) {
        log.info("smartEditor action");

        String callback = smartEditorDTO.getCallback();
        String callbackFunc = "?callback_func="+smartEditorDTO.getCallback_func();
        String returnUrl = "";
        log.info("callback: "+callback);
        log.info("callbackFunc: "+callbackFunc);

        try {
            String detailUploadFolder = request.getSession().getServletContext().getRealPath(uploadFolder)+File.separator+"detail";

            // make folder
            String uploadFolderPath = getFolder();
            String date = uploadFolderPath.replaceAll("\\\\", "/");
            File uploadPath = new File(detailUploadFolder, uploadFolderPath);
            log.info("uploadPath: "+uploadPath);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            // file upload
            MultipartFile multipartFile = smartEditorDTO.getFiledata();

            log.info("multipartFile: "+multipartFile);

            assert multipartFile != null;
            String originalFilename = multipartFile.getOriginalFilename();

            log.info("originalFilename: "+originalFilename);

            // UUID
            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + originalFilename;

            log.info("uuid uploadFileName: "+uploadFileName);

            File saveFile = new File(uploadPath, uploadFileName);
            multipartFile.transferTo(saveFile);

            log.info("file add!");

            if (checkImageType(saveFile)) {
                FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
                Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
                thumbnail.close();

                log.info("thumbnail add!");
            }

            returnUrl += "&bNewLine=true";
            returnUrl += "&sFileName="+originalFilename;
            returnUrl += "&sFileURL=/resources/foodive/detail/"+date+"/"+uploadFileName;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        String url = callback+callbackFunc+returnUrl;
        log.info("url: "+url);

        return "redirect:"+url;
    }

}
