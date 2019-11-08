package br.udesc.ceavi.dsd.chatio.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para execução dos comandos do servidor.
 * @author Bruno Galeazzi Rech, Jeferson Penz
 */
public class CommandInvoker {
    
    private List<ServerCommand> executed;
    private List<ServerCommand> waiting;

    /**
     * Inicia a classe para execução dos comandos.
     */
    public CommandInvoker() {
        executed = new ArrayList<>();
        waiting = new ArrayList<>();
    }

    /**
     * Adiciona um comando para a fila de execução.
     * @param command 
     */
    public void addCommand(ServerCommand command) {
        this.executed.add(command);
    }

    /**
     * Executa um comando instantâneamente sem afetar a fila.
     * @param command 
     */
    public void executeCommand(ServerCommand command) {
        command.execute();
        this.executed.add(command);
    }

    /**
     * Executa todos os comandos na fila.
     */
    public void executeQueue() {
        this.waiting.forEach((command) -> {
            executeCommand(command);
        });
        this.waiting.clear();
    }
    
}
