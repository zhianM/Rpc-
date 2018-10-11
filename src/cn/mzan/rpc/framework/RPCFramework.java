package cn.mzan.rpc.framework;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

public class RPCFramework {

	public static void export(final Object service,int port) throws Exception {
		if(service == null) {
			throw new IllegalArgumentException("service isn`t null");
		}
		if(port <0 || port >65535) {
			throw new IllegalArgumentException("Invalid port"+port);
		}
		System.out.println("export Service:"+service.getClass().getName()+",on port:"+port);
		
		ServerSocket server = new ServerSocket(port);
		while(true) {
			try {
				final Socket socket = server.accept();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ObjectInputStream ois = null;
						ObjectOutputStream oos = null;
						
						try {
							ois = new ObjectInputStream(socket.getInputStream());
							String methodName = ois.readUTF();
							Class<?>[] parameterTypes = (Class<?>[])ois.readObject();
							Object[] arguments = (Object[]) ois.readObject();
							oos = new ObjectOutputStream(socket.getOutputStream());
							
							try {
								Method method = service.getClass().getMethod(methodName, parameterTypes);
								Object result = method.invoke(service, arguments);
								oos.writeObject(result);
							} catch (NoSuchMethodException | SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally {
							try {
								oos.close();
								ois.close();
								socket.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			} catch (Exception e) {
				 e.printStackTrace();
			}
			
		}
		
	}
	public static <T> T refer(final Class<T> interfaceClass,final String host,final int port) {
		if(interfaceClass == null) {
			throw new IllegalArgumentException("InterfaceClass != null");
		}
		if(host == null)
			throw new IllegalArgumentException("host != null");
		if(port<0||port>65535)
			throw new IllegalArgumentException("I");
		System.out.println("Get remote service:"+interfaceClass.getName()+"from host"+host+":"+port);
		
		
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[] {interfaceClass}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Socket socket = new Socket(host,port);
				try {
					ObjectOutputStream oos_1 = new ObjectOutputStream(socket.getOutputStream());
					try {
						oos_1.writeUTF(method.getName());
						oos_1.writeObject(method.getParameterTypes());
						oos_1.writeObject(args);
						
						ObjectInputStream ois_1 = new ObjectInputStream(socket.getInputStream());
						try {
							Object result = ois_1.readObject();
							if(result instanceof Throwable) {
								throw (Throwable) result;
							}
							return result;
						} catch (Exception e) {
							// TODO: handle exception
						}finally {
							ois_1.close();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}finally {
						oos_1.close();
					}
					
				} catch (Exception e) {
					// TODO: handle exception
				}finally {
					socket.close();
				}
				
				return null;
			}
		});
	}
}
