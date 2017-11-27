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

public class ReplicaJDBCDao implements IReplicaDao {
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
                replicas.add(new Replica(rs.getInt("ReplicaId"), rs.getInt("ExhibitInfoId"), rs.getInt("Price"), rs.getString("Sprite"), rs.getString("Type"), rs.getInt("Position")));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @Override
    public void add(Replica entity) {

    }

    @Override
    public void update(Replica updatedEntity) {

    }

    @Override
    public void remove(Replica entity) {

    }

    @Override
    public List<Replica> list() {
        return null;
    }

    public Replica find(int id) {
        Connection connection = ConnectMySQL.getInstance().getConnection();
        ResultSet rs = null;
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `replica` WHERE `ReplicaId` = ? ");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if(rs.next()){
                return new Replica(rs.getInt("ReplicaId"), rs.getInt("ExhibitInfoId"), rs.getInt("Price"), rs.getString("Sprite"), rs.getString("Type"), rs.getInt("Position"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}