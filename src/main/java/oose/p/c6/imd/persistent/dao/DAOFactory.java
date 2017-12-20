package oose.p.c6.imd.persistent.dao;

public class DAOFactory {
    private DAOFactory(){
        throw new IllegalStateException("DAOFactory class");
    }

    private static IUserDao userDao = new UserJDBCDao();
    private static IReplicaDao replicaDao = new ReplicaJDBCDao();
    private static IExhibitDao exhibitDao = new ExhibitJDBCDao();
    private static IQuestDAO questDao = new QuestJDBCDao();

    public static IReplicaDao getReplicaDao() {
        return new ReplicaJDBCDao();
    }

    public static IUserDao getUserDao() {
        return userDao;
    }

    public static IQuestDAO getQuestDao(){
        return questDao;
    }

    public static void setQuestDao(IQuestDAO dao){
        questDao = dao;
    }


    public static IExhibitDao getExhibitDao() {
        return exhibitDao;
    }

    public static void setExhibitDao(IExhibitDao ed) {
        exhibitDao = ed;
    }

    public static void setUserDao(IUserDao dao) {
        userDao = dao;
    }
}