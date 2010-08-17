package honeycrm.server.domain;

import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldDateAnnotation;
import honeycrm.server.domain.decoration.fields.FieldIntegerAnnotation;
import honeycrm.server.domain.decoration.fields.FieldRelateAnnotation;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
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
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	@SearchableId
	// NOTE:
	// To allow full text search with compass / lucene the id field has to be of type long.
	// Additionally we have to use Long (not long) because of constrains of google app engine for
	// allowed id types.
	protected Key id;

	@Label("Assigned To")
	@FieldRelateAnnotation(Employee.class)
	protected long assignedTo;
	
	// TODO mark field has to be supported differently with mark FieldMark instance
	@Label("Marked")
	@FieldBooleanAnnotation
	protected boolean marked;

	@Label("Created at")
	@FieldDateAnnotation
	protected Date createdAt;

	@Label("Last updated at")
	@FieldDateAnnotation
	protected Date lastUpdatedAt;
	
	@Label("Views")
	@FieldIntegerAnnotation(0)
	protected long views;

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

	public long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(long assignedTo) {
		this.assignedTo = assignedTo;
	}
}
