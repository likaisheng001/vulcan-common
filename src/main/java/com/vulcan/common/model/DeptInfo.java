package com.vulcan.common.model;

import java.util.List;

public class DeptInfo {
	private List<DeptDetail> depts;//当前用户所在部门列表
	private List<String> lowerDepts;//当前用户所在部门的下属部门，以逗号隔开
	private List<DeptDetail> deptCodeAllList;//当前用户的所有deptCode列表(包括当前部门和下级部门)
	private String deptCodeStr;//当前用户所在部门code
	
	
	public String getDeptCodeStr() {
		return deptCodeStr;
	}

	public void setDeptCodeStr(String deptCodeStr) {
		this.deptCodeStr = deptCodeStr;
	}

	
	public List<DeptDetail> getDeptCodeAllList() {
		return deptCodeAllList;
	}

	public void setDeptCodeAllList(List<DeptDetail> deptCodeAllList) {
		this.deptCodeAllList = deptCodeAllList;
	}

	

	public List<String> getLowerDepts() {
		return lowerDepts;
	}

	public void setLowerDepts(List<String> lowerDepts) {
		this.lowerDepts = lowerDepts;
	}

	public List<DeptDetail> getDepts() {
		return depts;
	}

	public void setDepts(List<DeptDetail> depts) {
		this.depts = depts;
	}
	
}
