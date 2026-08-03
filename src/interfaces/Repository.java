package interfaces;

/**
 * Generic repository contract for persistence operations.
 * @param <T> entity type
 */
public interface Repository<T> {

    /**
     * Saves or updates an entity.
     * @param entity entity to persist
     */
    void save(T entity);

    /**
     * Finds an entity by its identifier.
     * @param id entity identifier
     * @return found entity or null
     */
    T findById(String id);
}
