
class Reader implements Runnable{
    RW_Lock rwlock;

    public void run() {
        try{
            rwlock.readLock();
            wait(500);
            rwlock.readUnlock();
        }
        catch(Throwable t){
            System.out.print("Reader Error: ");
            System.out.println(t);
        }
    }
}

class Writer implements Runnable{
    RW_Lock rwlock;

    public void run() {
        try{
            rwlock.writeLock();
            wait(500);
            rwlock.writeUnlock();
        }
        catch(Throwable t){
            System.out.print("Writer Error: ");
            System.out.println(t);
        }
    }
}



public class Ex3 {

    public static void main(String[] args){
        System.out.println("welele");

        RW_Lock rw = new RW_Lock();







    }

}
