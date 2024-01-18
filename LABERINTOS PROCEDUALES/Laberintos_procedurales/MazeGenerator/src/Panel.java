/*
Esta clase contiene las funciones que se encargan de mostrar la interfaz gráfica y
los elementos de la aplicación de manera correcta
*/

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Panel extends JPanel implements ActionListener, ChangeListener {

    // Variables de configuración y estado
    private final int tamLaberinto = 600;
    private int tamCelda = 40;
    private int retrasoLento = 110;
    private int retraso = 10;
    private int VentanaAncho, VentanaAlto;
    private int ejecucion = -1;
    private int VariableSwitch = 0;

    private Laberinto lab;
    private Timer tp;

    // Panel de la derecha
    private JPanel Panel_peque;
    private Button Boton_inicio;
    private Button Boton_reset;
    private JCheckBox lab_verificacion;
    private JSlider deslizador_celdas;
    private JLabel etiqueta_celdas;
    private JSlider deslizador_velocidad;
    private JLabel etiqueta_velocidad;
    private Button Boton_solucionar;
    private Button Boton_regenerar_laberinto;
    private JLabel Etiqueta_algo;
    private JCheckBox verifica_BFS;
    private JComboBox Caja;

    private int modo;
    private boolean flag = false;

    Panel(int VentanaAncho, int VentanaAlto) {

        this.VentanaAncho = VentanaAncho;
        this.VentanaAlto = VentanaAlto;

        tp = new Timer(retraso, this);

        this.setPreferredSize(new Dimension(VentanaAncho, VentanaAlto));
        this.setBounds(0, 0, VentanaAncho, VentanaAlto);
        this.setBackground(new Color(250, 250, 250));
        this.setLayout(null);

        // Configuración del panel de la derecha
        Panel_peque = new JPanel();
        Panel_peque.setPreferredSize(new Dimension((VentanaAncho - tamLaberinto), VentanaAlto));
        Panel_peque.setBounds(tamLaberinto, 0, VentanaAncho - tamLaberinto, VentanaAlto);
        Panel_peque.setLayout(null);
        this.add(Panel_peque);

        // Botones y controles en el panel de la derecha
        Boton_inicio = new Button("CREAR LABERINTO", (int) (Panel_peque.getSize().width / 2) - 165, 30, 150, 50, Color.PINK);
        Panel_peque.add(Boton_inicio);
        Boton_inicio.addActionListener(this);

        Boton_regenerar_laberinto = new Button("REINICIAR", (int) (Panel_peque.getSize().width / 2) + 25, 30, 150, 50, Color.CYAN);
        Panel_peque.add(Boton_regenerar_laberinto);
        Boton_regenerar_laberinto.addActionListener(this);
        Boton_regenerar_laberinto.setEnabled(false);

        lab_verificacion = new JCheckBox();
        lab_verificacion.setBounds((int) (Panel_peque.getSize().width / 2) - 120 - 30, 95, 250, 20);
        lab_verificacion.setText("GENERACION INSTANTANEA");
        lab_verificacion.setFont(new Font("", Font.BOLD, 15));
        lab_verificacion.setFocusable(false);
        Panel_peque.add(lab_verificacion);

        deslizador_celdas = new JSlider(5, 100, 40);
        deslizador_celdas.setBounds((VentanaAncho - tamLaberinto) / 2 - (350 / 2), 150, 350, 50);
        deslizador_celdas.setPaintTicks(true);
        deslizador_celdas.setMinorTickSpacing(5);
        deslizador_celdas.setPaintTrack(true);
        deslizador_celdas.setMajorTickSpacing(5);
        deslizador_celdas.setSnapToTicks(true);
        deslizador_celdas.setPaintLabels(true);
        deslizador_celdas.addChangeListener(this);
        deslizador_celdas.setFont(new Font("", Font.BOLD, 9));

        etiqueta_celdas = new JLabel();
        etiqueta_celdas.setText("TAMAÑO DE CELDA: " + deslizador_celdas.getValue());
        etiqueta_celdas.setBounds(35, 130, 250, 20);
        etiqueta_celdas.setFont(new Font("", Font.BOLD, 18));

        Panel_peque.add(etiqueta_celdas);
        Panel_peque.add(deslizador_celdas);

        deslizador_velocidad = new JSlider(1, 5, 5);
        deslizador_velocidad.setBounds((VentanaAncho - tamLaberinto) / 2 - (350 / 2), 230, 350, 40);
        deslizador_velocidad.setPaintTrack(true);
        deslizador_velocidad.setMajorTickSpacing(1);
        deslizador_velocidad.setSnapToTicks(true);
        deslizador_velocidad.setPaintLabels(true);
        deslizador_velocidad.addChangeListener(this);

        etiqueta_velocidad = new JLabel();
        etiqueta_velocidad.setText("VELOCIDAD: " + deslizador_velocidad.getValue());
        etiqueta_velocidad.setBounds(35, 210, 180, 20);
        etiqueta_velocidad.setFont(new Font("", Font.BOLD, 18));

        Panel_peque.add(etiqueta_velocidad);
        Panel_peque.add(deslizador_velocidad);

        Boton_solucionar = new Button("SOLUCIONAR", Panel_peque.getSize().width / 2 - 165, 300, 155, 60, new Color(255, 231, 122));
        Panel_peque.add(Boton_solucionar);
        Boton_solucionar.addActionListener(this);
        Boton_solucionar.setEnabled(false);

        Boton_reset = new Button("REINICIAR SOLUCION", (Panel_peque.getSize().width / 2) + 20, 300, 155, 60, new Color(44, 95, 45));
        Panel_peque.add(Boton_reset);
        Boton_reset.addActionListener(this);

        Etiqueta_algo = new JLabel("METODO DE SOLUCION");
        Etiqueta_algo.setBounds(Panel_peque.getSize().width / 2 - 155, 370, 220, 30);
        Etiqueta_algo.setFont(new Font("", Font.BOLD, 15));
        Panel_peque.add(Etiqueta_algo);

        String[] algoList = {"Breadth First Search(BFS)", "Depth First Search(DFS)"};
        Caja = new JComboBox(algoList);
        Caja.setBounds(Panel_peque.getSize().width / 2 - 160, 400, 220, 30);
        Caja.setFont(new Font("", Font.BOLD, 15));
        Caja.setFocusable(false);
        Caja.addActionListener(this);

        Panel_peque.add(Caja);

        modo = Caja.getSelectedIndex();

        initMaze();
        tp.start();
    }

    // Inicializa el laberinto
    private void initMaze() {
        lab = new Laberinto(tamLaberinto, tamCelda);
    }

    // Método de dibujo de la interfaz gráfica
    public void paintComponent(Graphics g) {
        if (lab.flag_solucionado()) {
            Boton_regenerar_laberinto.setEnabled(true);
            Boton_solucionar.setEnabled(true);
            Caja.setEnabled(true);
        } else {
            Boton_solucionar.setEnabled(false);
            Boton_reset.setEnabled(false);
            Caja.setEnabled(false);
        }

        super.paintComponent(g);
        lab.dibujar_laberinto(g);

        if (flag) {
            lab.buscador_de_camino(g, modo);
            if (lab.solucionado) {
                lab_verificacion.setEnabled(true);
                Boton_solucionar.setEnabled(true);
                Boton_reset.setEnabled(true);
                tp.stop();
            }
        }

        if (!lab_verificacion.isSelected()) {
            lab.algoritmo_laberinto(g);
        } else {
            lab.laberinto_instantaneo();
        }
    }

    // Reinicia el laberinto
    private void reset() {
        tp.start();
        ejecucion = -1;
        Boton_inicio.setEnabled(true);
        repaint();
    }

    // Eventos de la interfaz gráfica
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Boton_regenerar_laberinto) {
            flag = false;
            lab_verificacion.setEnabled(true);
            initMaze();
            reset();
            Boton_inicio.setText("CREAR LABERINTO");
        }

        if (e.getSource() == Boton_inicio) {
            ejecucion *= -1;
            if (ejecucion == 1)
                Boton_inicio.setText("PAUSAR");
            else
                Boton_inicio.setText("CREAR LABERINTO");
        }

        if (e.getSource() == Caja) {
            modo = Caja.getSelectedIndex();
        }

        if (e.getSource() == Boton_solucionar) {
            lab_verificacion.setSelected(false);
            lab_verificacion.setEnabled(false);

            if (!flag) {
                lab.inicializar_InicioyFin();
                flag = true;
            }
        }

        if (e.getSource() == Boton_reset) {
            flag = false;
            lab.reset_laberinto();
            tp.start();
        }

        if (ejecucion == 1) {
            repaint();
        }
    }

    // Maneja cambios en los deslizadores
    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == deslizador_celdas) {
            if (deslizador_celdas.getValue() % 5 == 0) {
                lab_verificacion.setEnabled(true);
                etiqueta_celdas.setText("TAMAÑO DE CELDA: " + deslizador_celdas.getValue());
                tamCelda = deslizador_celdas.getValue();
                Boton_inicio.setText("CREAR LABERINTO");
                flag = false;
                initMaze();
                reset();
            }
        }

        if (e.getSource() == deslizador_velocidad) {
            etiqueta_velocidad.setText("VELOCIDAD: " + deslizador_velocidad.getValue());
            retraso = retrasoLento - (deslizador_velocidad.getValue() - 1) * ((retrasoLento - 10) / 4);
            tp.setDelay(retraso);
        }
    }
}
