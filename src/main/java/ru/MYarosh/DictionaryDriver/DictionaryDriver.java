package ru.MYarosh.DictionaryDriver;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Реализует все методы для работы с хранилищем
 */
public class DictionaryDriver implements Dictionary {

    private ServerConnect server;

    /**
     * Конструктор класса для работы с хранилищем
     * @param address - адрес сервера хранилища
     * @param port - порт сервера хранилища
     * @throws ServerConnectException - ошибка, если не удалось получить доступ к серверу
     */
    public DictionaryDriver(String address, int port) throws ServerConnectException {
        server = new ServerConnect(address, port);
        if (!server.checkServer()){
            System.out.println("По заданным 'координатам' сервера не обнаружено!");
            throw new ServerConnectException();
        }
    }

    /**
     * Получение объекта из хранилища
     * @param key - ключ объекта
     * @return - значение объекта
     */
    public Object get(String key){
        return server.send("/DictionaryService/Get",key);
    }

    /**
     * Запись объекта в хранилище
     * @param key - ключ, по которому будет записан объект
     * @param data - объект записи
     * @param ttl - ограничение времени хранения объекта
     * @return - возвращает {@code null} при новой записи, старую запись при перезаписи,
     * пустую запись при незаписи
     */
    public DictionaryValue set(String key, Object data, int ttl){
        DictionaryValue dictionaryValue = new DictionaryValue(data, ttl);
        return server.send(key, dictionaryValue);
    }

    /**
     * Запись объекта в хранилище без времени жизни
     * @param key - ключ, по которому будет записан объект
     * @param data - объект записи
     * @return- возвращает {@code null} при новой записи, старую запись при перезаписи,
     * пустую запись при незаписи
     */
    public DictionaryValue set(String key, Object data){
        return this.set(key, data, -1);
    }

    /**
     * Удаляет объект из хранилища
     * @param key - ключ удаляемого объекта
     * @return - значение удаленного объекта
     */
    public Object remove(String key){
        return server.send("/DictionaryService/Remove",key);
    }

    /**
     * Сохраняет дамп хранилища в файл
     * @param filename - путь к файлу
     * @return - сообщение об успехе/провале
     */
    public String dump(String filename){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filename, false);
        } catch (IOException e) {
            System.out.println("Не получилось получить доступ по адресу " + filename);
            e.printStackTrace();
            return null;
        }
        return server.send(fileWriter);
    }

    /**
     * Загрузка хранилища из файла
     * @param filename - путь к файлу
     * @return - сообщение об успехе/провале
     */
    public String load(String filename){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filename);
        } catch (IOException e) {
            System.out.println("Не получилось получить доступ по адресу " + filename);
            e.printStackTrace();
            return null;
        }
        return server.send(fileReader);
    }
}
