package honeycrm.server.services;

import honeycrm.client.dto.Dto;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf extends HttpServlet {
	private static final long serialVersionUID = -5755890768164436414L;
	private static final ReadServiceImpl readService = new ReadServiceImpl();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, response.getOutputStream());

			document.addCreationDate();
			document.open();

			final String module;
			final long id;

			String values[] = request.getParameterValues("m");
			if (null != values && 1 == values.length) {
				module = values[0];
			} else {
				module = null;
			}
			values = request.getParameterValues("id");
			if (null != values && 1 == values.length) {
				id = Long.parseLong(values[0]);
			} else {
				id = -1;
			}
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=" + module + ".pdf"); // TODO add more information about the entity into the filename.

			final Dto dto = readService.get(module, id);

			for (final Entry<String, Serializable> entry : dto.getAllData().entrySet()) {
				final String value = String.valueOf((entry.getKey().endsWith("_resolved")) ? ((Dto) entry.getValue()).get("name") : entry.getValue());
				document.add(getAttributeParagraph(entry.getKey(), value));
			}

			PdfPTable table = new PdfPTable(2);
			table.setHeaderRows(1);
			table.addCell("Foo");
			table.addCell("Bar");
			for (int i = 0; i < 100; i++) {
				table.addCell(String.valueOf(i));
				table.addCell(String.valueOf(i + 1));
			}
			document.add(new Paragraph("Offering", new Font(FontFamily.HELVETICA, 20, Font.BOLD)));

			document.add(getAttributeParagraph("Description", "The description"));
			document.add(getAttributeParagraph("Assigned To", "James"));

			document.add(new Chunk(Chunk.NEWLINE));
			document.add(table);
			document.close();
		} catch (DocumentException ex) {
			ex.printStackTrace();
		}
	}

	protected Element getAttributeParagraph(final String key, final String value) {
		final Paragraph p = new Paragraph();
		p.add(new Chunk(key + ": ", new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
		p.add(new Chunk(value));
		return p;
	}

}
