package Xadrez;

import Pecas.Peao;
import Pecas.Cavalo;
import Pecas.Torre;
import Pecas.Bispo;
import Pecas.Rainha;
import Pecas.Rei;
import auxiliar.Posicao;

public class Main {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Jogo tMeuJogo = new Jogo();
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,0), true), Jogo.CoresConjuntos.BRANCAS);
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,1), true), Jogo.CoresConjuntos.BRANCAS);
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,2), true), Jogo.CoresConjuntos.BRANCAS);
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,3), true), Jogo.CoresConjuntos.BRANCAS);                
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,4), true), Jogo.CoresConjuntos.BRANCAS);
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,5), true), Jogo.CoresConjuntos.BRANCAS);
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,6), true), Jogo.CoresConjuntos.BRANCAS);
                tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,7), true), Jogo.CoresConjuntos.BRANCAS);  
                tMeuJogo.addPeca(new Torre("TorreBranca.png", new Posicao(7,7), true), Jogo.CoresConjuntos.BRANCAS);                  
                tMeuJogo.addPeca(new Torre("TorreBranca.png", new Posicao(7,0), true), Jogo.CoresConjuntos.BRANCAS);                  
                tMeuJogo.addPeca(new Cavalo("CavaloBranco.png", new Posicao(7,1), true), Jogo.CoresConjuntos.BRANCAS);                  
                tMeuJogo.addPeca(new Cavalo("CavaloBranco.png", new Posicao(7,6), true), Jogo.CoresConjuntos.BRANCAS);                                  
                tMeuJogo.addPeca(new Bispo("BispoBranco.png", new Posicao(7,5), true), Jogo.CoresConjuntos.BRANCAS);                                  
                tMeuJogo.addPeca(new Bispo("BispoBranco.png", new Posicao(7,2), true), Jogo.CoresConjuntos.BRANCAS);                                  
                tMeuJogo.addPeca(new Rainha("RainhaBranca.png", new Posicao(7,3), true), Jogo.CoresConjuntos.BRANCAS);                                  
                tMeuJogo.addPeca(new Rei("ReiBranco.png", new Posicao(7,4), true), Jogo.CoresConjuntos.BRANCAS);                                                  
                
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,0), false), Jogo.CoresConjuntos.PRETAS);                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,1), false), Jogo.CoresConjuntos.PRETAS);                                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,2), false), Jogo.CoresConjuntos.PRETAS);                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,3), false), Jogo.CoresConjuntos.PRETAS);                                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,4), false), Jogo.CoresConjuntos.PRETAS);                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,5), false), Jogo.CoresConjuntos.PRETAS);                                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,6), false), Jogo.CoresConjuntos.PRETAS);                 
                tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,7), false), Jogo.CoresConjuntos.PRETAS);                                 
                tMeuJogo.addPeca(new Torre("TorrePreta.png", new Posicao(0,7), false), Jogo.CoresConjuntos.PRETAS);                  
                tMeuJogo.addPeca(new Torre("TorrePreta.png", new Posicao(0,0), false), Jogo.CoresConjuntos.PRETAS);                  
                tMeuJogo.addPeca(new Cavalo("CavaloPreto.png", new Posicao(0,1), false), Jogo.CoresConjuntos.PRETAS);                  
                tMeuJogo.addPeca(new Cavalo("CavaloPreto.png", new Posicao(0,6), false), Jogo.CoresConjuntos.PRETAS);                                  
                tMeuJogo.addPeca(new Bispo("BispoPreto.png", new Posicao(0,5), false), Jogo.CoresConjuntos.PRETAS);                                  
                tMeuJogo.addPeca(new Bispo("BispoPreto.png", new Posicao(0,2), false), Jogo.CoresConjuntos.PRETAS);                                  
                tMeuJogo.addPeca(new Rainha("RainhaPreta.png", new Posicao(0,3), false), Jogo.CoresConjuntos.PRETAS);                                  
                tMeuJogo.addPeca(new Rei("ReiPreto.png", new Posicao(0,4), false), Jogo.CoresConjuntos.PRETAS);                                                  

                
                tMeuJogo.setVisible(true);                
                tMeuJogo.go();
            }
        });
    }
}

