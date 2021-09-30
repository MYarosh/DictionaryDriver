package ru.MYarosh.DictionaryDriver;


interface Dictionary {
    Object get(String key);
    DictionaryValue set(String key, Object data, int ttl);
    DictionaryValue set(String key, Object data);
    Object remove(String key);
    String dump(String filename);
    String load(String filename);
}
