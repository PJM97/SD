import java.util.List;

class ContaInvalida extends Exception{
    private String msg;

    public ContaInvalida(String msg){
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }
}

class SaldoInsuficiente extends Exception{
    private String msg;

    public SaldoInsuficiente(String msg){
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }
}

class Movimento{
    private int id_operacao;
    private String descricao;
    double valor, saldo_resultante;

    public Movimento (int id_operacao, String descritivo, double valor, double saldo_resultante){
        this.id_operacao=id_operacao;
        this.descricao=descritivo;
        this.valor=valor;
        this.saldo_resultante=saldo_resultante;
    }
}

interface InterfaceBanco {
    int criarConta(double saldo);
    double fecharConta(int id) throws ContaInvalida;
    double consultar(int id) throws ContaInvalida;
    double consultarTotal(int[] contasInput);
    void levantar(int id, double valor) throws SaldoInsuficiente, ContaInvalida;
    void depositar(int id, double valor) throws ContaInvalida;
    void transferir(int conta_origem, int conta_destino, double valor) throws ContaInvalida, SaldoInsuficiente;
    List<Movimento> movimentos(int id) throws ContaInvalida;
}


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

class Banco{
    private Conta[] contas;

    Banco(int n){
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
    private Banco banco;

    Cliente_1(Banco b){
        this.banco=b;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
        this.banco.transferir(0,1,1000);
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));

    }
}

class Cliente_2 implements Runnable{
    private Banco banco;

    Cliente_2(Banco b){
        this.banco=b;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
        this.banco.transferir(1,0,1000);
        System.out.println(Thread.currentThread().getName() +" C0:"+ banco.consultar(0)+" C1:"+ banco.consultar(1));
    }
}

// Main
public class Ex1 {
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
