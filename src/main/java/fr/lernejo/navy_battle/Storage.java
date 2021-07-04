package fr.lernejo.navy_battle;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class Storage<T> {
    private final List<T> list = new ArrayList<>();

    public void set(T obj) {
        list.clear();
        list.add(obj);
    }
    public void unset(){
        list.clear();
    }

    public boolean isEmpty() {
        return  list.isEmpty();
    }

    public boolean isNotEmpty()
    {
        return !isEmpty();
    }

    public T get() {
        if(list.isEmpty())
            throw new RuntimeException("Option is empty!");

        return list.get(0);
    }

}
