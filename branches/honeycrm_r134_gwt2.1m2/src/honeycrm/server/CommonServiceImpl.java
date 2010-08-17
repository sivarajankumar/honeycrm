package honeycrm.server;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.profiling.ServiceCallStatistics;
import honeycrm.client.services.CommonService;
import honeycrm.server.domain.AbstractEntity;
import honeycrm.server.profiling.ProfilingStatisticsCollector;
import honeycrm.server.profiling.ReadTest;
import honeycrm.server.profiling.ServiceCall;
import honeycrm.server.transfer.DtoWizard;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Is somewhat the business layer.
 */
public class CommonServiceImpl extends AbstractCommonService implements CommonService {
	private static final Logger log = Logger.getLogger(CommonServiceImpl.class.getName());
	private static final long serialVersionUID = -7312945910083902842L;
	private static final CommonServiceCreator creator = new CommonServiceCreator();
	private static final CommonServiceReader reader = new CommonServiceReader();
	private static final CommonServiceReaderFulltext fulltext = new CommonServiceReaderFulltext();
	private static final CommonServiceEmail email = new CommonServiceEmail();
	private static final CommonServiceReporter reporter = new CommonServiceReporter();
	private static final DtoWizard wizard = DtoWizard.instance;
	private static final ProfilingStatisticsCollector profiler = new ProfilingStatisticsCollector();

	@Override
	public long create(Dto dto) {
		return creator.create(dto);
	}

	@Override
	public void addDemo(String dtoIndex) {
		creator.addDemo(dtoIndex);
	}

	@Override
	public ListQueryResult getAll(final String dtoIndex, int from, int to) {
		ServiceCall call = new ServiceCall("getAll");

		// TODO using transactions currently breaks getAll()
		// TODO do everything within the context of a transaction
		// final Transaction t = m.currentTransaction();
		// try {
		// t.begin();
		final ListQueryResult result = reader.getAll(dtoIndex, from, to);
		// t.commit();

		endAndPersist(call);

		return result;
		// } finally {
		// if (t.isActive()) {
		// t.rollback();
		// }
		// }
	}

	@Override
	public Dto get(final String dtoIndex, final long id) {
		ServiceCall call = new ServiceCall("get");

		final Dto dto = reader.get(dtoIndex, id);

		endAndPersist(call);

		if (null == dto) {
			return null; // do not do anything more than that because we could not find the dto
		}

		// TODO in when the name of an relate field is determined this get() method is called
		// TODO this increases the number of views for the related entity every time it is
		// displayed..
		// TODO this should be avoided.
		// increase number of views on every get request for a specific DTO

		// TODO do this asynchronous since it is currently to slow if we do it synchronously
		// dto.setViews(dto.getViews() + 1);
		// update(dtoIndex, dto, id);
		return dto;
	}

	private void endAndPersist(ServiceCall call) {
		if (ServiceCallStatistics.PROFILING_ENABLED) {
			m.makePersistent(call.end());
		}
	}

	@Override
	public ListQueryResult search(String dtoIndex, Dto searchDto, int from, int to) {
		return reader.search(dtoIndex, searchDto, from, to);
	}

	@Override
	public ListQueryResult getAllByNamePrefix(String dtoIndex, String prefix, int from, int to) {
		log.fine("getAllByNamePrefix(" + dtoIndex + "," + prefix + ")");
		return reader.getAllByNamePrefix(dtoIndex, prefix, from, to);
	}

	@Override
	public Dto getByName(String dtoIndex, String name) {
		return reader.getByName(dtoIndex, name);
	}

	// create classes for delete and update operations when code grows
	// TODO does not update the relate field anymore
	@Override
	public void update(Dto dto, long id) {
		ServiceCall call = new ServiceCall("update");

		dto.set("lastUpdatedAt", (new Date(System.currentTimeMillis())));

		final AbstractEntity existingObject = getDomainObject(dto.getModule(), id);

		if (null != existingObject) {
			m.makePersistent(copy.copy(dto, existingObject));
		}

		endAndPersist(call);
	}

	@Override
	public void delete(String dtoIndex, long id) {
		final Object object = getDomainObject(dtoIndex, id);
		if (null != object) {
			m.deletePersistent(object);
		}
	}

	@Override
	public void deleteAll(String dtoIndex, Set<Long> ids) {
		for (final Long id : ids) {
			delete(dtoIndex, id);
		}
	}

	@Override
	public ListQueryResult fulltextSearch(String query, int from, int to) {
		return fulltext.fulltextSearch(query, from, to);
	}

	@Override
	public void mark(String dtoIndex, long id, boolean marked) {
		Dto viewable = get(dtoIndex, id);
		viewable.setMarked(marked);
		update(viewable, id);
	}

	@Override
	public ListQueryResult getAllMarked(String dtoIndex, int from, int to) {
		return reader.getAllMarked(dtoIndex, from, to);
	}

	@Override
	public void deleteAll(String dtoIndex) {
		final Query q = m.newQuery(getDomainClass(dtoIndex));

		// delete step by step instead to avoid "can only delete 500 entities en block" errors in app engine.
		for (final AbstractEntity entity : (Collection<AbstractEntity>) q.execute()) {
			m.deletePersistent(entity);
		}

		// m.deletePersistentAll((Collection) q.execute());
	}

	@Override
	public void deleteAllItems() {
		log.info("Deleting all items..");
		for (final Class<? extends AbstractEntity> entityClass : registry.getDomainClasses()) {
			try {
				m.deletePersistentAll((Collection) m.newQuery(entityClass).execute());
				/*
				 * for (final AbstractEntity entity : ) { m.deletePersistent(entity); }
				 */
			} catch (NullPointerException e) {
				log.warning("A NullPointerException occured during the deletion of all entities");
				e.printStackTrace();
			}
		}
		log.info("Deleting all items done.");
	}

	@Override
	public ListQueryResult fulltextSearchForModule(String dtoIndex, String query, int from, int to) {
		return fulltext.fulltextSearchForModule(dtoIndex, query, from, to);
	}

	@Override
	public void importCSV(final String module, List<Dto> dtos) {
		log.info("Starting importing " + dtos.size() + " " + module + "(s)");

		int done = 0;
		for (final Dto dto : dtos) {
			create(dto);
			log.fine("Created " + (++done) + " " + module + "(s)");
		}

		log.fine("Finished importing " + dtos.size() + " " + module + "(s)");
	}

	@Override
	public void feedback(String message) {
		email.feedback(message);
	}

	@Override
	public Map<Integer, Double> getAnnuallyOfferingVolumes() {
		return reporter.getAnnuallyOfferingVolumes();
	}

	@Override
	public Map<String, ModuleDto> getDtoConfiguration() {
		return wizard.getDtoConfiguration();
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

	@Override
	public Map<String, Map<String, Set<String>>> getRelationships() {
		return RelationshipFieldTable.instance.getMap();
	}

	@Override
	public Map<String, ListQueryResult> getAllRelated(Long id, String relatedDtoIndex) {
		return reader.getAllRelated(id, relatedDtoIndex);
	}

	@Override
	public ListQueryResult getAllAssignedTo(String dtoIndex, long employeeID, int from, int to) {
		return reader.getAllAssignedTo(dtoIndex, employeeID, from, to);
	}
}
