package com.springtest.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springtest.dao.UserInfoMapper;
import com.springtest.domain.UserInfo;
import com.springtest.service.IUserInfoService;
@Service
public class UserInfoServiceImpl implements IUserInfoService{
	@Autowired
	private UserInfoMapper userInfoMapper;
	public UserInfo getUserById(int id) {
		// TODO Auto-generated method stub
		userInfoMapper.selectByPrimaryKey(id);
		return userInfoMapper.selectByPrimaryKey(id);
	}
	public List<UserInfo> selectAll() {
		// TODO Auto-generated method stub
		
		return userInfoMapper.selectAll();
	}
	public int insert(UserInfo record) {
		// TODO Auto-generated method stub
		return userInfoMapper.insert(record);
	}
	public int insertSelective(UserInfo record) {
		// TODO Auto-generated method stub
		return userInfoMapper.insertSelective(record);
	}

}
