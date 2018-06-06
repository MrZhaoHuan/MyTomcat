package com.zhao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zhao.core.log.ConsoleLog;

public class ServletContext {
	private List<String> welcomeFiles = new ArrayList<String>(); // 默认首页文件列表
	private Map<String, String> mimeMapping = new HashMap<String, String>(); // mime映射
	private Map<String, String> servletMapping = new HashMap<String, String>(); // servlet映射
	private String projectName=""; // web项目名称
	private int serverListenerPort; // 监听的端口号
	private ConsoleLog log;
	private String defaultDispatcherClass;
	private ServletContext() {
		log = ConsoleLog.getLog(getClass());
		parseConfig();
	}

	/**
	 * 解析web.xml
	 */
	private void parseConfig() {
		try {
			SAXReader saxReader = new SAXReader();
			boolean isFind = true;
			String resourcePath = "WebRoot/WEB-INF/web.xml";
			while (isFind) {
				if (!projectName.equals("")) {
					isFind = false;
					resourcePath = "src/com/zhao/" + projectName + "/web.xml";
				}
				Document document = saxReader.read(resourcePath);
				Element rootElement = document.getRootElement();
				List<Element> childElements = rootElement.elements();
				// 存放临时servlet映射数据
				Map<String, String> servletTempMapping = new HashMap<String, String>();

				for (Element element : childElements) {
					String elementName = element.getName();
					if ("welcome-file-list".equals(elementName)) {
						List<Element> welcome = element.elements("welcome-file");
						if (!isFind) {
							if(welcome.size()>0) {
								welcomeFiles.clear(); // 覆盖全局的web.xml，清空集合数据
							}
						}
						for (Element w : welcome) {
							welcomeFiles.add(w.getText());
						}
					} else if ("servlet".equals(elementName)) {
						String servletName = element.element("servlet-name").getText();
						String servletClass = element.element("servlet-class").getText();
						if (servletTempMapping.get(servletName) == null) {
							servletTempMapping.put(servletName, servletClass);
						} else {
							servletMapping.put(servletTempMapping.get(servletName), servletClass);
							servletTempMapping.remove(servletName);
						}
					} else if ("servlet-mapping".equals(elementName)) {
						String servletName = element.element("servlet-name").getText();
						String urlPattern = element.element("url-pattern").getText();
						if (servletTempMapping.get(servletName) == null) {
							servletMapping.put(servletName, urlPattern);
						} else {
							servletMapping.put(urlPattern, servletTempMapping.get(servletName));
							servletTempMapping.remove(servletName);
						}
					} else if (projectName.equals("")&& "server-demo-name".equals(elementName)) {
						projectName = element.getText();
					} else if ("mime-mapping".equals(elementName)) {
						String extension = element.element("extension").getText();
						String mime_type = element.element("mime-type").getText();
						mimeMapping.put(extension, mime_type);
					} else if ("server-listener-port".equals(elementName)) {
						serverListenerPort = Integer.parseInt(element.getText());
					} else {
						log.error("---------不支持其他方式的资源配置-----------");
					}
				}

				if (projectName == null) {
					break;
				}

			}
			
			this.defaultDispatcherClass = getServletMapping().get("/");

			log.info("\n" + this.getWelcomeFiles());
			log.info("\n" + this.getMimeMapping());
			log.info("\n" + this.getServletMapping());
			log.info("\n" + this.getProjectName());

		} catch (DocumentException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private static class _inner {
		private static final ServletContext context = new ServletContext();
	}

	/**
	 * 获取ServletContext对象-(单例模式)
	 * 
	 * @return
	 */
	public static ServletContext getServletContext() {
		return _inner.context;
	}

	/**
	 * 获取默认首页文件列表
	 * 
	 * @return
	 */
	public List<String> getWelcomeFiles() {
		return welcomeFiles;
	}

	/**
	 * 获取mime映射
	 * 
	 * @return
	 */
	public Map<String, String> getMimeMapping() {
		return mimeMapping;
	}

	/**
	 * 获取servlet映射
	 * 
	 * @return
	 */
	public Map<String, String> getServletMapping() {
		return servletMapping;
	}

	/**
	 * 获取项目名称
	 * 
	 * @return
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * 获取服务器监听端口号
	 * 
	 * @return
	 */
	public int getServerListenerPort() {
		return serverListenerPort;
	}
	
	/**
	 *   默认的资源处理类
	 * @return
	 */
	public String getDefaultDispatcherClass() {
		return defaultDispatcherClass;
	}
}