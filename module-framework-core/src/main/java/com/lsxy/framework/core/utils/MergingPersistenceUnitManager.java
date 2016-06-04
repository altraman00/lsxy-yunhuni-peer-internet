package com.lsxy.framework.core.utils;

import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;

public class MergingPersistenceUnitManager extends
		DefaultPersistenceUnitManager {

	private static final Log logger = LogFactory.getLog(MergingPersistenceUnitManager.class);
	@Override
	protected void postProcessPersistenceUnitInfo(
			MutablePersistenceUnitInfo newPU) {
		
		super.postProcessPersistenceUnitInfo(newPU);
		final URL persistenceUnitRootUrl = newPU.getPersistenceUnitRootUrl();
		newPU.addJarFileUrl(persistenceUnitRootUrl);
		newPU.setPersistenceUnitRootUrl(null);
		final String persistenceUnitName = newPU.getPersistenceUnitName();
		final MutablePersistenceUnitInfo oldPU = getPersistenceUnitInfo(persistenceUnitName);
		if (oldPU != null) {
			List<URL> urls = oldPU.getJarFileUrls();
			for (URL url : urls) {
				logger.debug("pu jar file:"+url);
				newPU.addJarFileUrl(url);
			}
			List<String> managedClassNames = oldPU.getManagedClassNames();
			for (String managedClassName : managedClassNames){
				logger.debug("pu managed class:"+managedClassName);
				newPU.addManagedClassName(managedClassName);
			}
			List<String> mappingFileNames = oldPU.getMappingFileNames();
			for (String mappingFileName : mappingFileNames){
				logger.debug("pu mappingFileName:"+mappingFileName);
				newPU.addMappingFileName(mappingFileName);
			}
			Properties oldProperties = oldPU.getProperties();
			Properties newProperties = newPU.getProperties();
			newProperties.putAll(oldProperties);
			newPU.setProperties(newProperties);
		}
	}
	
	@Override
	protected boolean isPersistenceUnitOverrideAllowed() {
		return true;
	}
}
