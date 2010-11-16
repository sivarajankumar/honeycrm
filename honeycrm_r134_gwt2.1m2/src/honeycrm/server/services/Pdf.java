package honeycrm.server.services;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf extends HttpServlet {
	private static final long serialVersionUID = -5755890768164436414L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/pdf"); // Code 1
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, response.getOutputStream()); // Code 2
			document.open();

			// Code 3
			PdfPTable table = new PdfPTable(2);
			table.addCell("1");
			table.addCell("2");
			table.addCell("3");
			table.addCell("4");
			table.addCell("5");
			table.addCell("6");

			// Code 4
			document.add(table);
			document.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
