package com.crossinx.UI.openCV;

import com.crossinx.UI.Image;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.opencv.calib3d.Calib3d.findHomography;
import static org.opencv.core.CvType.CV_32F;
import static org.opencv.video.Video.MOTION_EUCLIDEAN;
import static org.opencv.video.Video.findTransformECC;

public class OpenCVService {
    public void adjustmentImagine(Image image) {

        OpenCV.loadShared();

//      String file = "C:\\Users\\veaceslav.gargaun\\Downloads\\pdf\\test.png";
        BufferedImage bufferedImage = image.getBufferedImage();
        File file = new File("temp-cv.png");
        try {
            ImageIO.write(bufferedImage, "png", file);

        } catch (Exception e) {

        }

        Mat src = Imgcodecs.imread(file.getAbsolutePath());

        Mat dst = new Mat();


//        findHomography();

//        Photo.fastNlMeansDenoising(src, dst);
//        alignImages(src,dst, bufferedImage);




        Mat processImage = processImage(src);

        final List<MatOfPoint> allContours = new ArrayList<>();
        Imgproc.findContours(
                processImage,
                allContours,
                new Mat(processImage.size(), processImage.type()),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_NONE
        );

        Imgproc.drawContours(
                src,
                allContours,
                -1, // Negative value indicates that we want to draw all of contours
                new Scalar(124, 252, 0), // Green color
                1
        );


        final double value = Imgproc.contourArea(allContours.get(0));
        final Rect rect = Imgproc.boundingRect(allContours.get(0));


        ocr(bufferedImage, allContours.get(0));

        Imgcodecs.imwrite("C:\\Users\\veaceslav.gargaun\\Downloads\\pdf\\test-rotate.png", src);

        System.out.println("Image Rotated Successfully");
    }

    public Mat processImage(final Mat mat) {
        final Mat processed = new Mat(mat.height(), mat.width(), mat.type());

        Imgproc.GaussianBlur(mat, processed, new Size(7, 7), 1);

        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_RGB2GRAY);

        Imgproc.Canny(processed, processed, 200, 25);

        Imgproc.dilate(processed, processed, new Mat(), new Point(-1, -1), 1);

        return processed;
    }

    private static BufferedImage convertMatToBufferedImage(final Mat mat) {

        final BufferedImage bufferedImage = new BufferedImage(
                mat.width(),
                mat.height(),
                mat.channels() == 1 ? BufferedImage.TYPE_BYTE_GRAY : BufferedImage.TYPE_3BYTE_BGR
        );

        // Write data to image
        final WritableRaster raster = bufferedImage.getRaster();
        final DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        mat.get(0, 0, dataBuffer.getData());

        return bufferedImage;
    }

    private static BufferedImage ocr(BufferedImage bufferedImage, MatOfPoint matOfPoint) {
        Rect rect = Imgproc.boundingRect(matOfPoint);

        BufferedImage subimage = bufferedImage.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return subimage;


    }

    private void stabil(Mat src, Mat dst, List<MatOfPoint> allContours) {

        Optional<MatOfPoint> first = allContours.stream().filter(contours -> Imgproc.boundingRect(contours).width>100)
                .findFirst();

        Rect rect = Imgproc.boundingRect(first.get());



        double angle = 0;


        Point rotPoint = new Point(src.cols() / 2.0,
                src.rows() / 2.0);

        // Create Rotation Matrix
        Mat rotMat = Imgproc.getRotationMatrix2D(
                rotPoint, angle, 1);

        // Apply Affine Transformation
        Imgproc.warpAffine(src, dst, rotMat, src.size(),
                Imgproc.WARP_INVERSE_MAP);

    }

    private void adjust(){

    }

    public static void alignImages(Mat A, Mat B, BufferedImage bufferedImage){
        final int warp_mode = MOTION_EUCLIDEAN;
//        Mat matA = new Mat(A.getHeight(),A.getWidth(), CvType.CV_8UC3);
//        Mat matAgray = new Mat(A.getHeight(),A.getWidth(), CvType.CV_8U);
//        Mat matB = new Mat(B.getHeight(),B.getWidth(), CvType.CV_8UC3);

        Mat matBgray = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8U);

//        Mat matBaligned = new Mat(A.getHeight(),A.getWidth(), CvType.CV_8UC3);
        Mat warpMatrix = Mat.eye(3,3,CV_32F);



        Imgproc.cvtColor(A,A, Imgproc.COLOR_BGR2GRAY);
//        Imgproc.cvtColor(A,A,Imgproc.COLOR_BGR2GRAY);

        int numIter = 5000;
        double terminationEps = 1e-10;
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT+TermCriteria.EPS,numIter,terminationEps);


        findTransformECC(A,B,warpMatrix,warp_mode, criteria, matBgray, 5);

        System.out.println();
//        Imgproc.warpPerspective(matA,matBaligned,warpMatrix,matA.size(),Imgproc.INTER_LINEAR+ Imgproc.WARP_INVERSE_MAP);

        Photo photo = new Photo();


    }

}
