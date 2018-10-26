//package com.firefly.interseptor;
//
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestAttributes;
//import org.springframework.web.context.request.RequestContextHolder;
//
//import com.firefly.exception.CustomException;
//
//import org.springframework.core.annotation.Order;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.log.Log;
//import cn.hutool.log.LogFactory;
//
//@Aspect
//@Component
//@Order(2)
//public class WebInterceptor {
//
//	private static final Log log = LogFactory.get();
//
//	@Autowired
//	private RedisTemplate<String, String> redisTemplate;
//
//	@Pointcut("execution(* com.firefly.controller.AdministratorCURDController.*(..)) "
//			+ "&& !execution(* com.firefly.controller.AdministratorCURDController.login(..))"
//			+ "&& !execution(* com.firefly.controller.AdministratorCURDController.login1(..))"
//			+ "&& !execution(* com.firefly.controller.AdministratorCURDController.adminIndex(..))")
//
//	private void authAspect() {
//	}
//
//	@Before(value = "authAspect()")
//	public void beforeService(JoinPoint joinPoint) throws CustomException {
//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		HttpServletRequest request = (HttpServletRequest) requestAttributes
//				.resolveReference(RequestAttributes.REFERENCE_REQUEST);
//		log.info("进入授权拦截器");
//		Map<String, String[]> args = request.getParameterMap();
//		String[] tmp = args.get("token");
//		if (tmp == null || tmp.length != 1) {
//			throw new CustomException("500401", "操作未授权");
//		}
//		String token = tmp[0];
//		String uid = redisTemplate.opsForValue().get(token);
//		if(StrUtil.isBlank(uid)) {
//			throw new CustomException("500401", "操作未授权");
//		}
//	}
//
//	@AfterReturning(returning = "o", pointcut = "authAspect()")
//	public void afterService(JoinPoint joinPoint, Object o) {
//
//	}
//}
