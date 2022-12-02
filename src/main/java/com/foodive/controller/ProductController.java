package com.foodive.controller;

import com.foodive.domain.*;
import com.foodive.persistence.ProductMsg;
import com.foodive.service.CategoryService;
import com.foodive.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.foodive.controller.ProductImageController.uploadFolder;

@RequestMapping("/product/*")
@Controller
@Log4j
@AllArgsConstructor
public class ProductController {
    private ProductService service;
    private CategoryService categoryService;

    @GetMapping(value = "/productPage")
    public void goProductPage(Model model) {
        CategoryPageDTO categoryPageDTO = new CategoryController(categoryService).getHighCategoryList();
        model.addAttribute("gnb", categoryPageDTO);
    }

    @GetMapping(value = "/pages/{hCode}/{state}")
    @ResponseBody
    public ResponseEntity<CategoryPageDTO> getCategoryList(
            @PathVariable("hCode") String hCode,
            @PathVariable("state") int state
    ) {
        return new CategoryController(categoryService).getList(hCode, state, 0);
    }

    @PostMapping(
            value = "/check",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> check(@RequestBody DuplicateInfo duplicateInfo) {
        return service.duplicate(duplicateInfo) ?
                new ResponseEntity<>(ProductMsg.SINGLE, HttpStatus.OK)
                : new ResponseEntity<>(ProductMsg.DUPLICATE, HttpStatus.OK);
    }

    @PostMapping(
            value = "/register",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> register(@RequestBody ProductVO product) {
        log.info("new product!");

        if (product.getImageList() != null) {
            product.getImageList().forEach(log::info);
        }

        service.register(product);

        return new ResponseEntity<>(ProductMsg.INSERT, HttpStatus.OK);
    }

    @GetMapping(
            value = {"/list/{state}/{page}", "/list/{state}/{page}"},
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<ProductPageDTO> getList(
            @PathVariable("state") int state,
            @PathVariable("page") int page,
            @RequestParam(value = "keyword", required = false) Optional<String> keyword,
            @RequestParam(value = "code", required = false) Optional<String> code
    ) {
        log.info("keyword: "+keyword);
        log.info("code: "+code);
        log.info("state: "+state);
        log.info("page: "+page);

        String name = keyword.orElse("null");
        String category = code.orElse("null");
        log.info("name: "+name);
        log.info("category: "+category);

        ProductVO product = new ProductVO();
        product.setCode(category);
        product.setKorName(name);
        product.setState(state);
        Criteria criteria = new Criteria(page, 10);

        return new ResponseEntity<>(service.getList(criteria, product), HttpStatus.OK);
    }

    @GetMapping(
            value = "/admin/{pno}",
            produces = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.APPLICATION_JSON_VALUE
            }
    )
    @ResponseBody
    public ResponseEntity<ProductVO> get(
            @PathVariable("pno") Long pno
    ) {
        ProductVO productVO = new ProductVO();
        productVO.setPno(pno);

        ProductVO product = service.get(productVO);
        product.setImageList(service.getImageList(product.getPno()));

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping(
            value = "/{pno}",
            consumes = "application/json; charset=utf-8",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> modify(
            @PathVariable("pno") Long pno,
            @RequestBody ProductVO product
    ) {
        product.setPno(pno);

        return service.modify(product) ?
                new ResponseEntity<>(ProductMsg.MODIFY, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 첨부 파일 삭제 처리
    public void deleteFiles(List<ProductImageVO> imageList, HttpServletRequest request) {
        if (imageList==null || imageList.size()==0) {
            return;
        }

        log.info("delete image files");

        imageList.forEach(image -> {
            try {
                // java.nio.file
                String realUploadFolder = request.getSession().getServletContext().getRealPath(uploadFolder) + File.separator + "img";
                Path file = Paths.get(realUploadFolder+image.getUploadPath()+File.separator+
                        image.getUuid()+"_"+image.getFileName());

                Files.deleteIfExists(file);

                if (Files.probeContentType(file).startsWith("image")) {
                    Path thumbNail = Paths.get(realUploadFolder+image.getUploadPath()+
                            File.separator+"s_"+image.getUuid()+"_"+image.getFileName());
                    Files.deleteIfExists(thumbNail);
                }
            } catch (Exception e) {
                log.error("delete file error: "+e.getMessage());
            }
        });
    }

    @PutMapping(
            value = "/drop/{pno}",
            produces = "text/plain; charset=utf-8"
    )
    @ResponseBody
    public ResponseEntity<String> drop(
            @PathVariable("pno") Long pno
    ) {
        return service.drop(pno) ?
                new ResponseEntity<>(ProductMsg.DROP, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/list")
    public void goProductList(
            @RequestParam(value = "keyword", required = false) Optional<String> keyword,
            @RequestParam(value = "code", required = false) Optional<String> code,
            Model model
    ) {
        String name = keyword.orElse("null");
        String category = code.orElse("null");

        model.addAttribute("category", category);
        model.addAttribute("keyword", name);
    }

}
