/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package monopooly;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;


/**
 *
 * @author xavis
 */
public class tableroMain extends javax.swing.JFrame {
    ////Tablero
    private int impuestosRecaudados = 0;
    private int[] posicionesJugadores;
    private int turnoActual = 0;
    private Map<Integer, Casilla> mapaCasillas;
    
    ////Jugadores
    private JPanel[] jugadores;
    private Jugador[] jugadoresObjetos;
    private int numeroJugadores;
    
    ////Clases
    private Juego juego;
    private Compras compras;
    

    public tableroMain(int numeroJugadores) {
        initComponents();
        this.setLocationRelativeTo(null);
        
        Tablero tablero = new Tablero();
        mapaCasillas = tablero.inicializarMapaCasillas();
            this.numeroJugadores = numeroJugadores;
            this.jugadores = new JPanel[numeroJugadores];
            this.jugadoresObjetos = new Jugador[numeroJugadores];
            this.posicionesJugadores = new int[numeroJugadores];
        inicializarJugadores(1500);
        
    ////Instancias    
        juego = new Juego(jugadoresObjetos, bienesJugador, mapaCasillas, turnoActual);
        compras = new Compras(jugadoresObjetos, eventosTablero, bienesJugador, mapaCasillas, turnoActual, posicionesJugadores, comprarCasilla, lanzarDado, regresar, numeroJugadores);
        lanzarDado.addActionListener(new Dado(this, posicionesJugadores, turnoActual, numeroJugadores, eventosTablero, regresar, dadoNum, dadoNum1));
        comprarCasilla.addActionListener(evt -> compras.comprarCasilla());
    }
    //////////////// inicializar jugadores
    private void inicializarJugadores(int dineroInicial) {
    Color[] colores = {
        new Color(255, 192, 203), new Color(173, 216, 230), new Color(230, 230, 250), new Color(255, 218, 185), new Color(255, 255, 153), new Color(152, 251, 152)   
    };

    for (int i = 0; i < numeroJugadores; i++) {
        jugadores[i] = new JPanel();
        jugadores[i].setBackground(colores[i % colores.length]);
        jugadores[i].setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Agregar un borde negro
        JLabel label = new JLabel("" + (i + 1));
        jugadores[i].add(label);
        jugadores[i].setPreferredSize(new Dimension(25, 25));
        jugadoresObjetos[i] = new Jugador(label, dineroInicial);
        posicionesJugadores[i] = 0;
    }
}
    
    //////////////// movimiento de los jugadores y casillas en el tablero
    public void moverJugador(int jugador, int posicion) {
    JPanel casillaActual = obtenerPanel(posicionesJugadores[jugador]);
    JPanel nuevaCasilla = obtenerPanel(posicion);

    if (nuevaCasilla != null) {
        casillaActual.removeAll();
        nuevaCasilla.add(jugadores[jugador]);
        jugadores[jugador].setBounds(0, 0, jugadores[jugador].getPreferredSize().width, jugadores[jugador].getPreferredSize().height);
        nuevaCasilla.revalidate();
        nuevaCasilla.repaint();
        posicionesJugadores[jugador] = posicion;
        Casilla casilla = obtenerCasilla(posicion);
        
        //////////////////////// Casilla de Salida, GO!!
        
        if (nuevaCasilla == salida) {
            int bonificacion = 200;
            jugadoresObjetos[jugador].modificarDinero(bonificacion);
            eventosTablero.append("Jugador " + (jugador + 1) + " ha pasado por Salida y ha ganado $" + bonificacion + "\n");
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");
}
        
        //////////////////////// Cofres comunitarios
        
         else if (nuevaCasilla == communityChest1 || nuevaCasilla == communityChest2 || nuevaCasilla == communityChest3) {
            jugadoresObjetos[jugador].gastarDinero(131);
            eventosTablero.append("Jugador " + (jugador + 1) + " ha caído en Cofre de la Comunidad y ha perdido $131\n");
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");
          
        //////////////////////// Impuestos casilla   
            
        } else if (nuevaCasilla == impuestos) {
            int cantidadImpuestos = casilla.getPrecio(); 
            impuestosRecaudados += cantidadImpuestos;
            jugadoresObjetos[jugador].gastarDinero(cantidadImpuestos);
            eventosTablero.append("Jugador " + (jugador + 1) + " ha caído en Impuestos y ha perdido $" + cantidadImpuestos + "\n");
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");
        
        
        //////////////////////// Suerte casilla 
            
        } else if (nuevaCasilla == suerte1 || nuevaCasilla == suerte2 || nuevaCasilla == suerte3){
           int cantidad = (Math.random() < 0.5) ? -100 : 200;
            jugadoresObjetos[jugador].modificarDinero(cantidad);
            eventosTablero.append((cantidad < 0 ? "Mala" : "Buena") + " suerte! Jugador " + (jugador + 1) + " ha " + (cantidad < 0 ? "perdido $" + Math.abs(cantidad) : "ganado $" + cantidad) + "\n");
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");
        }
        
        //////////////////////// Parquadero casilla 
        
        else if (nuevaCasilla == parqueadero) {
            int cantidadPremio = impuestosRecaudados / 2;
            jugadoresObjetos[jugador].modificarDinero(cantidadPremio); 
            impuestosRecaudados -= cantidadPremio; 
            eventosTablero.append("Jugador " + (jugador + 1) + " ha caído en Parqueadero Gratuito y ha ganado $" + cantidadPremio + "\n");
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");
        }
        
        //////////////////////// Ve carcel casilla 
        
        else if (nuevaCasilla == veACarcel){
            posicionesJugadores[jugador] = 10;
            eventosTablero.append("Jugador " + (jugador + 1) + " ha caído en Ve a la Cárcel y ha sido enviado a la cárcel\n");
            casillaActual.removeAll();
            JPanel casillaCárcel = obtenerPanel(10);
            casillaCárcel.add(jugadores[jugador]);
            casillaCárcel.revalidate();
            casillaCárcel.repaint();
        }
        
        else if (nuevaCasilla == impuestoLujoso){
             int dineroTotal = jugadoresObjetos[jugador].getDinero();
            int impuesto = (int) (dineroTotal * 0.3);
            jugadoresObjetos[jugador].gastarDinero(impuesto);
            eventosTablero.append("Jugador " + (jugador + 1) + " ha caído en Impuesto de Lujo y ha perdido $" + impuesto + "\n");
            eventosTablero.append("Dinero actual del jugador " + (jugador + 1) + ": $" + jugadoresObjetos[jugador].getDinero() + "\n");
        }
            
        else {
            JOptionPane.showMessageDialog(this, "Jugador " + (jugador + 1) + " ha caído en " + casilla.getNombre() + " con un precio de $" + casilla.getPrecio());
        }
        if (jugadoresObjetos[jugador].getDinero() <= 0) {
                finalizarJuego(jugador);
                lanzarDado.setEnabled(false);
                comprar.setEnabled(false);
                cancelarCompra.setEnabled(false);
            }
        }
}
    
    private void finalizarJuego(int jugadorPerdedor) {
    int opcion = JOptionPane.showOptionDialog(this,
            "El jugador " + (jugadorPerdedor + 1) + " se ha quedado sin dinero. ¡El juego ha terminado!",
            "Juego Terminado",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Aceptar"},
            "Aceptar");

    if (opcion == JOptionPane.OK_OPTION) {
        Main main = new Main();
        main.setVisible(true);
        dispose(); 
    }
}
      
    //////////////// get casillas del tablero
     JPanel obtenerPanel(int posicion) {
        switch (posicion) {
            case 0: return salida;
            case 1: return mediterraAvenue;
            case 2: return communityChest1;
            case 3: return balticAvenue;
            case 4: return impuestos;
            case 5: return trenReading;
            case 6: return orientalAvenue;
            case 7: return suerte1;
            case 8: return vermontAvenue;
            case 9: return connecticutAvenue;
            case 10: return enCarcel;
            
            case 11: return charlesPlace;
            case 12: return compElectricidad;
            case 13: return statesAvenue;
            case 14: return virginiaAvenue;
            case 15: return trenPensilvania;
            case 16: return jamesPlace;
            case 17: return communityChest2;
            case 18: return tenneseeAvenue;
            case 19: return newYorkAvenue;
            case 20: return parqueadero;
            
            case 21: return kentuckyAvenue;
            case 22: return suerte2;
            case 23: return indianaAvenue;
            case 24: return illinoisAvenue;
            case 25: return trenByO;
            case 26: return atlanticAvenue;
            case 27: return ventnorAvenue;
            case 28: return compAgua;
            case 29: return jardinesMarvin;
            case 30: return veACarcel;
            
            case 31: return pacificAvenue;
            case 32: return nCarolinaAvenue;
            case 33: return communityChest3;
            case 34: return penssylvaniAvenue;
            case 35: return trenLineaCorta;
            case 36: return suerte3;
            case 37: return parquePlace;
            case 38: return impuestoLujoso;
            case 39: return paseoMaritimo;
            default: return null;
        }
    }

    Casilla obtenerCasilla(int posicion) {
        return mapaCasillas.get(posicion);
    }
    
    

    public tableroMain() {
        
    }
    
    /////////////// Info del jugador en el txt
    
     public void actualizarInfoJugador() {
        juego.setTurnoActual(turnoActual);
        juego.actualizarInfoJugador();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel = new javax.swing.JPanel();
        Tablero = new javax.swing.JPanel();
        parqueadero = new javax.swing.JPanel();
        salida = new javax.swing.JPanel();
        veACarcel = new javax.swing.JPanel();
        enCarcel = new javax.swing.JPanel();
        mediterraAvenue = new javax.swing.JPanel();
        communityChest1 = new javax.swing.JPanel();
        impuestos = new javax.swing.JPanel();
        balticAvenue = new javax.swing.JPanel();
        trenReading = new javax.swing.JPanel();
        suerte1 = new javax.swing.JPanel();
        orientalAvenue = new javax.swing.JPanel();
        connecticutAvenue = new javax.swing.JPanel();
        vermontAvenue = new javax.swing.JPanel();
        compAgua = new javax.swing.JPanel();
        indianaAvenue = new javax.swing.JPanel();
        ventnorAvenue = new javax.swing.JPanel();
        trenByO = new javax.swing.JPanel();
        suerte2 = new javax.swing.JPanel();
        jardinesMarvin = new javax.swing.JPanel();
        kentuckyAvenue = new javax.swing.JPanel();
        atlanticAvenue = new javax.swing.JPanel();
        illinoisAvenue = new javax.swing.JPanel();
        newYorkAvenue = new javax.swing.JPanel();
        tenneseeAvenue = new javax.swing.JPanel();
        jamesPlace = new javax.swing.JPanel();
        communityChest2 = new javax.swing.JPanel();
        statesAvenue = new javax.swing.JPanel();
        compElectricidad = new javax.swing.JPanel();
        virginiaAvenue = new javax.swing.JPanel();
        trenPensilvania = new javax.swing.JPanel();
        charlesPlace = new javax.swing.JPanel();
        pacificAvenue = new javax.swing.JPanel();
        nCarolinaAvenue = new javax.swing.JPanel();
        communityChest3 = new javax.swing.JPanel();
        penssylvaniAvenue = new javax.swing.JPanel();
        trenLineaCorta = new javax.swing.JPanel();
        suerte3 = new javax.swing.JPanel();
        parquePlace = new javax.swing.JPanel();
        impuestoLujoso = new javax.swing.JPanel();
        paseoMaritimo = new javax.swing.JPanel();
        Dado = new javax.swing.JPanel();
        dadoNum = new javax.swing.JLabel();
        Dado1 = new javax.swing.JPanel();
        dadoNum1 = new javax.swing.JLabel();
        tableroBG = new javax.swing.JLabel();
        lanzarDado = new javax.swing.JButton();
        comprarCasilla = new javax.swing.JButton();
        regresar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        eventosTablero = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        bienesJugador = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        cancelarCompra = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        comprar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        Panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Tablero.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        parqueadero.setOpaque(false);

        javax.swing.GroupLayout parqueaderoLayout = new javax.swing.GroupLayout(parqueadero);
        parqueadero.setLayout(parqueaderoLayout);
        parqueaderoLayout.setHorizontalGroup(
            parqueaderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        parqueaderoLayout.setVerticalGroup(
            parqueaderoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        Tablero.add(parqueadero, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 2, 102, 91));

        salida.setOpaque(false);

        javax.swing.GroupLayout salidaLayout = new javax.swing.GroupLayout(salida);
        salida.setLayout(salidaLayout);
        salidaLayout.setHorizontalGroup(
            salidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        salidaLayout.setVerticalGroup(
            salidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        Tablero.add(salida, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 627, 102, 91));

        veACarcel.setOpaque(false);

        javax.swing.GroupLayout veACarcelLayout = new javax.swing.GroupLayout(veACarcel);
        veACarcel.setLayout(veACarcelLayout);
        veACarcelLayout.setHorizontalGroup(
            veACarcelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        veACarcelLayout.setVerticalGroup(
            veACarcelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        Tablero.add(veACarcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 2, 102, 91));

        enCarcel.setOpaque(false);

        javax.swing.GroupLayout enCarcelLayout = new javax.swing.GroupLayout(enCarcel);
        enCarcel.setLayout(enCarcelLayout);
        enCarcelLayout.setHorizontalGroup(
            enCarcelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 102, Short.MAX_VALUE)
        );
        enCarcelLayout.setVerticalGroup(
            enCarcelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 91, Short.MAX_VALUE)
        );

        Tablero.add(enCarcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 627, 102, 91));

        mediterraAvenue.setOpaque(false);

        javax.swing.GroupLayout mediterraAvenueLayout = new javax.swing.GroupLayout(mediterraAvenue);
        mediterraAvenue.setLayout(mediterraAvenueLayout);
        mediterraAvenueLayout.setHorizontalGroup(
            mediterraAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );
        mediterraAvenueLayout.setVerticalGroup(
            mediterraAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(mediterraAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 628, 68, 90));

        communityChest1.setOpaque(false);

        javax.swing.GroupLayout communityChest1Layout = new javax.swing.GroupLayout(communityChest1);
        communityChest1.setLayout(communityChest1Layout);
        communityChest1Layout.setHorizontalGroup(
            communityChest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        communityChest1Layout.setVerticalGroup(
            communityChest1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(communityChest1, new org.netbeans.lib.awtextra.AbsoluteConstraints(564, 628, 65, -1));

        impuestos.setOpaque(false);

        javax.swing.GroupLayout impuestosLayout = new javax.swing.GroupLayout(impuestos);
        impuestos.setLayout(impuestosLayout);
        impuestosLayout.setHorizontalGroup(
            impuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        impuestosLayout.setVerticalGroup(
            impuestosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(impuestos, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 628, -1, -1));

        balticAvenue.setOpaque(false);

        javax.swing.GroupLayout balticAvenueLayout = new javax.swing.GroupLayout(balticAvenue);
        balticAvenue.setLayout(balticAvenueLayout);
        balticAvenueLayout.setHorizontalGroup(
            balticAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        balticAvenueLayout.setVerticalGroup(
            balticAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(balticAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(498, 628, 65, 90));

        trenReading.setOpaque(false);

        javax.swing.GroupLayout trenReadingLayout = new javax.swing.GroupLayout(trenReading);
        trenReading.setLayout(trenReadingLayout);
        trenReadingLayout.setHorizontalGroup(
            trenReadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        trenReadingLayout.setVerticalGroup(
            trenReadingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(trenReading, new org.netbeans.lib.awtextra.AbsoluteConstraints(366, 628, -1, -1));

        suerte1.setOpaque(false);

        javax.swing.GroupLayout suerte1Layout = new javax.swing.GroupLayout(suerte1);
        suerte1.setLayout(suerte1Layout);
        suerte1Layout.setHorizontalGroup(
            suerte1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        suerte1Layout.setVerticalGroup(
            suerte1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(suerte1, new org.netbeans.lib.awtextra.AbsoluteConstraints(235, 628, -1, -1));

        orientalAvenue.setOpaque(false);

        javax.swing.GroupLayout orientalAvenueLayout = new javax.swing.GroupLayout(orientalAvenue);
        orientalAvenue.setLayout(orientalAvenueLayout);
        orientalAvenueLayout.setHorizontalGroup(
            orientalAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        orientalAvenueLayout.setVerticalGroup(
            orientalAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(orientalAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 628, -1, -1));

        connecticutAvenue.setOpaque(false);

        javax.swing.GroupLayout connecticutAvenueLayout = new javax.swing.GroupLayout(connecticutAvenue);
        connecticutAvenue.setLayout(connecticutAvenueLayout);
        connecticutAvenueLayout.setHorizontalGroup(
            connecticutAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        connecticutAvenueLayout.setVerticalGroup(
            connecticutAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(connecticutAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 628, -1, -1));

        vermontAvenue.setOpaque(false);

        javax.swing.GroupLayout vermontAvenueLayout = new javax.swing.GroupLayout(vermontAvenue);
        vermontAvenue.setLayout(vermontAvenueLayout);
        vermontAvenueLayout.setHorizontalGroup(
            vermontAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        vermontAvenueLayout.setVerticalGroup(
            vermontAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(vermontAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 628, -1, -1));

        compAgua.setOpaque(false);

        javax.swing.GroupLayout compAguaLayout = new javax.swing.GroupLayout(compAgua);
        compAgua.setLayout(compAguaLayout);
        compAguaLayout.setHorizontalGroup(
            compAguaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        compAguaLayout.setVerticalGroup(
            compAguaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(compAgua, new org.netbeans.lib.awtextra.AbsoluteConstraints(565, 2, -1, 90));

        indianaAvenue.setOpaque(false);

        javax.swing.GroupLayout indianaAvenueLayout = new javax.swing.GroupLayout(indianaAvenue);
        indianaAvenue.setLayout(indianaAvenueLayout);
        indianaAvenueLayout.setHorizontalGroup(
            indianaAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        indianaAvenueLayout.setVerticalGroup(
            indianaAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(indianaAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(239, 2, -1, -1));

        ventnorAvenue.setOpaque(false);

        javax.swing.GroupLayout ventnorAvenueLayout = new javax.swing.GroupLayout(ventnorAvenue);
        ventnorAvenue.setLayout(ventnorAvenueLayout);
        ventnorAvenueLayout.setHorizontalGroup(
            ventnorAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        ventnorAvenueLayout.setVerticalGroup(
            ventnorAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(ventnorAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(501, 2, -1, -1));

        trenByO.setOpaque(false);

        javax.swing.GroupLayout trenByOLayout = new javax.swing.GroupLayout(trenByO);
        trenByO.setLayout(trenByOLayout);
        trenByOLayout.setHorizontalGroup(
            trenByOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        trenByOLayout.setVerticalGroup(
            trenByOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(trenByO, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 2, -1, -1));

        suerte2.setOpaque(false);

        javax.swing.GroupLayout suerte2Layout = new javax.swing.GroupLayout(suerte2);
        suerte2.setLayout(suerte2Layout);
        suerte2Layout.setHorizontalGroup(
            suerte2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        suerte2Layout.setVerticalGroup(
            suerte2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(suerte2, new org.netbeans.lib.awtextra.AbsoluteConstraints(171, 2, 65, 90));

        jardinesMarvin.setOpaque(false);

        javax.swing.GroupLayout jardinesMarvinLayout = new javax.swing.GroupLayout(jardinesMarvin);
        jardinesMarvin.setLayout(jardinesMarvinLayout);
        jardinesMarvinLayout.setHorizontalGroup(
            jardinesMarvinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        jardinesMarvinLayout.setVerticalGroup(
            jardinesMarvinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(jardinesMarvin, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 2, -1, -1));

        kentuckyAvenue.setOpaque(false);

        javax.swing.GroupLayout kentuckyAvenueLayout = new javax.swing.GroupLayout(kentuckyAvenue);
        kentuckyAvenue.setLayout(kentuckyAvenueLayout);
        kentuckyAvenueLayout.setHorizontalGroup(
            kentuckyAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        kentuckyAvenueLayout.setVerticalGroup(
            kentuckyAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(kentuckyAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(105, 2, -1, -1));

        atlanticAvenue.setOpaque(false);

        javax.swing.GroupLayout atlanticAvenueLayout = new javax.swing.GroupLayout(atlanticAvenue);
        atlanticAvenue.setLayout(atlanticAvenueLayout);
        atlanticAvenueLayout.setHorizontalGroup(
            atlanticAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        atlanticAvenueLayout.setVerticalGroup(
            atlanticAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(atlanticAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(438, 2, -1, -1));

        illinoisAvenue.setOpaque(false);

        javax.swing.GroupLayout illinoisAvenueLayout = new javax.swing.GroupLayout(illinoisAvenue);
        illinoisAvenue.setLayout(illinoisAvenueLayout);
        illinoisAvenueLayout.setHorizontalGroup(
            illinoisAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 65, Short.MAX_VALUE)
        );
        illinoisAvenueLayout.setVerticalGroup(
            illinoisAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 90, Short.MAX_VALUE)
        );

        Tablero.add(illinoisAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(303, 2, 65, -1));

        newYorkAvenue.setOpaque(false);

        javax.swing.GroupLayout newYorkAvenueLayout = new javax.swing.GroupLayout(newYorkAvenue);
        newYorkAvenue.setLayout(newYorkAvenueLayout);
        newYorkAvenueLayout.setHorizontalGroup(
            newYorkAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        newYorkAvenueLayout.setVerticalGroup(
            newYorkAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(newYorkAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 95, 101, 60));

        tenneseeAvenue.setOpaque(false);

        javax.swing.GroupLayout tenneseeAvenueLayout = new javax.swing.GroupLayout(tenneseeAvenue);
        tenneseeAvenue.setLayout(tenneseeAvenueLayout);
        tenneseeAvenueLayout.setHorizontalGroup(
            tenneseeAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        tenneseeAvenueLayout.setVerticalGroup(
            tenneseeAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(tenneseeAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 156, 101, -1));

        jamesPlace.setOpaque(false);

        javax.swing.GroupLayout jamesPlaceLayout = new javax.swing.GroupLayout(jamesPlace);
        jamesPlace.setLayout(jamesPlaceLayout);
        jamesPlaceLayout.setHorizontalGroup(
            jamesPlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        jamesPlaceLayout.setVerticalGroup(
            jamesPlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(jamesPlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 277, 101, -1));

        communityChest2.setOpaque(false);

        javax.swing.GroupLayout communityChest2Layout = new javax.swing.GroupLayout(communityChest2);
        communityChest2.setLayout(communityChest2Layout);
        communityChest2Layout.setHorizontalGroup(
            communityChest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        communityChest2Layout.setVerticalGroup(
            communityChest2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(communityChest2, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 217, 101, 60));

        statesAvenue.setOpaque(false);

        javax.swing.GroupLayout statesAvenueLayout = new javax.swing.GroupLayout(statesAvenue);
        statesAvenue.setLayout(statesAvenueLayout);
        statesAvenueLayout.setHorizontalGroup(
            statesAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        statesAvenueLayout.setVerticalGroup(
            statesAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

        Tablero.add(statesAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 453, 101, 57));

        compElectricidad.setOpaque(false);

        javax.swing.GroupLayout compElectricidadLayout = new javax.swing.GroupLayout(compElectricidad);
        compElectricidad.setLayout(compElectricidadLayout);
        compElectricidadLayout.setHorizontalGroup(
            compElectricidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        compElectricidadLayout.setVerticalGroup(
            compElectricidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
        );

        Tablero.add(compElectricidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 513, 101, 52));

        virginiaAvenue.setOpaque(false);

        javax.swing.GroupLayout virginiaAvenueLayout = new javax.swing.GroupLayout(virginiaAvenue);
        virginiaAvenue.setLayout(virginiaAvenueLayout);
        virginiaAvenueLayout.setHorizontalGroup(
            virginiaAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        virginiaAvenueLayout.setVerticalGroup(
            virginiaAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 54, Short.MAX_VALUE)
        );

        Tablero.add(virginiaAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 397, 101, 54));

        trenPensilvania.setOpaque(false);

        javax.swing.GroupLayout trenPensilvaniaLayout = new javax.swing.GroupLayout(trenPensilvania);
        trenPensilvania.setLayout(trenPensilvaniaLayout);
        trenPensilvaniaLayout.setHorizontalGroup(
            trenPensilvaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        trenPensilvaniaLayout.setVerticalGroup(
            trenPensilvaniaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(trenPensilvania, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 338, 101, 60));

        charlesPlace.setOpaque(false);

        javax.swing.GroupLayout charlesPlaceLayout = new javax.swing.GroupLayout(charlesPlace);
        charlesPlace.setLayout(charlesPlaceLayout);
        charlesPlaceLayout.setHorizontalGroup(
            charlesPlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        charlesPlaceLayout.setVerticalGroup(
            charlesPlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(charlesPlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 565, -1, -1));

        pacificAvenue.setOpaque(false);

        javax.swing.GroupLayout pacificAvenueLayout = new javax.swing.GroupLayout(pacificAvenue);
        pacificAvenue.setLayout(pacificAvenueLayout);
        pacificAvenueLayout.setHorizontalGroup(
            pacificAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        pacificAvenueLayout.setVerticalGroup(
            pacificAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(pacificAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 95, 101, 60));

        nCarolinaAvenue.setOpaque(false);

        javax.swing.GroupLayout nCarolinaAvenueLayout = new javax.swing.GroupLayout(nCarolinaAvenue);
        nCarolinaAvenue.setLayout(nCarolinaAvenueLayout);
        nCarolinaAvenueLayout.setHorizontalGroup(
            nCarolinaAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        nCarolinaAvenueLayout.setVerticalGroup(
            nCarolinaAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        Tablero.add(nCarolinaAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 155, 101, 55));

        communityChest3.setOpaque(false);

        javax.swing.GroupLayout communityChest3Layout = new javax.swing.GroupLayout(communityChest3);
        communityChest3.setLayout(communityChest3Layout);
        communityChest3Layout.setHorizontalGroup(
            communityChest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        communityChest3Layout.setVerticalGroup(
            communityChest3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(communityChest3, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 210, 101, 60));

        penssylvaniAvenue.setOpaque(false);

        javax.swing.GroupLayout penssylvaniAvenueLayout = new javax.swing.GroupLayout(penssylvaniAvenue);
        penssylvaniAvenue.setLayout(penssylvaniAvenueLayout);
        penssylvaniAvenueLayout.setHorizontalGroup(
            penssylvaniAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        penssylvaniAvenueLayout.setVerticalGroup(
            penssylvaniAvenueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(penssylvaniAvenue, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 270, 101, -1));

        trenLineaCorta.setOpaque(false);

        javax.swing.GroupLayout trenLineaCortaLayout = new javax.swing.GroupLayout(trenLineaCorta);
        trenLineaCorta.setLayout(trenLineaCortaLayout);
        trenLineaCortaLayout.setHorizontalGroup(
            trenLineaCortaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        trenLineaCortaLayout.setVerticalGroup(
            trenLineaCortaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 63, Short.MAX_VALUE)
        );

        Tablero.add(trenLineaCorta, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 333, 101, 63));

        suerte3.setOpaque(false);

        javax.swing.GroupLayout suerte3Layout = new javax.swing.GroupLayout(suerte3);
        suerte3.setLayout(suerte3Layout);
        suerte3Layout.setHorizontalGroup(
            suerte3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        suerte3Layout.setVerticalGroup(
            suerte3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        Tablero.add(suerte3, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 400, 101, 50));

        parquePlace.setOpaque(false);

        javax.swing.GroupLayout parquePlaceLayout = new javax.swing.GroupLayout(parquePlace);
        parquePlace.setLayout(parquePlaceLayout);
        parquePlaceLayout.setHorizontalGroup(
            parquePlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        parquePlaceLayout.setVerticalGroup(
            parquePlaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 57, Short.MAX_VALUE)
        );

        Tablero.add(parquePlace, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 450, 101, 57));

        impuestoLujoso.setOpaque(false);

        javax.swing.GroupLayout impuestoLujosoLayout = new javax.swing.GroupLayout(impuestoLujoso);
        impuestoLujoso.setLayout(impuestoLujosoLayout);
        impuestoLujosoLayout.setHorizontalGroup(
            impuestoLujosoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        impuestoLujosoLayout.setVerticalGroup(
            impuestoLujosoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 55, Short.MAX_VALUE)
        );

        Tablero.add(impuestoLujoso, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 510, 101, 55));

        paseoMaritimo.setOpaque(false);

        javax.swing.GroupLayout paseoMaritimoLayout = new javax.swing.GroupLayout(paseoMaritimo);
        paseoMaritimo.setLayout(paseoMaritimoLayout);
        paseoMaritimoLayout.setHorizontalGroup(
            paseoMaritimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 101, Short.MAX_VALUE)
        );
        paseoMaritimoLayout.setVerticalGroup(
            paseoMaritimoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        Tablero.add(paseoMaritimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(698, 568, -1, -1));

        Dado.setBackground(new java.awt.Color(255, 255, 255));
        Dado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dadoNum.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout DadoLayout = new javax.swing.GroupLayout(Dado);
        Dado.setLayout(DadoLayout);
        DadoLayout.setHorizontalGroup(
            DadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dadoNum, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        DadoLayout.setVerticalGroup(
            DadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dadoNum, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                .addContainerGap())
        );

        Tablero.add(Dado, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 510, 30, 30));

        Dado1.setBackground(new java.awt.Color(255, 255, 255));
        Dado1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        dadoNum1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout Dado1Layout = new javax.swing.GroupLayout(Dado1);
        Dado1.setLayout(Dado1Layout);
        Dado1Layout.setHorizontalGroup(
            Dado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Dado1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dadoNum1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );
        Dado1Layout.setVerticalGroup(
            Dado1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Dado1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dadoNum1, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                .addContainerGap())
        );

        Tablero.add(Dado1, new org.netbeans.lib.awtextra.AbsoluteConstraints(427, 530, 30, 30));

        tableroBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopooly/imagenes/Tablero.png"))); // NOI18N
        tableroBG.setToolTipText("");
        Tablero.add(tableroBG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        Panel.add(Tablero, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        lanzarDado.setFont(new java.awt.Font("Microsoft YaHei UI", 3, 14)); // NOI18N
        lanzarDado.setText("LANZAR DADOS");
        lanzarDado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lanzarDadoActionPerformed(evt);
            }
        });
        Panel.add(lanzarDado, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 500, 490, 90));

        comprarCasilla.setFont(new java.awt.Font("Microsoft YaHei UI", 3, 14)); // NOI18N
        comprarCasilla.setText("COMPRAR");
        comprarCasilla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comprarCasillaActionPerformed(evt);
            }
        });
        Panel.add(comprarCasilla, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 620, 170, 80));

        regresar.setFont(new java.awt.Font("Microsoft YaHei UI", 3, 14)); // NOI18N
        regresar.setText("REGRESAR");
        regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarActionPerformed(evt);
            }
        });
        Panel.add(regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 620, 110, 80));

        jLabel1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Jugador 1");
        Panel.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 30, -1, -1));

        jLabel2.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Jugador 2");
        Panel.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 30, -1, -1));

        jLabel3.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Jugador 3");
        Panel.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 30, -1, -1));

        jLabel4.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Jugador 4");
        Panel.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1085, 30, -1, -1));

        jLabel5.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Jugador 5");
        Panel.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 30, -1, -1));

        jLabel6.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Jugador 6");
        Panel.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1250, 30, -1, -1));

        jLabel7.setBackground(new java.awt.Color(255, 192, 203));
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel7.setOpaque(true);
        Panel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 60, 30, 30));

        jLabel8.setBackground(new java.awt.Color(173, 216, 230));
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel8.setOpaque(true);
        Panel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 60, 30, 30));

        eventosTablero.setEditable(false);
        eventosTablero.setColumns(20);
        eventosTablero.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 14)); // NOI18N
        eventosTablero.setRows(5);
        jScrollPane1.setViewportView(eventosTablero);

        Panel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 280, 490, 200));

        bienesJugador.setEditable(false);
        bienesJugador.setColumns(20);
        bienesJugador.setFont(new java.awt.Font("Microsoft YaHei Light", 0, 14)); // NOI18N
        bienesJugador.setRows(5);
        jScrollPane2.setViewportView(bienesJugador);

        Panel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 120, 370, 140));

        jLabel13.setBackground(new java.awt.Color(230, 230, 250));
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel13.setOpaque(true);
        Panel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 60, 30, 30));

        jLabel14.setBackground(new java.awt.Color(255, 218, 185));
        jLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel14.setOpaque(true);
        Panel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(1105, 60, 30, 30));

        jLabel15.setBackground(new java.awt.Color(255, 242, 164));
        jLabel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel15.setOpaque(true);
        Panel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(1190, 60, 30, 30));

        jLabel16.setBackground(new java.awt.Color(152, 252, 176));
        jLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel16.setOpaque(true);
        Panel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1270, 60, 30, 30));

        cancelarCompra.setFont(new java.awt.Font("Microsoft YaHei UI", 3, 14)); // NOI18N
        cancelarCompra.setText("SALTAR TURNO");
        cancelarCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarCompraActionPerformed(evt);
            }
        });
        Panel.add(cancelarCompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 620, 170, 80));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/monopooly/imagenes/bgJugador.png"))); // NOI18N
        jLabel9.setToolTipText("");
        Panel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(797, 0, 540, 720));

        comprar.setText("jButton1");
        Panel.add(comprar, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 660, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lanzarDadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lanzarDadoActionPerformed
        // TODO add your handling code here:
        comprarCasilla.setEnabled(true);
        lanzarDado.setEnabled(false);
    }//GEN-LAST:event_lanzarDadoActionPerformed

    private void comprarCasillaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comprarCasillaActionPerformed

    }//GEN-LAST:event_comprarCasillaActionPerformed

    private void regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarActionPerformed

    Main main = new Main();
    main.setVisible(true);
    dispose();
    }//GEN-LAST:event_regresarActionPerformed

    private void cancelarCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarCompraActionPerformed
        // TODO add your handling code here:
    lanzarDado.setEnabled(true);
    comprar.setEnabled(false);
    regresar.setEnabled(false);

    turnoActual = (turnoActual + 1) % numeroJugadores;
    eventosTablero.append("\n");
    eventosTablero.append("Nuevo turno: Jugador " + (turnoActual + 1));

    juego.actualizarInfoJugador();
    }//GEN-LAST:event_cancelarCompraActionPerformed


    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tableroMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tableroMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tableroMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tableroMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tableroMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Dado;
    private javax.swing.JPanel Dado1;
    private javax.swing.JPanel Panel;
    private javax.swing.JPanel Tablero;
    private javax.swing.JPanel atlanticAvenue;
    private javax.swing.JPanel balticAvenue;
    private javax.swing.JTextArea bienesJugador;
    private javax.swing.JButton cancelarCompra;
    private javax.swing.JPanel charlesPlace;
    private javax.swing.JPanel communityChest1;
    private javax.swing.JPanel communityChest2;
    private javax.swing.JPanel communityChest3;
    private javax.swing.JPanel compAgua;
    private javax.swing.JPanel compElectricidad;
    private javax.swing.JButton comprar;
    private javax.swing.JButton comprarCasilla;
    private javax.swing.JPanel connecticutAvenue;
    private javax.swing.JLabel dadoNum;
    private javax.swing.JLabel dadoNum1;
    private javax.swing.JPanel enCarcel;
    private javax.swing.JTextArea eventosTablero;
    private javax.swing.JPanel illinoisAvenue;
    private javax.swing.JPanel impuestoLujoso;
    private javax.swing.JPanel impuestos;
    private javax.swing.JPanel indianaAvenue;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel jamesPlace;
    private javax.swing.JPanel jardinesMarvin;
    private javax.swing.JPanel kentuckyAvenue;
    private javax.swing.JButton lanzarDado;
    private javax.swing.JPanel mediterraAvenue;
    private javax.swing.JPanel nCarolinaAvenue;
    private javax.swing.JPanel newYorkAvenue;
    private javax.swing.JPanel orientalAvenue;
    private javax.swing.JPanel pacificAvenue;
    private javax.swing.JPanel parquePlace;
    private javax.swing.JPanel parqueadero;
    private javax.swing.JPanel paseoMaritimo;
    private javax.swing.JPanel penssylvaniAvenue;
    private javax.swing.JButton regresar;
    private javax.swing.JPanel salida;
    private javax.swing.JPanel statesAvenue;
    private javax.swing.JPanel suerte1;
    private javax.swing.JPanel suerte2;
    private javax.swing.JPanel suerte3;
    private javax.swing.JLabel tableroBG;
    private javax.swing.JPanel tenneseeAvenue;
    private javax.swing.JPanel trenByO;
    private javax.swing.JPanel trenLineaCorta;
    private javax.swing.JPanel trenPensilvania;
    private javax.swing.JPanel trenReading;
    private javax.swing.JPanel veACarcel;
    private javax.swing.JPanel ventnorAvenue;
    private javax.swing.JPanel vermontAvenue;
    private javax.swing.JPanel virginiaAvenue;
    // End of variables declaration//GEN-END:variables
}
