package crm.server;

import java.util.Set;

import crm.client.CommonService;
import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;
import crm.client.dto.Viewable;

public class CommonServiceImpl extends AbstractCommonService implements CommonService {
	private static final long serialVersionUID = -7312945910083902842L;

	private static final CommonServiceCreator creator = new CommonServiceCreator();
	private static final CommonServiceReader reader = new CommonServiceReader();
	
	@Override
	public void create(int dtoIndex, Viewable dto) {
		creator.create(dtoIndex, dto);
	}

	@Override
	public void addDemo(int dtoIndex) {
		creator.addDemo(dtoIndex);
	}
	
	@Override
	public ListQueryResult<? extends Viewable> getAll(final int dtoIndex, int from, int to) {
		return reader.getAll(dtoIndex, from, to);
	}
	
	@Override
	public Viewable get(final int dtoIndex, final long id) {
		return reader.get(dtoIndex, id);
	}
	
	@Override
	public ListQueryResult<? extends Viewable> search(int dtoIndex, Viewable searchDto, int from, int to) {
		return reader.search(dtoIndex, searchDto, from, to);
	}

	@Override
	public ListQueryResult<? extends Viewable> getAllByNamePrefix(int dtoIndex, String prefix, int from, int to) {
		return reader.getAllByNamePrefix(dtoIndex, prefix, from, to);
	}

	@Override
	public Viewable getByName(int dtoIndex, String name) {
		return reader.getByName(dtoIndex, name);
	}

	// create classes for delete and update operations when code grows
	// TODO does not update the relate field anymore
	@Override
	public void update(int dtoIndex, Viewable dto, long id) {
		final Object existingObject = getDomainObject(dtoIndex, id);

		if (null != existingObject) {
			m.makePersistent(copy.getUpdatedInstance(dto, existingObject));
		}		
	}

	@Override
	public void delete(int dtoIndex, long id) {
		final Object object = getDomainObject(dtoIndex, id);
		if (null != object) {
			m.deletePersistent(object);
		}
	}

	@Override
	public void deleteAll(int dtoIndex, Set<Long> ids) {
		for (final Long id: ids) {
			delete(dtoIndex, id);
		}
	}

	@Override
	public ListQueryResult<? extends Viewable> fulltextSearch(String query, int from, int to) {
		return reader.fulltextSearch(query, from, to);
	}

	@Override
	public void mark(int dtoIndex, long id, boolean marked) {
		Viewable viewable = get(dtoIndex, id);
		viewable.setFieldValue(AbstractDto.INDEX_MARKED, marked);
		update(dtoIndex, viewable, id);
	}

	@Override
	public ListQueryResult<? extends Viewable> getAllMarked(int dtoIndex, int from, int to) {
		return reader.getAllMarked(dtoIndex, from, to);
	}
}
