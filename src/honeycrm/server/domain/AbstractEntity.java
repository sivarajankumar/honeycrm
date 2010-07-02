package honeycrm.server.domain;

import honeycrm.client.field.AbstractField;
import honeycrm.client.field.FieldDate;
import honeycrm.client.field.FieldInteger;
import honeycrm.client.field.FieldMark;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.compass.annotations.Searchable;
import org.compass.annotations.SearchableId;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
@Searchable
abstract public class AbstractEntity {
	@NotPersistent
	protected Set<AbstractField> fields = new HashSet<AbstractField>();

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	@SearchableId
	// NOTE:
	// To allow full text search with compass / lucene the id field has to be of type long.
	// Additionally we have to use Long (not long) because of constrains of google app engine for
	// allowed id types.
	protected Key id;
	@Persistent
	protected boolean marked;
	@Persistent
	protected Date createdAt;
	@Persistent
	protected Date lastUpdatedAt;
	@Persistent
	protected long views;

	public AbstractEntity() {
		fields.add(new FieldInteger("views", "Views"));
		fields.add(new FieldDate("createdAt", "Created at"));
		fields.add(new FieldDate("lastUpdatedAt", "Last updated at"));
		fields.add(new FieldMark("marked", "Marked", this.getClass().getSimpleName().toLowerCase(), null == id ? -1 : id.getId()));
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public Set<AbstractField> getFields() {
		return fields;
	}
}
