package oose.p.c6.imd.domain;

public abstract class IQuestGenerator {
    int questTypeId;
    int chanceRemovedQuest = 5;
    int chanceNotRemovedQuest = 95;
    int chanceQuest = 100;

    public abstract void generateQuest(int userId);


}
