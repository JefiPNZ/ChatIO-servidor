package br.udesc.ceavi.dsd.chatio;

import java.io.BufferedReader;
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
    
    /**
     * Cria um novo cliente para se comunicar através do IP informado.
     * @param input  Objeto para leitura dos dados do cliente.
     * @param output Objeto para envio de dados para o cliente.
     * @param ip     Endereço IP do CLiente.
     */
    public ClientNode(BufferedReader input, PrintWriter output, String ip) {
        this.input = input;
        this.output = output;
        this.ip = ip;
    }

    @Override
    public void run() {
        
    }
    
}