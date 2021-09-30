package ru.MYarosh.DictionaryDriver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Setter
@Getter
@AllArgsConstructor
public class DictionaryValue {

    Object value;
    int ttl;

    @Override
    public String toString(){
        return "{\"value\":\""+ value +"\",\"ttl\":"+ ttl +"}";
    }
}
