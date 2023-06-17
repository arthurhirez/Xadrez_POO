/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Auxiliar;

import auxiliar.Posicao;

/**
 *
 * @author ArthurHR
 */
public interface Movimentos {
    
    // Qual o limite de casas que cada peca pode se mover
    double limiteMovimento();
    
    // Define as regras de movimento das pecas
    boolean direcaoMovimento(Posicao pIncremento);
}
