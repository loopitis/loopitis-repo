package test;

interface Bla {
    void koko();

}

public class TestInterface {

    public static void main(String[] args) {
        System.out.println("test start");
        doSomething(SimpleClass::simple);
        SimpleClass s = new SimpleClass();
        doSomething(() -> s.simple2());
    }


    public static void doSomething(Bla b) {
        b.koko();
    }

}

class SimpleClass {

    public static void simple() {
        System.out.println("This is Simple Class");
    }


    public void simple2() {
        System.out.println("This is Simple22 Class");
    }


}

