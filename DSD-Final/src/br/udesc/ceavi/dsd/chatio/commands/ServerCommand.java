package br.udesc.ceavi.dsd.chatio.commands;

/**
 * Classe para execução dos comandos no servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public interface ServerCommand {
    
    /**
     * Realiza a execução do comando desejado.
     */
    public void execute();
    
    /**
     * Retorna o resultado da execução do comando.
     * @return 
     */
    public String getResult();
    
    /**
     * Define quem está executando o comando.
     * @param executor
     */
    public void setExecutor(String executor);
    
    /**
     * Define os parâmetros do comando.
     * @param params 
     */
    public void setParams(String params);
}
