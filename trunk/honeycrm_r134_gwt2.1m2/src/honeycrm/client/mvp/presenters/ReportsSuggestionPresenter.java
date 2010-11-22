package honeycrm.client.mvp.presenters;

import java.util.Map;

import honeycrm.client.mvp.events.OpenReportEvent;
import honeycrm.client.mvp.events.OpenReportEventHandler;
import honeycrm.client.reports.ReportData;
import honeycrm.client.reports.ReportMetaData;
import honeycrm.client.services.ReportServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.LineChart;

public class ReportsSuggestionPresenter implements Presenter {
	public interface Display {
		void setChart(ReportData<Map<Integer, Map<String, Integer>>> reportData, ReportMetaData reportMeta);
		void setRelatedReports(ReportMetaData[] relatedReports);
		Widget asWidget();
	}

	private final Display view;
	private final ReportServiceAsync reportService;
	private final SimpleEventBus eventBus;

	public ReportsSuggestionPresenter(final Display view, final SimpleEventBus eventBus, final ReportServiceAsync reportService) {
		this.reportService = reportService;
		this.eventBus = eventBus;
		this.view = view;
		bind();
	}

	private void bind() {
		eventBus.addHandler(OpenReportEvent.TYPE, new OpenReportEventHandler() {
			@Override
			public void onOpenReport(OpenReportEvent openReportEvent) {
				if (0 <= openReportEvent.getReportId() && openReportEvent.getReportId() <= 2) {
					showReport(openReportEvent.getReportId());
				}
			}
		});

		if (GWT.isClient()) {
			VisualizationUtils.loadVisualizationApi(new Runnable() {
				@Override
				public void run() {
					showReport(0);
				}
			}, LineChart.PACKAGE);
		}
	}

	protected void showReport(final int startingReport) {
		reportService.getReport(startingReport, new AsyncCallback<ReportData<Map<Integer, Map<String, Integer>>>>() {
			@Override
			public void onSuccess(ReportData<Map<Integer, Map<String, Integer>>> reportData) {
				final ReportMetaData[] allMeta = reportData.getMeta();
				final ReportMetaData reportMeta = ReportMetaData.getReportById(startingReport, allMeta);

				view.setChart(reportData, reportMeta);
				view.setRelatedReports(reportMeta.getRelatedReports(allMeta));
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	@Override
	public void go(HasWidgets container) {
		container.add(view.asWidget());
	}
}
