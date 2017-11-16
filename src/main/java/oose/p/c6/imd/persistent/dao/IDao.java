package oose.p.c6.imd.persistent.dao;

import java.util.List;

public interface IDao<T> {
    void add(T entity);

    void update(T updatedEntity);

    void remove(T entity);

    List<T> list();

    T find(int id);
}