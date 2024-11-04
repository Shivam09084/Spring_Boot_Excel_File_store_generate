package com.coder.service;

import java.util.List;

import com.coder.model.Student;

public interface StudentService {

	public Boolean saveStudent(List<Student> students);
	public List<Student> getAllStudents();
}
