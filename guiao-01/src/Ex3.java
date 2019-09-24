import java.lang.Runnable;

class Counter2 {
    private int count;

    public Counter2()       {this.count=0;}
    public int getCounter() {return this.count;}
    public void increment() {
        synchronized (this){
            this.count++;
        }
    }
    // OU «, se eu quiser protejer o método completo:
    public synchronized void increment2(){
        this.count++;
    }
}

class IncrementerRunnable2 implements Runnable{
    private Counter2 counter;
    private int maxInc;
    public IncrementerRunnable2(Counter2 c, int i){
        this.counter = c;
        this.maxInc = i;
    }
    public void run(){
        for(int i = 0; i<this.maxInc; i++) {
            this.counter.increment();
        }
    }
}

public class Ex3 {
    public static void main(String[] args) throws Exception {
        int N = 10;
        int I = 10000;
        Counter2 c = new Counter2();

        Thread T[] = new Thread[N];

        for(int i=0;i<N;i++){
            IncrementerRunnable2 r = new IncrementerRunnable2(c,I);
            T[i] = new Thread(r);
        }

        System.out.println("Antes");
        for(int i=0;i<N;i++)
            T[i].start();

        System.out.println("Depois");

        for(int i=0;i<N;i++)
            T[i].join();

        System.out.println("Fim");
        System.out.println(c.getCounter());
    }
}

