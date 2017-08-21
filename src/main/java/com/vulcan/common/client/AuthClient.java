package com.vulcan.common.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chunbo.purchase.common.util.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.vulcan.common.model.Authorize;
import com.vulcan.common.model.AuthorizeBackend;
import com.vulcan.common.model.Category;

/**
 * 权限客户端
 * @author likaisheng
 *
 */
public class AuthClient {
	private Map<String, List<Category>> authCache = new HashMap<String,List<Category>>();
	private String userAuthorityUrl;
	
	public AuthClient() {
		
	}
	public AuthClient(String userAuthorityUrl) {
		this.userAuthorityUrl = userAuthorityUrl;
	}
	
	public Long getDeptByUseremailAndClass(String email, Long classId){
		try{
			List<Category> cs = getCategoryOfAuth(email);
			if(cs != null){
				for(Category c:cs){
					if(classId == Long.valueOf(c.getCategory_id()).longValue()){
						return c.getCategory_depts();
					}
				}
			}
		}catch(Exception e){
			throw new RuntimeException("获取权限失败：email="+email+"$classId="+classId);
		}
		return null;
	}

	private List<Category> getCategoryOfAuth(String email)throws Exception{
		if(authCache.containsKey(email)){
			return authCache.get(email);
		}
		HttpClientUtil client = HttpClientUtil.getInstance();
		Map<String, String> param = new HashMap<String,String>();
		param.put("email", email);
		String authorizeStr = client.doGetForString(userAuthorityUrl, param);
		AuthorizeBackend authorizeBackendCurrent = JSONUtil.toObject(authorizeStr, new TypeReference<AuthorizeBackend>(){});
		
		if(authorizeBackendCurrent.getStatus() == 0){ //status为0，代表无权限
			authCache.put(email, null);
			return null;
		}
		
		Authorize authorize = JSONUtil.toObject(authorizeBackendCurrent.getAuthorize(), new TypeReference<Authorize>(){});
		List<Category> cs = authorize.getCategory_info().getCategory();
		authCache.put(email, cs);
		return cs;
	}
	
	public String getUserAuthorityUrl() {
		return userAuthorityUrl;
	}

	public void setUserAuthorityUrl(String userAuthorityUrl) {
		this.userAuthorityUrl = userAuthorityUrl;
	}
}
