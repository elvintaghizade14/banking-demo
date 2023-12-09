package com.azercell.banking.commonlib.aop;

import com.azercell.banking.commonlib.messaging.BaseEvent;
import com.azercell.banking.commonlib.messaging.BaseResultEvent;
import com.azercell.banking.commonlib.util.LogUtil;
import com.azercell.banking.commonlib.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

import static net.logstash.logback.argument.StructuredArguments.v;

@Log4j2
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "common.logging", name = "enabled")
public class CommonLoggingAspect {

    private final WebUtil webUtil;

    @Around("execution(* com.azercell.banking.*.controller..*(..)))")
    public Object logControllerEndpoints(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final String uri = webUtil.getRequestUriWithQueryString();
        final Map<String, String> customHeaders = webUtil.getCustomHeaders();

        final MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();
        final Map<String, Object> params =
                LogUtil.getParamsAsMap(methodSignature.getParameterNames(), proceedingJoinPoint.getArgs());

        log.debug("[Request]  | Uri: {} [{}.{}] | Custom Headers: {} | Params: {}",
                v("uri", uri), className, methodName, customHeaders.toString(), v("params", params));

        final long start = System.currentTimeMillis();
        final Object result = proceedingJoinPoint.proceed();
        final long elapsedTime = System.currentTimeMillis() - start;

        log.debug("[Response] | Uri: {} [{}.{}] | Elapsed time: {} ms | Result: {}",
                uri, className, methodName, v("elapsed_time", elapsedTime), result);

        return result;
    }

    @Before("execution(* com.azercell.banking.*.messaging.MessageConsumer.*(..)) && args(baseEvent)")
    public void logConsumerBaseEventData(JoinPoint joinPoint, BaseEvent<?> baseEvent) {
        logMessagingEvent(joinPoint, baseEvent.getEventId(), "CONSUMER");
    }

    @Before("execution(* com.azercell.banking.*.messaging.MessageConsumer.*(..)) && args(baseResultEvent)")
    public void logConsumerBaseResultEventData(JoinPoint joinPoint, BaseResultEvent<?> baseResultEvent) {
        logMessagingEvent(joinPoint, baseResultEvent.getEventId(), "CONSUMER");
    }

    @After("execution(* com.azercell.banking.*.messaging.MessageProducer.*(..)) && args(baseEvent)")
    public void logProducerBaseEventData(JoinPoint joinPoint, BaseEvent<?> baseEvent) {
        logMessagingEvent(joinPoint, baseEvent.getEventId(), "PRODUCER");
    }

    @After("execution(* com.azercell.banking.*.messaging.MessageProducer.*(..)) && args(baseResultEvent)")
    public void logProducerBaseResultEventData(JoinPoint joinPoint, BaseResultEvent<?> baseResultEvent) {
        logMessagingEvent(joinPoint, baseResultEvent.getEventId(), "PRODUCER");
    }

    private void logMessagingEvent(JoinPoint joinPoint, String eventId, String eventType) {
        final MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        final String className = methodSignature.getDeclaringType().getSimpleName();
        final String methodName = methodSignature.getName();
        final Map<String, Object> params =
                LogUtil.getParamsAsMap(methodSignature.getParameterNames(), joinPoint.getArgs());
        log.debug("[Event]  | eventType: {} | eventId: {} | [{}.{}] | Params: {}",
                eventType, v("eventId", eventId), className, methodName, v("params", params));
    }

}


