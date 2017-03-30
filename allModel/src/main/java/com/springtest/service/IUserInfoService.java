package com.springtest.service;

import java.util.List;

import com.springtest.domain.UserInfo;

public interface IUserInfoService {
	public UserInfo getUserById(int id);
	List<UserInfo> selectAll();
	 int insert(UserInfo record);
	 int insertSelective(UserInfo record);
}
