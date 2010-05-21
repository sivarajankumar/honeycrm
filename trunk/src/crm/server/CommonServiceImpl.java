package crm.server;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.Query;

import crm.client.CommonService;
import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;
import crm.server.domain.AbstractEntity;

/**
 * Is somewhat the business layer.
 */
public class CommonServiceImpl extends AbstractCommonService implements CommonService {
	private static final Logger log = Logger.getLogger(CommonServiceImpl.class.getName());
	private static final long serialVersionUID = -7312945910083902842L;
	private static final CommonServiceCreator creator = new CommonServiceCreator();
	private static final CommonServiceReader reader = new CommonServiceReader();

	@Override
	public void create(int dtoIndex, AbstractDto dto) {
		creator.create(dtoIndex, dto);
	}

	@Override
	public void addDemo(int dtoIndex) {
		creator.addDemo(dtoIndex);
	}

	@Override
	public ListQueryResult<? extends AbstractDto> getAll(final int dtoIndex, int from, int to) {
		return reader.getAll(dtoIndex, from, to);
	}

	@Override
	public AbstractDto get(final int dtoIndex, final long id) {
		final AbstractDto dto = reader.get(dtoIndex, id);

		if (null == dto) {
			return null; // do not do anything more than that because we could not find the dto
		}

		// TODO in when the name of an relate field is determined this get() method is called
		// TODO this increases the number of views for the related entity every time it is displayed..
		// TOD this should be avoided.
		// increase number of views on every get request for a specific DTO
		dto.setViews(dto.getViews() + 1);
		update(dtoIndex, dto, id);
		return dto;
	}

	@Override
	public ListQueryResult<? extends AbstractDto> search(int dtoIndex, AbstractDto searchDto, int from, int to) {
		return reader.search(dtoIndex, searchDto, from, to);
	}

	@Override
	public ListQueryResult<? extends AbstractDto> getAllByNamePrefix(int dtoIndex, String prefix, int from, int to) {
		log.fine("getAllByNamePrefix("+dtoIndex+","+prefix+")");
		return reader.getAllByNamePrefix(dtoIndex, prefix, from, to);
	}

	@Override
	public AbstractDto getByName(int dtoIndex, String name) {
		return reader.getByName(dtoIndex, name);
	}

	// create classes for delete and update operations when code grows
	// TODO does not update the relate field anymore
	@Override
	public void update(int dtoIndex, AbstractDto dto, long id) {
		dto.setLastUpdatedAt(new Date(System.currentTimeMillis()));

		final AbstractEntity existingObject = (AbstractEntity) getDomainObject(dtoIndex, id);

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
		for (final Long id : ids) {
			delete(dtoIndex, id);
		}
	}

	@Override
	public ListQueryResult<? extends AbstractDto> fulltextSearch(String query, int from, int to) {
		return reader.fulltextSearch(query, from, to);
	}

	@Override
	public void mark(int dtoIndex, long id, boolean marked) {
		AbstractDto viewable = get(dtoIndex, id);
		viewable.setFieldValue(AbstractDto.INDEX_MARKED, marked);
		update(dtoIndex, viewable, id);
	}

	@Override
	public ListQueryResult<? extends AbstractDto> getAllMarked(int dtoIndex, int from, int to) {
		return reader.getAllMarked(dtoIndex, from, to);
	}

	@Override
	public void deleteAll(int dtoIndex) {
		final Query q = m.newQuery(getDomainClass(dtoIndex));
		final Collection collection = (Collection) q.execute();
		m.deletePersistentAll(collection);
	}
}
