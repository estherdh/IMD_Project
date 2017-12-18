package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;

import java.util.Map;

public class QuestCompletedNotificationStrategy implements INotificationStrategy{
    @Override
    public Notification createNotification(User u, String s, Map<String, String> properties) {
        return null;
    }
}
