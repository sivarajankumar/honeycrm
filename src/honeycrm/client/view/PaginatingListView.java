package honeycrm.client.view;

import honeycrm.client.dto.Dto;

public class PaginatingListView extends ListView {
	public PaginatingListView(final Dto clazz) {
		super(clazz);

		panel.add(new ListViewPaginationBar(this, label));
	}

	public void showLastPage() {
		showPage(numberOfPages);
	}

	public void showFirstPage() {
		showPage(1);
	}

	public void showPageLeft() {
		showPage(currentPage - 1);
	}

	public void showPageRight() {
		showPage(currentPage + 1);
	}
}
