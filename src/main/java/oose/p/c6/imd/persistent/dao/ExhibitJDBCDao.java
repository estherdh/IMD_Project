package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;

import javax.el.MethodNotFoundException;
import javax.mail.MethodNotSupportedException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExhibitJDBCDao implements IExhibitDao {
    private static final Logger LOGGER = Logger.getLogger(IExhibitDao.class.getName());

    @Override
    public Exhibit find(User user, int exhibitId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT *  FROM Exhibit e  LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId  WHERE e.ExhibitId = ? AND ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?),  1))");
            ps.setInt(1, exhibitId);
            ps.setInt(2, user.getLanguageId());
            rs = ps.executeQuery();
            Exhibit e = null;
            if(rs.next()){
                e = new Exhibit(rs.getInt("ExhibitId"), rs.getString("name"), rs.getString("description"), rs.getString("video"), rs.getString("image"), rs.getInt("year"));
            }
            connection.close();
            return e;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
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
        throw new MethodNotFoundException();
    }

    @Override
    public void update(Exhibit updatedEntity) { throw new MethodNotFoundException(); }

    @Override
    public void remove(Exhibit entity) {

    }

    @Override
    public List<Exhibit> list() {
        throw new MethodNotFoundException();
    }

    @Override
    public Exhibit find(int id) {
        throw new MethodNotFoundException();
    }
}
