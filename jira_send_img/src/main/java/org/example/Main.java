package org.example;


public class Main {
    public static void main(String[] args){
        try{
            JiraCommentWithImage commentWithImage = new JiraCommentWithImage();

            String comment = "Comment";
            String imagePath = "path/to/your/image.png";
            commentWithImage.addCommentWithImage(comment, imagePath);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}