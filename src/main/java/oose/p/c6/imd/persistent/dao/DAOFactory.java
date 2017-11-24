package oose.p.c6.imd.persistent.dao;

public class DAOFactory {
    public static IQuestDAO getQuestDao(){
        return new QuestJDBCDao();
    }
}
