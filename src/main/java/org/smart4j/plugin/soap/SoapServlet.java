package org.smart4j.plugin.soap;

import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

@WebServlet(urlPatterns = SoapConstant.SERVLET_URL, loadOnStartup = 0)
public class SoapServlet extends CXFNonSpringServlet {

	@Override
	protected void loadBus(ServletConfig sc) {
		super.loadBus(sc);
		Bus bus = getBus();
		BusFactory.setDefaultBus(bus);
		publishSoapService();
	}

	private void publishSoapService() {
		Set<Class<?>> soapClassSet = ClassHelper.getClassSetByAnnotation(Soap.class);
		if (CollectionUtil.isNotEmpty(soapClassSet)) {
			for (Class<?> soapClass : soapClassSet) {
				String address = getAddress(soapClass);
				Class<?> soapInterfaceClass = getSoapInterfaceClass(soapClass);
				Object soapInstance = BeanHelper.getBean(soapClass);
				SoapHelper.publishService(address, soapInterfaceClass, soapInstance);
			}
		}
	}

	private String getAddress(Class<?> soapClass) {
		String address;
		String soapValue = soapClass.getAnnotation(Soap.class).value();
		if (StringUtil.isNotEmpty(soapValue)) {
			address = soapValue;
		} else {
			address = getSoapInterfaceClass(soapClass).getSimpleName();
		}

		if (!address.startsWith("/")) {
			address = "/" + address;
		}
		address = address.replaceAll("\\/+", "/");
		return address;
	}

	private Class<?> getSoapInterfaceClass(Class<?> soapClass) {
		return soapClass.getInterfaces()[0];
	}
}
