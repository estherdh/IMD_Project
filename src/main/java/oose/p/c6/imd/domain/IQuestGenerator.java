package oose.p.c6.imd.domain;

public abstract class IQuestGenerator {
    private int questTypeId;

    protected IQuestGenerator(int questTypeId) {
        this.questTypeId = questTypeId;
    }

    public abstract void generateQuest(int userId);
}
