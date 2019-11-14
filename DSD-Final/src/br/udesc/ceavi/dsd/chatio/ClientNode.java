package br.udesc.ceavi.dsd.chatio;

import br.udesc.ceavi.dsd.chatio.commands.CommandInvoker;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommand;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Nodo de um cliente.<br/>
 * Armazena a conexão com aquele cliente e realiza o controle da comunicação.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ClientNode implements Runnable {
    
    private final BufferedReader input;
    private final PrintWriter    output;
    private final Socket         connection;
    private String               ip;
    private CommandInvoker       invoker;
    private ServerCommandFactory factory;
    private String               login;
    private long                 timeoutMiliseconds;
    private boolean              connected;
    
    /**
     * Cria um novo cliente para se comunicar através do IP informado.
     * @param input  Objeto para leitura dos dados do cliente.
     * @param output Objeto para envio de dados para o cliente.
     * @param ip     Endereço IP do CLiente.
     */
    public ClientNode(Socket connection, BufferedReader input, PrintWriter output, String ip) {
        this.timeoutMiliseconds = 0;
        this.connection = connection;
        this.input      = input;
        this.output     = output;
        this.ip         = ip;
        this.factory    = new ServerCommandFactory();
        this.connected  = true;
        this.invoker    = new CommandInvoker();
        this.notifyConnected();
    }

    @Override
    public void run() {
        while (this.connected) {
            String message;
            try {
                if(connection.isConnected() && input.ready()) {
                    message = input.readLine();
                    if(message != null){
                        Server.getInstance().notifyMessageForUser("Comando recebido: " + message + " de " + this.ip);
                        ServerCommand command = this.getCommandFromMessage(message);
                        if(command != null){
                            command.setExecutor(this.login != null ? this.login : this.ip);
                            invoker.executeCommand(command);
                            Server.getInstance().notifyMessageForUser("Retorno do comando do Usuário " + (this.login != null ? this.login : this.ip) + ": " + command.getResult());
                            output.println(command.getResult());
                        }
                        else {
                            Server.getInstance().notifyMessageForUser("Comando desconhecido: " + message);
                            output.println(MessageList.MESSAGE_ERROR + "{\"message\":\"Comando Desconhecido\"}");
                        }
                        this.notifyConnected();
                    }
                }
                try {
                    Thread.sleep(100);
                    // Se a conexão expirou, para a thread.
                    if(this.timeoutMiliseconds < System.currentTimeMillis()){
                        this.disconnect();
                    }
                }
                catch (InterruptedException ex){
                    this.disconnect();
                    break;
                }
            } catch (IOException ex) {
                this.disconnect();
                break;
            } catch(Exception ex) {
                output.println(MessageList.MESSAGE_ERROR +  "{\"mensagem\":\"" + ex.getClass().toString() + ":" + ex.getMessage() + "\"}");
            }
        }
        Server.getInstance().notifyClientDisconected(this);
    }
    
    /**
     * Notifica que o cliente esta conectado.
     */
    public void notifyConnected(){
        this.timeoutMiliseconds = System.currentTimeMillis() + 10 * 1000; // Sessão expira depois de 10 sec sem resposta.
    }
    
    /**
     * Retorna o comando para execução a partir da mensagem.
     * @param message 
     * @return  
     */
    public ServerCommand getCommandFromMessage(String message){
        if(message.equals("CLEAR>" + Server.TEST_UNIQUE_TOKEN)){
            // Comando específico para limpar a base para testes.
            Server.getInstance().notifyMessageForUser("Limpando arquivo da base de testes.");
            File dbFile  = new File("server.db");
            if(dbFile.exists()){
                dbFile.delete();
                if(dbFile.exists()){
                    Server.getInstance().notifyMessageForUser("Não foi possivel remover o arquivo de banco de dados.");
                }
            }
            return null;
        }
        else {
            return factory.createCommand(message);
        }
    }
    
    /**
     * Desconecta o cliente, terminando a Thread.
     */
    public void disconnect(){
        this.connected = false;
        try {
            this.connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientNode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public String getIp() {
        return ip;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
}