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
    public List<Integer> getPositionsForReplicaType(int replicaType) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        List<Integer> positions = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT `ReplicaPositionId` FROM replicapositions WHERE `ReplicaTypeId` = ?");
            ps.setInt(1, replicaType);
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
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `userreplica` ur " +
                    "INNER JOIN `replica` r ON r.ReplicaId=ur.ReplicaId " +
                    "WHERE `UserId` = ?");
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
        return new Replica(rs.getInt("ReplicaId"), rs.getInt("ExhibitInfoId"), rs.getInt("Price"), rs.getString("Sprite"), rs.getInt("ReplicaTypeId"));
    }
}
