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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
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
//    private static final String imageUploadFolder = "c:\\upload\\image";

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
    public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile, HttpServletRequest request) {
        log.info("upload ajax post");

        List<AttachFileDTO> list = new ArrayList<>();
        String imageUploadFolder = request.getSession().getServletContext().getRealPath(uploadFolder) + File.separator + "img";

        // make folder
        String uploadFolderPath = getFolder();
        File uploadPath = new File(imageUploadFolder, uploadFolderPath);
        log.info("uploadPath: "+uploadPath);

        if(!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile multipartFile : uploadFile) {
            log.info("upload file name: "+multipartFile.getOriginalFilename());
            log.info("upload file size: "+multipartFile.getSize());

            AttachFileDTO attachFileDTO = new AttachFileDTO();

            String uploadFileName = multipartFile.getOriginalFilename();
            attachFileDTO.setFileName(uploadFileName);

            //UUID
            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);

                attachFileDTO.setUuid(uuid.toString());
                attachFileDTO.setUploadPath(uploadFolderPath);

                // check image type file
                if (checkImageType(saveFile)) {
                    attachFileDTO.setImage(true);

                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
                    thumbnail.close();
                }

                list.add(attachFileDTO);
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
            String detailUploadFolder = request.getSession().getServletContext().getRealPath(uploadFolder) + File.separator + "detail";

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

    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName, boolean isImage, HttpServletRequest request) {
        log.info("file name: "+fileName);

        String path = request.getSession().getServletContext().getRealPath(uploadFolder) + File.separator;
        path += isImage ? "img" : "detail";
        File file = new File(path+File.separator+fileName);
//        File file = new File("c:\\upload\\image\\"+fileName);

        log.info("FILE: "+file);

        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
            log.info("result");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(String fileName, String type, boolean isImage, HttpServletRequest request) {
        File file;

        String path = request.getSession().getServletContext().getRealPath(uploadFolder) + File.separator;
        path += isImage ? "img" : "detail";

        ResponseEntity<String> result;

        try {
            file = new File(path + File.separator + URLDecoder.decode(fileName, "UTF-8"));
            log.info(file.getName());
            log.info("file: "+file);

            file.delete();

            if (type.equals("image")) {
                String largeFileName = file.getAbsolutePath().replace("s_", "");
                log.info("large file name: "+largeFileName);
                file = new File(largeFileName);

                file.delete();
            }

            result = new ResponseEntity<>("deleted", HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return result;
    }
}
