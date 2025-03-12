package Repository;

import java.util.List;

public interface CrudRepository<T> {
    T save(T object);
    void delete(T object);
    void update(T object) throws Exception;
    List<T> getAll() throws Exception;
    T getById(int id) throws Exception;
}
