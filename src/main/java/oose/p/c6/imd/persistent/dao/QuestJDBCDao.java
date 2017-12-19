package oose.p.c6.imd.persistent.dao;

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
        ResultSet rs = null;
        try {
            PreparedStatement psInsert1 = connection.prepareStatement("INSERT INTO Questlog (UserId, QuestTypeId) VALUES (?, ?)");
            psInsert1.setInt(1, userId);
            psInsert1.setInt(2, questTypeId);
            psInsert1.execute();

            PreparedStatement psInsert2 = connection.prepareStatement("INSERT INTO Questproperties (EntryId, Key, Value) VALUES ((SELECT LAST_INSERT_ID()), ?, ?) ");

            psInsert2.setString(1, properties.get("Key"));
            psInsert2.setString(2, properties.get("Value"));
            psInsert2.execute();

//            connection.close();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    @Override
    public Quest find(int id, User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        QuestFactory factory = QuestFactory.getInstance();
        try {
            PreparedStatement ps = connection.prepareStatement("" +
                    "SELECT * FROM (questlog ql INNER JOIN QuestType qt" +
                    "    ON ql.QuestTypeId = qt.QuestTypeId)" +
                    "  INNER JOIN QuestTypeLanguage qtl" +
                    "    ON qt.QuestTypeId = qtl.QuestTypeId " +
                    "WHERE UserId = ? " +
                    "AND LanguageId IN" +
                    "    (SELECT COALESCE(" +
                    "        (SELECT languageId FROM questtypelanguage qtl2 WHERE qtl2.LanguageId = ? AND qtl2.QuestTypeId = qt.QuestTypeId)" +
                    "        , 1)" +
                    "    );");
            ps.setInt(1, user.getId());
            ps.setInt(2, user.getLanguageId());
            ResultSet rs = ps.executeQuery();
            Quest result = null;
            if (rs.next()) {
                int questType = rs.getInt("QuestTypeId");
                IQuestType typeStrategy = factory.generateQuest(QuestTypes.values()[questType - 1], getVariablesOfQuest(rs.getInt("EntryId")));
                result = new Quest(
                        rs.getInt("EntryId"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("Reward"),
                        typeStrategy
                );
            }
            return result;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return null;
        }
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
        QuestFactory factory = QuestFactory.getInstance();
        try {
            PreparedStatement ps = connection.prepareStatement("" +
                    "SELECT * FROM (questlog ql INNER JOIN QuestType qt" +
                    "    ON ql.QuestTypeId = qt.QuestTypeId)" +
                    "  INNER JOIN QuestTypeLanguage qtl" +
                    "    ON qt.QuestTypeId = qtl.QuestTypeId " +
                    "WHERE UserId = ? " +
                    "AND Completed = 0 " +
                    "AND LanguageId IN" +
                    "    (SELECT COALESCE(" +
                    "        (SELECT languageId FROM questtypelanguage qtl2 WHERE qtl2.LanguageId = ? AND qtl2.QuestTypeId = qt.QuestTypeId)" +
                    "        , 1)" +
                    "    );");
            ps.setInt(1, userId);
            ps.setInt(2, languageId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int questType = rs.getInt("QuestTypeId");
                IQuestType typeStrategy = factory.generateQuest(QuestTypes.values()[questType - 1], getVariablesOfQuest(rs.getInt("EntryId")));
                questList.add(new Quest(
                        rs.getInt("EntryId"),
                        rs.getString("Name"),
                        rs.getString("Description"),
                        rs.getInt("Reward"),
                        typeStrategy
                ));
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
            PreparedStatement query = connection.prepareStatement("DELETE FROM QuestLog WHERE EntryId = ? AND UserId = ?;");
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
}
