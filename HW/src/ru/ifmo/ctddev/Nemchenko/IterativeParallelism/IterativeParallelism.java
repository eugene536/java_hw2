package ru.ifmo.ctddev.Nemchenko.IterativeParallelism;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by eugene on 2015/03/31.
 */
public class IterativeParallelism implements ListIP {
    private final ParallelMapper parallelMapper;

    public IterativeParallelism() {
        parallelMapper = null;
    }

    public IterativeParallelism(ParallelMapper parallelMapper) {
        this.parallelMapper = parallelMapper;
    }

    /**
     * Returns concatenated representations (in the sense of {@link Object#toString()} method)
     * of all the elements of the given {@code list}.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of {@link java.lang.Thread}
     * objects. Each of them gets its own part of work to perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       {@code List} of elements
     * @return concatenated {@code String} representation of elements
     * @throws InterruptedException if any worker thread was interrupted during its work
     */
    @Override
    public String concat(int cntThreads, List<?> list) throws InterruptedException {
        @SuppressWarnings("unchecked")
        String result = (String) applyFunctionToList(cntThreads, list, (List<?> subList) -> {
            String concatString = "";
            for (int j = 0; j < subList.size(); ++j) {
                concatString += subList.get(j).toString();
            }
            return concatString;
        }, (Object oldElements, Object newElements) -> oldElements.toString() + newElements.toString());
        return result;
    }

    /**
     * For the given {@code list} returns another {@code list} which consists
     * of elements that match given {@code predicate}.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of {@link java.lang.Thread}
     * objects. Each of them gets its own part of work to
     * perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       {@code List} which should be filtered
     * @param predicate  a {@code non-interfering} and {@code stateless}
     *                   predicate to apply to each element to determine if it should be included
     * @param <T>        {@code parent} {@code generic} type of elements
     * @return minimum element in list in the sense of {@code comparator}
     * @throws InterruptedException if any worker thread was interrupted during its work
     */
    @Override
    public <T> List<T> filter(int cntThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) applyFunctionToList(cntThreads, list, (List<? extends T> subList) -> {
            ArrayList<T> goodElements = new ArrayList<>();
            for (int j = 0; j < subList.size(); ++j) {
                if (predicate.test(subList.get(j))) {
                    goodElements.add(subList.get(j));
                }
            }
            return goodElements;
        }, (Object oldElements, Object newElements) -> {
            ((List<T>) oldElements).addAll((List<T>) newElements);
            return oldElements;
        });
        return result;
    }

    /**
     * For the given {@code list} returns another {@code list} which consists
     * of the results of applying given {@code function} to the elements of the initial one.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of {@link java.lang.Thread}
     * objects. Each of them gets its own part of work to
     * perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       initial {@code List} of elements to which {@code function} must
     *                   be applied
     * @param function   a {@code non-interfering} and {@code stateless}
     *                   function to apply to each element
     * @param <T>        {@code parent} {@code generic} type of elements
     * @return {@code List} of results of applying {@code function}
     * @throws InterruptedException if any worker thread was interrupted during its work
     */
    @Override
    public <T, U> List<U> map(int cntThreads, List<? extends T> list, Function<? super T, ? extends U> function) throws InterruptedException {
        @SuppressWarnings("unchecked")
        List<U> result = (List<U>) applyFunctionToList(cntThreads, list, (List<? extends T> subList) -> {
            ArrayList<U> res = new ArrayList<U>();
            for (int j = 0; j < subList.size(); ++j) {
                res.add(function.apply(subList.get(j)));
            }
            return res;
        }, (Object oldElements, Object newElements) -> {
            ((List<U>) oldElements).addAll((List<U>) newElements);
            return oldElements;
        });
        return result;
    }

    /**
     * Returns whether all elements of the given {@code list} match
     * the provided {@code predicate}.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of {@link java.lang.Thread}
     * objects. Each of them gets its own part of work to perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       {@code List} of elements to be checked
     * @param predicate  a {@code non-interfering} and {@code stateless}
     *                   predicate to be check to each element
     * @param <T>        {@code parent} {@code generic} type of elements
     * @return {@code true} if all elements match the given {@code predicate},
     * {@code false} otherwise.
     * @throws InterruptedException if any worker thread was interrupted during its work
     */
    @Override
    public <T> boolean all(int cntThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        return filter(cntThreads, list, predicate).size() == list.size();
    }

    /**
     * Returns whether any element of the given {@code list} matches
     * the provided {@code predicate}.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of {@link java.lang.Thread}
     * objects. Each of them gets its own part of work to perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       {@code List} of elements for which {@code predicate} should be checked
     * @param predicate  a {@code non-interfering} and {@code stateless}
     *                   predicate to be checked
     * @param <T>        {@code parent} {@code generic} type of elements
     * @return {@code true} if any element of the {@code list} matches the given {@code predicate},
     * {@code false} otherwise.
     * @throws InterruptedException if any worker thread was interrupted during its work
     */
    @Override
    public <T> boolean any(int cntThreads, List<? extends T> list, Predicate<? super T> predicate) throws InterruptedException {
        return filter(cntThreads, list, predicate).size() > 0;
    }

    /**
     * For the given {@code list} returns maximum element in this list according to
     * {@code comparator}.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of
     * {@link java.lang.Thread} objects. Each of them gets its own part of work to
     * perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       {@code List} where to find minimum element
     * @param comparator comparator according to which compares performs
     * @param <T>        {@code parent} {@code generic} type of elements
     * @return maximum element in list in the sense of {@code comparator}
     * @throws InterruptedException if any worker thread was interrupted during its work
     */
    @Override
    public <T> T maximum(int cntThreads, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        return minimum(cntThreads, list, comparator.reversed());
    }

    /**
     * For the given {@code list} returns minimum element in this list according to
     * {@code comparator}.
     * <p>
     * For intrinsic purposes it uses {@code cntThreads} of
     * {@link java.lang.Thread} objects. Each of them gets its own part of work to
     * perform the whole work in parallel.
     *
     * @param cntThreads number of {@code Threads} in which work should be done.
     * @param list       {@code List} where to find minimum element
     * @param comparator comparator according to which compares performs
     * @param <T>        {@code parent} {@code generic} type of elements
     * @return minimum element in list in the sense of {@code comparator}
     * @throws InterruptedException if any worker thread was interrupted during his work
     */
    @Override
    public <T> T minimum(int cntThreads, List<? extends T> list, Comparator<? super T> comparator) throws InterruptedException {
        @SuppressWarnings("unchecked")
        T result = (T) applyFunctionToList(cntThreads, list, (List<? extends T> subList) -> {
            T resElement = subList.get(0);
            for (int j = 1; j < subList.size(); ++j) {
                if (comparator.compare(subList.get(j), resElement) < 0) {
                    resElement = subList.get(j);
                }
            }
            return resElement;
        }, (Object oldElement, Object newElement) -> {
            if (comparator.compare((T) newElement, (T) oldElement) < 0) {
                return newElement;
            }
            return oldElement;
        });
        return result;
    }


    private <T> Object applyFunctionToList(int cntThreads, List<? extends T> list,
                                           Function<List<? extends T>, Object> function,
                                           BiFunction<Object, Object, Object> funForGenerateResult)
            throws InterruptedException {
        if (list.isEmpty()) {
            return null;
        }
        cntThreads = Math.min(cntThreads, list.size());
        if (parallelMapper == null) {
            return applyWithoutParallelMapper(cntThreads, list, function, funForGenerateResult);
        } else {
            Object result;
            List<List<? extends T>> partsOfList = new ArrayList<>();
            for (int i = 0; i < cntThreads; ++i) {
                partsOfList.add(getSubList(cntThreads, i, list));
            }
            List<Object> resultList = parallelMapper.map(function, partsOfList);
            result = resultList.get(0);
            for (int i = 1; i < cntThreads; ++i) {
                result = funForGenerateResult.apply(result, resultList.get(i));
            }
            return result;
        }
    }

    private <T> Object applyWithoutParallelMapper(int cntThreads, List<? extends T> list,
                                                  Function<List<? extends T>, Object> function,
                                                  BiFunction<Object, Object, Object> funForGenerateResult)
            throws InterruptedException {
        Thread threads[] = new Thread[cntThreads];
        ArrayList<RunFunction<T>> functions = new ArrayList<>();

        for (int i = 0; i < cntThreads; ++i) {
            functions.add(new RunFunction<T>(getSubList(cntThreads, i, list), function));
            threads[i] = new Thread(functions.get(i));
            threads[i].start();
        }

        threads[0].join();
        Object result = functions.get(0).getResult();
        for (int i = 1; i < cntThreads; ++i) {
            threads[i].join();
            result = funForGenerateResult.apply(result, functions.get(i).getResult());
        }

        return result;
    }

    private <T> List<? extends T> getSubList(int cntThreads, int i, List<? extends T> list) {
        int lenInterval = list.size() / cntThreads;
        int l = lenInterval * i;
        int r = lenInterval * (i + 1);
        if (cntThreads - 1 == i) {
            r = list.size();
        }
        return list.subList(l, r);
    }

    private class RunFunction<T> implements Runnable {
        private Object result;
        List<? extends T> list;
        Function<List<? extends T>, Object> function;

        RunFunction(List<? extends T> list, Function<List<? extends T>, Object> function) {
            this.function = function;
            this.list = list;
        }

        @Override
        public void run() {
            result = function.apply(list);
        }

        public Object getResult() {
            return result;
        }
    }
}
