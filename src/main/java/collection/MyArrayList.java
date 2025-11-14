package collection;

import java.util.*;

public class MyArrayList<E> implements List<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] EMPTY_ELEMENTDATA = {};

    private Object[] elementData;
    private int size;

    public MyArrayList() {
        this.elementData = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Неверный размер: " + initialCapacity);
        }
        this.size = 0;
    }


    public MyArrayList(MyArrayList<? extends E> other) {
        if (other == null || other.size == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
            this.size = 0;
        } else {
            this.size = other.size;
            this.elementData = Arrays.copyOf(other.elementData, size, Object[].class);
        }
    }

    //Override group
    /*realized*/
    @Override
    public int size() {
        return size;
    }

    /*realized*/
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /*realized*/
    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    //TODO:подумать, что делать с итератором
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int cursor = 0;
            private int lastRet = -1; // Индекс последнего возвращенного элемента

            @Override
            public boolean hasNext() {
                return cursor < size;
            }

            @Override
            public E next() {
                if (cursor >= size) {
                    throw new NoSuchElementException();
                }
                lastRet = cursor;
                return (E) elementData[cursor++];
            }

            @Override
            public void remove() {
                if (lastRet < 0) {
                    throw new IllegalStateException();
                }
                MyArrayList.this.remove(lastRet);
                cursor = lastRet; // Корректируем курсор после удаления
                lastRet = -1;
            }
        };
    }

    /*realized*/
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /*realized*/
    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
        return true;
    }

    /*realized*/
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    fastRemove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    fastRemove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /*realized*/
    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size + numNew);
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
    }

    /*realized*/
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacity(size + numNew);

        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
        }

        System.arraycopy(a, 0, elementData, index, numNew);
        size += numNew;
        return numNew != 0;
    }

    /*realized*/
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }


    /*realized*/
    @Override
    public E get(int index) {
        rangeCheck(index);
        return (E) elementData[index];
    }

    //TODO: протестировать
    @Override
    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = (E) get(index);
        elementData[index] = element;
        return oldValue;
    }

    /*realized*/
    @Override
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);

        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    /*realized*/
    @Override
    public E remove(int index) {
        rangeCheck(index);

        E oldValue = (E) elementData[index];
        int numMoved = size - index - 1;

        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }

        elementData[--size] = null;
        return oldValue;
    }

    /*realized*/
    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /*realized*/
    @Override
    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size-1; i >= 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size-1; i >= 0; i--) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    //TODO:
    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    //TODO:
    @Override
    public ListIterator<E> listIterator(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        return new ListIterator<E>() {
            private int cursor = index;

            @Override
            public boolean hasNext() { return cursor < size; }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (E) elementData[cursor++];
            }

            @Override
            public boolean hasPrevious() { return cursor > 0; }

            @Override
            public E previous() {
                if (!hasPrevious()) throw new NoSuchElementException();
                return (E) elementData[--cursor];
            }

            @Override
            public int nextIndex() { return cursor; }

            @Override
            public int previousIndex() { return cursor - 1; }

            @Override
            public void remove() { throw new UnsupportedOperationException(); }

            @Override
            public void set(E e) { throw new UnsupportedOperationException(); }

            @Override
            public void add(E e) { throw new UnsupportedOperationException(); }
        };
    }

    //TODO:протестировать
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex + ", size: " + size);
        }

        int subListSize = toIndex - fromIndex;
        MyArrayList<E> subList = new MyArrayList<>(subListSize);
        System.arraycopy(elementData, fromIndex, subList.elementData, 0, subListSize);
        subList.size = subListSize;
        return subList;
    }

    //TODO:протестировать
    @Override
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(elementData[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    //TODO:протестировать
    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;

        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(elementData[i])) {
                remove(i);
                modified = true;
            }
        }
        return modified;
    }

    //TODO:протестировать
    @Override
    public boolean containsAll(Collection<?> c) {
        Objects.requireNonNull(c);
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        }
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }


    //addict
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elementData.length) {
            int oldCapacity = elementData.length;
            int newCapacity = (int)(oldCapacity * 1.5); // Увеличиваем на 50%
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    private void fastRemove(int index) {
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, index + 1, elementData, index, numMoved);
        }
        elementData[--size] = null;
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void rangeCheck(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}
