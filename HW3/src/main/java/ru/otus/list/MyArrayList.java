package ru.otus.list;

import java.util.*;

public class MyArrayList<T> implements List<T>, RandomAccess {

    private static final int INITIAL_CAPACITY = 10;
    private static final int FACTOR = 2;

    private Object[] array;
    private int size;

    public MyArrayList() {
        this(INITIAL_CAPACITY);
    }

    public MyArrayList(int initialCapacity) {
        array = new Object[initialCapacity];
    }

    private int capacity() {
        return array.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (size == Integer.MAX_VALUE) {
            return false;
        }
        if (size == capacity()) {
            growUp();
        }
        array[size++] = t;
        return true;
    }

    private void growUp() {
        Object[] newArray = new Object[FACTOR * capacity()];
        System.arraycopy(array, 0, newArray, 0, size);
        array = newArray;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (T) array[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        Object current = array[index];
        array[index] = element;
        return (T) current;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {

            int index = -1;

            @Override
            public boolean hasNext() {
                throw new UnsupportedOperationException();
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                return (T) array[++index];
            }

            @Override
            public boolean hasPrevious() {
                throw new UnsupportedOperationException();
            }

            @Override
            public T previous() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int nextIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public int previousIndex() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(T t) {
                MyArrayList.this.set(index, t);
            }

            @Override
            public void add(T t) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
