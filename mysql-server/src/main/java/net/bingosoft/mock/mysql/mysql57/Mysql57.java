package net.bingosoft.mock.mysql.mysql57;

import net.bingosoft.mock.mysql.AuthHandler;
import net.bingosoft.mock.mysql.Authentication;
import net.bingosoft.mock.mysql.CommandHandler;
import net.bingosoft.mock.mysql.MysqlServer;
import net.bingosoft.mock.mysql.protocol.MysqlPacket;
import net.bingosoft.mock.mysql.protocol.v10.MysqlPacketFactory;
import net.bingosoft.mock.mysql.protocol.v10.payload.CommandPayload;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author kael.
 */
public class Mysql57 implements MysqlServer {
    
    private static Logger log = Logger.getLogger(Mysql57.class.getName());
    
    private volatile  boolean started = false;
    private int port;
    private final ServerSocket ss;
    private Thread serverThread;
    private ExecutorService pool = Executors.newFixedThreadPool(10);
    private AuthHandler authHandler = new Mysql57AuthHandler();
    private CommandHandler commandHandler = new Mysql57CommandHandler();
    
    public Mysql57(int port) throws IOException {
        ss = new ServerSocket(port);
        initServerThread();
    }

    @Override
    public void start() {
        if (!serverThread.isAlive()){
            started = true;
            serverThread.start();
        }else {
            started = true;
        }
    }
    
    protected void initServerThread(){
        serverThread = new Thread(this::waitForConnect);
        serverThread.setDaemon(true);
        serverThread.setName("mysql57");
    }
    
    protected void waitForConnect(){
        do {
            try {
                Socket s = ss.accept();
                try {
                    Authentication auth = authHandler.authenticate(s);
                    Future<Socket> future = pool.submit(() -> waitForCommand(s, auth), s);
                    future.get();
                } catch (AuthenticationException e) {
                    log.log(Level.INFO, "authenticate fail, refuse connect", e);
                } catch (InterruptedException e) {
                    log.log(Level.INFO, "connection interrupted, try to close connection.", e);
                } catch (ExecutionException e) {
                    log.log(Level.INFO, "server error, try to close connection.", e);
                }finally {
                    if (null != s && !s.isClosed()){
                        try {
                            s.close();
                        } catch (IOException e) {
                            log.log(Level.SEVERE, "close socket fail", e);
                        }
                    }
                }
            } catch (IOException e) {
                log.log(Level.SEVERE, "accept client connect fail", e);
                break;
            }
        }while (started);
    }
    
    protected void waitForCommand(Socket socket, Authentication authentication) {
        do {
            try {
                MysqlPacket<CommandPayload> mp = MysqlPacketFactory.readMysqlPacket(socket.getInputStream());
                boolean result = commandHandler.handle(socket,mp,authentication);
                if (!result){
                    break;
                }
            }catch (IOException e){
                throw new UncheckedIOException(e);
            }
        }while (true);
    }
    
}
