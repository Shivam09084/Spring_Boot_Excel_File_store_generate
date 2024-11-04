package com.coder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.coder.model.Student;
import com.coder.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService{

	@Autowired
	private StudentRepository studentRepository;

	
	@Override
	public Boolean saveStudent(List<Student> students) {
		List<Student> saveAll = studentRepository.saveAll(students);
		if(!CollectionUtils.isEmpty(saveAll)) {
			return true;
		}
		return false;
	}


	@Override
	public List<Student> getAllStudents() {
		
		return studentRepository.findAll();
	}
	
	

}
