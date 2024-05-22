/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopooly;

import java.util.Map;
import javax.swing.JTextArea;

/**
 *
 * @author xavis
 */
public class Juego {
    private Jugador[] jugadoresObjetos;
    private JTextArea bienesJugador;
    private Map<Integer, Casilla> mapaCasillas;
    private int turnoActual;

    public Juego(Jugador[] jugadoresObjetos, JTextArea bienesJugador, Map<Integer, Casilla> mapaCasillas, int turnoActual) {
        this.jugadoresObjetos = jugadoresObjetos;
        this.bienesJugador = bienesJugador;
        this.mapaCasillas = mapaCasillas;
        this.turnoActual = turnoActual;
    }

    public void actualizarInfoJugador() {
        int jugadorActual = turnoActual;
        Jugador jugador = jugadoresObjetos[jugadorActual];
        StringBuilder infoJugador = new StringBuilder();

        infoJugador.append("Dinero actual del jugador ").append(jugadorActual + 1).append(": $").append(jugador.getDinero()).append("\n");
        infoJugador.append("Propiedades del jugador ").append(jugadorActual + 1).append(":\n");
        for (Casilla casilla : mapaCasillas.values()) {
            if (casilla.getPropietario() == jugadorActual) {
                infoJugador.append("- ").append(casilla.getNombre()).append("\n");
            }
        }

        bienesJugador.setText(infoJugador.toString());
    }

    public void setTurnoActual(int turnoActual) {
        this.turnoActual = turnoActual;
    }
} 
