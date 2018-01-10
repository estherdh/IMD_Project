package oose.p.c6.imd.persistent.dao;

import com.mysql.cj.api.mysqla.result.Resultset;
import oose.p.c6.imd.domain.*;
import oose.p.c6.imd.persistent.ConnectMySQL;

import javax.el.MethodNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuestJDBCDao implements IQuestDAO {
    private static final Logger LOGGER = Logger.getLogger(QuestJDBCDao.class.getName());

    public void addQuestToQuestlog(Map<String, String> properties, int userId, int questTypeId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement psInsert1 = connection.prepareStatement("INSERT INTO Questlog (UserId, QuestTypeId) VALUES (?, ?)");
            psInsert1.setInt(1, userId);
            psInsert1.setInt(2, questTypeId);
            psInsert1.execute();
            ResultSet rs1 = connection.prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
            rs1.next();
            int entryId = rs1.getInt(1);
            PreparedStatement psInsert2 = connection.prepareStatement(buildQuestPropertyQuery(properties));
            int j = 1;
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                psInsert2.setInt(j++, entryId);
                psInsert2.setString(j++, entry.getKey());
                psInsert2.setString(j++, entry.getValue());
            }
            psInsert2.execute();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private String buildQuestPropertyQuery(Map<String, String> properties) {
        StringBuilder sb = new StringBuilder("INSERT INTO Questproperties (EntryId, `Key`, `Value`) VALUES ");
        for (int i = 0; i < properties.size(); i++) {
            sb.append("(? , ?, ?), ");
        }
        return sb.toString().replaceAll(", $", "");
    }

    @Override
    public Quest find(int id, User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM (questlog ql INNER JOIN QuestType qt ON ql.QuestTypeId = qt.QuestTypeId) " +
                    "INNER JOIN QuestTypeLanguage qtl ON qt.QuestTypeId = qtl.QuestTypeId " +
                    "WHERE UserId = ? And EntryId = ? AND LanguageId IN " +
                    "(SELECT COALESCE((SELECT languageId FROM questtypelanguage qtl2 WHERE qtl2.LanguageId = ? AND qtl2.QuestTypeId = qt.QuestTypeId), 1));");
            ps.setInt(1, user.getId());
            ps.setInt(2, id);
            ps.setInt(3, user.getLanguageId());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return createQuest(rs, generateQuest(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }

    public void add(Quest entity) {
        //Not yet implemented

    }

    public void update(Quest updatedEntity) {
        //Not yet implemented
    }

    public void remove(Quest entity) {
        //Not yet implemented
    }

    public List<Quest> list() {
        return new ArrayList<Quest>();
    }

    public Quest find(int id) {
        throw new MethodNotFoundException();
    }

    public List<Quest> getQuestsForUser(int userId, int languageId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<Quest> questList = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM (questlog ql INNER JOIN QuestType qt ON ql.QuestTypeId = qt.QuestTypeId) " +
                    "INNER JOIN QuestTypeLanguage qtl ON qt.QuestTypeId = qtl.QuestTypeId INNER JOIN questproperties qp ON qp.EntryId = ql.EntryId " +
                    "WHERE UserId = ? AND Completed = 0 AND LanguageId IN " +
                    "(SELECT COALESCE(" +
                    "(SELECT languageId FROM questtypelanguage qtl2 WHERE qtl2.LanguageId = ? AND qtl2.QuestTypeId = qt.QuestTypeId), 1));");
            ps.setInt(1, userId);
            ps.setInt(2, languageId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                questList.add(createQuest(rs, generateQuest(rs)));
                getQuestDescription(rs);
            }
            if (!connection.isClosed()) {
                connection.close();
            }
            return questList;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return questList;
        }
    }

    public boolean removeQuestFromQuestLog(int entryId, int userId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement query = connection.prepareStatement("UPDATE questlog ql SET ql.Removed = 1 WHERE EntryId = ? AND UserId = ?;");
            query.setInt(1, entryId);
            query.setInt(2, userId);
            query.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return false;
        }
    }

    public void setQuestComplete(int entryId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("" +
                    "UPDATE questlog " +
                    "SET Completed = 1 " +
                    "WHERE EntryId = ?");
            ps.setInt(1, entryId);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private Map<String, String> getVariablesOfQuest(int entryId) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        Map<String, String> propertyList = new HashMap<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM questproperties WHERE EntryId = ?;");
            ps.setInt(1, entryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String key = rs.getString("Key");
                String value = rs.getString("Value");
                propertyList.put(key, value);
            }
            return propertyList;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
    }

    private Quest createQuest(ResultSet rs, IQuestType typeStrategy) throws SQLException {


        Quest q = new Quest(
                rs.getInt("ql.EntryId"),
                rs.getString("Name"),
                rs.getString("Description"),
                rs.getInt("Reward"),
                rs.getInt("QuestTypeId"),
                rs.getInt("Removed"),
                rs.getInt("Completed"),
                typeStrategy
        );


        createQuestDescription(rs, Integer.parseInt(rs.getString("Description")), q);

        return q;
    }

    public void createQuestDescription(ResultSet rs, int questTypeId, Quest q) throws SQLException {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        String valueOfId = "";

        switch (questTypeId) {
            case 1:
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM museum m INNER JOIN questproperties qp ON qp.Value = m.QrCode " +
                            "WHERE UserId = ? AND Completed = 0 AND qp.Value = ? AND LanguageId = " +
                            "(SELECT LanguageId FROM users u WHERE u.UserId = ?);");
                    ps.setInt(1, rs.getInt("UserId"));
                    ps.setInt(2, rs.getInt("Value"));
                    ps.setInt(3, rs.getInt("UserId"));
                    ResultSet rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        valueOfId = rs2.getString("MuseumName");
                    }
                    if (!connection.isClosed()) {
                        connection.close();
                    }
                    q.setQuestDescription(rs.getString("Description") + " " + valueOfId);
                    q.setValueOfQuest(valueOfId);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            case 3:
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM exhibitinfo e INNER JOIN questproperties qp ON qp.Value = e.ExhibitId " +
                            "WHERE UserId = ? AND Completed = 0 AND qp.Value = ? AND LanguageId = " +
                            "(SELECT LanguageId FROM users u WHERE u.UserId = ?);");
                    ps.setInt(1, rs.getInt("UserId"));
                    ps.setInt(2, rs.getInt("Value"));
                    ps.setInt(3, rs.getInt("UserId"));
                    ResultSet rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        valueOfId = rs2.getString("Name");
                    }
                    if (!connection.isClosed()) {
                        connection.close();
                    }
                    q.setQuestDescription(rs.getString("Description") + " " + valueOfId);
                    q.setValueOfQuest(valueOfId);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            case 4:
                try {
                    PreparedStatement ps = connection.prepareStatement("SELECT * FROM eralanguage  e INNER JOIN questproperties qp ON qp.Value = e.EraId " +
                            "WHERE UserId = ? AND Completed = 0 AND qp.Value = ? AND LanguageId = " +
                            "(SELECT LanguageId FROM users u WHERE u.UserId = ?);");
                    ps.setInt(1, rs.getInt("UserId"));
                    ps.setInt(2, rs.getInt("Value"));
                    ps.setInt(3, rs.getInt("UserId"));
                    ResultSet rs2 = ps.executeQuery();
                    while (rs2.next()) {
                        valueOfId = rs2.getString("Name");
                    }
                    if (!connection.isClosed()) {
                        connection.close();
                    }
                    q.setQuestDescription(rs.getString("Description") + " " + valueOfId);
                    q.setValueOfQuest(valueOfId);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, e.toString(), e);
                }
            default:
                LOGGER.log(Level.WARNING, "Het questtype: " + rs.getString("QuestTypeId") + " is niet gevonden");
        }
    }

    private IQuestType generateQuest(ResultSet rs) throws SQLException {
        return QuestFactory.getInstance().generateQuest(QuestTypes.values()[rs.getInt("QuestTypeId") - 1], getVariablesOfQuest(rs.getInt("EntryId")));
    }

    public void getQuestDescription(ResultSet questDescription) {

    }
}
