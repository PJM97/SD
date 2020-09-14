import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Item{
    ReentrantLock lock;
    Condition isEmpty;
    int quantity;

    public Item(){
        this.quantity=0;
        this.lock=new ReentrantLock();
        this.isEmpty=this.lock.newCondition();
    }

    public synchronized void supply(int quantity){
        this.quantity+=quantity;
        isEmpty.signalAll();
    }
/*
    public void consume(){
        try{
            // esperar quando nao houver unidades
            while(quantity==0){
                System.out.println("Consumer: não há unidades suficientes");
                isEmpty.await();
            }
            // decrementar uma unidade



        }

        this.quantity--;

    }
*/
    void lock(){
        this.lock.lock();
    }

    void unlock(){
        this.lock.unlock();
    }
}
/*
class Warehouse{
    private HashMap<String,Item> stack;

    void supply(String item, int quantity){
        stack.get(item).supply(quantity);
    }

    void consume(String[] items){
        for (String i:items) {
            Item I=stack.get(i);
            I.lock();
            I.consume();
            I.unlock();
        }
    }
}

class Producer implements Runnable{
    private Warehouse wh;

    public void run(){
        this.wh.supply("item 1", 1);
        wait(3);
        this.wh.supply("item 2", 1);
        wait(3);
        this.wh.supply("item 3", 1);
    }
}

class Consumer implements Runnable{
    private Warehouse wh;

    public void run(){

    }
}

*/
//Main
public class Ex1 {
}
