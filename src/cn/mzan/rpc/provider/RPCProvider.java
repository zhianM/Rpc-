package cn.mzan.rpc.provider;

import cn.mzan.rpc.framework.RPCFramework;
import cn.mzan.rpc.serivce.HelloSerivce;
import cn.mzan.rpc.serivce.impl.HelloServiceImp;

public class RPCProvider {

	public static void main(String[] args) throws Exception {
		HelloSerivce hs = new HelloServiceImp();
		
		RPCFramework.export(hs, 1234); 
	}
}
