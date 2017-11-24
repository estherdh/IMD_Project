package oose.p.c6.imd.domain;

public class DummyQuest implements IQuestType {
    @Override
    public boolean checkQuestComplete(Action action) {
        return true;
    }
}
