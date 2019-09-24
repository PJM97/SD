import java.lang.Runnable;

class Counter {
    private int count;

    public Counter()          {this.count=0;}
    public void increment()   {this.count++;}
    public int getCounter()   {return this.count;}
}

class IncrementerRunnable implements Runnable{
    private Counter counter;
    private int maxInc;
    public IncrementerRunnable(Counter c, int i){
        this.counter = c;
        this.maxInc = i;
    }
    public void run(){
        for(int i = 0; i<this.maxInc; i++) {
            this.counter.increment();
        }
    }
}

public class Ex2 {
    public static void main(String[] args) throws Exception {
        int N = 10;
        int I = 10000;
        Counter c = new Counter();

        Thread T[] = new Thread[N];

        for(int i=0;i<N;i++){
            IncrementerRunnable r = new IncrementerRunnable(c,I);
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

