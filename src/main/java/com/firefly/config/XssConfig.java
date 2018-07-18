package com.firefly.config;

import com.google.common.collect.Maps;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class XssConfig {
	
	/**
	 * 1、JsoupUtil提供基于Jsoup过滤非法标签的工具类：
	 * xss非法标签过滤
	 * {@link http://www.jianshu.com/p/32abc12a175a?nomobile=yes}
	 */
	public static class JsoupUtil {

	    /**
	     * 使用自带的basicWithImages 白名单
	     * 允许的便签有a,b,blockquote,br,cite,code,dd,dl,dt,em,i,li,ol,p,pre,q,small,span,
	     * strike,strong,sub,sup,u,ul,img
	     * 以及a标签的href,img标签的src,align,alt,height,width,title属性
	     */
	    private static final Whitelist whitelist = Whitelist.basicWithImages();
	    /** 配置过滤化参数,不对代码进行格式化 */
	    private static final Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
	    static {
	        // 富文本编辑时一些样式是使用style来进行实现的
	        // 比如红色字体 style="color:red;"
	        // 所以需要给所有标签添加style属性
	        whitelist.addAttributes(":all", "style");
	    }

	    public static String clean(String content) {
	        return Jsoup.clean(content, "", whitelist, outputSettings);
	    }

	    public static void main(String[] args) throws FileNotFoundException, IOException {
	        String text = "<a href=\"http://www.baidu.com/a\" onclick=\"alert(1);\">sss</a><script>alert(0);</script>sss";
	        System.out.println(clean(text));
	    }

	}
	
	/**
	 * 2、创建XssHttpServletRequestWrapper
	 * 这是实现XSS过滤的关键，在其内重写了getParameter，getParameterValues，getHeader等方法，对http请求内的参数进行了过滤。
	 */

	public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {  
	    HttpServletRequest orgRequest = null;  
	    private boolean isIncludeRichText = false;

	    public XssHttpServletRequestWrapper(HttpServletRequest request, boolean isIncludeRichText) {  
	        super(request);  
	        orgRequest = request;
	        this.isIncludeRichText = isIncludeRichText;
	    }  

	    /** 
	    * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/> 
	    * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/> 
	    * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖 
	    */  
	    @Override  
	    public String getParameter(String name) {  
	            if(("content".equals(name) || name.endsWith("WithHtml")) && !isIncludeRichText){
	                return super.getParameter(name);
	            }
	            name = JsoupUtil.clean(name);
	        String value = super.getParameter(name);  
	        if (StringUtils.isNotBlank(value)) {
	            value = JsoupUtil.clean(value);  
	        }
	        return value;  
	    }  

	    @Override
	    public String[] getParameterValues(String name) {
	        String[] arr = super.getParameterValues(name);
	        if(arr != null){
	            for (int i=0;i<arr.length;i++) {
	                arr[i] = JsoupUtil.clean(arr[i]);
	            }
	        }
	        return arr;
	    }


	    /** 
	    * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/> 
	    * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/> 
	    * getHeaderNames 也可能需要覆盖 
	    */  
	    @Override  
	    public String getHeader(String name) {  
	            name = JsoupUtil.clean(name);
	        String value = super.getHeader(name);  
	        if (StringUtils.isNotBlank(value)) {  
	            value = JsoupUtil.clean(value); 
	        }  
	        return value;  
	    }  

	    /** 
	    * 获取最原始的request 
	    * 
	    * @return 
	    */  
	    public HttpServletRequest getOrgRequest() {  
	        return orgRequest;  
	    }  

	    /** 
	    * 获取最原始的request的静态方法 
	    * 
	    * @return 
	    */  
	    public HttpServletRequest getOrgRequest(HttpServletRequest req) {  
	        if (req instanceof XssHttpServletRequestWrapper) {  
	            return ((XssHttpServletRequestWrapper) req).getOrgRequest();  
	        }  

	        return req;  
	    }  

	} 
	
	/** 
	 * 3、创建XssFilter
	 * 拦截防止xss注入
	 * 通过Jsoup过滤请求参数内的特定字符
	 * @author yangwk 
	 */  
	public class XssFilter implements Filter {  
	    private Logger logger = LoggerFactory.getLogger(XssFilter.class);

	    private  boolean IS_INCLUDE_RICH_TEXT = false;//是否过滤富文本内容

	    public List<String> excludes = new ArrayList<String>();

	    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {  
	        if(logger.isDebugEnabled()){
	            logger.debug("xss filter is open");
	        }

	        HttpServletRequest req = (HttpServletRequest) request;
	        HttpServletResponse resp = (HttpServletResponse) response;
	        if(handleExcludeURL(req, resp)){
	            filterChain.doFilter(request, response);
	            return;
	        }

	        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request,IS_INCLUDE_RICH_TEXT);
	        filterChain.doFilter(xssRequest, response);
	    }

	    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

	        if (excludes == null || excludes.isEmpty()) {
	            return false;
	        }

	        String url = request.getServletPath();
	        for (String pattern : excludes) {
	            Pattern p = Pattern.compile("^" + pattern);
	            Matcher m = p.matcher(url);
	            if (m.find()) {
	                return true;
	            }
	        }

	        return false;
	    }

	    @Override
	    public void init(FilterConfig filterConfig) throws ServletException {
	        if(logger.isDebugEnabled()){
	            logger.debug("xss filter init~~~~~~~~~~~~");
	        }
	        String isIncludeRichText = filterConfig.getInitParameter("isIncludeRichText");
	        if(StringUtils.isNotBlank(isIncludeRichText)){
	            IS_INCLUDE_RICH_TEXT = BooleanUtils.toBoolean(isIncludeRichText);
	        }

	        String temp = filterConfig.getInitParameter("excludes");
	        if (temp != null) {
	            String[] url = temp.split(",");
	            for (int i = 0; url != null && i < url.length; i++) {
	                excludes.add(url[i]);
	            }
	        }
	    }

	    @Override
	    public void destroy() {}

	}
	

	/**
	 * 4、注册XssFilter
	 * xss过滤拦截器
	 */
	@Bean
	public FilterRegistrationBean xssFilterRegistrationBean() {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(new XssFilter());
	    filterRegistrationBean.setOrder(1);
	    filterRegistrationBean.setEnabled(true);
	    filterRegistrationBean.addUrlPatterns("/*");
	    Map<String, String> initParameters = Maps.newHashMap();
	    initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
	    initParameters.put("isIncludeRichText", "true");
	    filterRegistrationBean.setInitParameters(initParameters);
	    return filterRegistrationBean;
	}

}
