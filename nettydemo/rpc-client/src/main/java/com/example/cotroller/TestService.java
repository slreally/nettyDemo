package com.example.cotroller;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.TreeSet;

@Service
public class TestService {

    public static void main(String[] args) {
        String s = "-0.13 ~ -0.01";
        String[] split = s.split("-");
        TreeSet<String> maxValue = new TreeSet<>();
        for (String s1 : split) {
//            maxValue.add(s1);
            System.out.println(s1);
        }
        maxValue.add("1");
        maxValue.add("-3");
        maxValue.add("2");
        System.out.println(maxValue.last());
    }
}
