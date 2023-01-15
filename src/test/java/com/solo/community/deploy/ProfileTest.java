package com.solo.community.deploy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

import static org.assertj.core.api.Assertions.*;


public class ProfileTest {

    @Test
    void profile_real1() {
        //given
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile("real1");
        ProfileController profileController = new ProfileController(env);

        //when
        String profile = profileController.profile();

        //then
        assertThat(profile).isEqualTo("real1");
    }

    @Test
    void profile_real2() {
        //given
        MockEnvironment env = new MockEnvironment();
        env.addActiveProfile("real2");
        ProfileController profileController = new ProfileController(env);

        //when
        String profile = profileController.profile();

        //then
        assertThat(profile).isEqualTo("real2");
    }

    @Test
    void profile_default() {
        //given
        MockEnvironment env = new MockEnvironment();
        ProfileController profileController = new ProfileController(env);

        //when
        String profile = profileController.profile();

        //then
        assertThat(profile).isEqualTo("default");
    }
}
