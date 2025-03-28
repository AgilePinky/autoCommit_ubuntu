// JiraCommentWithImage.java
package org.example;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.util.Base64;

public class JiraCommentWithImage {
    private static final String JIRA_URL = "https://your-jira-instance.atlassian.net/rest/api/2/issue/";
    private static final String ISSUE_ID = "PROJ-123"; // Идентификатор задачи
    private static final String USERNAME = "your-email@example.com"; // Ваша почта
    private static final String TOKEN = "your-api-token"; // Ваш API-токен

    public void addCommentWithImage(String comment, String imagePath) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();

        // Сначала добавляем комментарий
        String commentJson = "{\"body\":\"" + comment + "\"}";
        HttpPost commentPost = new HttpPost(JIRA_URL + ISSUE_ID + "/comment");
        System.out.println("Добавляем комментари");

        // Устанавливаем заголовки
        commentPost.setHeader("Content-Type", "application/json");
        commentPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));
        System.out.println("Устанавливаем заголовки");

        // Устанавливаем тело запроса
        StringEntity commentEntity = new StringEntity(commentJson);
        commentPost.setEntity(commentEntity);
        System.out.println("Устанавливаем тело запроса");

        // Выполняем запрос для добавления комментария
        HttpResponse commentResponse = client.execute(commentPost);
        String commentResponseString = EntityUtils.toString(commentResponse.getEntity());
        System.out.println("Ответ от Jira (комментарий): " + commentResponseString);
        System.out.println("Выполняем запрос для добавления комментария");

        // Теперь загружаем изображение
        File imageFile = new File(imagePath);
        HttpPost attachmentPost = new HttpPost(JIRA_URL + ISSUE_ID + "/attachments");
        System.out.println("Загружаем изображение");

        // Настроим заголовки для загрузки вложения
        attachmentPost.setHeader("X-Atlassian-Token", "no-check");
        attachmentPost.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));
        System.out.println("Настроим заголовки для загрузки вложения");

        // Создаем MultipartEntity для загрузки файла
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", imageFile);
        System.out.println("Создаем MultipartEntity для загрузки файла");

        attachmentPost.setEntity(builder.build());

        // Выполним запрос для загрузки изображения
        HttpResponse attachmentResponse = client.execute(attachmentPost);
        String attachmentResponseString = EntityUtils.toString(attachmentResponse.getEntity());
        System.out.println("Ответ от Jira (вложение): " + attachmentResponseString);
        System.out.println("Выполним запрос для загрузки изображения");

        client.close();
    }
}