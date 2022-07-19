package com.missionchecker.annotation;

import com.missionchecker.domain.MemberRole;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
    MemberRole value() default MemberRole.MEMBER;
}
