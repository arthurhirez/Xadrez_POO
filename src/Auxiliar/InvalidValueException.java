/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Auxiliar;

/**
 *
 * @author ArthurHR
 */
public class InvalidValueException extends Exception{
    public InvalidValueException () { }
    public InvalidValueException (String msg) {
        super("Posição inválida!!\nEstá fora dos limites do tabuleiro!!");
    }
    
}
