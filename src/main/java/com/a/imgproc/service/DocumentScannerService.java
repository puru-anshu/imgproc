package com.a.imgproc.service;

import com.a.imgproc.model.DocEntity;
import com.a.imgproc.util.ImageUtil;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentScannerService {
    private Mat image;
    private List<DocEntity> entities;
    private  static final Logger logger = LoggerFactory.getLogger("DocumentScannerService");

    public DocumentScannerService scanDocument(MultipartFile file) throws IOException {
        entities = new ArrayList<>();
        Mat orgImage = Imgcodecs.imdecode(new MatOfByte(file.getBytes()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
        if(orgImage.width() < orgImage.height())
        {
            ImageUtil.rotateMat(orgImage,90);
        }
        image = orgImage.clone();
        Mat cImg = ImageUtil.edgeDetection(orgImage);
        MatOfPoint2f largest = ImageUtil.findLargestContour(cImg);

        if (largest != null) {
            Point[] points = ImageUtil.sortPoints(largest.toArray());
            largest.release();
            image = ImageUtil.fourPointTransform(image, points);
            ImageUtil.applyThreshold(image);
        }else
        {
            logger.info("Could not find boundary");
        }


        return this;

    }


    public List<DocEntity> toList() {
        return entities;
    }


    public byte[] toImage() {
        return mat2Image(image);
    }

    private byte[] mat2Image(Mat frame) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(".jpg", frame, buffer);
        return buffer.toArray();
    }

}
