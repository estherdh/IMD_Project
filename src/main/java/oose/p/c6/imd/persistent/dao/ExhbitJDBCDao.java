package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.User;

import java.util.List;

public class ExhbitJDBCDao implements IExhibitDao {
    @Override
    public Exhibit find(User user, int exhibitId) {
        return null;
    }

    @Override
    public List<Exhibit> listByMuseum(User user, int museumId) {
        return null;
    }

    @Override
    public List<Exhibit> listByEra(User user, int eraId) {
        return null;
    }

    @Override
    public List<Exhibit> list(User user) {
        return null;
    }


    @Override
    public void add(Exhibit entity) {

    }

    @Override
    public void update(Exhibit updatedEntity) {

    }

    @Override
    public void remove(Exhibit entity) {

    }

    @Override
    public List<Exhibit> list() {
        return null;
    }

    @Override
    public Exhibit find(int id) {
        return null;
    }
}
