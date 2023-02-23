package com.example.demo;

import org.springframework.http.ResponseEntity;

public class Person{
    private String first;
    private String second;
    private int age;

    public Person(){
    }

    public Person(String david, String levi, int i) {
        this.first = david;
        this.second = levi;
        this.age =i;
    }

    public String getFirst() {
        return first;
    }

    public String getSecond() {
        return second;
    }

    public int getAge() {
        return age;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
