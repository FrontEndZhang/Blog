package com.liu.service;

import java.util.List;
import java.util.Map;

import com.liu.entity.UserLog;
import com.liu.entity.Vistor;

public interface VistorService {
	List<Vistor> listVistor();
	Integer insertVistor(Vistor vistor);
	Integer countVistor();
	Integer deleteVistor(Integer id);

}
