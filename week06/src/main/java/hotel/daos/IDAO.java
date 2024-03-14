package hotel.daos;

import java.util.List;

interface IDAO<T> {
    public List<T> getAll();
    public T getById(int id);
    public T create(T t);
    public T update(T t);
    public void delete(T t);
}