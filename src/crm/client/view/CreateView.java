package crm.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import crm.client.dto.AbstractDto;

public class CreateView extends AbstractView {
	private final FlexTable table;
	private final CreateViewButtonBar buttonBar;
	private final Button createBtn = new Button("Create");
	
	public CreateView(final Class<? extends AbstractDto> clazz) {
		super(clazz);

		buttonBar = new CreateViewButtonBar(this);
		
		createBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createBtn.setVisible(false);
				table.setVisible(true);
				buttonBar.setVisible(true);
			}
		});
		table = getTable();

		final VerticalPanel panel = new VerticalPanel();
		panel.add(createBtn);
		panel.add(table);
		panel.add(buttonBar);

		cancel();
		
		initWidget(panel);
	}

	public void create() {
		save(table, -1);
	}

	private FlexTable getTable() {
		final int[][] fields = viewable.getFormFieldIds();
		final FlexTable table = new FlexTable();

		for (int y = 0; y < fields.length; y++) {
			for (int x = 0; x < fields[y].length; x++) {
				final int id = fields[y][x];

				final Widget widgetLabel = new Label(viewable.getFieldById(id).getLabel());
				final Widget widgetEdit = getWidgetByType(viewable, id, false);

				if (widgetEdit instanceof FocusWidget) {
					((FocusWidget) widgetEdit).addKeyDownHandler(new KeyDownHandler() {
						@Override
						public void onKeyDown(KeyDownEvent event) {
							if (KeyCodes.KEY_ENTER == event.getNativeKeyCode()) {
								create();
							}
						}
					});
				}

				table.setWidget(y, x * 2 + 0, widgetLabel);
				table.setWidget(y, x * 2 + 1, widgetEdit);
			}
		}

		return table;
	}

	public void cancel() {
		createBtn.setVisible(true);
		emptyInputFields(table);
		table.setVisible(false);
		buttonBar.setVisible(false);
	}
}
