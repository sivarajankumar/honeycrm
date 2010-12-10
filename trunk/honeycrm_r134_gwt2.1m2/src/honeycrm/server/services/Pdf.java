package honeycrm.server.services;

import honeycrm.client.dto.Configuration;
import honeycrm.client.dto.Dto;
import honeycrm.client.dto.ModuleDto;
import honeycrm.server.NewDtoWizard;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf extends HttpServlet {
	private static final long serialVersionUID = -5755890768164436414L;
	private static final ReadServiceImpl readService = new ReadServiceImpl();
	private static final Configuration config = NewDtoWizard.getConfiguration();

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
			final ModuleDto moduleDto = config.getModuleDtos().get(module);

			document.add(new Paragraph(moduleDto.getTitle(), new Font(FontFamily.HELVETICA, 20, Font.BOLD)));
			
			for (final Entry<String, Serializable> entry : dto.getAllData().entrySet()) {
				if ("id".equals(entry.getKey()) || entry.getKey().endsWith("_resolved")) {
					continue;
				}
				final String label = moduleDto.getFieldById(entry.getKey()).getLabel();
				final String value;
				
				if (dto.getAllData().containsKey(entry.getKey() + "_resolved")) {
					final Dto resolved = (Dto) dto.getAllData().get(entry.getKey() + "_resolved");
					value = String.valueOf(resolved.get("name"));
				} else {
					value = null == entry.getValue() ? "-" : String.valueOf(entry.getValue());
				}

				if (entry.getValue() instanceof List<?>) {
					if (!((List<?>)entry.getValue() ).isEmpty()) {
						final List<Dto> list = (List<Dto>) entry.getValue();
						final ModuleDto listModuleDto = config.getModuleDtos().get(list.get(0).getModule());
						
						PdfPTable table = new PdfPTable(listModuleDto.getListFieldIds().length);
						table.setHeaderRows(1);

						for (final String col: listModuleDto.getListFieldIds()) {
							final String colHeader = listModuleDto.getFieldById(col).getLabel();
							table.addCell(new Phrase(colHeader, new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
						}
						
						for (int i = 0; i < list.size(); i++) {
							for (final String col: listModuleDto.getListFieldIds()) {
								final String colValue;
								
								if (list.get(i).getAllData().containsKey(col + "_resolved")) {
									final Dto resolved = (Dto) list.get(i).get(col + "_resolved");
									colValue = String.valueOf(resolved.get("name"));
								} else {
									colValue = String.valueOf(list.get(i).get(col));
								}

								if ("true".equals(colValue) || "false".equals(colValue)) {
									// final RadioCheckField r = new RadioCheckField(writer, new Rectangle(10, 10, 20, 20), "asd", "v1");
									// r.setChecked("true".equals(colValue));
									table.addCell(new Phrase("true".equals(colValue) ? "X" : ""));
								} else {
									table.addCell(new Phrase(colValue));
								}
							}
						}
						
						document.add(new Chunk(label, new Font(FontFamily.HELVETICA, 12, Font.BOLD)));
						document.add(new Chunk(Chunk.NEWLINE));
						document.add(table);
						document.add(new Chunk(Chunk.NEWLINE));
					}
				} else {
					document.add(getAttributeParagraph(label, value));
				}
			}

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
