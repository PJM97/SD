class Banco{
    private double[] contas;
        Banco(int n){
        this.contas = new double[n];
        for(int i=0;i<n;i++){
            this.contas[i] = 0;
        }
    }

    public double consultar(int conta){
        return this.contas[conta];
    }

    public synchronized void depositar(int conta, double valor){
        this.contas[conta]+=valor;
    }

    public synchronized void levantar(int conta, double valor){
        this.contas[conta]-=valor;
    }
}

class Cliente1 implements Runnable{
    private Banco banco;

    Cliente1(Banco b){
        this.banco=b;
    }

    public void run() {
        for(int i=0;i<100000;i++)
            this.banco.depositar(0,0.5);
    }
}

class Cliente2 implements Runnable{
    private Banco banco;

    Cliente2(Banco b){
        this.banco=b;
    }

    public void run() {
        for(int i=0;i<100000;i++)
            this.banco.levantar(0,0.5);
    }
}

// Main
public class Ex1 {
    public static void main(String[] args) throws Exception {
        int n_contas = 10;
        Banco banco = new Banco(n_contas);
        Thread T[] = new Thread[2];
        T[0] = new Thread(new Cliente1(banco));
        T[1] = new Thread(new Cliente2(banco));

        T[0].start();
        T[1].start();

        T[0].join();
        T[1].join();

        System.out.println(banco.consultar(0));
    }
}
