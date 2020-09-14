

class BoundedBuffer implements Runnable{
    //private int size = 10;
    private int[] values;
    private int poswrite;

    BoundedBuffer(){

    }

    public void run(){

    }

    synchronized void put(int v) throws Exception {
        while(poswrite==values.length){
            this.wait();
        }
        values[poswrite]=v;
        poswrite++; // a zona critica é a (de)incrementação da posição de escrita
        this.notifyAll();
    }

    synchronized int get(int v) throws Exception{
        while (poswrite==0){
            wait();
        }
        int res=values[poswrite-1];
        poswrite--;
        this.notifyAll();
        return res;
    }
}


//Main
public class Ex1 {

}
