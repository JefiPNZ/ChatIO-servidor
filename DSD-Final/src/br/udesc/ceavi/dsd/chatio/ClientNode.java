package br.udesc.ceavi.dsd.chatio;

import br.udesc.ceavi.dsd.chatio.commands.CommandInvoker;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommand;
import br.udesc.ceavi.dsd.chatio.commands.ServerCommandFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Nodo de um cliente.<br/>
 * Armazena a conexão com aquele cliente e realiza o controle da comunicação.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class ClientNode implements Runnable {
    
    private BufferedReader input;
    private PrintWriter output;
    private String ip;
    private CommandInvoker invoker;
    private ServerCommandFactory factory;
    private boolean connected;
    
    /**
     * Cria um novo cliente para se comunicar através do IP informado.
     * @param input  Objeto para leitura dos dados do cliente.
     * @param output Objeto para envio de dados para o cliente.
     * @param ip     Endereço IP do CLiente.
     */
    public ClientNode(BufferedReader input, PrintWriter output, String ip) {
        this.input     = input;
        this.output    = output;
        this.ip        = ip;
        this.factory   = new ServerCommandFactory();
        this.connected = true;
        this.invoker   = new CommandInvoker();
    }

    @Override
    public void run() {
        while (this.connected) {
            String message;
            try {
                message = input.readLine();
                if(message != null){
                    System.out.println("Comando recebido: " + message);
                    ServerCommand command = this.getCommandFromMessage(message);
                    invoker.executeCommand(command);
                    output.println(command.getResult());
                }
            } catch (IOException ex) {
                this.connected = false;
                break;
            } catch(Exception ex) {
                output.println(MessageList.MESSAGE_ERROR +  "{\"mensagem\":\"" + ex.getMessage() + "\"}");
            }
        }
    }
    
    /**
     * Retorna o comando para execução a partir da mensagem.
     * @param message 
     * @return  
     */
    public ServerCommand getCommandFromMessage(String message){
        return factory.createCommand(message);
    }
    
    /**
     * Desconecta o cliente, terminando a Thread.
     */
    public void disconnect(){
        this.connected = false;
    }
    
}