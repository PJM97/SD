import java.lang.Runnable;

class N_Runnable implements Runnable {
    private int maxI;
    public void run() {
        for (int i = 1; i <= maxI; i++) {
            System.out.println(i);
        }
    }

    N_Runnable(int a) { maxI=a; }
}


public class Main {
    public static void main(String[] args) throws Exception {
        int N = 3;
        int I = 100;

        Thread T[] = new Thread[N];

        for(int i=0;i<N;i++){
            N_Runnable r = new N_Runnable(I);
            T[i] = new Thread(r);
        }

        System.out.println("Antes");
        for(int i=0;i<N;i++)
            T[i].start();

        System.out.println("Depois");

        for(int i=0;i<N;i++)
            T[i].join();

        System.out.println("Fim");
    }
}

