package br.udesc.ceavi.dsd.chatio;

import br.udesc.ceavi.dsd.chatio.data.Contact;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
     * Gera o Hash da senha para armazenamento no banco.
     * @param password
     * @return 
     */
    public String passwordHash(String password){
        // Hash atual é um SHA-512.
        try {
            byte[] bytes = password.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            BigInteger bigInt = new BigInteger(1, md.digest(bytes));
            password = bigInt.toString(16);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return password;
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
                this.notifyMessageForUser("Aguardando conexão na porta " + this.socket.getLocalPort());
                Socket         connection = this.socket.accept();
                PrintWriter    out        = new PrintWriter(connection.getOutputStream(), true);
                BufferedReader in         = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String         ip         = connection.getInetAddress().getHostAddress() + ":" + connection.getPort();
                this.initClient(connection, out, in, ip);
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
    
    public void loadContactListStatus(List<Contact> contacts, String executor){
        contacts.forEach((contact) -> {
            ClientNode client;
            if(contact.getContact().getNickname().equals(executor)){
                client = this.findClientConnectionByLogin(contact.getUser().getNickname());
            }
            else {
                client = this.findClientConnectionByLogin(contact.getContact().getNickname());
            }
            if(client != null){
                contact.setOnline(true);
                contact.setIp(client.getIp());
            }
            else {
                contact.setOnline(false);
                contact.setIp("");
            }
        });
    }
    
    /**
     * Notifica uma mensagem para o Usuário.
     * @param message 
     */
    public void notifyMessageForUser(String message){
        System.out.println(message);
    }
    
    public void initClient(Socket connection, PrintWriter out, BufferedReader in, String ip){
        if(this.findClientConnectionByIp(ip) != null){
            this.notifyMessageForUser("Usuário já conectado do ip" + ip);
        }
        else {
            this.notifyMessageForUser("Usuário conectou do ip " + ip);
        }
        ClientNode node = new ClientNode(connection, in, out, ip);
        this.clients.add(node);
        new Thread(node).start();
    }
    
    public void notifyClientDisconected(ClientNode client){
        this.notifyMessageForUser("Usuário de ip " + client.getIp() + " foi desconectado...");
        this.clients.remove(client);
    }
    
}
