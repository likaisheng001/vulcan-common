package com.vulcan.common.model;

/**
 * 当前用户权限信息实体
 * @author chenguoqing
 *
 */

public class AuthorizeBackend {
	private String authorize;
	private int status;
	public String getAuthorize() {
		return authorize;
	}
	public void setAuthorize(String authorize) {
		this.authorize = authorize;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
