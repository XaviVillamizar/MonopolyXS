/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopooly;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
/**
 *
 * @author xavis
 */
public class Dado implements ActionListener {

    private int[] posicionesJugadores;
    private int turnoActual;
    private tableroMain main;
    private int numeroJugadores;
    private JTextArea eventosTablero;
    private JButton cancelarCompra;
    private JLabel dadoNum;
    private JLabel dadoNum1;

    public Dado(tableroMain main, int[] posicionesJugadores, int turnoActual, int numeroJugadores, JTextArea eventosTablero, JButton cancelarCompra, JLabel dadoNum, JLabel dadoNum1) {
        this.main = main;
        this.posicionesJugadores = posicionesJugadores;
        this.turnoActual = turnoActual;
        this.numeroJugadores = numeroJugadores;
        this.eventosTablero = eventosTablero; 
        this.cancelarCompra = cancelarCompra;
        this.dadoNum = dadoNum;
        this.dadoNum1 = dadoNum1;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
    cancelarCompra.setEnabled(true);
        int dado = (int)(Math.random() * 6) + 1;  // Simula un dado real de 6 caras
        eventosTablero.append("\n" + "Dado: " + dado + "\n");
        dadoNum.setText(""+dado);
        dadoNum1.setText(""+dado);

        int nuevaPosicion = (posicionesJugadores[turnoActual] + dado) % 40;
        eventosTablero.append("Jugador " + (turnoActual + 1) + " se mueve a la posición " + nuevaPosicion + "\n");

        JPanel casillaActual = main.obtenerPanel(posicionesJugadores[turnoActual]);
        casillaActual.setVisible(false);

        JPanel nuevaCasilla = main.obtenerPanel(nuevaPosicion);
        nuevaCasilla.setVisible(true);

        posicionesJugadores[turnoActual] = nuevaPosicion;

        main.moverJugador(turnoActual, nuevaPosicion);

        eventosTablero.append("Esperando la decision de el jugador " + (turnoActual + 1) + "\n");
        turnoActual = (turnoActual + 1) % numeroJugadores;
    }
}