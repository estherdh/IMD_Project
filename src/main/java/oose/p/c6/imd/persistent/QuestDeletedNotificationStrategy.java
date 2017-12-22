package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Quest;
import oose.p.c6.imd.domain.User;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import oose.p.c6.imd.persistent.dao.IQuestDAO;

import java.util.Map;

public class QuestDeletedNotificationStrategy implements INotificationStrategy{
    private IQuestDAO questDAO = DAOFactory.getQuestDao();
    @Override
    public String createNotificationText(User u, String s, Map<String, String> properties) {
        int questId = Integer.parseInt(properties.get("QuestId"));
        Quest completed = questDAO.find(questId, u);
        String result = s;
        result = result.replace("{{{1}}}", completed.getName());
        return result;
    }
}
