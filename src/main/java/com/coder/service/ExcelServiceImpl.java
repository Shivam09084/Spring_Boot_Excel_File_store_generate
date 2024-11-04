package com.coder.service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coder.model.Student;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class ExcelServiceImpl implements ExcelService{

	@Override
	public List<Student> importExcel(MultipartFile file) throws Exception {
		
		List<Student> studentList = new ArrayList<>();
		
		if(file.isEmpty()) {
			throw new IllegalArgumentException("Kindly Import File");
		}
		if(!file.isEmpty() && !file.getOriginalFilename().endsWith(".xlsx")) {
			throw new IllegalArgumentException("Invalid excel file");
		}
		
		InputStream ios = file.getInputStream();
		XSSFWorkbook workbook = new XSSFWorkbook(ios);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		for(int i=1; i<sheet.getPhysicalNumberOfRows();i++) {
			
			XSSFRow row = sheet.getRow(i);
			if(row==null)
				continue;
			
			String name = row.getCell(0).getStringCellValue();
			String post = row.getCell(1).getStringCellValue();
			Double salary = row.getCell(2).getNumericCellValue();
			
			Student student = Student.builder().name(name).post(post).salary(salary).build();
			
			studentList.add(student);
		}
		workbook.close();
		return studentList;
	}

	// Download Logic 
	
	private String[] header = {"name","post","salary"};
	
	@Override
	public void generateExcel(List<Student> students, HttpServletResponse response) {
		
		ServletOutputStream outputStream = null;
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {
			
			XSSFSheet  sheet = workbook.createSheet("Employee Details");
			
//			ye uppar vala cell style jisme name likha use kaise rekhna
			
			XSSFCellStyle headerStyle = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontHeight(14);
			headerStyle.setFont(font);
			
//			isme data ayega uska kya font style rkhna hai
			
			XSSFCellStyle dataStyle = workbook.createCellStyle();
			XSSFFont font2 = workbook.createFont();
			font2.setBold(false);
			font2.setFontHeight(12);
			headerStyle.setFont(font);
			
			int rowCount = 0;
//			 header Creation 
			XSSFRow headerRow = sheet.createRow(rowCount);
			createCell(sheet, headerRow, header, headerStyle);
			
//			 Data Creation 
			for(Student student : students) {
				
				rowCount++;
				XSSFRow row = sheet.createRow(rowCount);
				Object[] cellValue = {student.getName(),student.getPost(),student.getSalary()};
				createCell(sheet, row, cellValue, dataStyle);
				
			}
			outputStream = response.getOutputStream();
			workbook.write(outputStream);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(workbook != null) {
					workbook.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void createCell(XSSFSheet sheet,XSSFRow row,Object[]data,XSSFCellStyle style) {
		
		for(int i=0; i<data.length;i++) {
			sheet.autoSizeColumn(i);
			
			// create cell
			XSSFCell cell = row.createCell(i);
			Object dataValue = data[i];
			
			if(dataValue instanceof String) {
				cell.setCellValue((String) dataValue);
			}else if(dataValue instanceof Integer) {
				cell.setCellValue((Integer)dataValue);
			}else if(dataValue instanceof Long) {
				cell.setCellValue((Long)dataValue);
			}else if(dataValue instanceof Boolean) {
				cell.setCellValue((Boolean)dataValue);
			}else {
				cell.setCellValue(cell != null ? dataValue.toString() :"");
			}
			cell.setCellStyle(style);
		}
	}	
}
