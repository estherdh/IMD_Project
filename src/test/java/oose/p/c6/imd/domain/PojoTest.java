package oose.p.c6.imd.domain;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.affirm.Affirm;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;
import oose.p.c6.imd.persistent.dao.DAOFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PojoTest {
	List<PojoClass> pojoList;

	@Before
	public void setUp() {
		pojoList = new ArrayList<>();
		pojoList.add(PojoClassFactory.getPojoClass(Replica.class));
		pojoList.add(PojoClassFactory.getPojoClass(User.class));
		pojoList.add(PojoClassFactory.getPojoClass(Era.class));
		pojoList.add(PojoClassFactory.getPojoClass(Exhibit.class));
		pojoList.add(PojoClassFactory.getPojoClass(Model.class));
		pojoList.add(PojoClassFactory.getPojoClass(Museum.class));
		pojoList.add(PojoClassFactory.getPojoClass(QrScanAction.class));
		pojoList.add(PojoClassFactory.getPojoClass(Quest.class));
		pojoList.add(PojoClassFactory.getPojoClass(Notification.class));
	}

	@Test
	public void testPojoStructureAndBehavior() {
		Validator validator = ValidatorBuilder.create()
				// Add Testers to validate behaviour for POJO_PACKAGE
				// See com.openpojo.validation.test.impl for more ...
				.with(new NoPublicFieldsRule())
				.with(new SetterTester())
				.with(new GetterTester())
				.build();

		validator.validate(pojoList);
	}
}
