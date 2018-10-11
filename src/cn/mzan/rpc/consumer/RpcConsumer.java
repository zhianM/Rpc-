package cn.mzan.rpc.consumer;

import cn.mzan.rpc.framework.RPCFramework;
import cn.mzan.rpc.serivce.HelloSerivce;
import cn.mzan.rpc.serivce.impl.HelloServiceImp;

public class RpcConsumer {
	public static void main(String[] args) throws Exception {
		HelloSerivce hs_1 = RPCFramework.refer(HelloSerivce.class, "127.0.0.1", 1234);
		for(int i = 0;i<100;i++) {
			String hello = hs_1.hello("World"+i);
			System.out.println(hello);
			Thread.sleep(1000);
		}
	}
}
