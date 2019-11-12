package br.udesc.ceavi.dsd.chatio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principal do servidor.<br/>
 * Realiza o controle de clientes e conexões.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class Server {
    
    private List<ClientNode> clients;
    private ServerSocket     socket;
    
    public static final int SERVER_CONNECTION_PORT = 56000;
    
    private static Server instance;
    /**
     * Retorna a instância do servidor ou cria uma nova caso não exista.
     * @return 
     */
    public static Server getInstance(){
        if(instance == null){
            instance = new Server();
        }
        return instance;
    }
    
    private Server(){
        super();
        try {
            this.socket = new ServerSocket(SERVER_CONNECTION_PORT);
            this.socket.setReuseAddress(true);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método principal para execução da aplicação.
     * @param args 
     */
    public static void main(String[] args) {
        Server serverInstance = Server.getInstance();
        serverInstance.listen();
    }
    
    /**
     * Começa a ouvir conexões vindas.
     */
    private void listen() {
        while(true){
            try {
                Socket         connection = this.socket.accept();
                PrintWriter    out        = new PrintWriter(connection.getOutputStream(), true);
                BufferedReader in         = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                this.initClient(out, in, connection.getInetAddress().getHostAddress() + ":" + connection.getPort());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void initClient(PrintWriter out, BufferedReader in, String ip){
        ClientNode node = new ClientNode(in, out, ip);
        clients.add(node);
        new Thread(node).start();
    }
    
}
