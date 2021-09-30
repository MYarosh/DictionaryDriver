package ru.MYarosh.DictionaryDriver;

import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.Socket;

/**
 * Управляет соединением с сервером
 */
class ServerConnect {

    private static final int CONNECTION_TIMEOUT = 10000;
    private String address;
    private int port;
    private Gson gson = new Gson();

    ServerConnect(String address, int port){
        this.address = address;
        this.port = port;
    }

    /**
     * Проверка на доступность сервера
     * @return - логическое значение наличия сервера
     */
    boolean checkServer(){
        try {
            Socket socket = new Socket(address, port);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Отправляет запросы к хранилищу с записью
     * @param key - ключ объекта
     * @param dictionaryValue - значение объекта
     * @return - значение объекта
     */
    DictionaryValue send(String key,@NotNull DictionaryValue dictionaryValue){
        final HttpPost httpPost = new HttpPost("http://"+address+":"+port+"/DictionaryService/Set");
        try {
            httpPost.setEntity(new StringEntity("{\"key\":\""+key+"\",\"value\":"+dictionaryValue.toString()));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Не получилось отправить объект. Проверьте правильность объекта.");
            e.printStackTrace();
            return null;
        }
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(httpPost)) {
            dictionaryValue = gson.fromJson(EntityUtils.toString(response.getEntity()), DictionaryValue.class);
        } catch (IOException e) {
            return null;
        }
        return dictionaryValue;
    }

    /**
     * Отправляет запросы по адресу {@code "http://"+address+":"+port+rest}
     * @param rest - адрес
     * @param key - ключ объекта
     * @return - значение объекта
     */
    DictionaryValue send(String rest, String key){
        final HttpPost httpPost = new HttpPost("http://"+address+":"+port+rest);
        try {
            httpPost.setEntity(new StringEntity(key));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        DictionaryValue dictionaryValue;
        try {
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {
                dictionaryValue = gson.fromJson(EntityUtils.toString(response.getEntity()), DictionaryValue.class);
            }
        } catch (IOException e) {
            return null;
        }
        return dictionaryValue;
    }

    /**
     * Сохраняет состояние хранилища
     * @param fileWriter - записывает в файл состояние хранилища
     * @return - сообщение об успехе/провале
     */
    String send(FileWriter fileWriter){
        final HttpPost httpPost = new HttpPost("http://"+address+":"+port+ "/DictionaryService/Dump");
        String dictionary = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(httpPost)) {
            dictionary = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.out.println("Не получилось получить состояние хранилища сервиса.");
            e.printStackTrace();
            return null;
        }
        try {
            fileWriter.write(dictionary);
            fileWriter.flush();
        }catch (IOException e){
            System.out.println("Не получилось записать состояние хранилища сервиса в файл.");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Загружает из файла состояние хранилища
     * @param fileReader - читает состояние из файла
     * @return - сообщение об успехе/провале
     */
    String send(@NotNull FileReader fileReader){
        final HttpPost httpPost = new HttpPost("http://"+address+":"+port+ "/DictionaryService/Load");
        StringBuilder stringBuilder = new StringBuilder("");
        int c;
        try {
            while ((c = fileReader.read()) != -1) {
                stringBuilder.append((char) c);
            }
        }catch (IOException e){
            System.out.println("Не получилось прочитать файл.");
            return null;
        }
        String string = stringBuilder.toString();
        System.out.println(string);
        try {
            httpPost.setEntity(new StringEntity(string));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        try {
            try (CloseableHttpClient httpClient = HttpClients.createDefault();
                 CloseableHttpResponse response = httpClient.execute(httpPost)) {
                string = EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            System.out.println("Не получилось получить ответ от сервера.");
            e.printStackTrace();
            return null;
        }
        return string;
    }
}
