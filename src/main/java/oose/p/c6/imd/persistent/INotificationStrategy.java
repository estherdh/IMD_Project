package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.Notification;
import oose.p.c6.imd.domain.User;

import java.util.Map;

public interface INotificationStrategy {
    Notification createNotification(User u, String s, Map<String, String> properties);
}
