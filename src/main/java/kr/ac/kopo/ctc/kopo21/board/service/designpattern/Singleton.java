package kr.ac.kopo.ctc.kopo21.board.service.designpattern;

 class Singleton {
    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

}


