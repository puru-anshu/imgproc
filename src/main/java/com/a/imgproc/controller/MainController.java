package com.a.imgproc.controller;


import com.a.imgproc.model.DocEntity;
import com.a.imgproc.service.DocumentScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MainController {

    @Autowired
    DocumentScannerService mService;

    @ResponseBody
    @RequestMapping(value = "/imgproc/image", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] detectDocument(@RequestParam("File") MultipartFile file) throws IOException {
        if (!validateImage(file)) {
            return new byte[1];
        }
        return mService.scanDocument(file).toImage();

    }


    @ResponseBody
    @RequestMapping(value = "/imgproc/json", method = RequestMethod.POST)
    public List<DocEntity> detectBoundaries(@RequestParam("file") MultipartFile file) throws IOException {

        if (!validateImage(file)) {
            return new ArrayList<>();
        }

        return mService.scanDocument(file).toList();
    }


    private Boolean validateImage(MultipartFile image) {
        return image.getContentType().equals("image/jpeg");
    }
}
