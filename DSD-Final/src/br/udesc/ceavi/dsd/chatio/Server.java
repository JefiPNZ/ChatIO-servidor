package br.udesc.ceavi.dsd.chatio;

import java.util.List;

/**
 * Classe principal do servidor.<br/>
 * Realiza o controle de clientes e conexões.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class Server {
    
    private List<ClientNode> clientes;
    
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
    
    /**
     * Método principal para execução da aplicação.
     * @param args 
     */
    public static void main(String[] args) {
        
    }
    
}
