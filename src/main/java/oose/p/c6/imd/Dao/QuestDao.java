package oose.p.c6.imd.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class QuestDao {
	private Connection conn = ConnectMySQL.getConnection();

	public boolean removeQuest(int entryId, int userId) {
		try {
			PreparedStatement query = conn.prepareStatement("DELETE FROM QuestLog WHERE EntryId = ? AND UserId = ?;");
			query.setInt(1, entryId);
			query.setInt(2, userId);
			query.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
