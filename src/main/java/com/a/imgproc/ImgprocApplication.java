package com.a.imgproc;

import nu.pattern.OpenCV;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImgprocApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgprocApplication.class, args);
        OpenCV.loadShared();
    }

}
