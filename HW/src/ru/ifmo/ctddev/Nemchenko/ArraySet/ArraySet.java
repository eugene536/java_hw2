package ru.ifmo.ctddev.Nemchenko.ArraySet;

import java.util.*;

/**
 * Created by eugene on 2/25/15.
 */

/**
 *
 * @param <T>
 */
public class ArraySet<T> extends AbstractSet<T> implements NavigableSet<T> {
    private List<T> elements;
    private Comparator<? super T> comparator;
    private boolean isDefaultComparator = true;

    public ArraySet() {
        elements = new ArrayList<T>();
        comparator = new DefaultComparator();
        isDefaultComparator = true;
    }

    public ArraySet(Collection<? extends T> collection, Comparator<? super T> comparator) {
        elements = new ArrayList<T>();
        TreeSet<T> treeSet = new TreeSet<T>(comparator);
        treeSet.addAll(collection);
        for (T e : treeSet) {
            elements.add(e);
        }
        this.comparator = comparator;
        isDefaultComparator = false;
    }

    public ArraySet(Collection<? extends T> collection) {
        this(collection, null);
        comparator = new DefaultComparator();
        isDefaultComparator = true;
    }

    public ArraySet(SortedSet<? extends T> set) {
        this();
        if (set.comparator() == null) {
            this.comparator = new DefaultComparator();
            isDefaultComparator = true;
        } else {
            this.comparator = (Comparator<T>) set.comparator();
            isDefaultComparator = false;
        }

        for (T e : set) {
            this.elements.add(e);
        }
    }

    private ArraySet(int from, int to, ArraySet<T> parent) {
        this.comparator = parent.comparator;
        this.elements = parent.elements.subList(from, to);
        isDefaultComparator = parent.isDefaultComparator;
    }

    private ArraySet(boolean descending, ArraySet<T> parent) {
        elements = getReversed(parent.elements);
        comparator = Collections.reverseOrder(parent.comparator);
        isDefaultComparator = parent.isDefaultComparator;
    }

    @Override
    public T lower(T e) {
        int pos = Collections.binarySearch(elements, e, comparator);
        pos = pos < 0 ? -pos - 2 : pos - 1;
        if (pos < 0)
            return null;
        return elements.get(pos);
    }

    private int getFloorPosition(T e) {
        int pos = Collections.binarySearch(elements, e, comparator);
        pos = pos < 0 ? -pos - 2 : pos;
        return pos;
    }

    @Override
    public T floor(T e) {
        int pos = getFloorPosition(e);
        if (pos < 0)
            return null;
        return elements.get(pos);
    }

    private int getCeilingPosition(T e) {
        int pos = Collections.binarySearch(elements, e, comparator);
        pos = pos < 0 ? -pos - 1 : pos;
        return pos;
    }

    @Override
    public T ceiling(T e) {
        int pos = getCeilingPosition(e);
        if (pos > size() - 1)
            return null;
        return elements.get(pos);
    }

    @Override
    public T higher(T e) {
        int pos = Collections.binarySearch(elements, e, comparator);
        pos = pos < 0 ? -pos - 1 : pos + 1;
        if (pos > size() - 1)
            return null;
        return elements.get(pos);
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<T>(true, this);
    }

    @Override
    public Iterator<T> descendingIterator() {
        return Collections.unmodifiableNavigableSet(this).descendingIterator();
    }

    // [from, to)
    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        if (fromElement != null && toElement != null && comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("fromKey > toKey");
        }

        if (elements.isEmpty() ||
                (comparator.compare(fromElement, toElement) == 0 && !(fromInclusive && toInclusive))) {
            return Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet());
        }

        int from = getFloorPosition(fromElement);
        int to = getCeilingPosition(toElement);


        if (from >= 0 && !(fromInclusive && comparator.compare(elements.get(from), fromElement) == 0)) {
            from++;
        }

        if (to < size() && toInclusive && comparator.compare(elements.get(to), toElement) == 0) {
            to++;
        }

        from = Math.max(from, 0);

        if (to < from) {
            throw new IllegalArgumentException("fromKey > toKey");
        }

        return new ArraySet<T>(from, to, this);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        if (elements.isEmpty())
            return Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet());
        T firstElement = elements.get(0);
        if (comparator.compare(firstElement, toElement) > 0)
            return Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet());
        return subSet(firstElement, true, toElement, inclusive);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        if (elements.isEmpty())
            return Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet());
        T lastElement = elements.get(elements.size() - 1);
        if (comparator.compare(lastElement, fromElement) < 0)
            return Collections.unmodifiableNavigableSet(Collections.emptyNavigableSet());
        return subSet(fromElement, inclusive, lastElement, true);
    }

    @Override
    public Comparator<? super T> comparator() {
        if (isDefaultComparator)
            return null;
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    @Override
    public T first() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException();
        }
        return elements.get(0);
    }

    @Override
    public T last() {
        if (elements.isEmpty()) {
            throw new NoSuchElementException();
        }
        return elements.get(elements.size() - 1);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        T e = (T) o;
        T sought = floor(e);
        if (sought == null || e == null || comparator.compare(sought, e) != 0)
            return false;
        return true;
    }

    private static <T> List<T> getReversed(List<T> list) {
        return new AbstractList<T>() {
            @Override
            public T get(int index) {
                index = list.size() - index - 1;
                return list.get(index);
            }

            @Override
            public int size() {
                return list.size();
            }
        };
    }

    private class DefaultComparator implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            if (o1 != null)
                return ((Comparable) o1).compareTo(o2);
            else if (o2 != null)
                return -1 * ((Comparable<T>) o2).compareTo(o1);
            return 0;
        }
    }
}
