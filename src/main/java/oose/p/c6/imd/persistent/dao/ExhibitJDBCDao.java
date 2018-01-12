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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Exhibit e LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId AND ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?), 1)) WHERE e.ExhibitId = ?");
            ps.setInt(2, exhibitId);
            ps.setInt(1, user.getLanguageId());
            rs = ps.executeQuery();
            Exhibit e = null;
            if (rs.next()) {
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
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Exhibit e LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId AND ei.languageId  IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?), 1)) WHERE e.MuseumId = ?");
            ps.setInt(2, museumId);
            ps.setInt(1, user.getLanguageId());
            return getListFromPreparedStatement(ps, connection);
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Exhibit>());
        }
    }

    @Override
    public List<Exhibit> listByEra(User user, int eraId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Exhibit e LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId AND ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?), 1)) WHERE e.EraId = ?;");
            ps.setInt(2, eraId);
            ps.setInt(1, user.getLanguageId());
            return getListFromPreparedStatement(ps, connection);
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Exhibit>());

        }
    }

    private Exhibit createExhibitFromResultset(ResultSet rs) throws SQLException {
        return new Exhibit(rs.getInt("ExhibitId"), rs.getString("name"), rs.getString("description"), rs.getString("video"), findExhibitImagesFromExhibit(rs.getInt("ExhibitId")), rs.getInt("year"), rs.getInt(eraIdColomnName), rs.getInt(museumIdColomnName));
    }

    @Override
    public List<Exhibit> list(User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Exhibit e LEFT JOIN ExhibitInfo ei ON e.ExhibitId = ei.ExhibitId AND ei.languageId IN (SELECT COALESCE((SELECT languageId FROM ExhibitInfo eil WHERE eil.ExhibitId = e.ExhibitId AND languageId = ?), 1));");
            ps.setInt(1, user.getLanguageId());
            return getListFromPreparedStatement(ps, connection);
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Exhibit>());

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
            if (rs.next()) {
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
            while (rs.next()) {
                Era e = new Era(rs.getInt(eraIdColomnName), rs.getString("name"));
                list.add(e);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Era>());

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
            if (rs.next()) {
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
            while (rs.next()) {
                Museum m = new Museum(rs.getInt(museumIdColomnName), rs.getString("MuseumName"), rs.getString("Website"), rs.getString("Region"));
                list.add(m);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Museum>());
        }
    }

    @Override
    public List<Exhibit> findExhibitsNotYetInQuestlog(int userId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
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
            return getListFromPreparedStatement(ps, connection);
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Exhibit>());
        }
    }

    @Override
    public List<Era> findErasNotYetInQuestlog(int userId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM era e INNER JOIN eralanguage el " +
                    "ON e.EraId = el.EraId WHERE el.EraId " +
                    "NOT IN(SELECT qp.Value FROM questlog ql INNER JOIN " +
                    "questProperties qp ON ql.EntryId = qp.EntryId " +
                    "WHERE UserId = ? AND QuestTypeId = 4 AND Removed = 0 AND Completed = 0) " +
                    "AND el.LanguageId = (SELECT u.LanguageId FROM users u " +
                    "WHERE u.UserId = ?)");
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            List<Era> list = new ArrayList<>();
            while (rs.next()) {
                Era e = new Era(rs.getInt("EraId"), rs.getString("Name"));
                list.add(e);
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Era>());
        }
    }

    private List<String> findExhibitImagesFromExhibit(int exhibitId) throws SQLException {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        PreparedStatement ps = connection.prepareStatement("SELECT Path FROM ExhibitImage e INNER JOIN Image i ON i.ImageId=e.ImageId WHERE ExhibitId = ?");
        ps.setInt(1, exhibitId);
        ResultSet rs = ps.executeQuery();
        List<String> images = new ArrayList<>();
        while(rs.next()) {
            images.add(rs.getString(1));
        }
        return images;
    }

    @Override
    public List<Museum> findMuseumsNotYetInQuestlog(int userId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM museum m WHERE MuseumId " +
                    "NOT IN(SELECT qp.Value FROM questlog ql INNER JOIN " +
                    "questProperties qp ON ql.EntryId = qp.EntryId WHERE " +
                    "UserId = ? AND QuestTypeId = 1 AND Removed = 0 AND Completed = 0) " +
                    "AND LanguageId = COALESCE((SELECT LanguageId FROM users WHERE UserId = ?), 1) AND QrCode IS NOT NULL");
            ps.setInt(1, userId);
            ps.setInt(2, userId);
            rs = ps.executeQuery();
            List<Museum> list = new ArrayList<>();
            while (rs.next()) {
                Museum m = new Museum(rs.getInt("MuseumId"), rs.getString("MuseumName"), rs.getString("Website"), rs.getString("Region"));
                list.add(m);
                m.setQrCode(rs.getString("QrCode"));
            }
            connection.close();
            return list;
        } catch (SQLException e) {
            return (ArrayList) handleException(e, new ArrayList<Museum>());
        }
    }

    @Override
    public void add(Exhibit entity) {
        throw new MethodNotFoundException();
    }

    @Override
    public void update(Exhibit updatedEntity) {
        throw new MethodNotFoundException();
    }

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

    private Object handleException(Exception e, Object o) {
        LOGGER.log(Level.SEVERE, e.toString(), e);
        return o;
    }

    private List<Exhibit> getListFromPreparedStatement(PreparedStatement ps, Connection connection) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<Exhibit> list = new ArrayList<>();
        while (rs.next()) {
            list.add(createExhibitFromResultset(rs));
        }
        connection.close();
        return list;
    }
}
