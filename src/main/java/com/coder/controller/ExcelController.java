package com.coder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.coder.model.Student;
import com.coder.service.ExcelService;
import com.coder.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ExcelController {
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private ExcelService excelService;
	
	@PostMapping("/import-excel")
	public ResponseEntity<?> importExcel(@RequestParam MultipartFile file) {
		
		
		try {
			List<Student> importExcelStudent = excelService.importExcel(file);
		
		Boolean saveStudent = studentService.saveStudent(importExcelStudent);
		
				if(saveStudent) {
					return new ResponseEntity("Store Successfully",HttpStatus.OK);
				}else {
					return new ResponseEntity("Upload failed" ,HttpStatus.OK);
				}
		} catch (Exception e) {
		return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/downloadExcel")
	public void generateExcel(HttpServletResponse response) {
		
		try {
			List<Student> allStudents = studentService.getAllStudents();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("content-Disposition", "attachment;filename=Employee_Detail.xlsx");
			excelService.generateExcel(allStudents, response);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
}
