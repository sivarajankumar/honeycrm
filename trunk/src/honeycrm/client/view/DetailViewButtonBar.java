package honeycrm.client.view;

import honeycrm.client.dto.Dto;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class DetailViewButtonBar extends AbstractView {
	private Button saveBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");
	private Button editBtn = new Button("Edit");
	private Button deleteBtn = new Button("Delete");

	private final DetailView detailview;

	public DetailViewButtonBar(final Dto clazz, final DetailView detailview) {
		super(clazz);

		this.detailview = detailview;

		final HorizontalPanel panel = new HorizontalPanel();
		panel.add(saveBtn);
		panel.add(cancelBtn);
		panel.add(editBtn);
		panel.add(deleteBtn);

		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailview.saveChanges();
				// startViewing();
			}
		});

		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startViewing();
			}
		});

		editBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				startEditing();
			}
		});

		deleteBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// TODO should close detailview after delete..
				detailview.delete();
				startViewing();
			}
		});

		stopViewing();

		initWidget(panel);
	}

	/**
	 * displaying detail information for an entity
	 */
	public void startViewing() {
		detailview.view();

		// only show if detailview is displaying a valid item
		editBtn.setVisible(detailview.isShowing()); 
		deleteBtn.setVisible(detailview.isShowing());

		saveBtn.setVisible(false);
		cancelBtn.setVisible(false);
	}

	/**
	 * display all input fields for creating a new entity
	 */
	public void startCreating() {
		// if (detailview.isShowing()) {
		detailview.stopViewing();
		detailview.startCreating();

		saveBtn.setVisible(true);
		cancelBtn.setVisible(true);

		editBtn.setVisible(false);
		deleteBtn.setVisible(false);
		// }
	}

	public void startEditing() {
		if (detailview.isShowing()) {
			detailview.startEditing();

			saveBtn.setVisible(true);
			cancelBtn.setVisible(true);

			editBtn.setVisible(false);
			deleteBtn.setVisible(false);
		}
	}

	public void stopViewing() {
		saveBtn.setVisible(false);
		cancelBtn.setVisible(false);
		editBtn.setVisible(false);
		deleteBtn.setVisible(false);
	}
}
