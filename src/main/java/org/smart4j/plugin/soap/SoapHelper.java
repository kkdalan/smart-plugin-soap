package org.smart4j.plugin.soap;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;

public final class SoapHelper {

	private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<Interceptor<? extends Message>>();
	private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<Interceptor<? extends Message>>();

	static {
		if (SoapConfig.isLog()) {
			inInterceptorList.add(new LoggingInInterceptor());
			outInterceptorList.add(new LoggingOutInterceptor());
		}
	}

	public static void publishService(String wsdl, Class<?> interfaceClass, Object implementInstance) {
		ServerFactoryBean factory = new ServerFactoryBean();
		factory.setAddress(wsdl);
		factory.setServiceClass(interfaceClass);
		factory.setServiceBean(implementInstance);
		factory.setInInterceptors(inInterceptorList);
		factory.setOutInterceptors(outInterceptorList);
		factory.create();
	}

	public static <T> T createClient(String wsdl, Class<? extends T> interfaceClass) {
		ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
		factory.setAddress(wsdl);
		factory.setServiceClass(interfaceClass);
		factory.setInInterceptors(inInterceptorList);
		factory.setOutInterceptors(outInterceptorList);
		return factory.create(interfaceClass);
	}

}
