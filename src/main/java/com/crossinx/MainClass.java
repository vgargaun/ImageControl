package com.crossinx;

public class MainClass {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
//        File myFile = new File("C:\\Users\\veaceslav.gargaun\\Downloads\\002.png");
//        BufferedImage bufferedImage = new BufferedImage(240, 240, BufferedImage.TYPE_INT_ARGB);
//
//        try {
//            bufferedImage = ImageIO.read(myFile);
//        }
//        catch (IOException e) { }
//
//        String scanText = OcrWorker.processOcr(bufferedImage);
//        System.out.println(scanText);
//
//        TestOpenCV testOpenCV = new TestOpenCV();
//        testOpenCV.test();
        MainManager.getInstance().start();

    }
}
