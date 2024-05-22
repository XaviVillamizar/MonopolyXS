/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monopooly;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 *
 * @author xavis
 */

public class Tablero extends JFrame {
    private Map<Integer, Casilla> mapaCasillas;
    private JLabel[] jugadores;
    
    
    public static Map<Integer, Casilla> inicializarMapaCasillas() {
        Map<Integer, Casilla> mapaCasillas = new HashMap<>();
        
        mapaCasillas.put(0, new Casilla("Salida", 0));
        mapaCasillas.put(1, new Casilla("Mediterranean Avenue", 60));
        mapaCasillas.put(2, new Casilla("Community Chest 1", 0));
        mapaCasillas.put(3, new Casilla("Baltic Avenue", 60));
        mapaCasillas.put(4, new Casilla("Impuestos", 200));
        mapaCasillas.put(5, new Casilla("Tren Reading", 200));
        mapaCasillas.put(6, new Casilla("Oriental Avenue", 100));
        mapaCasillas.put(7, new Casilla("Suerte 1", 0));
        mapaCasillas.put(8, new Casilla("Vermont Avenue", 100));
        mapaCasillas.put(9, new Casilla("Connecticut Avenue", 120));
        mapaCasillas.put(10, new Casilla("En Carcel", 0));
        mapaCasillas.put(11, new Casilla("St. Charles Place", 140));
        mapaCasillas.put(12, new Casilla("Compañía de Electricidad", 150));
        mapaCasillas.put(13, new Casilla("States Avenue", 140));
        mapaCasillas.put(14, new Casilla("Virginia Avenue", 160));
        mapaCasillas.put(15, new Casilla("Tren Pensilvania", 200));
        mapaCasillas.put(16, new Casilla("St. James Place", 180));
        mapaCasillas.put(17, new Casilla("Community Chest 2", 0));
        mapaCasillas.put(18, new Casilla("Tennessee Avenue", 180));
        mapaCasillas.put(19, new Casilla("New York Avenue", 200));
        mapaCasillas.put(20, new Casilla("Parqueadero", 0));
        mapaCasillas.put(21, new Casilla("Kentucky Avenue", 220));
        mapaCasillas.put(22, new Casilla("Suerte 2", 0));
        mapaCasillas.put(23, new Casilla("Indiana Avenue", 220));
        mapaCasillas.put(24, new Casilla("Illinois Avenue", 240));
        mapaCasillas.put(25, new Casilla("Tren ByO", 200));
        mapaCasillas.put(26, new Casilla("Atlantic Avenue", 260));
        mapaCasillas.put(27, new Casilla("Ventnor Avenue", 260));
        mapaCasillas.put(28, new Casilla("Compañía de Agua", 150));
        mapaCasillas.put(29, new Casilla("Marvin Gardens", 280));
        mapaCasillas.put(30, new Casilla("Ve a la Cárcel", 0));
        mapaCasillas.put(31, new Casilla("Pacific Avenue", 300));
        mapaCasillas.put(32, new Casilla("North Carolina Avenue", 300));
        mapaCasillas.put(33, new Casilla("Community Chest 3", 0));
        mapaCasillas.put(34, new Casilla("Pennsylvania Avenue", 320));
        mapaCasillas.put(35, new Casilla("Tren Linea Corta", 200));
        mapaCasillas.put(36, new Casilla("Suerte 3", 0));
        mapaCasillas.put(37, new Casilla("Park Place", 350));
        mapaCasillas.put(38, new Casilla("Impuesto de Lujo", 100));
        mapaCasillas.put(39, new Casilla("Paseo Marítimo", 400));
        
        return mapaCasillas;
    }
}
