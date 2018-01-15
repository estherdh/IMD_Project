package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Era;
import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.Museum;
import oose.p.c6.imd.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IExhibitDao extends IDao<Exhibit> {
    Exhibit find(User user, int exhibitId);
    List<Exhibit> listByMuseum(User user, int museumId);
    List<Exhibit> listByEra(User user, int eraId);
    List<Exhibit> list(User user);
    Era findEra(User user, int eraId);
    List<Era> listEra(User user);
    Museum findMuseum(int museumId);
    List<Museum> listMuseums();
    List<Exhibit> findExhibitsNotYetInQuestlog(int userId);
    List<Era> findErasNotYetInQuestlog(int userId);
    Exhibit createExhibitFromResultset(ResultSet rs) throws SQLException;
}
