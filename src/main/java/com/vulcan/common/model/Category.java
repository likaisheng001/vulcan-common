package com.vulcan.common.model;
/**
 * 
 * @author likaisheng
 *
 */
public class Category {
	
	private String category_id;//品类ID
	private int category_level;//品类级别，1为1级分类，2为2级分类
	private String category_name;//品类名称
	private Long category_depts;//一级分类对应的部门id
	
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public int getCategory_level() {
		return category_level;
	}
	public void setCategory_level(int category_level) {
		this.category_level = category_level;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public Long getCategory_depts() {
		return category_depts;
	}
	public void setCategory_depts(Long category_depts) {
		this.category_depts = category_depts;
	}
}
