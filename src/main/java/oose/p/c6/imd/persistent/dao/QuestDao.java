package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Quest;
import oose.p.c6.imd.persistent.ConnectMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QuestDao implements IDao<Quest>{
	private Connection conn = new ConnectMySQL().getConnection();

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
}
