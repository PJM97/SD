import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class RW_Lock {
    ReentrantLock lock;
    Condition readerWait, writerWait;
    int readers = 0, writer = 0, writersRequests = 0;

    void readLock() throws InterruptedException{
        lock.lock();
        //while (writer>0)
        while (writer>0 || writersRequests>0) // para inverter a starvation (agora os escritores têm prioridade, enquanto que os readers ficam à espera)
                                              // alternativa, podemos garantir um ambiente 'fair', onde são intercalados os escritores e leitores (pode ser por tempo, pode ter um contador que garante um determinado numero de readers e wirters... => tudo isto vai depender dos requisitos da aplicação)
            this.readerWait.await();
        readers++;
        lock.unlock();
    }

    void readUnlock() {
        lock.lock();
        readers--;
        if(readers==0)
            writerWait.signal();
        lock.unlock();
    }

    void writeLock() throws InterruptedException{
        lock.lock();
        writersRequests++;
        while (readers>0 || writer>0)
            this.writerWait.await();
        writersRequests--;
        writer=1;
        lock.unlock();
    }

    void writeUnlock(){
        lock.lock();
        writer=0;
        writerWait.signal(); // só pode escrever um writer de cada vez
        readerWait.signalAll(); // podem existir vários readers à espera para ler (em simultâneo)
        // em caso de dúvida usar sempre o signalAll...
        lock.unlock();
    }


}
