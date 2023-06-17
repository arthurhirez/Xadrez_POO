package Xadrez;

import Auxiliar.Consts;
import auxiliar.Posicao;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;


public class Tabuleiro extends JPanel implements Serializable{
    Graphics g2;
    Jogo j;

    Tabuleiro(){
        j = null;
    }
    

  
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        /*64 é o numedo de quadrantes de um tabuleiro de xadrez*/
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((j + i) % 2 == 0) {
                    g2d.setColor(Color.lightGray);
                } else {
                    g2d.setColor(Color.gray);
                }
                g2d.fillRect(j * Consts.SIZE, i * Consts.SIZE,
                        Consts.SIZE, Consts.SIZE);
            }
        }
    }
    
    public void destacarPosicao(Posicao posicaoSelecionada, boolean moveOuAtaca) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        int x = posicaoSelecionada.getColuna() * Consts.SIZE;
        int y = posicaoSelecionada.getLinha() * Consts.SIZE;

        if(moveOuAtaca){
            g2d.setColor(Color.yellow);
        }else{
            g2d.setColor(Color.black);
        }
        // Desenhar um retângulo de destaque na posição selecionada
        
        g2d.setStroke(new BasicStroke(10));
        g2d.drawRect(x, y, Consts.SIZE, Consts.SIZE);
    }
    
    
    public void preencherPosicao(Posicao posicaoSelecionada, boolean moveOuAtaca) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        int x = posicaoSelecionada.getColuna() * Consts.SIZE;
        int y = posicaoSelecionada.getLinha() * Consts.SIZE;

        // Definir cor azul clara com transparência de 50%
        Color azulClaroTranslucido = new Color(173, 216, 230, 128); // R, G, B, Alpha
        Color vermelhoTranslucido = new Color(255, 0, 0, 128); // R, G, B, Alpha


        // Definir o modo de composição para permitir a transparência
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

        // Preencher a posição com a cor definida
        // 
        if(moveOuAtaca){
            g2d.setColor(azulClaroTranslucido);
        }else{
            g2d.setColor(vermelhoTranslucido);
            this.destacarPosicao(posicaoSelecionada, moveOuAtaca);
        }
        
        g2d.fillRect(x, y, Consts.SIZE, Consts.SIZE);
    }
    
    public void destaqueXeque(Posicao posicaoSelecionada) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        int x = posicaoSelecionada.getColuna() * Consts.SIZE;
        int y = posicaoSelecionada.getLinha() * Consts.SIZE;


        g2d.setColor(Color.red);

        // Desenhar um retângulo de destaque na posição selecionada
        
        g2d.setStroke(new BasicStroke(15));
        g2d.drawRect(x, y, Consts.SIZE, Consts.SIZE);
    }
    
    
    
    


}
