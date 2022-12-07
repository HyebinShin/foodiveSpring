package com.foodive.task;

import com.foodive.domain.ProductImageVO;
import com.foodive.mapper.ProductImageMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.foodive.controller.ProductImageController.uploadFolder;

@Log4j
@Component
public class FileCheckTask {
    @Setter(onMethod_ = {@Autowired})
    private ProductImageMapper imageMapper;

    // 어제 폴더
    private String getFolderYesterday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        String folder = simpleDateFormat.format(calendar.getTime());

        return folder.replace("-", File.separator);
    }

    @Scheduled(cron = "0 0 2 * * *") // 2시간 0분 0초마다 실행
    public void checkFiles() throws Exception {
        log.warn("file check task run");

        log.warn(new Date());

        // file list in DB
        List<ProductImageVO> fileList = imageMapper.getOldFiles();

        // ready for check file in directory with DB file list
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()).getRequest();

        String realUploadFolder = request.getSession().getServletContext().getRealPath(uploadFolder) +
                File.separator + "img";

        List<Path> fileListPaths = fileList.stream()
                .map(vo -> Paths.get(realUploadFolder, vo.getUploadPath()
                        , vo.getUuid()+"_"+vo.getFileName()))
                .collect(Collectors.toList());

        // image file has thumbnail file
        fileList.stream()
                .filter(ProductImageVO::isFileType)
                .map(vo -> Paths.get(realUploadFolder,
                        vo.getUploadPath(),
                        "s_"+vo.getUuid()+"_"+vo.getFileName()))
                .forEach(fileListPaths::add);

        log.warn("=====================");

        fileListPaths.forEach(log::warn);

        // files in yesterday directory
        File targetDir = Paths.get(realUploadFolder, getFolderYesterday()).toFile();

        File[] removeFiles = targetDir.listFiles(file -> !fileListPaths.contains(file.toPath()));

        log.warn("=====================");

        assert removeFiles != null;
        for (File file : removeFiles) {
            log.warn(file.getAbsolutePath());
            file.delete();
        }
    }
}
