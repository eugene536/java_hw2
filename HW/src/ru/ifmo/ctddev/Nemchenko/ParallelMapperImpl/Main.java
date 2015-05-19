package ru.ifmo.ctddev.Nemchenko.ParallelMapperImpl;

import ru.ifmo.ctddev.Nemchenko.IterativeParallelism.IterativeParallelism;

import java.util.*;

/**
 * Created by eugene on 2015/04/14.
 */
class Main {
    public static void main(String... args) throws InterruptedException {
        List<Integer> list = new ArrayList<>();
        int n = 1000;
        Random rand = new Random();
        for (int i = 0; i < n; ++i) {
            list.add(rand.nextInt());
        }
        double oldTime = System.nanoTime();
        ParallelMapperImpl mapper1 = new ParallelMapperImpl(1);
        mapper1.map((Integer x)-> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return x * 2;
        }, list);
        mapper1.close();
        System.out.println((System.nanoTime() - oldTime) / 1e9);

        oldTime = System.nanoTime();
        ParallelMapperImpl mapper2 = new ParallelMapperImpl(4);
        mapper2.map((Integer x)-> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return x * 2;
        }, list);
        mapper2.close();
        System.out.println((System.nanoTime() - oldTime) / 1e9);

//        IterativeParallelism iterative = new IterativeParallelism(mapper1);
//        iterative.maximum(4, list, Integer::compareTo);
//        iterative.maximum(4, list, Integer::compareTo);
//        new Thread(run).start();
//        new Thread(run).start();
    }
}
