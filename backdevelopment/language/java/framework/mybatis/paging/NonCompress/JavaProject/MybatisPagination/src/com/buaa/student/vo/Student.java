package com.buaa.student.vo;

import com.buaa.pageplugin.PageVO;

/** 
* @ClassName Student 
* @Description TODO
* @author 刘吉超
* @date 2016-01-02 11:08:12
*/
public class Student extends PageVO{
	private int id;
	
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
