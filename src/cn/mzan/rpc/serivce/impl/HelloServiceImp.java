package cn.mzan.rpc.serivce.impl;

import cn.mzan.rpc.serivce.HelloSerivce;

public class HelloServiceImp implements HelloSerivce {

	@Override
	public String hello(String name) {
		
		return "Hello "+name;
	}

}
