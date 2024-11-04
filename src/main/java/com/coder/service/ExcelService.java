package com.coder.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.coder.model.Student;

import jakarta.servlet.http.HttpServletResponse;

public interface ExcelService {

	// store logic
	public List<Student> importExcel(MultipartFile file) throws Exception;
	
	// download Logic 
	public void generateExcel(List<Student> students, HttpServletResponse response);
}
