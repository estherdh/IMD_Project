package oose.p.c6.imd.domain;

public abstract class IQuestGenerator {
    protected int questTypeId;

    public abstract void generateQuest(int userId);


}
