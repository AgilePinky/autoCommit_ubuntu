package org.example;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.Base64;

import java.io.File;

public class JiraUploadImage {
    private static final String JIRA_URL = "https://your-jira-instance.atlassian.net/rest/api/2/issue/";
    private static final String ISSUE_ID = "PROJ-123"; // Идентификатор задачи
    private static final String USERNAME = "your-email@example.com"; // Ваша почта
    private static final String TOKEN = "your-api-token"; // Ваш API-токен

    public static void JiraUploadImageMethod(String imagePath) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(JIRA_URL + ISSUE_ID + "/attachments");

        // Настроим заголовки
        post.setHeader("X-Atlassian-Token", "no-check");
        post.setHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));

        // Укажите путь к вашей картинке
        File imageFile = new File(imagePath);

        // Создаем MultipartEntity для загрузки файла
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", imageFile);

        post.setEntity(builder.build());

        // Выполним запрос
        HttpResponse response = client.execute(post);
        String responseString = EntityUtils.toString(response.getEntity());

        System.out.println("Ответ от Jira: " + responseString);
        client.close();
    }
}

