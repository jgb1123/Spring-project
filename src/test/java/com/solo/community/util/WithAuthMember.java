package com.solo.community.util;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithAuthMemberSecurityContextFactory.class)
public @interface WithAuthMember {
    String email();
    String[] roles();
}
