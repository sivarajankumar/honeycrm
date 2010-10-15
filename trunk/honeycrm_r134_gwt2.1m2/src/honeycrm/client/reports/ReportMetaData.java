package honeycrm.client.reports;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportMetaData implements Serializable {
	private static final long serialVersionUID = -9159637532838086697L;
	private long id;
	private String title;
	private String columns[];
	private String tags[];

	public ReportMetaData() {
	}

	public ReportMetaData(final long id, final String title, final String columns[], final String tags[]) {
		this.id = id;
		this.title = title;
		this.columns = columns;
		this.tags = tags;
	}

	public long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String[] getColumns() {
		return columns;
	}

	public String[] getTags() {
		return tags;
	}
	
	public static ReportMetaData getReportById(final int reportId, final ReportMetaData metaData[]) {
		for (int i=0; i<metaData.length; i++) {
			if (reportId == metaData[i].id) {
				return metaData[i];
			}
		}
		return null;
	}

	public ReportMetaData[] getRelatedReports(final ReportMetaData allReportsMetaData[]) {
		final ArrayList<ReportMetaData> matches = new ArrayList<ReportMetaData>();
		
		for (int i = 0; i < allReportsMetaData.length; i++) {
			final ReportMetaData otherModule = allReportsMetaData[i];
			
			if (id != otherModule.id && tagsIntersect(otherModule.getTags())) {
				matches.add(otherModule);
			}
		}

		return matches.toArray(new ReportMetaData[0]);
	}
	
	public boolean tagsIntersect(final String[] foreignTags) {
		for (int i=0; i<foreignTags.length; i++) {
			if (hasTag(foreignTags[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasTag(final String tag) {
		for (int i=0; i<tags.length; i++) {
			if (tags[i].equals(tag)) {
				return true;
			}
		}
		return false;
	}
}
