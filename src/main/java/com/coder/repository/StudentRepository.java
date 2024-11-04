package com.coder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coder.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{

}
