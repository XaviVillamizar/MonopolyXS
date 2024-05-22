/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopooly;

import javax.swing.*;
import java.util.Map;

/**
 *
 * @author xavis
 */
public class Compras {
    private Jugador[] jugadoresObjetos;
    private JTextArea eventosTablero;
    private Map<Integer, Casilla> mapaCasillas;
    private int turnoActual;
    private int[] posicionesJugadores;
    private JButton comprarCasilla;
    private JButton lanzarDado;
    private JTextArea bienesJugador;
    private JButton cancelarCompra;
    private int numeroJugadores;

    public Compras(Jugador[] jugadoresObjetos, JTextArea eventosTablero, JTextArea bienesJugador, Map<Integer, Casilla> mapaCasillas, int turnoActual, int[] posicionesJugadores, JButton comprarCasilla, JButton lanzarDado, JButton cancelarCompra, int numeroJugadores) {
        this.jugadoresObjetos = jugadoresObjetos;
        this.eventosTablero = eventosTablero;
        this.bienesJugador = bienesJugador;
        this.mapaCasillas = mapaCasillas;
        this.turnoActual = turnoActual;
        this.posicionesJugadores = posicionesJugadores;
        this.comprarCasilla = comprarCasilla;
        this.lanzarDado = lanzarDado;
        this.cancelarCompra = cancelarCompra;
        this.numeroJugadores = numeroJugadores;
    }

    public void comprarCasilla() {
        int jugador = turnoActual;
        int posicion = posicionesJugadores[jugador];
        Casilla casilla = obtenerCasilla(posicion);

        if (casilla.isComprada()) {
            eventosTablero.append("La casilla " + casilla.getNombre() + " ya ha sido comprada.\n");
        } else {
            jugadoresObjetos[jugador].gastarDinero(casilla.getPrecio());
            eventosTablero.append("Jugador " + (jugador + 1) + " ha gastado $" + casilla.getPrecio() + " en " + casilla.getNombre() + "\n");
            casilla.setPropietario(jugador);
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");

            turnoActual = (turnoActual + 1) % numeroJugadores;
            eventosTablero.append("\n");
            eventosTablero.append("Nuevo turno: Jugador " + (turnoActual + 1) + "\n");
        }

        comprarCasilla.setEnabled(false);
        lanzarDado.setEnabled(true);
        cancelarCompra.setEnabled(true);

        actualizarInfoJugador();
    }

    private Casilla obtenerCasilla(int posicion) {
        return mapaCasillas.get(posicion);
    }

    private void actualizarInfoJugador() {
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
}
