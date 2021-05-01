package com.longyan.policy.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class AuditAop {

    @Pointcut("@annotation(com.longyan.policy.annotation.Audit)")
    private void cutPoint() {

    }

    @AfterReturning(pointcut = "cutPoint()", returning = "val")
    public void after(JoinPoint joinPoint, Object val) {
        return;
    }
}
