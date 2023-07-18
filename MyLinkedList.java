public class MyLinkedList<E> implements List<E> {

        private class Node {
            private E element;
            private Node next;
            private Node previous;

            public Node(E element, Node next, Node previous) {
                this.element = element;
                this.next = next;
                this.previous = previous;
            }
        }

        private Node head;
        private Node tail;
        private int size;

        // Constructor
        public MyLinkedList() {
            this.head = null;
            this.tail = null;
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
            return indexOf(o) != -1;
        }

        public Iterator<E> iterator() {
            return new MyIterator();
        }

        public Object[] toArray() {
            Object[] array = new Object[size];
            int index = 0;
            for (E element : this) {
                array[index++] = element;
            }
            return array;
        }

        public <T> T[] toArray(T[] a) {
            if (a.length < size) {
                a = Arrays.copyOf(a, size);
            }
            int index = 0;
            for (E element : this) {
                a[index++] = (T) element;
            }
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }

        public boolean add(E e) {
            Node newNode = new Node(e, null, tail);
            if (isEmpty()) {
                head = newNode;
            } else {
                tail.next = newNode;
            }
            tail = newNode;
            size++;
            return true;
        }

        public boolean remove(Object o) {
            if (isEmpty()) {
                return false;
            }
            Node current = head;
            while (current != null) {
                if (Objects.equals(current.element, o)) {
                    unlinkNode(current);
                    return true;
                }
                current = current.next;
            }
            return false;
        }

        private void unlinkNode(Node node) {
            if (node == head) {
                head = node.next;
            } else {
                node.previous.next = node.next;
            }
            if (node == tail) {
                tail = node.previous;
            } else {
                node.next.previous = node.previous;
            }
            size--;
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
            for (E element : c) {
                modified |= add(element);
            }
            return modified;
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            if (c.isEmpty()) {
                return false;
            }
            Node successor;
            Node predecessor;
            if (index == size) {
                successor = null;
                predecessor = tail;
            } else {
                successor = getNode(index);
                predecessor = successor.previous;
            }
            for (E element : c) {
                Node newNode = new Node(element, successor, predecessor);
                if (predecessor == null) {
                    head = newNode;
                } else {
                    predecessor.next = newNode;
                }
                predecessor = newNode;
                size++;
            }
            if (successor == null) {
                tail = predecessor;
            } else {
                successor.previous = predecessor;
            }
            return true;
        }

        public boolean removeAll(Collection<?> c) {
            boolean modified = false;
            for (Object obj : c) {
                modified |= remove(obj);
            }
            return modified;
        }

        public boolean retainAll(Collection<?> c) {
            boolean modified = false;
            Node current = head;
            while (current != null) {
                if (!c.contains(current.element)) {
                    Node next = current.next;
                    unlinkNode(current);
                    current = next;
                    modified = true;
                } else {
                    current = current.next;
                }
            }
            return modified;
        }

        public void clear() {
            head = null;
            tail = null;
            size = 0;
        }

        public E get(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            return getNode(index).element;
        }

        private Node getNode(int index) {
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException();
            }
            Node current;
            if (index < size / 2) {
                current = head;
                for (int i = 0; i < index; i++) {
                    current = current.next;
                }
            } else {
                current = tail;
                for (int i = size - 1; i > index; i--) {
                    current = current.previous;
                }
            }
            return current;
        }

        public E set(int index, E element) {
            Node node = getNode(index);
            E oldElement = node.element;
            node.element = element;
            return oldElement;
        }

        public void add(int index, E element) {
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException();
            }
            if (index == size) {
                add(element);
                return;
            }
            Node successor = getNode(index);
            Node predecessor = successor.previous;
            Node newNode = new Node(element, successor, predecessor);
            if (predecessor == null) {
                head = newNode;
            } else {
                predecessor.next = newNode;
            }
            successor.previous = newNode;
            size++;
        }

        public E remove(int index) {
            Node node = getNode(index);
            unlinkNode(node);
            return node.element;
        }

        public int indexOf(Object o) {
            Node current = head;
            int index = 0;
            while (current != null) {
                if (Objects.equals(current.element, o)) {
                    return index;
                }
                current = current.next;
                index++;
            }
            return -1;
        }
        public int lastIndexOf(Object o) {
            Node current = tail;
            int index = size - 1;
            while (current != null) {
                if (Objects.equals(current.element, o)) {
                    return index;
                }
                current = current.previous;
                index--;
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
            MyLinkedList<E> subList = new MyLinkedList<>();
            Node current = getNode(fromIndex);
            for (int i = fromIndex; i < toIndex; i++) {
                subList.add(current.element);
                current = current.next;
            }
            return subList;
        }

        // Inner class for the iterator
        private class MyIterator implements Iterator<E> {
            private Node current;

            public MyIterator() {
                this.current = head;
            }

            public boolean hasNext() {
                return current != null;
            }

            public E next() {
                if (hasNext()) {
                    E element = current.element;
                    current = current.next;
                    return element;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        // Inner class for the list iterator
        private class MyListIterator extends MyIterator implements ListIterator<E> {
            private Node lastReturned;
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
                this.lastReturned = null;
            }

            public boolean hasPrevious() {
                return currentIndex > 0;
            }

            public E previous() {
                if (hasPrevious()) {
                    Node previous = current != null ? current.previous : tail;
                    current = previous;
                    lastReturned = current;
                    currentIndex--;
                    return current.element;
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
                if (lastReturned == null) {
                    throw new IllegalStateException();
                }
                lastReturned.element = e;
            }

            public void add(E e) {
                Node newNode;
                if (current == null) {
                    newNode = new Node(e, null, tail);
                    tail = newNode;
                } else {
                    Node predecessor = current.previous;
                    newNode = new Node(e, current, predecessor);
                    current.previous = newNode;
                    if (predecessor != null) {
                        predecessor.next = newNode;
                    } else {
                        head = newNode;
                    }
                }
                lastReturned = null;
                currentIndex++;
                size++;
            }
        }
    }

}
