package com.zhao.core.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import com.zhao.core.ServletContext;
import com.zhao.core.http.ServletRequest;
import com.zhao.core.http.ServletResponse;
import com.zhao.core.http.impl.HttpServletRequest;
import com.zhao.core.http.impl.HttpServletResponse;
import com.zhao.core.io.ServletOutputStream;

public class DefaultServlet implements Servlet{
	    private ServletContext context;
	    
	    public void setContext(ServletContext context) {
			this.context = context;
		}
	    
	    public ServletContext getContext() {
			return context;
		}
		public void init() {
			System.out.println(" init()");
		}
		
		public void service(ServletRequest req,ServletResponse resp) {
		     HttpServletRequest httReq =  (HttpServletRequest)req;
		     HttpServletResponse httResp =  (HttpServletResponse)resp;
		     String filePath = "";
		     String uri = httReq.getUri().substring(1);
		     System.out.println(uri);
		     try {
				uri = URLDecoder.decode(uri, "UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		     System.out.println(uri);
		     if(uri.equals("") || ( httReq.getParameterMap().isEmpty()&&uri.equals(getContext().getProjectName()) )) {
		    	   //响应默认的welcome-list-file
			       List<String> weList = 	 getContext().getWelcomeFiles();
			       if(weList.size()>0) {
			    	   filePath = weList.get(0);
			       }
		     }else {
		    	String[] project_resource =  uri.split("/");
		    	
		    	if(project_resource.length==1) {
		    		 filePath = project_resource[0].split("\\?")[0];
		    	}else {
		    		 if(project_resource[0].equals(getContext().getProjectName())) {
		    			
		    			 for(int i=1;i<project_resource.length;i++) {
		    				 filePath +=project_resource[i]+"/";
		    			 }
		    			 filePath = filePath.substring(0, filePath.length()-1);
		    			 filePath = filePath.split("\\?")[0];
		    		 }else {
		    			 filePath = uri.split("\\?")[0];
		    		 }
		    	}
		     }
		     filePath = "D:/workspace/eclipse/MyTomcat/WebRoot/"+filePath;
		     System.out.println("资源文件:"+filePath);
		     
		     File file = new File(filePath);
		     if(file.exists() && file.isFile()) {
		    	 try {
		    		 ServletOutputStream out = resp.getOutput();
		    		 FileInputStream in  =  new FileInputStream(file);
		    		 int end = -1;
			    	 while((end=in.read())!=-1) {
			    		 out.print((byte)end);
			    	 }
			    	 in.close();
			    	 
					//设置响应文件的 contentType
					String prefix=file.getName().substring(file.getName().lastIndexOf(".")+1);
					String contentType = context.getMimeMapping().get(prefix);
					if(null!=contentType) {
						resp.setContentType(contentType);
					}
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}catch (IOException e) {
					e.printStackTrace();
				}
		    	 
		     }else {
		    	 resp.getWriter().write("您请求的资源不存在");
		     }
		}

		@Override
		public void destroy() {
			System.out.println(" destroy()");
		}
		
		
}