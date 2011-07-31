package honeycrm.server;

import honeycrm.client.misc.ServiceCallStatistics;
import honeycrm.client.services.CommonService;
import honeycrm.server.profiling.ProfilingStatisticsCollector;
import honeycrm.server.profiling.ReadTest;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Is somewhat the business layer.
 */
@Deprecated
public class CommonServiceImpl implements CommonService {
	private static final Logger log = Logger.getLogger(CommonServiceImpl.class.getName());
	private static final long serialVersionUID = -7312945910083902842L;
	private static final CommonServiceEmail email = new CommonServiceEmail();
	private static final ProfilingStatisticsCollector profiler = new ProfilingStatisticsCollector();
	
	@Override
	public void feedback(String message) {
		email.feedback(message);
	}

	@Override
	public Collection<ServiceCallStatistics> getServiceCallStatistics() {
		return profiler.get();
	}

	@Override
	public void bulkCreate() {
		log.warning("Entered bulkCreate()");
		final PersistenceManager m = PMF.get().getPersistenceManager();
		final int count = 500; // app engine restriction

		Collection<ReadTest> reads = new HashSet<ReadTest>();
		final Random r = new Random();

		for (int i = 0; i < count; i++) {
			final ReadTest t = new ReadTest();
			t.setFoo(r.nextLong());
			reads.add(t);
		}

		log.warning("Started creation of " + count + " values");
		final long before = System.currentTimeMillis();
		m.makePersistentAll(reads);
		log.warning("Creation of " + count + " values took " + (System.currentTimeMillis() - before) + " ms.");
	}

	@Override
	public void bulkRead() {
		final PersistenceManager m = PMF.get().getPersistenceManager();

		final long before = System.currentTimeMillis();
		log.warning("Started full table scan");
		System.out.flush();
		final Query q = m.newQuery(ReadTest.class);
		q.setRange(1, 1000);
		final Collection<ReadTest> result = (Collection<ReadTest>) q.execute();

		log.warning("Finished full table scan: Read items in " + (System.currentTimeMillis() - before) + " ms.");
	}
}
