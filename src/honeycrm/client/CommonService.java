package honeycrm.client;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.dto.ModuleDto;
import honeycrm.client.profiling.ServiceCallStatistics;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("common")
public interface CommonService extends RemoteService {
	// create
	public long create(Dto viewable);

	// read
	public Dto get(String dtoIndex, long id);

	public Dto getByName(String dtoIndex, String name);

	public ListQueryResult getAll(final String dtoIndex, int from, int to);

	public ListQueryResult getAllMarked(final String dtoIndex, int from, int to);

	public ListQueryResult getAllByNamePrefix(final String dtoIndex, String prefix, int from, int to);

	public ListQueryResult search(String dtoIndex, Dto searchContact, int from, int to);

	public ListQueryResult fulltextSearch(String query, int from, int to);

	public ListQueryResult getAllRelated(final String originatingDtoIndex, final Long id, final String relatedDtoIndex);

	public ListQueryResult fulltextSearchForModule(final String dtoIndex, String query, int from, int to);

	// update
	public void update(Dto account, long id);

	public void mark(String dtoIndex, long id, boolean marked);

	// delete
	public void delete(String dtoIndex, long id);

	public void deleteAll(String dtoIndex, Set<Long> ids);

	public void deleteAll(String dtoIndex);

	public void deleteAllItems();

	public void addDemo(String dtoIndex);

	// misc
	public void wakeupServer();

	public Map<String, ModuleDto> getDtoConfiguration();

	// import operations
	public void importCSV(final String module, final List<Dto> dtos);

	// email
	public void feedback(final String message);

	// reports
	public Map<Integer, Double> getAnnuallyOfferingVolumes();
	
	// profiling
	public Collection<ServiceCallStatistics> getServiceCallStatistics();
}
