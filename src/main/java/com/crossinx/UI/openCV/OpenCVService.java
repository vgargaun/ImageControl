package com.crossinx.UI.openCV;

import com.crossinx.UI.Image;
import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.photo.Photo;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.empty;
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

        Mat src = Imgcodecs.imread(file.getAbsolutePath(), CV_32F);
//        Imgcodecs.imread(file.getAbsolutePath(), CV_32F);

        Mat dst = new Mat();

//        adjustImg(src);


        Mat processImage = processImage(src);

        final List<MatOfPoint> allContours = new ArrayList<>();
        Imgproc.findContours(
                processImage,
                allContours,
                new Mat(processImage.size(), processImage.type()),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE
        );

//        final double value = Imgproc.contourArea(allContours.get(0));
//        final Rect rect = Imgproc.boundingRect(allContours.get(0));
        List<MatOfPoint> contours = new ArrayList<>();
        allContours.forEach(contour -> {
            double area = Imgproc.contourArea(contour);
            Rect rect = Imgproc.boundingRect(contour);

//            if(area > 1500 && area < 2000) contours.add(contour);
            if (rect.height > 22 && rect.height < 35
                    && rect.width > 100 && rect.width < 600) {
                Point[] points = contour.toArray();
                Point minPoint = points[0];
                Point maxPoint = points[0];
//                for (int i = 0; i < points.length; i++) {
//                    if (minPoint.x > points[i].x) minPoint = points[i];
//                    if (maxPoint.x < points[i].x) maxPoint = points[i];
//                }

                int v = ((int) points.length / 10);

//                List<Point> yMinPointList = new ArrayList<>();
////                for(int i = 5; i < points.length/2-5; i++){
////                    yMinPointList.add(points[i]);
////
////                }
//                yMinPointList.add(points[5]);
//                yMinPointList.add(points[17]);
//                int i = 0;
//                while (i + v < points.length) {
//                    Point yMax = points[i];
//                    for (int j = i; j < i + v; j++) {
//                        if (yMax.y < points[j].y) yMax = points[j];
//                    }
//                    yMinPointList.add(yMax);
//                    i += v;
//                }
//                contour.fromList(yMinPointList);
                List<Point> pointsTest = new ArrayList<>();
                pointsTest.add(new Point(rect.x, rect.y));
                pointsTest.add(new Point(rect.x, rect.y + rect.height));
                pointsTest.add(new Point(rect.x + rect.width, rect.y + rect.height));
                pointsTest.add(new Point(rect.x + rect.width, rect.y));
                HashMap<Object, Object> objectObjectHashMap = new HashMap<>();

                MatOfPoint matOfPoint = new MatOfPoint();
                matOfPoint.fromList(pointsTest);
                contours.add(matOfPoint);
            }
        });

//        Imgproc.drawMarker(src,);

        Imgproc.drawContours(
                src,
                contours,
                -1, // Negative value indicates that we want to draw all of contours
                new Scalar(0, 252, 0), // Green color
                1
        );


        ocr(bufferedImage, allContours.get(0));

        Imgcodecs.imwrite("test-rotate.png", src);

    }

    public static Mat processImage(final Mat mat) {
        final Mat processed = new Mat(mat.height(), mat.width(), mat.type());

        Imgproc.GaussianBlur(mat, processed, new Size(7, 7), 1);

        Imgproc.cvtColor(processed, processed, Imgproc.COLOR_RGB2GRAY);

        Imgproc.Canny(processed, processed, 100, 10);

        Imgproc.dilate(processed, processed, new Mat(), new Point(-1, -1), 2);
//        Imgproc.erode(processed, processed, 1);

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

        Optional<MatOfPoint> first = allContours.stream().filter(contours -> Imgproc.boundingRect(contours).width > 100)
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

    private void adjust() {

    }

    public static void alignImages(Mat A, Mat B, BufferedImage bufferedImage) {
        final int warp_mode = MOTION_EUCLIDEAN;
        Mat matBgray = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8U);

        Mat warpMatrix = Mat.eye(3, 3, CV_32F);


        Imgproc.cvtColor(A, A, Imgproc.COLOR_BGR2GRAY);

        int numIter = 5000;
        double terminationEps = 1e-10;
        TermCriteria criteria = new TermCriteria(TermCriteria.COUNT + TermCriteria.EPS, numIter, terminationEps);


        findTransformECC(A, B, warpMatrix, warp_mode, criteria, matBgray, 5);

        System.out.println();
//        Imgproc.warpPerspective(matA,matBaligned,warpMatrix,matA.size(),Imgproc.INTER_LINEAR+ Imgproc.WARP_INVERSE_MAP);

        Photo photo = new Photo();

    }

    private static void adjustImg(Mat scr) {
        VideoCapture camera = new VideoCapture(1);

        Mat processedImage = processImage(scr);


        final List<MatOfPoint> allContours = new ArrayList<>();
        Imgproc.findContours(
                processedImage,
                allContours,
                new Mat(processedImage.size(), processedImage.type()),
                Imgproc.RETR_EXTERNAL,
                Imgproc.CHAIN_APPROX_SIMPLE

        );

        AtomicReference<MatOfPoint> aux = new AtomicReference<>(allContours.get(0));
        allContours.forEach(contour -> {
            if (Imgproc.contourArea(contour) > Imgproc.contourArea(aux.get()))
                aux.set(contour);
        });

        List<MatOfPoint> listContour = new ArrayList();
        Mat matrix = new Mat();

        MatOfPoint2f sortedPoints = createSortedPoints(aux.get());

        MatOfPoint2f dstMatOfPoint = new MatOfPoint2f(
                new Point(0, 0),
                new Point(450 - 1, 0),
                new Point(0, 450 - 1),
                new Point(450 - 1, 450 - 1)
        );


        Imgproc.getPerspectiveTransform(sortedPoints, dstMatOfPoint);
        Mat dst = new Mat(CV_32F);
        Imgproc.warpPerspective(scr, dst, sortedPoints, scr.size(), Imgproc.INTER_LINEAR);

        listContour.add(aux.get());
        Imgproc.drawContours(
                scr,
                listContour,
                -1, // Negative value indicates that we want to draw all of contours
                new Scalar(124, 252, 0), // Green color
                1
        );


        System.out.println();
    }

    private static MatOfPoint2f createSortedPoints(MatOfPoint matOfPoint) {

        MatOfPoint2f m2f = new MatOfPoint2f(matOfPoint.toArray());
        double arc = Imgproc.arcLength(m2f, true);
        MatOfPoint2f approx = new MatOfPoint2f();
        Imgproc.approxPolyDP(m2f, approx, arc * 0.02, true);


        Moments moment = Imgproc.moments(approx.col(0));
        int x = (int) (moment.get_m10() / moment.get_m00());
        int y = (int) (moment.get_m01() / moment.get_m00());


        Point[] sortedPoints = new Point[4];

        double[] data;
        int count = 0;
        for (int i = 0; i < approx.col(0).rows(); i++) {
            data = approx.col(0).get(i, 0);
            double datax = data[0];
            double datay = data[1];
            if (datax < x && datay < y) {
                sortedPoints[0] = new Point(datax, datay);
                count++;
            } else if (datax > x && datay < y) {
                sortedPoints[1] = new Point(datax, datay);
                count++;
            } else if (datax < x && datay > y) {
                sortedPoints[2] = new Point(datax, datay);
                count++;
            } else if (datax > x && datay > y) {
                sortedPoints[3] = new Point(datax, datay);
                count++;
            }
        }


        MatOfPoint2f src = new MatOfPoint2f(
                sortedPoints[0],
                sortedPoints[1],
                sortedPoints[2],
                sortedPoints[3]);
        return src;
    }

//    public Mat transform(Mat src, MatOfPoint2f corners) {
//        MatOfPoint2f sortedCorners = sortCorners(corners);
//        Size size = getRectangleSize(sortedCorners);
//
//        Mat result = Mat.zeros(size, src.type());
//        MatOfPoint2f imageOutline = getOutline(result);
//
//        Mat transformation = Imgproc.getPerspectiveTransform(sortedCorners, imageOutline);
//        Imgproc.warpPerspective(src, result, transformation, size);
//
//        return result;
//    }

}
