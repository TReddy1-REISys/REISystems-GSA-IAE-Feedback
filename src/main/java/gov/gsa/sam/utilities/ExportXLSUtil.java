package gov.gsa.sam.utilities;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import gov.gsa.sam.domain.Feedback;
import gov.gsa.sam.domain.Question;
import gov.gsa.sam.exception.BaseApplicationException;
import gov.gsa.sam.service.QuestionService;

@Component
public class ExportXLSUtil {

	@Resource
	private Environment environment;

	@Autowired
	private QuestionService questionService;

	private static final Logger log = LoggerFactory.getLogger(ExportXLSUtil.class);

	public static final String CURR_PATH = System.getProperty("user.dir");
	public static final String EXT_NAME = ".XLSX";

	public String exportXLS(List<Feedback> feedbackList) {
		log.info("Number of Feedback Records are " + feedbackList.size());
		String FILE_NAME = CURR_PATH + environment.getProperty("excel.path");
		SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

		// Creating HashMap to get the question description
		HashMap<Long, String> questionHashMap = new HashMap<>();
		List<Question> allquestions;
		try {
			allquestions = questionService.getAllQuestions();
			log.info("All questions retrieved from database " + allquestions.size());
			for (Question ques : allquestions) {
				questionHashMap.put(ques.getQuestionId(), ques.getQuestionDesc());
			}
		} catch (BaseApplicationException e1) {
			// TODO Auto-generated catch block
			log.error("Error retrieving questions from database " + e1);
		}

		String completeFileName;
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Feedback Data");

		sheet.setColumnWidth(0, 6000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 10000);
		sheet.setColumnWidth(3, 10000);
		sheet.setColumnWidth(4, 6000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 6000);
		sheet.setColumnWidth(7, 6000);

		Font bold = workbook.createFont();
		bold.setBold(true);
		bold.setFontHeightInPoints((short) 10);

		Font normaltext = workbook.createFont();
		normaltext.setFontHeightInPoints((short) 10);

		CellStyle header = workbook.createCellStyle();
		header.setBorderBottom(BorderStyle.THIN);
		header.setBorderLeft(BorderStyle.THIN);
		header.setBorderRight(BorderStyle.THIN);
		header.setBorderTop(BorderStyle.THIN);
		header.setFont(bold);
		header.setWrapText(true);
		header.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		header.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		CellStyle normal = workbook.createCellStyle();
		normal.setBorderBottom(BorderStyle.THIN);
		normal.setBorderLeft(BorderStyle.THIN);
		normal.setBorderRight(BorderStyle.THIN);
		normal.setBorderTop(BorderStyle.THIN);
		normal.setWrapText(true);
		normal.setFont(normaltext);

		int row = 0;
		int rowColumn = 0;
		// prepare headers for the XLSX
		List<String> headerColumns = new ArrayList<>();
		headerColumns.add("FEEDBACK ID");
		headerColumns.add("FEEDBACK PATH");
		headerColumns.add("QUESTION");
		headerColumns.add("RESPONSE");
		headerColumns.add("QUESTION TYPE");
		headerColumns.add("USER ID");
		headerColumns.add("CREATE DATE");

		Row headerRow = sheet.createRow(row++);
		Cell cell;
		for (String headerColumn : headerColumns) {
			cell = headerRow.createCell(rowColumn++);
			cell.setCellValue(headerColumn);
			cell.setCellStyle(header);
		}

		for (Feedback feedback : feedbackList) {
			// prepare the data for the XLSX
			rowColumn = 0;

			Row dataRow = sheet.createRow(row++);

			cell = dataRow.createCell(rowColumn++);
			cell.setCellValue(feedback.getFeedbackId());
			cell.setCellStyle(normal);

			cell = dataRow.createCell(rowColumn++);
			cell.setCellValue(feedback.getFeedbackPath());
			cell.setCellStyle(normal);

			cell = dataRow.createCell(rowColumn++);
			cell.setCellValue(questionHashMap.get(feedback.getQuestionId()));
			cell.setCellStyle(normal);

			String feedbackResponse = "No";
			String isSelected = null;
			if (!feedback.getFeedbackResponse().getSelected().isEmpty()
					&& feedback.getFeedbackResponse().getSelected() != null) {
				isSelected = StringUtils.join(feedback.getFeedbackResponse().getSelected(), ",");
			}

			if (isSelected != null && !"".equals(isSelected)) {
				if ((environment.getProperty("final.question")
						.equalsIgnoreCase(questionHashMap.get(feedback.getQuestionId())))
						&& ("radio-text").equalsIgnoreCase(feedback.getFeedbackResponse().getType())) {
					feedbackResponse = "Yes," + isSelected;
				} else {
					feedbackResponse = isSelected;
				}
			}

			cell = dataRow.createCell(rowColumn++);
			cell.setCellValue(feedbackResponse);
			cell.setCellStyle(normal);

			cell = dataRow.createCell(rowColumn++);
			cell.setCellValue(feedback.getFeedbackResponse().getType());
			cell.setCellStyle(normal);

			cell = dataRow.createCell(rowColumn++);
			cell.setCellValue(feedback.getUserId());
			cell.setCellStyle(normal);

			cell = dataRow.createCell(rowColumn);
			cell.setCellValue(feedback.getCreatedDate().toString());
			cell.setCellStyle(normal);
		}

		try {
			completeFileName = FILE_NAME + FORMATTER.format(new Date()) + EXT_NAME;

			log.info("Feedbacks Export File exported to " + completeFileName);
			FileOutputStream outputStream = new FileOutputStream(completeFileName);
			workbook.write(outputStream);
			outputStream.close();
			// workbook.close();

		} catch (FileNotFoundException e) {
			completeFileName = "";
			log.warn("Following Exception Occured::" + e);
		} catch (IOException e) {
			completeFileName = "";
			log.warn("Following Exception Occured::" + e);
		}
		return completeFileName;
	}
}
