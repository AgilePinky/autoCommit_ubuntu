// JiraCommentWithImage.java
package com.example;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Base64;

public class JiraCommentWithImage {
//    private static final String JIRA_URL = "https://ticketon.atlassian.net/rest/api/2/issue/";
//    private static final String ISSUE_ID = "TDF-10118"; // Идентификатор задачи
//    private static final String USERNAME = "i.sharipov@lantan.info"; // Ваша почта
//    private static final String TOKEN = "="; // Ваш API-токен

    static public void addCommentWithImages(String JIRA_URL, String USERNAME, String TOKEN, String ISSUE_ID, String comment, String imagePath1, boolean sendImage1,
                                            String imagePath2, boolean sendImage2, String imagePath3, boolean sendImage3) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        String attachmentId1 = null;
        String attachmentId2 = null;
        String attachmentId3 = null;

        // Загружаем первое изображение, если это необходимо
        if (sendImage1) {
            File imageFile1 = new File(imagePath1);
            if (!imageFile1.exists()) {
                System.out.println("Файл не найден: " + imagePath1);
                return;
            }
            attachmentId1 = uploadAttachment(JIRA_URL, USERNAME, TOKEN, ISSUE_ID, client, imageFile1);
        }

        // Загружаем второе изображение, если это необходимо
        if (sendImage2) {
            File imageFile2 = new File(imagePath2);
            if (!imageFile2.exists()) {
                System.out.println("Файл не найден: " + imagePath2);
                return;
            }
            attachmentId2 = uploadAttachment(JIRA_URL, USERNAME, TOKEN, ISSUE_ID, client, imageFile2);
        }

        // Загружаем второе изображение, если это необходимо
        if (sendImage3) {
            File imageFile3 = new File(imagePath3);
            if (!imageFile3.exists()) {
                System.out.println("Файл не найден: " + imagePath3);
                return;
            }
            attachmentId3 = uploadAttachment(JIRA_URL, USERNAME, TOKEN, ISSUE_ID, client, imageFile3);
        }

        // Формируем текст комментария с учетом загруженных изображений
        StringBuilder updatedCommentJson = new StringBuilder("{\"body\":\"" + comment);

        if (attachmentId1 != null) {
            updatedCommentJson.append("\\n!ScreenshotRRS.png|width=800,height=550!");
        }

        if (attachmentId2 != null) {
            updatedCommentJson.append("\\n!ScreenshotDRS.png|width=800,height=550!");
        }

        if (attachmentId3 != null) {
            updatedCommentJson.append("\\n!ScreenshotFCS.png|width=800,height=550!");
        }

        updatedCommentJson.append("\"}");

        // Добавляем новый комментарий с обновленным текстом
        HttpPost updatedCommentPost = new HttpPost(JIRA_URL + ISSUE_ID + "/comment");
        updatedCommentPost.setHeader("Content-Type", "application/json; charset=UTF-8");
        updatedCommentPost.setHeader("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));

        StringEntity updatedCommentEntity = new StringEntity(updatedCommentJson.toString(), "UTF-8");
        updatedCommentPost.setEntity(updatedCommentEntity);

        // Выполняем запрос для добавления обновленного комментария
        HttpResponse updatedCommentResponse = client.execute(updatedCommentPost);
        String updatedCommentResponseString = EntityUtils.toString(updatedCommentResponse.getEntity());

        System.out.println("Ответ от Jira (обновленный комментарий): " +
                updatedCommentResponseString);

        client.close();
    }

    static private String uploadAttachment(String JIRA_URL, String USERNAME, String TOKEN, String ISSUE_ID, CloseableHttpClient client, File imageFile) throws Exception {
        HttpPost attachmentPost = new HttpPost(JIRA_URL + ISSUE_ID + "/attachments");

        // Настроим заголовки для загрузки вложения
        attachmentPost.setHeader("X-Atlassian-Token", "no-check");
        attachmentPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));

        // Создаем MultipartEntity для загрузки файла
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", imageFile);

        attachmentPost.setEntity(builder.build());

        // Выполним запрос для загрузки изображения
        HttpResponse attachmentResponse = client.execute(attachmentPost);

        String attachmentResponseString = EntityUtils.toString(attachmentResponse.getEntity());

        // Извлекаем ID вложения из ответа
        JSONArray attachmentsArray = new JSONArray(attachmentResponseString);

        if (attachmentsArray.length() > 0) {
            return attachmentsArray.getJSONObject(0).getString("id");
        }

        return null; // Если вложение не было загружено
    }
}