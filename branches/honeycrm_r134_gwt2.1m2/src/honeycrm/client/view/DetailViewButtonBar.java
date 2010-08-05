package honeycrm.client.view;

import honeycrm.client.dto.ExtraButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;

public class DetailViewButtonBar extends AbstractView {
	private Button saveBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");
	private Button editBtn = new Button("Edit");
	private Button deleteBtn = new Button("Delete");

	private final DetailView detailview;

	public DetailViewButtonBar(final String module, final DetailView detailview) {
		super(module);

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
				if (Window.confirm("Do you really want to cancel your changes?")) {
					startViewing();
				}
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

		addExtraButtons(panel);
		stopViewing();
		initWidget(panel);
	}

	private void addExtraButtons(final Panel panel) {
		for (final ExtraButton extraButton : moduleDto.getExtraButtons()) {
			final Button b = new Button(extraButton.getLabel());
			b.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					extraButton.getAction().doAction(detailview.getCurrentDto());
				}
			});
			
			panel.add(b);
		}
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
		startEditing(null);
	}

	public void startEditing(final String focussedField) {
		if (detailview.isShowing()) {
			detailview.startEditing(focussedField);

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
