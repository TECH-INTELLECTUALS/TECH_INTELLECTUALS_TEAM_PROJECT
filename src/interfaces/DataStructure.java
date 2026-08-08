package interfaces;

public interface DataStructure<T> {

    void add(T item);
    void remove(T item);
    int size();
    boolean isEmpty();

    default T get(int index) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not support indexed get()");
    }

    default void set(int index, T item) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not support indexed set()");
    }
}