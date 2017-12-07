package oose.p.c6.imd.domain;

import oose.p.c6.imd.persistent.dao.IExhibitDao;
import oose.p.c6.imd.persistent.dao.IQuestDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Random;


public class ExhibitViewQuestGeneratorTest {

    @Mock
    IQuestDAO questDAO;
    IExhibitDao exhibitDao;

    @InjectMocks
    ExhibitViewQuestGenerator generator;

    @Test
    public void testGenerateQuestProperties() {
        //init
        HashMap<String, String> propertiesExpected = new HashMap<>();
        HashMap<String, String> propertiesActual = new HashMap<>();

        Random r = new Random(exhibitDao.findExhibitsNotYetInQuestlog(userId).size());
        Exhibit e = exhibitDao.findExhibitsNotYetInQuestlog(userId).get(r.nextInt());

        //test
        propertiesActual.put("Key", "1");
        propertiesActual.put("Value", "2");
        propertiesActual.put("ExhibitId", String.valueOf(3));

        propertiesExpected.put("Key", "1");
        propertiesExpected.put("Value", "2");
        propertiesExpected.put("ExhibitId", String.valueOf(3));

        //check
        }
}
