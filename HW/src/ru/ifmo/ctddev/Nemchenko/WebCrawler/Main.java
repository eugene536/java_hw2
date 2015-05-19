package ru.ifmo.ctddev.Nemchenko.WebCrawler;

import sun.net.util.URLUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by eugene on 2015/04/21.
 */
public class Main {
    static int getCapacity(ArrayList<?> l) throws Exception {
        Field dataField = ArrayList.class.getDeclaredField("elementData");
        dataField.setAccessible(true);
        return ((Object[]) dataField.get(l)).length;
    }

    public static void main(String...args) throws Exception {
        System.out.println("hello");
        ArrayList<Integer> list = new ArrayList<Integer>();
        int n = (int) 1e7;
        for (int i = 0; i < n; i++) {
           list.add(i);
        }

        System.out.println(getCapacity(list));

        for (int i = 0; i < n; i++) {
            list.remove(list.size() - 1);
        }

        System.out.println(getCapacity(list));
    }
}
