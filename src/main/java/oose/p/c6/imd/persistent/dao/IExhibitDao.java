package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Era;
import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.Museum;
import oose.p.c6.imd.domain.User;

import java.util.List;

public interface IExhibitDao extends IDao<Exhibit> {
    public Exhibit find(User user, int exhibitId);
    public List<Exhibit> listByMuseum(User user, int museumId);
    public List<Exhibit> listByEra(User user, int eraId);
    public List<Exhibit> list(User user);
    public Era findEra(User user, int eraId);
    public List<Era> listEra(User user);
    public Museum findMuseum(int museumId);
    public List<Museum> listMuseums();
    public List<Exhibit> findExhibitsNotYetInQuestlog(int userId);
}
