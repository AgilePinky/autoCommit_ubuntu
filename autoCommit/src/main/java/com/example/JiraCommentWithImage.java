// JiraCommentWithImage.java
package com.example;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.ContentType;
import org.json.JSONArray;

import java.io.File;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;


public class JiraCommentWithImage {

    static public void addCommentWithImages(String JIRA_URL, String USERNAME, String TOKEN, String ISSUE_ID, String comment,
                                            boolean[] servicesArray, boolean[] branchesArray, String[] imagePathArray,
                                            String[] IMAGE_LABELS, String IMAGE_TEMPLATE, String[] IMAGE_BRANCHES) throws Exception {

        if (imagePathArray.length != servicesArray.length || imagePathArray.length > IMAGE_LABELS.length) {
            throw new IllegalArgumentException("Invalid number of image paths or flags");
        }

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            List<String> attachmentIds = new ArrayList<>();
            List<String> uploadedFileNames = new ArrayList<>(); // Для хранения имен загруженных файлов
            // Загружаем изображения
            for (int j = 0; j < branchesArray.length; j++) {
                if (branchesArray[j]) {
                    for (int i = 0; i < servicesArray.length; i++) {
                        if (servicesArray[i]) {
                            // Формируем ожидаемое имя файла
//                            String expectedFileName = "resources/Screenshot" + IMAGE_LABELS[i] + IMAGE_BRANCHES[j] + ".png";
                            String expectedFileName = "Screenshot_" + IMAGE_LABELS[i] + "_" + IMAGE_BRANCHES[j] + ".png";
                            uploadedFileNames.add(expectedFileName);
                            File imageFile = new File("resources/" + expectedFileName);
//                            File imageFile = new File(expectedFileName);
                            File currentDir = new File(".");
                            System.out.println("Ищем файл: " + imageFile.getAbsolutePath());

                            if (!imageFile.exists()) {
                                System.out.println("Файл не найден: " + expectedFileName);
                                return;
                            }

                            // Загружаем с явным указанием имени файла
                            String attachmentId = uploadAttachment(JIRA_URL, USERNAME, TOKEN, ISSUE_ID,
                                    client, imageFile, expectedFileName);
                            attachmentIds.add(attachmentId);
//                            uploadedFileNames.add(expectedFileName); // Сохраняем имя файла

                        } else {
                            attachmentIds.add(null);
                            uploadedFileNames.add(null);
                        }
                    }
                }
            }

            // Формируем текст комментария
            StringBuilder updatedCommentJson = new StringBuilder("{\"body\":\"" + comment);

            // Используем счетчик для правильного сопоставления
            int counter = 0;
            for (int j = 0; j < branchesArray.length; j++) {
                if (branchesArray[j]) {
                    for (int i = 0; i < servicesArray.length; i++) {
                        if (servicesArray[i] && attachmentIds.get(counter) != null) {
                            // Используем правильные индексы для сервиса и ветки
                            updatedCommentJson.append(String.format(IMAGE_TEMPLATE, IMAGE_BRANCHES[j], IMAGE_LABELS[i],
                                    uploadedFileNames.get(counter)));
                            counter++;
                        } else if (!servicesArray[i]) {
                            counter++;
                        }
                    }
                }
            }

            updatedCommentJson.append("\"}");

            // Отправляем комментарий
            HttpPost updatedCommentPost = new HttpPost(JIRA_URL + ISSUE_ID + "/comment");
            updatedCommentPost.setHeader("Content-Type", "application/json; charset=UTF-8");
            updatedCommentPost.setHeader("Authorization", "Basic " +
                    Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));

            updatedCommentPost.setEntity(new StringEntity(updatedCommentJson.toString(), "UTF-8"));
            HttpResponse updatedCommentResponse = client.execute(updatedCommentPost);
            System.out.println("Ответ от Jira: " + EntityUtils.toString(updatedCommentResponse.getEntity()));
        }
    }

    // Обновленный метод загрузки с указанием имени файла
    static private String uploadAttachment(String JIRA_URL, String USERNAME, String TOKEN, String ISSUE_ID,
                                           CloseableHttpClient client, File imageFile, String fileName) throws Exception {
        HttpPost attachmentPost = new HttpPost(JIRA_URL + ISSUE_ID + "/attachments");
        attachmentPost.setHeader("X-Atlassian-Token", "no-check");
        attachmentPost.setHeader("Authorization", "Basic " +
                Base64.getEncoder().encodeToString((USERNAME + ":" + TOKEN).getBytes()));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // Явно указываем имя файла для загрузки
        builder.addBinaryBody("file", imageFile, ContentType.APPLICATION_OCTET_STREAM, fileName);
        attachmentPost.setEntity(builder.build());

        HttpResponse attachmentResponse = client.execute(attachmentPost);
        String attachmentResponseString = EntityUtils.toString(attachmentResponse.getEntity());
        JSONArray attachmentsArray = new JSONArray(attachmentResponseString);

        return attachmentsArray.length() > 0 ? attachmentsArray.getJSONObject(0).getString("id") : null;
    }
}
