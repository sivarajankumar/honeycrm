package honeycrm.server.test.small.mocks;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

import honeycrm.client.mvp.presenters.ReportsSuggestionPresenter.Display;
import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;

public class ReportsSuggestionViewMock implements Display {

	@Override
	public void setChart(ReportData<Map<Integer, Map<String, Integer>>> reportData, ReportMetaData reportMeta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setRelatedReports(ReportMetaData[] relatedReports) {
		// TODO Auto-generated method stub

	}

	@Override
	public Widget asWidget() {
		// TODO Auto-generated method stub
		return null;
	}

}
