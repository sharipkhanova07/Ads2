 public class MyArrayList<E> implements List<E> {

        private Object[] elements;
        private int size;

        public MyArrayList() {
            this.elements = new Object[10];
            this.size = 0;
        }

        // Implementation of the List interface methods

        public int size() {
            return size;
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean contains(Object o) {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(o)) {
                    return true;
                }
            }
            return false;
        }

        public Iterator<E> iterator() {
            return new MyIterator();
        }

        public Object[] toArray() {
            return Arrays.copyOf(elements, size);
        }

        public <T> T[] toArray(T[] a) {
            if (a.length < size) {
                return (T[]) Arrays.copyOf(elements, size, a.getClass());
            }
            System.arraycopy(elements, 0, a, 0, size);
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }

        public boolean add(E e) {
            if (size == elements.length) {
                // Create a new array with double the size
                Object[] newElements = new Object[size * 2];
                // Copy elements from the original array to the new array
                System.arraycopy(elements, 0, newElements, 0, size);
                // Update the elements reference to point to the new array
                elements = newElements;
            }
            elements[size++] = e;
            return true;
        }

        public boolean remove(Object o) {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(o)) {
                    fastRemove(i);
                    return true;
                }
            }
            return false;
        }

        private void fastRemove(int index) {
            int numToMove = size - index - 1;
            if (numToMove > 0) {
                System.arraycopy(elements, index + 1, elements, index, numToMove);
            }
            elements[--size] = null;
        }

        public boolean containsAll(Collection<?> c) {
            for (Object obj : c) {
                if (!contains(obj)) {
                    return false;
                }
            }
            return true;
        }

        public boolean addAll(Collection<? extends E> c) {
            boolean modified = false;
            for (E e : c) {
                modified |= add(e);
            }
            return modified;
        }

        public boolean removeAll(Collection<?> c) {
            boolean modified = false;
            for (Object obj : c) {
                modified |= remove(obj);
            }
            return modified;
        }

        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            return (E) elements[index];
        }

        public E remove(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            E removedElement = (E) elements[index];
            int numToMove = size - index - 1;
            if (numToMove > 0) {
                System.arraycopy(elements, index + 1, elements, index, numToMove);
            }
            elements[--size] = null;
            return removedElement;
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            Object[] newElements = new Object[size + c.size()];
            System.arraycopy(elements, 0, newElements, 0, index);
            int numToMove = size - index;
            if (numToMove > 0) {
                System.arraycopy(elements, index, newElements, index + c.size(), numToMove);
            }
            int i = index;
            for (E e : c) {
                newElements[i++] = e;
            }
            elements = newElements;
            size += c.size();
            return true;
        }

        public boolean retainAll(Collection<?> c) {
            boolean modified = false;
            for (int i = 0; i < size; i++) {
                if (!c.contains(elements[i])) {
                    fastRemove(i);
                    i--;
                    modified = true;
                }
            }
            return modified;
        }

        public void clear() {
            Arrays.fill(elements, null);
            size = 0;
        }

        public E set(int index, E element) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            E oldElement = (E) elements[index];
            elements[index] = element;
            return oldElement;
        }

        public void add(int index, E element) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            if (size == elements.length) {
                Object[] newElements = new Object[size * 2];
                System.arraycopy(elements, 0, newElements, 0, index);
                int numToMove = size - index;
                if (numToMove > 0) {
                    System.arraycopy(elements, index, newElements, index + 1, numToMove);
                }
                elements = newElements;
            } else {
                System.arraycopy(elements, index, elements, index + 1, size - index);
            }
            elements[index] = element;
            size++;
        }

        public int indexOf(Object o) {
            for (int i = 0; i < size; i++) {
                if (elements[i].equals(o)) {
                    return i;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object o) {
            for (int i = size - 1; i >= 0; i--) {
                if (elements[i].equals(o)) {
                    return i;
                }
            }
            return -1;
        }

        public ListIterator<E> listIterator() {
            return new MyListIterator();
        }

        public ListIterator<E> listIterator(int index) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            return new MyListIterator(index);
        }

        public List<E> subList(int fromIndex, int toIndex) {
            if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
                throw new IndexOutOfBoundsException();
            }
            MyArrayList<E> subList = new MyArrayList<>();
            for (int i = fromIndex; i < toIndex; i++) {
                subList.add((E) elements[i]);
            }
            return subList;
        }

        // Inner class for the iterator
        private class MyIterator implements Iterator<E> {
            private int cursor;

            public MyIterator() {
                this.cursor = 0;
            }

            public boolean hasNext() {
                return cursor < size;
            }

            public E next() {
                if (hasNext()) {
                    return (E) elements[cursor++];
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        // Inner class for the list iterator
        private class MyListIterator extends MyIterator implements ListIterator<E> {
            private int currentIndex;

            public MyListIterator() {
                this(0);
            }

            public MyListIterator(int index) {
                super();
                if (index < 0 || index > size) {
                    throw new IndexOutOfBoundsException();
                }
                this.currentIndex = index;
            }

            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            public E previous() {
                if (hasPrevious()) {
                    return (E) elements[--currentIndex];
                }
                throw new NoSuchElementException();
            }

            public int nextIndex() {
                return currentIndex;
            }

            public int previousIndex() {
                return currentIndex - 1;
            }

            public void set(E e) {
                if (currentIndex < 0 || currentIndex >= size) {
                    throw new IndexOutOfBoundsException();
                }
                elements[currentIndex] = e;
            }

            public void add(E e) {
                MyArrayList.this.add(currentIndex++, e);
            }
        }
    }
    }
