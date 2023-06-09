package Xadrez;

import Auxiliar.Consts;
import Pecas.*;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Jogo extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Tabuleiro tTabuleiro;//atributo com a janela de desenho
    Conjunto cBrancas;
    Conjunto cPretas;
    boolean bEmJogada;
    Peca pecaEmMovimento;
    int controleMouse;

    public enum CoresConjuntos {

        BRANCAS, PRETAS
    };

    public Jogo() {
        cBrancas = new Conjunto();
        cPretas = new Conjunto();
        //tTabuleiro = new Tabuleiro(cBrancas, cPretas);//alocação do painel de desenho
        tTabuleiro = new Tabuleiro();
        tTabuleiro.setFocusable(false);
        tTabuleiro.addMouseListener(this);//Adiciona evento de mouse ao Painel de desenho
        tTabuleiro.addKeyListener(this);
        bEmJogada = false;
        pecaEmMovimento = null;
        controleMouse = -1;
        initComponents();
    }

    // Inicializa pecas e suas posicoes na main
    public void addPeca(Peca aPeca, CoresConjuntos aCorConjunto) {
        //aPeca.setTabuleiro(this.tTabuleiro);
        if (aCorConjunto == CoresConjuntos.BRANCAS) {
            cBrancas.add(aPeca);
        } else {
            cPretas.add(aPeca);
        }
    }

    // Retorna objeto que foi selecionado pelo jogador
    public Peca getPecaClicada(Posicao aPosicao) {
        Peca pTemp = cBrancas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        pTemp = cPretas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        return null;
    }
    
    

    
    
    
    // Funcao grafica - DELETAR
    protected void mostraMovimento(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            for(Posicao pMove : this.pecaEmMovimento.movimentosPossiveis()){
                boolean flag = true;
                for(Peca pBloqueia : this.cBrancas){
                    if(pBloqueia.getPosicaoPeca().igual(pMove)){
                        flag = false;
                        break;
                    }
                }

                for(Peca pBloqueia : this.cPretas){
                    if(pBloqueia.getPosicaoPeca().igual(pMove)){
                        flag = false;
                        break;
                    }
                }
                if(flag) this.tTabuleiro.preencherPosicao(pMove, true);
                else break; 
            }
        }
    }
    
    protected ArrayList<Posicao> osAlemao(){
        if(this.pecaEmMovimento != null){
            ArrayList<Posicao> meteBala = new ArrayList<>();
            
            return meteBala;
        }
        return null;
    }
    
    protected ArrayList<Posicao> praOnde(ArrayList<Posicao> osAlemao){
        if(this.pecaEmMovimento != null){
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            ArrayList<Posicao> ondePode = new ArrayList<>();
            ondePode.add(atual);
            
            int distAtual = 0;
            int colAtual = atual.getColuna();
            int linAtual = atual.getLinha();
            Posicao posAtual;
            
            // Move SUL
            System.out.println("NOVA TESTA: " + linAtual + " " + colAtual + " limite: " + pecaEmMovimento.limiteMovimento());
            while(++distAtual <= pecaEmMovimento.limiteMovimento() &&
                    pecaEmMovimento.direcaoMovimento(new Posicao(atual.getLinha() + 1, atual.getColuna()))){
                if(linAtual == 7) break;
                posAtual = new Posicao(++linAtual, colAtual);
                if(!this.cBrancas.PecaPosicao(posAtual) && !this.cPretas.PecaPosicao(posAtual)){
                    ondePode.add(posAtual);
                }else{
                    if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(posAtual)))
                        osAlemao.add(posAtual);
                    break;
                }
            }
                    
            // Move NORTE
            distAtual = 0;
            colAtual = atual.getColuna();
            linAtual = atual.getLinha();
            while(++distAtual <= pecaEmMovimento.limiteMovimento() &&
                    pecaEmMovimento.direcaoMovimento(new Posicao(atual.getLinha() - 1, atual.getColuna()))){
                if(linAtual == 0) break;
                posAtual = new Posicao(--linAtual, colAtual);
                if(!this.cBrancas.PecaPosicao(posAtual) && !this.cPretas.PecaPosicao(posAtual)){
                    ondePode.add(posAtual);
                }else{
                    if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(posAtual)))
                        osAlemao.add(posAtual);
                    break;
                }
            }

            
            // Move ESQUERDA
            distAtual = 0;
            colAtual = atual.getColuna();
            linAtual = atual.getLinha();
            while(++distAtual <= pecaEmMovimento.limiteMovimento() &&
                    pecaEmMovimento.direcaoMovimento(new Posicao(atual.getLinha(), atual.getColuna() - 1))){
                if(colAtual == 0) break;
                posAtual = new Posicao(linAtual, --colAtual);
                if(!this.cBrancas.PecaPosicao(posAtual) && !this.cPretas.PecaPosicao(posAtual)){
                    ondePode.add(posAtual);
                }else{
                    if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(posAtual)))
                        osAlemao.add(posAtual);
                    break;
                }
            }
            
            // Move DIREITA
            distAtual = 0;
            colAtual = atual.getColuna();
            linAtual = atual.getLinha();
            while(++distAtual <= pecaEmMovimento.limiteMovimento() &&
                    pecaEmMovimento.direcaoMovimento(new Posicao(atual.getLinha(), atual.getColuna() + 1))){
                if(colAtual == 7) break;
                posAtual = new Posicao(linAtual, ++colAtual);
                if(!this.cBrancas.PecaPosicao(posAtual) && !this.cPretas.PecaPosicao(posAtual)){
                    ondePode.add(posAtual);
                }else{
                    if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(posAtual)))
                        osAlemao.add(posAtual);
                    break;
                }
            }
                

            
            System.out.println("NOVA FUNCAO TESTANDO: ");
            for(Posicao p : ondePode){
                System.out.println(p);
            }
               
            return ondePode;
        }
        
        return null;
    }
    
    protected ArrayList<Posicao> movimentoValido(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            ArrayList<Posicao> ondePode = new ArrayList<>();
            ondePode.add(atual);
            /*
            for(Posicao pMove : this.pecaEmMovimento.movimentosPossiveis()){
                System.out.println("MOVE: " + pMove);
                boolean flag = true;
                for(Peca pBloqueia : this.cBrancas){
                    if(pBloqueia.getPosicaoPeca().igual(pMove)){
                        flag = false;
                        break;
                    }
                }

                for(Peca pBloqueia : this.cPretas){
                    if(pBloqueia.getPosicaoPeca().igual(pMove)){
                        flag = false;
                        break;
                    }
                }
                if(flag) ondePode.add(pMove);
                else break;
            }
            */
            
            for(Posicao pMove : this.pecaEmMovimento.movimentosPossiveis()){
                int linhaColisao = -1;
                int colunaColisao = -1;
                if(!this.cBrancas.PecaPosicao(pMove) && !this.cPretas.PecaPosicao(pMove)){
                    if(linhaColisao != -1){
                        
                    }
                
                    if(colunaColisao != -1){
                        
                    }
                    
                    ondePode.add(pMove);
                }else{
                    linhaColisao = pMove.getLinha();
                    colunaColisao = pMove.getColuna();
                }
                     
                    
               
            }
            
            
            return ondePode;
        }
        return null;
    }
    
   
    protected ArrayList<Posicao> ataqueValido(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            ArrayList<Posicao> ondePode = new ArrayList<>();
            
            for(Posicao pAtaque : this.pecaEmMovimento.ataquesPossiveis()){
                
                    for(Peca pAlvo : this.cBrancas){
                       if((!this.pecaEmMovimento.temAMesmaCorQue(pAlvo)) && (pAlvo.getPosicaoPeca().igual(pAtaque))){
                            ondePode.add(pAtaque);
                        }
                    }

                    for(Peca pAlvo : this.cPretas){
                        if((!this.pecaEmMovimento.temAMesmaCorQue(pAlvo)) && (pAlvo.getPosicaoPeca().igual(pAtaque))){
                            ondePode.add(pAtaque);
                        }
                    }
            }
            return ondePode;
        }
        return null;
    }
    
    
    protected void destaqueDoCarnaval(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            this.tTabuleiro.destacarPosicao(atual, true);
            
            ArrayList<Posicao> ajudaNois = this.osAlemao();
            ArrayList<Posicao> levaAiTio = this.praOnde(ajudaNois);
            
            System.out.println("TA FALANDO PRA METE BALA:");
            
            for(Posicao p : ajudaNois){
                System.out.println(p);
            }
            
            // Mostra movimentos possiveis - botao direito
            if(this.controleMouse == 3){
                for(Posicao pMove : levaAiTio){
                    this.tTabuleiro.preencherPosicao(pMove, true);
                }
            }

            // Mostra ataques possiveis - botao scroll (meio)
            if(this.controleMouse == 2){
                // Verificacao ataques possiveis
                for(Posicao pAtaque : ajudaNois){
                    this.tTabuleiro.preencherPosicao(pAtaque, false);
                }
            }

        }
    }

                

    protected void destaquePecaAtual(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            this.tTabuleiro.destacarPosicao(atual, true);
            
            // Mostra movimentos possiveis - botao direito
            if(this.controleMouse == 3){
                for(Posicao pMove : this.movimentoValido()){
                    this.tTabuleiro.preencherPosicao(pMove, true);
                }
            }

            // Mostra ataques possiveis - botao scroll (meio)
            if(this.controleMouse == 2){
                // Verificacao ataques possiveis
                for(Posicao pAtaque : this.ataqueValido()){
                    this.tTabuleiro.preencherPosicao(pAtaque, false);
                }
            }

        }
    }
    
    /*
    protected void destaquePecaAtual(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            this.tTabuleiro.destacarPosicao(atual, true);
            
            // Mostra movimentos possiveis - botao direito
            if(this.controleMouse == 3){
                // Verificacao movimentos possíveis
                // obs so cavalo pode pular sobre outras pecas
                for(Posicao pMove : this.pecaEmMovimento.movimentosPossiveis()){
                    boolean flag = true;
                    for(Peca pBloqueia : this.cBrancas){
                        if(pBloqueia.getPosicaoPeca().igual(pMove)){
                            flag = false;
                            break;
                        }
                    }

                    for(Peca pBloqueia : this.cPretas){
                        if(pBloqueia.getPosicaoPeca().igual(pMove)){
                            flag = false;
                            break;
                        }
                    }
                    if(flag) this.tTabuleiro.preencherPosicao(pMove, true);
                    else break;
                }
            }
            
            // Mostra ataques possiveis - botao scroll (meio)
            if(this.controleMouse == 2){
                // Verificacao ataques possiveis
                for(Posicao pAtaque : this.pecaEmMovimento.ataquesPossiveis()){

                    for(Peca pAlvo : this.cBrancas){
                       if((!this.pecaEmMovimento.temAMesmaCorQue(pAlvo)) && (pAlvo.getPosicaoPeca().igual(pAtaque))){
                            this.tTabuleiro.preencherPosicao(pAtaque, false);
                        }
                    }

                    for(Peca pAlvo : this.cPretas){
                        if((!this.pecaEmMovimento.temAMesmaCorQue(pAlvo)) && (pAlvo.getPosicaoPeca().igual(pAtaque))){
                            this.tTabuleiro.preencherPosicao(pAtaque, false);
                        }
                    }
                }
            }

        }
    }
*/
    public void paint(Graphics g) {
        super.paint(g);
        cBrancas.AutoDesenho(tTabuleiro);
        cPretas.AutoDesenho(tTabuleiro);    
        /*
        Posicao teste = new Posicao(5,5);
        this.tTabuleiro.destacarPosicao(teste);
        
        Posicao teste2 = new Posicao(4,3);
        this.tTabuleiro.preencherPosicao(teste2);
        */
        
        destaqueDoCarnaval();
        //destaquePecaAtual();
        //mostraMovimento();
        
        
    }

    public void go() {       
        TimerTask task = new TimerTask() {
            public void run() {
                repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.DELAY);
        
    }

    public Posicao getPosicaoDoClique(MouseEvent aMouseEvent) {
        return new Posicao(aMouseEvent.getY() / Consts.SIZE,
                aMouseEvent.getX() / Consts.SIZE);
    }
    

    public void mousePressed(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        this.clickLabel.setText("x:" + x + "  y:" + y + "   -   Quadrante: [" + y / Consts.SIZE + "," + x / Consts.SIZE + "]");     


        // Controle para mostrar dica de ataque/movimento possivel
        this.controleMouse = e.getButton();
        
        // Objeto peca clicada
        Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e));       
        
        // Output tela sem jogada ativa
        if (!bEmJogada){
            if(pecaClicada == null)
                System.out.println("Nenhuma peca selecionada");
            else
                System.out.println("Peca " + pecaClicada + " selecionada");
        }

        // Peca que vai executar movimento ja selecionada
        if(bEmJogada){
            if(pecaClicada != null){
                System.out.println("ta falando que aqui da");
                for(Posicao p : this.movimentoValido()){
                    System.out.println(p);
                }
                //this.praOnde();
            }
            // Selecionou uma posicao vazia
            if (pecaClicada == null){
                // Condicao acabou movimento ou esta em movimento
                System.out.println(pecaEmMovimento);
                if(this.movimentoValido().contains(getPosicaoDoClique(e))){
                    pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro);
                    pecaEmMovimento = null;
                    bEmJogada = false;
                }else{
                    System.out.println("AVISO 1: Jogada ainda em movimento, selecione uma posicao valida");
                }
                
            // Selecionou uma peca
            }else{     
                System.out.println("selecionou essa coisa :" + pecaClicada.getPosicaoPeca());
                System.out.println("PRIMEIRO TESTE:" + this.movimentoValido().contains(pecaClicada.getPosicaoPeca()));
                System.out.println("SEGUNDO TESTE:" + this.ataqueValido().contains(pecaClicada.getPosicaoPeca()));

                //if(pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro)){
                if(this.ataqueValido().contains(pecaClicada.getPosicaoPeca()) || pecaEmMovimento == pecaClicada){
                    pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro);
                    // Selecionou mesma peca
                    if(pecaEmMovimento == pecaClicada){
                        System.out.println("A peca foi deselecionada, escolha outra peca");
                    }


                    if(!pecaEmMovimento.temAMesmaCorQue(pecaClicada)){
                        cBrancas.pecaFora(pecaClicada);
                        cPretas.pecaFora(pecaClicada);

                        
                }
                
                    
                // Fim da jogada
                
                pecaEmMovimento = null;
                bEmJogada = false;
                    
                // Selecionou uma peca invalida
                }else{
                    System.out.println("aviso 2: Jogada ainda em movimento, selecione uma posicao valida");
                }
            }
        // Selecao da peca que vai executar a jogada
        }else{
            if (pecaClicada != null) {           
                System.out.println("Movimentacao em andamento, selecione o destino da peca");
                pecaEmMovimento = pecaClicada;
                bEmJogada = true;
            }
        }

        repaint();
    }
    
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_S){
            /* "jogoSalvos"+File.pathSeparator+"Nome" */
            File tanque = new File("Teste_Save_Peca.dat");
            try {
                tanque.createNewFile();
                FileOutputStream canoOut = new FileOutputStream(tanque);
                GZIPOutputStream compactador = new GZIPOutputStream(canoOut);
                ObjectOutputStream serializador = new ObjectOutputStream(compactador);
                serializador.writeObject(this.cBrancas);
                serializador.writeObject(this.cPretas);
                serializador.flush();
                serializador.close();
                System.out.println("Jogo salvo!");
            } catch (Exception ex) {
                System.out.println("Ocorreu o seguinte erro:" + ex.getMessage());
            }
            
        }
        
        if(e.getKeyCode() == KeyEvent.VK_O){
            File tanque = new File("Teste_Save_Peca.dat");
            try {
                FileInputStream canoIn = new FileInputStream(tanque);
                GZIPInputStream descompactador = new GZIPInputStream(canoIn);
                ObjectInputStream deserializador = new ObjectInputStream(descompactador);
                this.cBrancas = (Conjunto) deserializador.readObject();    
                this.cPretas = (Conjunto) deserializador.readObject();
                deserializador.close();
                System.out.println("Jogo carregado!");  
            } catch (Exception ex) {
                System.out.println("Ocorreu o seguinte erro:" + ex.getMessage());
            }
        }

        repaint();

    }

    public void mouseClicked(MouseEvent e) {
        /*Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e)); 
        if(pecaClicada != null){
            this.tTabuleiro.destacarPosicao(pecaClicada.getPosicaoPeca());
        }*/
        
    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        /*
        Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e)); 
        if(pecaClicada != null){
            this.tTabuleiro.destacarPosicao(pecaClicada.getPosicaoPeca());
        }
        */
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanelCenario = tTabuleiro;
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        coordenadaLabel = new javax.swing.JLabel();
        clickLabel = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SCC0204 - Xadrez");
        setResizable(false);

        jPanelCenario.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setMinimumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setPreferredSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout jPanelCenarioLayout = new javax.swing.GroupLayout(jPanelCenario);
        jPanelCenario.setLayout(jPanelCenarioLayout);
        jPanelCenarioLayout.setHorizontalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );
        jPanelCenarioLayout.setVerticalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 800, Short.MAX_VALUE)
        );

        jLabel2.setText("Coordenada:");

        jLabel3.setText("click:");

        coordenadaLabel.setText("10");

        clickLabel.setText("quadrante");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clickLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(coordenadaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(293, 293, 293))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(clickLabel)
                        .addComponent(coordenadaLabel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clickLabel;
    private javax.swing.JLabel coordenadaLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelCenario;
    // End of variables declaration//GEN-END:variables

}
