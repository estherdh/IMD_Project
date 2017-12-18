package oose.p.c6.imd.persistent;

import oose.p.c6.imd.domain.User;

import java.util.Map;

public interface INotificationStrategy {
    String createNotificationText(User u, String s, Map<String, String> properties);
}
