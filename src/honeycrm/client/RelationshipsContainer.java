package honeycrm.client;

import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ListQueryResult;
import honeycrm.client.prefetch.Consumer;
import honeycrm.client.prefetch.Prefetcher;
import honeycrm.client.prefetch.ServerCallback;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RelationshipsContainer extends Composite {
	private final Dto relatedDtoClass;
	private Panel panel = new VerticalPanel();

	public RelationshipsContainer(final Dto relatedDtoClass) {
		this.relatedDtoClass = relatedDtoClass;
		initWidget(panel);
	}

	public void refresh(final Long relatedId) {
		clear();

		for (final Dto originalDtoClass : DtoRegistry.instance.getDtos()) {
			panel.add(new SingleRelationshipPanel(originalDtoClass, relatedId, relatedDtoClass));
		}
	}

	/**
	 * Visually throw away all panels.
	 */
	public void clear() {
		panel.clear();
	}
}

// TODO instantiate and reuse SearchableListView instead. only problem left to solve is: how to
// determine the name of the id field in the original dto that stores the id of the related dto.
// e.g. if we want to display all contacts for account 23. how to we know in client side code that
// we have to search for all contacts with accountId = 23? currently this is only known on server
// side since there the RelatesTo annotation is read using reflection
class SingleRelationshipPanel extends Composite {
	private static final CommonServiceAsync commonService = ServiceRegistry.commonService();
	private final Dto originatingDtoClass;
	private final Dto relatedDtoClass;
//	private final AbstractDto originatingDto;
	private final Long id;
	private FlexTable table = new FlexTable();

	public SingleRelationshipPanel(final Dto originatingDto, final Long id, final Dto relatedDto) {
		this.originatingDtoClass = originatingDto;
//		this.originatingDto = DtoRegistry.instance.getDto(originatingDtoClass);
		this.relatedDtoClass = relatedDto;
		this.id = id;

		initWidget(table);

		refresh();
	}

	public void refresh() {
		Prefetcher.instance.get(new Consumer<ListQueryResult>() {
			@Override
			public void setValueAsynch(ListQueryResult value) {
				insertRelatedDtos(value);
			}
		}, new ServerCallback<ListQueryResult>() {
			@Override
			public void doRpc(final Consumer<ListQueryResult> internalCacheCallback) {
				LoadIndicator.get().startLoading();

				commonService.getAllRelated(originatingDtoClass.getModule(), id, relatedDtoClass.getModule(), new AsyncCallback<ListQueryResult>() {
					@Override
					public void onSuccess(ListQueryResult result) {
						LoadIndicator.get().endLoading();
						internalCacheCallback.setValueAsynch(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						LoadIndicator.get().endLoading();
						Window.alert(caught.getLocalizedMessage());
					}
				});
			}
		}, 60 * 1000, originatingDtoClass.getModule(), id, relatedDtoClass.getModule());
	}

	private void insertRelatedDtos(ListQueryResult result) {
		if (0 == result.getItemCount()) {
			// hide this relationship since no entries have been found for this relationship
			setVisible(false);
		} else {
			setVisible(true);

			// set title
			table.setWidget(0, 0, getTitleLabel());

			for (int i = 0; i < result.getResults().length; i++) {
				final Dto originatingDto = result.getResults()[i];
				// display related item somewhat
				// TODO display more columns
				table.setWidget(1 + i, 0, new Hyperlink(originatingDto.getQuicksearchItem(), originatingDto.getHistoryToken() + " " + originatingDto.getId()));
			}
		}
	}

	private Label getTitleLabel() {
		final Label title = new Label(originatingDtoClass.getTitle() + "s");
		// TODO add style for this in custom css file
		title.setStyleName("relationship_title");
		return title;
	}
}