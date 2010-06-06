package crm.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import crm.client.dto.AbstractDto;
import crm.client.dto.ListQueryResult;

public class RelationshipsContainer extends Composite {
	private final Class<? extends AbstractDto> relatedDtoClass;
	private Panel panel = new VerticalPanel();

	public RelationshipsContainer(final Class<? extends AbstractDto> relatedDtoClass) {
		this.relatedDtoClass = relatedDtoClass;
		initWidget(panel);
	}

	public void refresh(final Long relatedId) {
		panel.clear();

		for (final Class<? extends AbstractDto> originalDtoClass : DtoRegistry.instance.getAllDtoClasses()) {
			panel.add(new SingleRelationshipPanel(originalDtoClass, relatedId, relatedDtoClass));
		}
	}
}

// TODO instantiate and reuse SearchableListView instead. only problem left to solve is: how to
// determine the name of the id field in the original dto that stores the id of the related dto.
// e.g. if we want to display all contacts for account 23. how to we know in client side code that
// we have to search for all contacts with accountId = 23? currently this is only known on server
// side since there the RelatesTo annotation is read using reflection
class SingleRelationshipPanel extends Composite {
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();
	private final Class<? extends AbstractDto> originatingDtoClass;
	private final Class<? extends AbstractDto> relatedDtoClass;
	private final AbstractDto originatingDto;
	private final Long id;
	private FlexTable table = new FlexTable();

	public SingleRelationshipPanel(final Class<? extends AbstractDto> originatingDto, final Long id, final Class<? extends AbstractDto> relatedDto) {
		this.originatingDtoClass = originatingDto;
		this.originatingDto = DtoRegistry.instance.getDto(originatingDtoClass);
		this.relatedDtoClass = relatedDto;
		this.id = id;

		refresh();

		initWidget(table);
	}

	public void refresh() {
		commonService.getAllRelated(IANA.mashal(originatingDtoClass), id, IANA.mashal(relatedDtoClass), new AsyncCallback<ListQueryResult<? extends AbstractDto>>() {
			@Override
			public void onSuccess(ListQueryResult<? extends AbstractDto> result) {
				insertRelatedDtos(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(caught.getLocalizedMessage());
			}
		});
	}

	private void insertRelatedDtos(ListQueryResult<? extends AbstractDto> result) {
		if (0 == result.getItemCount()) {
			// hide this relationship since no entries have been found for this relationship
			setVisible(false);
		} else {
			setVisible(true);

			// set title
			table.setWidget(0, 0, getTitleLabel());

			for (int i = 0; i < result.getResults().length; i++) {
				final AbstractDto originatingDto = result.getResults()[i];
				// display related item somewhat
				// TODO display more columns
				table.setWidget(1 + i, 0, new Hyperlink(originatingDto.getQuicksearchItem(), originatingDto.getHistoryToken() + " " + originatingDto.getId()));
			}
		}
	}

	private Label getTitleLabel() {
		final Label title = new Label(originatingDto.getTitle() + "s");
		// TODO add style for this in custom css file
		title.setStyleName("relationship_title");
		return title;
	}
}