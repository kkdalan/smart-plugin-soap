package org.smart4j.plugin.soap;

import org.smart4j.framework.helper.ConfigHelper;

public class SoapConfig {

	public static boolean isLog() {
		return ConfigHelper.getBoolean(SoapConstant.LOG);
	}
}
