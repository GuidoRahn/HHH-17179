/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.bugs;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.AuditReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * This template demonstrates how to develop a test case for Hibernate Envers, using
 * its built-in unit test framework.
 */
public class EnversUnitTestCase extends AbstractEnversTestCase {

	public static final String OBJECT_ID = "34234234";

	// Add your entities here.
	@Override
	protected Class[] getAnnotatedClasses() {
		return new Class[] {
				MesswertSammlungEntity.class,
				MesswertEmbeddable.class
		};
	}

	// If you use *.hbm.xml mappings, instead of annotations, add the mappings here.
	@Override
	protected String[] getMappings() {
		return new String[] {
//				"Foo.hbm.xml",
//				"Bar.hbm.xml"
		};
	}
	// If those mappings reside somewhere other than resources/org/hibernate/test, change this.
	@Override
	protected String getBaseForMappings() {
		return "org/hibernate/test/";
	}

	// Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
	@Override
	protected void configure(Configuration configuration) {
		super.configure( configuration );

		configuration.setProperty( AvailableSettings.SHOW_SQL, Boolean.TRUE.toString() );
		configuration.setProperty( AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString() );
		//configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
	}

	// Add your tests, using standard JUnit.
	@Test
	public void hhh17179Test()  {
		AuditReader reader = getAuditReader();

		Session session = openSession();
		Transaction tx = session.beginTransaction();
		MesswertSammlungEntity main = new MesswertSammlungEntity();
		main.setObjectId(OBJECT_ID);
		MesswertEmbeddable embeddable = new MesswertEmbeddable();
		embeddable.setMesswert(BigDecimal.valueOf(334345));
		main.getMesswerte().put(1, embeddable);
		session.persist(main);
		session.flush();
		tx.commit();
		session.clear();
		Transaction newTx = session.beginTransaction();
		List<MesswertSammlungEntity> result = session.createQuery(
				"select m from MesswertSammlungEntity m",
				MesswertSammlungEntity.class).getResultList();
		MesswertSammlungEntity messwertSammlungEntity = result.get(0);
		assert (messwertSammlungEntity.getObjectId().getId().equals(OBJECT_ID));
		MesswertEmbeddable messwertEmbeddable = messwertSammlungEntity.getMesswerte().get(1);
		messwertEmbeddable.setMesswert(BigDecimal.valueOf(44));
		messwertSammlungEntity.getMesswerte().clear();
		messwertSammlungEntity.getMesswerte().put(2, messwertEmbeddable);
		session.flush();
		newTx.commit();
		session.close();
	}
}
