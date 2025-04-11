package org.example;


public class Main {
    public static void main (String[] args){
        try{
            JiraCommentWithImage commentWithImage = new JiraCommentWithImage();

            String comment = "На русском";
            String imagePath1 = "/home/ivan/Pictures/Screenshots/ScreenshotRRS.png";
            String imagePath2 = "/home/ivan/Pictures/Screenshots/ScreenshotDRS.png";
            boolean sendImage1 = true;
            boolean sendImage2 = true;

            commentWithImage.addCommentWithImages(comment, imagePath1, sendImage1, imagePath2, sendImage2);
//            commentWithImage.addCommentWithImage(comment, imagePath1);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}