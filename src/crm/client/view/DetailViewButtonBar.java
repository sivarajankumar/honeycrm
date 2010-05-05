package crm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class DetailViewButtonBar extends Composite {
	private Button saveBtn = new Button("Save");
	private Button cancelBtn = new Button("Cancel");
	private Button editBtn = new Button("Edit");
	private Button deleteBtn = new Button("Delete");
	
	private final DetailView detailview;

	public DetailViewButtonBar(final DetailView detailview) {
		this.detailview = detailview;
		
		final HorizontalPanel panel = new HorizontalPanel();
		panel.add(saveBtn);
		panel.add(cancelBtn);
		panel.add(editBtn);
		panel.add(deleteBtn);
		panel.add(new Button("Close"));

		saveBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				detailview.saveChanges();
				startViewing();
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

		startViewing();

		initWidget(panel);
	}

	public void startViewing() {
		detailview.view();

		saveBtn.setVisible(false);
		cancelBtn.setVisible(false);
		editBtn.setVisible(true);
		deleteBtn.setVisible(true);
	}

	public void startEditing() {
		if (detailview.isShowing()) {
			detailview.edit();

			saveBtn.setVisible(true);
			cancelBtn.setVisible(true);
			editBtn.setVisible(false);
			deleteBtn.setVisible(false);
		}
	}
}
