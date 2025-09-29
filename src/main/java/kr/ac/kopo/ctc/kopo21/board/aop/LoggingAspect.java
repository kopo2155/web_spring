package kr.ac.kopo.ctc.kopo21.board.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* kr.ac.kopo.ctc.kopo21.board.service.*.*(..))")
    public void onBefore(JoinPoint joinPoint) {
        System.out.println("LoggingAspect.onBefore() 메소드 호출: " + joinPoint.getSignature());
    }

    @After("execution(* kr.ac.kopo.ctc.kopo21.board.servoce.*.*(..))")
    public void onAfter(JoinPoint joinPoint) {
        System.out.println("LoggingAspect.onAfter() 메소드 호출: " + joinPoint.getSignature());
    }

    @AfterReturning(pointcut = "execution(* kr.ac.kopo.ctc.kopo21.board.service.*.*.(..))", returning =  "result")
    public void  onAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("LoggingAspect.onAfterReturning() 메소드 호출 " + joinPoint.getSignature());
        System.out.println("성공 리턴값:" + result);
    }

    @AfterThrowing(pointcut = "execution(* kr.ac.kopo.ctc.kopo21.board.service.*.*(..))", throwing = "exception")
            public void onThrowing(JoinPoint joinPoint, Exception exception){
        System.out.println("LoggingAspect.onAfterThrowing() 메소드 호출 " + joinPoint.getSignature());
        System.out.println("예외: " + exception.getMessage());
    }

    @Around("execution(* kr.ac.kopo.ctc.kopo21.board.service.*.*(..))")
    public Object onAround(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("LoggingAspect.onAround() Before 실행: " + joinPoint.getSignature());
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();
        System.out.println("LoggingAspect.onAround() After 실행 " + joinPoint.getSignature());
        System.out.println("실행 시간 " + (end - start) + "ms");

        return result;
    }
}
