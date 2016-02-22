package com.buaa.student.service;

import java.util.List;

import com.buaa.student.vo.Student;

/** 
* @ClassName IStudent 
* @Description TODO
* @author 刘吉超
* @date 2016-01-02 20:02:03
*/
public interface IStudent {
	public List<Student> selectStudent(Student Student);
}
