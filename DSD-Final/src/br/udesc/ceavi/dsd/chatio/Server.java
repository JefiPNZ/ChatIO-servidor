package br.udesc.ceavi.dsd.chatio;

import br.udesc.ceavi.dsd.chatio.data.ChatUserDao;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Classe principal do servidor.<br/>
 * Realiza o controle de clientes e conexões.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class Server {
    
    private List<ClientNode> clients;
    private ServerSocket     socket;
    
    public static final int SERVER_CONNECTION_PORT = 56000;
    
    // Token utilizado para os comandos de testes.
    public static final String TEST_UNIQUE_TOKEN = "7f77d5c2b5667c7b2302a6ce8dd5f000";
    
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
        this.clients = new ArrayList<>();
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
                System.out.println("Aguardando conexão na porta " + this.socket.getLocalPort());
                Socket         connection = this.socket.accept();
                System.out.println("Conectado!");
                PrintWriter    out        = new PrintWriter(connection.getOutputStream(), true);
                BufferedReader in         = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                this.initClient(out, in, connection.getInetAddress().getHostAddress() + ":" + connection.getPort());
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Busca o cliente com o login informado.
     * @param login 
     * @return  
     */
    public ClientNode findClientConnectionByLogin(String login){
        for (ClientNode client : this.clients) {
            if(client.getLogin() != null && client.getLogin().equals(login)){
                return client;
            }
        }
        return null;
    }
    
    /**
     * Busca o cliente com o ip informado.
     * @param ip
     * @return 
     */
    public ClientNode findClientConnectionByIp(String ip){
        for (ClientNode client : this.clients) {
            if(client.getIp().equals(ip)){
                return client;
            }
        }
        return null;
    }
    
    /**
     * Notifica uma mensagem para o Usuário.
     * @param message 
     */
    public void notifyMessageForUser(String message){
        System.out.println(message);
    }
    
    public void initClient(PrintWriter out, BufferedReader in, String ip){
        if(this.findClientConnectionByIp(ip) != null){
            this.notifyMessageForUser("Usuário já conectado do ip" + ip);
        }
        else {
            this.notifyMessageForUser("Usuário conectou do ip " + ip);
        }
        ClientNode node = new ClientNode(in, out, ip);
        this.clients.add(node);
        new Thread(node).start();
    }
    
}
