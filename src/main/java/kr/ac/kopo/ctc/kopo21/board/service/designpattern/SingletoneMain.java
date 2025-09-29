package kr.ac.kopo.ctc.kopo21.board.service.designpattern;

public class SingletoneMain {
    public static void main(String[] args) {
        Singleton i1 = Singleton.getInstance();
        Singleton i2 = Singleton.getInstance();
        Singleton i3 = Singleton.getInstance();

        System.out.println(i1.toString());
        System.out.println(i2.toString());
        System.out.println(i3.toString());
        System.out.println(i1 == i2);

    }
}