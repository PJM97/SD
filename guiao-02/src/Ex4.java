class Conta{
    private double valor;

    Conta(){
        this.valor=0;
    }

    public synchronized double consultar(){
        return this.valor;
    }

    public synchronized void depositar(double valor){
        this.valor+=valor;
    }

    public synchronized void levantar(double valor){
        this.valor-=valor;
    }
}

class Novo_Banco{
    private Conta[] contas;

    Novo_Banco(int n){
        this.contas = new Conta[n];
        for(int i=0;i<n;i++)
            this.contas[i] = new Conta();
    }

    public double consultar(int conta){
        return this.contas[conta].consultar();
    }

    public void depositar(int conta, double valor){
        this.contas[conta].depositar(valor);
    }

    public void levantar(int conta, double valor){
        this.contas[conta].levantar(valor);
    }

    public void transferir(int origem, int destino, double valor){
        int min = Math.min(origem,destino);
        int max = Math.max(origem,destino);
        synchronized (this.contas[min]){
            synchronized (this.contas[max]){
                this.levantar(origem,valor);
                this.depositar(destino,valor);
            }
        }
    }
}


class Cliente_1 implements Runnable{
    private Novo_Banco banco;

    Cliente_1(Novo_Banco b){
        this.banco=b;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
        this.banco.transferir(0,1,1000);
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));

    }
}

class Cliente_2 implements Runnable{
    private Novo_Banco banco;

    Cliente_2(Novo_Banco b){
        this.banco=b;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
        this.banco.transferir(1,0,1000);
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
    }
}

// Main
public class Ex4 {
    public static void main(String[] args) throws Exception {
        int n_contas = 10;
        Novo_Banco banco = new Novo_Banco(n_contas);
        Thread T[] = new Thread[2];
        T[0] = new Thread(new Cliente_1(banco));
        T[0].setName("Thread0");
        T[1] = new Thread(new Cliente_2(banco));
        T[1].setName("Thread1");

        T[0].start();
        T[1].start();

        T[0].join();
        T[1].join();

        System.out.println(banco.consultar(0));
        System.out.println(banco.consultar(1));
    }
}
