package honeycrm.server.domain;

import honeycrm.server.domain.decoration.DetailViewable;
import honeycrm.server.domain.decoration.Label;
import honeycrm.server.domain.decoration.ListViewable;
import honeycrm.server.domain.decoration.Quicksearchable;
import honeycrm.server.domain.decoration.fields.FieldBooleanAnnotation;
import honeycrm.server.domain.decoration.fields.FieldCurrencyAnnotation;
import honeycrm.server.domain.decoration.fields.FieldStringAnnotation;
import honeycrm.server.domain.decoration.fields.FieldTextAnnotation;

import javax.jdo.annotations.PersistenceCapable;

import org.compass.annotations.Searchable;

@PersistenceCapable
@Searchable
@ListViewable( { "name", "price" })
@DetailViewable( { "name", "price", "published", "description" })
@Quicksearchable( { "name" })
public class Product extends AbstractEntity {
	@FieldStringAnnotation
	@Label("Name")
	private String name;

	@FieldCurrencyAnnotation("0")
	@Label("Price")
	private double price;

	@FieldTextAnnotation
	@Label("Description")
	private String description;

	@FieldBooleanAnnotation
	@Label("Published")
	private boolean published;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(final double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(final boolean published) {
		this.published = published;
	}
}
