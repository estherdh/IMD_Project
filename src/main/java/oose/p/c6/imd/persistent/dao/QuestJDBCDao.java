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

    public void addQuestToQuestlog(Map<String, String> properties, int userId, int questTypeId, List<String> valuesById) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement psInsert1 = connection.prepareStatement("INSERT INTO Questlog (UserId, QuestTypeId) VALUES (?, ?)");
            psInsert1.setInt(1, userId);
            psInsert1.setInt(2, questTypeId);
            psInsert1.execute();
            ResultSet rs1 = connection.prepareStatement("SELECT LAST_INSERT_ID()").executeQuery();
            rs1.next();
            int entryId = rs1.getInt(1);

            String questDescription = "";
            PreparedStatement psInsert2 = connection.prepareStatement(buildQuestPropertyQuery(properties));
            int j = 1;
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                psInsert2.setInt(j++, entryId);
                psInsert2.setString(j++, entry.getKey());
                psInsert2.setString(j++, entry.getValue());

                questDescription = createQuestDescription(questTypeId, userId, valuesById);
            }
            psInsert2.execute();

            updateDescription(connection, entryId, questDescription);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    private void updateDescription(Connection connection, int entryId, String questDescription) throws SQLException {
        PreparedStatement psInsert3 = connection.prepareStatement("UPDATE Questlog SET Description = ? WHERE EntryId = ?");
        psInsert3.setString(1, questDescription);
        psInsert3.setInt(2, entryId);
        psInsert3.executeUpdate();
    }

    private String buildQuestPropertyQuery(Map<String, String> properties) {
        StringBuilder sb = new StringBuilder("INSERT INTO Questproperties (EntryId, `Key`, `Value`) VALUES ");
        for (int i = 0; i < properties.size(); i++) {
            sb.append("(? , ?, ?), ");
        }
        return sb.toString().replaceAll(", $", "");
    }

    private String createQuestDescription(int questTypeId, int userId, List<String> valuesById) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement psSelectTypeDescription = connection.prepareStatement("SELECT * FROM questtypelanguage qt INNER JOIN " +
                    "questlog ql ON qt.QuestTypeId = ql.QuestTypeId " +
                    "WHERE qt.QuestTypeId = ? AND qt.LanguageId IN (SELECT " +
                    "COALESCE((SELECT qt2.LanguageId FROM questtypelanguage qt2 WHERE qt2.LanguageId = (SELECT u.LanguageId FROM users u WHERE u.UserId = ?) " +
                    "AND qt2.QuestTypeId = qt.QuestTypeId), 1))");
            psSelectTypeDescription.setInt(1, questTypeId);
            psSelectTypeDescription.setInt(2, userId);
            ResultSet rsTypeDescription = psSelectTypeDescription.executeQuery();
            rsTypeDescription.next();

            return buildDescription(valuesById, rsTypeDescription.getString("Description"));

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }

    private String buildDescription(List<String> values, String description) {
        String result = description;
        for (int i = 0; i < values.size(); i++) {
            result = result.replace("{{{" + (i + 1) + "}}}", values.get(i));
        }
        return result;
    }

    @Override
    public Quest find(int id, User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT ql.EntryId, qtl.Name, qtl.Description, qt.Reward " +
                    ", ql.Removed, ql.Completed, ql.Description as DescriptionQL, qt.QuestTypeId, qp.Value " +
                    "FROM (questlog ql INNER JOIN QuestType qt ON ql.QuestTypeId = qt.QuestTypeId) " +
                    "INNER JOIN QuestTypeLanguage qtl ON qt.QuestTypeId = qtl.QuestTypeId INNER JOIN questproperties qp " +
                    "ON qp.EntryId = ql.EntryId WHERE UserId = ? And ql.EntryId = ?  AND Removed = 0 AND LanguageId IN " +
                    "(SELECT COALESCE((SELECT languageId FROM questtypelanguage qtl2 WHERE qtl2.LanguageId = ? AND qtl2.QuestTypeId = qt.QuestTypeId), 1)) " +
                    "ORDER BY PropertyId DESC;");
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
            PreparedStatement ps = connection.prepareStatement("SELECT ql.EntryId, qtl.Name, qtl.Description, qt.Reward " +
                    ", ql.Removed, ql.Completed, ql.Description as DescriptionQL, qt.QuestTypeId, qp.Value FROM (questlog ql INNER JOIN QuestType qt ON ql.QuestTypeId = qt.QuestTypeId) " +
                    "INNER JOIN QuestTypeLanguage qtl ON qt.QuestTypeId = qtl.QuestTypeId INNER JOIN questproperties qp ON qp.EntryId = ql.EntryId " +
                    "WHERE UserId = ? AND Completed = 0 AND LanguageId IN " +
                    "(SELECT COALESCE(" +
                    "(SELECT languageId FROM questtypelanguage qtl2 WHERE qtl2.LanguageId = ? AND qtl2.QuestTypeId = qt.QuestTypeId), 1)) " +
                    "ORDER BY PropertyId DESC;");
            ps.setInt(1, userId);
            ps.setInt(2, languageId);
            ResultSet rs = ps.executeQuery();
            return addQuestsToQuestList(connection, questList, rs);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
            return questList;
        }
    }

    private List<Quest> addQuestsToQuestList(Connection connection, List<Quest> questList, ResultSet rs) throws SQLException {
        int[] entryId = new int[4];
        int j = 1;
        boolean isUnique = true;
        while (rs.next()) {
            entryId[j] = rs.getInt("EntryId");
            for (int anEntryId : entryId) {
                isUnique = (anEntryId != entryId[j]);
            }
            if (isUnique) {
                questList.add(createQuest(rs, generateQuest(rs)));
            }
            j++;
        }
        if (!connection.isClosed()) {
            connection.close();
        }
        return questList;
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
                rs.getInt("EntryId"),
                rs.getString("Name"),
                rs.getString("Description"),
                rs.getInt("Reward"),
                rs.getInt("QuestTypeId"),
                rs.getInt("Removed"),
                rs.getInt("Completed"),
                typeStrategy
        );
        q.setQuestDescription(rs.getString("DescriptionQL"));
        return q;
    }

    private IQuestType generateQuest(ResultSet rs) throws SQLException {
        return QuestFactory.getInstance().generateQuest(QuestTypes.values()[rs.getInt("QuestTypeId") - 1], getVariablesOfQuest(rs.getInt("EntryId")));
    }
}