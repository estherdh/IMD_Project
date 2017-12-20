package oose.p.c6.imd.domain;

import java.util.Map;

public abstract class BaseQuestGenerator {
    int questTypeId;

    public abstract void generateQuest(int userId);
    public abstract void addQuestToQuestlog(Map<String, String> properties, int userId);

}
