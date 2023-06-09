/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Auxiliar;

import Pecas.Peca;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.util.ArrayList;

/**
 *
 * @author ArthurHR
 */
public interface Movimentos {
    //boolean temAlvo();
    
    ArrayList<Posicao> movimentosPossiveis();
    ArrayList<Posicao> ataquesPossiveis();
    
    double limiteMovimento();
    boolean direcaoMovimento(Posicao pIncremento);
}
