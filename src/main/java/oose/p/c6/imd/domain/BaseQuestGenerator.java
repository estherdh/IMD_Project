package oose.p.c6.imd.domain;

public abstract class BaseQuestGenerator {
    int questTypeId;

    public abstract void generateQuest(int userId);

}
