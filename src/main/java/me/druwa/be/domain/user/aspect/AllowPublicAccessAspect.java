package me.druwa.be.domain.user.aspect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;
import me.druwa.be.domain.user.annotation.AllowPublicAccess;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.PublicUser;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.global.exception.UnauthorizedException;
import me.druwa.be.global.exception.UnexpectedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
@Component
public class AllowPublicAccessAspect {
    @Pointcut("execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
    public void requestMappingAnnotations() { }

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllers() {}

    @Around("requestMappingAnnotations()")
    public Object allowPublicAccess(ProceedingJoinPoint pjp) throws Throwable {
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        final Method method = signature.getMethod();

        final Optional<?> currentUserOptional = Arrays.stream(method.getParameterAnnotations())
                                      .flatMap(Arrays::stream)
                                      .map(Annotation::annotationType)
                                      .filter(CurrentUser.class::isAssignableFrom)
                                      .findAny();
        if (false == currentUserOptional.isPresent()) {
            return pjp.proceed();
        }

        final Optional<User> userOptional = Arrays.stream(pjp.getArgs())
                                                  .filter(arg -> arg instanceof User)
                                                  .map(arg -> (User) arg)
                                                  .findAny();
        if (userOptional.isPresent() == false) {
            return pjp.proceed();
        }

        final AllowPublicAccess annotation = method.getAnnotation(AllowPublicAccess.class);
        User user = userOptional.get();

        if (Objects.isNull(annotation) && user instanceof PublicUser) {
            throw new UnauthorizedException();
        }
        if (user instanceof PublicUser) {
            if (annotation.value()) {
                return pjp.proceed();
            }
            if (annotation.value() == false) {
                throw new UnauthorizedException();
            }
        }
        return pjp.proceed();
    }
}
