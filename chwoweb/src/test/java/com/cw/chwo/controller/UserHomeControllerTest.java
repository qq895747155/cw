package com.cw.chwo.controller;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by handl on 2017/5/21.
 */

public class UserHomeControllerTest {
    @Test
    public void userHomeTest(){
      /*  UserHomeController userHomeController = new UserHomeController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userHomeController).build();
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/"))
                    .andExpect(MockMvcResultMatchers.view()
                            .name("home"));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static void main(String[] args) {
        Integer i = 100;
        Integer i2 = 100;

        Integer i3 = 1000;
        Integer i4 = 1000;

        System.out.println(i==i2);
        System.out.println(i3==i4);
    }
}
