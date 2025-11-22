package com.vladdjuga.blogsite.aop;

import com.vladdjuga.blogsite.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class ResultWrapperAspect {

    @Around("@annotation(com.vladdjuga.blogsite.annotation.WrapResult)")
    public Object wrapResult(ProceedingJoinPoint pjp)throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();

        if(!Result.class.isAssignableFrom(method.getReturnType())){
            throw new IllegalStateException("Method " + method.getName() + " does not return Result type");
        }

        try{
            Object result = pjp.proceed();

            if (result == null) {
                log.warn("Method {}() in {} returned null",
                    method.getName(),
                    signature.getDeclaringTypeName());
                return Result.fail("Method " + method.getName() + " returned null");
            }

            if (result instanceof Result<?> resultObj && !resultObj.isSuccess) {
                log.warn("Method {}() in {} returned failure: {}",
                        method.getName(),
                        signature.getDeclaringTypeName(),
                        resultObj.error != null ? resultObj.error.message : "Unknown error");
            }

            return result;
        }catch(Throwable e){
            log.error("Exception in method {}() in class {}: {} - {}",
                method.getName(),
                signature.getDeclaringTypeName(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                e);
            return Result.exception("Exception in method " + method.getName(), e);
        }
    }
}
