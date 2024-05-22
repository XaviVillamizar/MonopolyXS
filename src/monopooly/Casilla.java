/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopooly;

import javax.swing.JPanel;

/**
 *
 * @author xavis
 */
public class Casilla {
    private String nombre;
    private int precio;
    private int propietario; // Cambiado a tipo int para representar el jugador propietario

    public Casilla(String nombre, int precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.propietario = -1; // Inicializado a -1 para indicar que no tiene propietario al principio
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isComprada() {
        return propietario != -1; // La casilla está comprada si tiene un propietario distinto de -1
    }

    public int getPropietario() {
        return propietario;
    }

    public void setPropietario(int propietario) {
        this.propietario = propietario;
    }
}