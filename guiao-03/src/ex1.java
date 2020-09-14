import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

class ContaInvalida extends Exception {
    public ContaInvalida(String message) {
        super(message);
    }
}

class Conta{
    private int id;
    private double valor;
    private ReentrantLock lock_Conta;

    Conta(int id){
        this.id = id;
        this.valor=0;
        this.lock_Conta = new ReentrantLock();
    }

    public synchronized double consultar(){
        return this.valor;
    }

    public void depositar(double valor){
        this.lock_Conta.lock();
        this.valor+=valor;
        this.lock_Conta.unlock();
    }

    public void levantar(double valor){
        this.lock_Conta.lock();
        this.valor-=valor;
        this.lock_Conta.unlock();
    }

    public void lock()   {this.lock_Conta.lock();}
    public void unlock() {this.lock_Conta.unlock();}
}

class Banco{
    private HashMap<Integer,Conta> contas;
    private ReentrantLock lock_Banco;

    Banco(int n){
        this.contas = new HashMap<>();
        this.lock_Banco = new ReentrantLock();
    }

    public double consultar(int conta){
        return this.contas.get(conta).consultar();
    }

    public void depositar(int id, double valor) throws ContaInvalida{
        if(!contas.containsKey(id)) {
            throw new ContaInvalida(String.valueOf(id));
        }

        this.contas.get(id).lock();

        this.contas.get(id).unlock();
    }

    public void levantar(int conta, double valor){
        this.contas.get(conta).levantar(valor);
    }

    public void transferir(int origem, int destino, double valor) throws Exception{
        int min = Math.min(origem,destino);
        int max = Math.max(origem,destino);

        this.contas.get(min).lock();
        this.contas.get(max).lock();

        this.levantar(origem,valor);
        this.depositar(destino,valor);

        this.contas.get(max).unlock();
        this.contas.get(min).unlock();

    }
}


class Cliente_1 implements Runnable{
    private Banco banco;


    Cliente_1(Banco b){
        this.banco=b;
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
            this.banco.transferir(0,1,1000);
            System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
        } catch (Exception e){

        }

    }
}

class Cliente_2 implements Runnable{
    private Banco banco;

    Cliente_2(Banco b){
        this.banco=b;
    }

    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
            this.banco.transferir(1,0,1000);
            System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
        } catch (Exception e){

        }
    }
}

// Main
public class ex1 {
    public static void main(String[] args) throws Exception {
        int n_contas = 10;
        Banco banco = new Banco(n_contas);
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

