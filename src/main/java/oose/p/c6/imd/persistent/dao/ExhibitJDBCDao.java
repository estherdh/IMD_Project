package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Era;
import oose.p.c6.imd.domain.Exhibit;
import oose.p.c6.imd.domain.Museum;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;

import javax.el.MethodNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExhibitJDBCDao implements IExhibitDao {
    private static final Logger LOGGER = Logger.getLogger(IExhibitDao.class.getName());
    private String museumIdColomnName = "MuseumId";
    private String eraIdColomnName = "EraId";

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
                e = createExhibitFromResultset(rs);
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
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT *  FROM Exhibit e  LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId  WHERE e.MuseumId = ? AND ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?),  1))");
            ps.setInt(1, museumId);
            ps.setInt(2, user.getLanguageId());
            rs = ps.executeQuery();
            List<Exhibit> list = new ArrayList<Exhibit>();
            while(rs.next()){
                list.add(createExhibitFromResultset(rs));
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList)handleException(e, new ArrayList<Exhibit>());

        }
    }

    @Override
    public List<Exhibit> listByEra(User user, int eraId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT *  FROM Exhibit e  LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId  WHERE e.EraId = ? AND ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?),  1))");
            ps.setInt(1, eraId);
            ps.setInt(2, user.getLanguageId());
            rs = ps.executeQuery();
            List<Exhibit> list = new ArrayList<Exhibit>();
            while(rs.next()){
                list.add(createExhibitFromResultset(rs));
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList)handleException(e, new ArrayList<Exhibit>());

        }
    }

    private Exhibit createExhibitFromResultset(ResultSet rs) throws SQLException{
        return new Exhibit(rs.getInt("ExhibitId"), rs.getString("name"), rs.getString("description"), rs.getString("video"), rs.getString("image"), rs.getInt("year"), rs.getInt(eraIdColomnName), rs.getInt(museumIdColomnName));
    }

    @Override
    public List<Exhibit> list(User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT *  FROM Exhibit e  LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId  WHERE ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?),  1))");
            ps.setInt(1, user.getLanguageId());
            rs = ps.executeQuery();
            List<Exhibit> list = new ArrayList<Exhibit>();
            while(rs.next()){
                list.add(createExhibitFromResultset(rs));
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList)handleException(e, new ArrayList<Exhibit>());

        }
    }

    @Override
    public Era findEra(User user, int eraId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM EraLanguage el WHERE el.EraId = ? AND LanguageId = COALESCE((SELECT el2.LanguageId FROM EraLanguage el2 WHERE el2.LanguageId = ? AND el2.EraId = el.EraId), 1)");
            ps.setInt(1, eraId);
            ps.setInt(2, user.getLanguageId());
            rs = ps.executeQuery();
            Era e = null;
            if(rs.next()){
                e = new Era(rs.getInt(eraIdColomnName), rs.getString("name"));
            }
            connection.close();
            return e;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    @Override
    public List<Era> listEra(User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM EraLanguage el WHERE LanguageId = COALESCE((SELECT el2.LanguageId FROM EraLanguage el2 WHERE el2.LanguageId = ? AND el2.EraId = el.EraId), 1)");
            ps.setInt(1, user.getLanguageId());
            rs = ps.executeQuery();
            List<Era> list = new ArrayList<>();
            while(rs.next()){
                Era e = new Era(rs.getInt(eraIdColomnName), rs.getString("name"));
                list.add(e);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList)handleException(e, new ArrayList<Era>());

        }
    }

    @Override
    public Museum findMuseum(int museumId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("Select * FROM Museum WHERE museumId = ?");
            ps.setInt(1, museumId);
            rs = ps.executeQuery();
            Museum m = null;
            if(rs.next()){
                m = new Museum(rs.getInt(museumIdColomnName), rs.getString("MuseumName"), rs.getString("Website"), rs.getString("Region"));
            }
            connection.close();
            return m;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    @Override
    public List<Museum> listMuseums() {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("Select * FROM Museum");
            rs = ps.executeQuery();
            List<Museum> list = new ArrayList<>();
            while(rs.next()){
                Museum m = new Museum(rs.getInt(museumIdColomnName), rs.getString("MuseumName"), rs.getString("Website"), rs.getString("Region"));
                list.add(m);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList)handleException(e, new ArrayList<Museum>());
        }
    }

    @Override
    public List<Exhibit> findExhibitsNotYetInQuestlog(int userId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM exhibit e INNER JOIN exhibitinfo ei " +
                    "ON e.ExhibitId = ei.ExhibitId WHERE e.ExhibitId " +
                    "NOT IN(SELECT qp.Value FROM questlog ql INNER JOIN " +
                    "questProperties qp ON ql.EntryId = qp.EntryId " +
                    "WHERE UserId = ? AND QuestTypeId = 3 AND Removed = 0 AND Completed = 0) AND " +
                    "ei.LanguageId = (SELECT u.LanguageId FROM users u " +
                    "WHERE u.UserId = ?)");
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            List<Exhibit> list = new ArrayList<>();
            while (rs.next()) {
                Exhibit e = new Exhibit(rs.getInt("ExhibitId"), rs.getString("Name"),
                        rs.getString("Description"), rs.getString("Video"), rs.getString("Image"),
                        rs.getInt("Year"), rs.getInt("EraId"), rs.getInt("MuseumId"));
                list.add(e);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList)handleException(e, new ArrayList<Exhibit>());
        }
    }



    @Override
    public void add(Exhibit entity) {
        throw new MethodNotFoundException();
    }

    @Override
    public void update(Exhibit updatedEntity) { throw new MethodNotFoundException(); }

    @Override
    public void remove(Exhibit entity) {
        throw new MethodNotFoundException();
    }

    @Override
    public List<Exhibit> list() {
        throw new MethodNotFoundException();
    }

    @Override
    public Exhibit find(int id) {
        throw new MethodNotFoundException();
    }

    protected Object handleException(Exception e, Object o){
        LOGGER.log(Level.SEVERE, e.toString(), e);
        return o;
    }
}
