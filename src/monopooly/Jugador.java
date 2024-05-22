/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopooly;

import javax.swing.JLabel;



/**
 *
 * @author xavis
 */
import javax.swing.JLabel;

public class Jugador {
    private int posicion;
    private JLabel label;
    private int dinero;

    public Jugador(JLabel label, int dineroInicial) {
        this.posicion = 0;
        this.label = label;
        this.dinero = dineroInicial;

    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public JLabel getLabel() {
        return label;
    }

    public int getDinero() {
        return dinero;
    }
    
    public void gastarDinero(int cantidad) {
        dinero -= cantidad;
    }
    
    public void modificarDinero(int cantidad) {
        this.dinero += cantidad;
    }
    
    
}
