package oose.p.c6.imd.persistent.dao;

import oose.p.c6.imd.domain.Replica;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.ConnectMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReplicaJDBCDao implements IReplicaDao {
    private static final Logger LOGGER = Logger.getLogger(ReplicaJDBCDao.class.getName());

    @Override
    public List<Replica> findAvailableReplicas(User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<Replica> replicas = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `replica`" +
                    " WHERE `ReplicaId` NOT IN (SELECT `ReplicaId` FROM `userreplica` WHERE `UserId` = ?)");
            ps.setInt(1, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                replicas.add(createReplica(rs));
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return replicas;
    }

    @Override
    public void giveReplicaToUser(User user, Replica replica) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `userreplica` (`UserId`, `ReplicaId`) VALUES (?, ?)");
            ps.setInt(1, user.getId());
            ps.setInt(2, replica.getId());
            ps.execute();
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    @Override
    public void updateReplicaPosition(User user, Replica replica, int position) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement("UPDATE `userreplica` SET `ReplicaPositionId` = ? WHERE `ReplicaId` = ? AND `UserId` = ?");
            ps.setInt(1, position);
            ps.setInt(2, replica.getId());
            ps.setInt(3, user.getId());
            ps.execute();
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
    }

    @Override
    public List<Integer> getFreePositions(User user, int replicaType) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<Integer> positions = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT `ReplicaPositionId` FROM replicapositions " +
                    "WHERE `ReplicaTypeId` = ? " +
                    "AND `ReplicaPositionId` NOT IN " +
                    "(SELECT `ReplicaPositionId` " +
                            "FROM userreplica " +
                            "WHERE `UserId` = ? " +
                            "AND `ReplicaPositionId` IS NOT NULL)");
            ps.setInt(1, replicaType);
            ps.setInt(2, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                positions.add(rs.getInt("ReplicaPositionId"));
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return positions;
    }

    @Override
    public List<Replica> getReplicasFromUser(User user) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<Replica> replicas = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT r.*, ei.Name, e.Year, erlan.Name as era, ur.ReplicaPositionId " +
                    "FROM `userreplica` ur " +
                    "INNER JOIN `replica` r ON r.ReplicaId=ur.ReplicaId " +
                    "INNER JOIN `exhibit` e ON e.ExhibitId=r.ExhibitId " +
                    "INNER JOIN `exhibitinfo` ei ON ei.ExhibitId=e.ExhibitId " +
                    "INNER JOIN `eralanguage` erlan ON erlan.EraId=e.EraId " +
                    "WHERE `UserId` = ? AND erlan.`LanguageId` = (SELECT `LanguageId` FROM `users` WHERE `UserId` = ?) " +
                    "AND ei.`LanguageId` = (SELECT `LanguageId` FROM `users` WHERE `UserId` = ?)");
            ps.setInt(1, user.getId());
            ps.setInt(2, user.getId());
            ps.setInt(3, user.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Replica replica = new Replica(rs.getInt("ReplicaId"), rs.getInt("ExhibitId"), rs.getInt("Price"), rs.getString("Sprite"), rs.getInt("ReplicaTypeId"), rs.getInt("ReplicaPositionId"), rs.getInt("Year"), rs.getString("Name"), rs.getString("era"));
                replicas.add(replica);
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return replicas;
    }

    @Override
    public void add(Replica entity) {
        //Not yet implemented
    }

    @Override
    public void update(Replica updatedEntity) {
        //Not yet implemented
    }

    @Override
    public void remove(Replica entity) {
            //Not yet implemented
    }

    @Override
    public List<Replica> list() {
        return new ArrayList<Replica>();
    }

    public Replica find(int id) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `replica` WHERE `ReplicaId` = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                return createReplica(rs);
            }
            connection.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.toString(), e);
        }
        return null;
    }

    private Replica createReplica(ResultSet rs) throws SQLException {
        return new Replica(rs.getInt("ReplicaId"), rs.getInt("ExhibitId"), rs.getInt("Price"), rs.getString("Sprite"), rs.getInt("ReplicaTypeId"));
    }
}
