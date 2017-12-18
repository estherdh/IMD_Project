package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;

import java.util.Map;

public class NotificationCreator {
    public static Notification createNotification(User u, String s, Map<String, String> m, int n, String date, Boolean read, int id){
        switch(n){
            case 1:
                return new Notification(id, date, new QuestCompletedNotificationStrategy().createNotificationText(u, s, m), read );

        }
        return null;
    }
}
