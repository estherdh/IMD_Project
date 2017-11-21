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

public class QuestJDBCDao implements IDao<Quest>{

	public boolean removeQuest(int entryId, int userId) {
		return false;
	}

	public void add(Quest entity) {

	}

	public void update(Quest updatedEntity) {

	}

	public void remove(Quest entity) {
//		try {
//			PreparedStatement query = conn.prepareStatement("DELETE FROM QuestLog WHERE EntryId = ? AND UserId = ?;");
//			query.setInt(1, entryId);
//			query.setInt(2, userId);
//			query.executeUpdate();
//			return true;
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
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
					"WHERE UserId = ?" +
					"AND Completed = 0 " +
					"AND LanguageId IN" +
					"    (SELECT COALESCE(" +
					"        (SELECT languageId FROM questtypelanguage WHERE LanguageId = ?)" +
					"        , 1)" +
					"    );");
			ps.setInt(1, userId);
			ps.setInt(2, languageId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int questType = rs.getInt("ql.QuestTypeId");
				IQuestType typeStrategy = factory.generateQuest(QuestTypes.values()[questType-1], getVariablesOfQuest(questType));
				questList.add(new Quest(
						rs.getString("Name"),
						rs.getString("Description"),
						rs.getInt("Reward"),
						typeStrategy
				));
			}
			connection.close();
			return questList;
		} catch (SQLException e) {
			return null;
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
			connection.close();
			return propertyList;
		} catch (SQLException e) {
			return null;
		}
	}
}
