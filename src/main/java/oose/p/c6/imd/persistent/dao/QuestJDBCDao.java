package oose.p.c6.imd.persistent.dao;

import com.sun.org.apache.xpath.internal.SourceTree;
import oose.p.c6.imd.domain.IQuestType;
import oose.p.c6.imd.domain.Quest;
import oose.p.c6.imd.domain.QuestFactory;
import oose.p.c6.imd.domain.QuestTypes;
import oose.p.c6.imd.persistent.ConnectMySQL;
import org.hamcrest.Description;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestJDBCDao implements IQuestDAO{
	public void add(Quest entity) {

	}

	public void update(Quest updatedEntity) {

	}

	public void remove(Quest entity) {

	}

	public List<Quest> list() {
		return null;
	}

	public Quest find(int id) {
		return null;
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
			System.out.println(ps.toString());
			ps.setInt(1, userId);
			ps.setInt(2, languageId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int questType = rs.getInt("QuestTypeId");
				IQuestType typeStrategy = factory.generateQuest(QuestTypes.values()[questType-1], getVariablesOfQuest(rs.getInt("EntryId")));
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
			e.printStackTrace();
			return null;
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
			e.printStackTrace();
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
			e.printStackTrace();
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
			return null;
		}
	}
}
