package honeycrm.client.admin;

import java.util.Date;

import honeycrm.client.prefetch.Prefetcher;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class CacheStatsWidget extends Composite {
	final Label lblHits = new Label();
	final Label lblMisses = new Label();
	final Label lblAccess = new Label();
	final Label lblDate = new Label();

	public CacheStatsWidget() {
		final VerticalPanel panel = new VerticalPanel();

		panel.add(lblAccess);
		panel.add(lblHits);
		panel.add(lblMisses);
		panel.add(lblDate);

		new Timer() {
			@Override
			public void run() {
				updateStats();
			}
		}.scheduleRepeating(1000);
		
		initWidget(panel);
	}

	private void updateStats() {
		final long hits = Prefetcher.instance.getHits();
		final long misses = Prefetcher.instance.getMisses();
		final double hitsRel = 100 * ((double) hits / (double) (hits+misses));
		final double missesRel = 100 *((double) misses / (double) (hits+misses));
		
		lblHits.setText("Hits: " + hitsRel + "% ("+ hits +")");
		lblMisses.setText("Misses: " + missesRel + "% ("+ misses +")");
		lblAccess.setText("Accesses: " + (hits+misses));
		lblAccess.setText("Last refreshed " + new Date(System.currentTimeMillis()));
	}
}
