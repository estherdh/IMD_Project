package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.User;

import java.util.List;

public interface IExhibitDao extends IDao<Exhibit> {
    public Exhibit find(User user, int exhibitId);
    public List<Exhibit> listByMuseum(User user, int museumId);
    public List<Exhibit> listByEra(User user, int eraId);
    public List<Exhibit> list(User user);
}
