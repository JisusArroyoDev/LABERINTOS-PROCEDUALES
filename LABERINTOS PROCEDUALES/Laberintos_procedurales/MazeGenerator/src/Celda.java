/*
Esta clase Contiene las funciones necesarias para generar e imprimir cada celda del laberinto
*/

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Celda {
    // Variables de instancia
    public int fila, f, columna, c, tam_Celda;
    public boolean[] Paredes = { true, true, true, true }; // Paredes: superior, derecha, inferior, izquierda
    public ArrayList<Celda> sig;
    public Celda padre;
    public boolean visitado = false; // Utilizado para el dibujado de laberinto (mazeDrawer)
    private int grosor = 1;
    private int tam_trazo;
    public boolean camino_visitado = false; // Utilizado para la búsqueda de camino (pathFinder)

    // Constructor de la clase Celda
    Celda(int fila, int columna, int tam_Celda) {
        this.fila = fila;
        this.columna = columna;
        f = fila * tam_Celda;
        c = columna * tam_Celda;
        this.tam_Celda = tam_Celda;
        sig = new ArrayList<Celda>();
        padre = null;
        tam_trazo = (int) (Math.log(tam_Celda) - 2); // Cálculo del grosor del trazo en función del tamaño de la celda
    }

    // Método para dibujar la celda
    public void dibujar_celda(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(new Color(0, 0, 0));
        g2.setStroke(new BasicStroke(tam_trazo));

        if (Paredes[0] == true)
            g2.drawLine(c, f, c + tam_Celda, f); // Pared superior
        if (Paredes[1] == true)
            g2.drawLine(c + tam_Celda, f, c + tam_Celda, f + tam_Celda); // Pared derecha
        if (Paredes[2] == true)
            g2.drawLine(c + tam_Celda, f + tam_Celda, c, f + tam_Celda); // Pared inferior
        if (Paredes[3] == true)
            g2.drawLine(c, f + tam_Celda, c, f); // Pared izquierda

        g2.setStroke(new BasicStroke(1));
    }

    public void drawWeight(Graphics g) {
        // Método sin implementación
    }

    // Método para marcar la celda como visitada y colorearla
    public void marcar_visitado(Graphics g, Color color) {
        Graphics2D g2 = (Graphics2D) g;
        if (this.visitado == true) {
            g2.setColor(color);
            g2.fillRect(c, f, tam_Celda, tam_Celda);
        }
    }

    // Método para dibujar el camino recorrido
    public void dibujar_camino(Graphics g, Color color) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        if (padre != null) {
            if (this.camino_visitado == true) {
                g2.fillRect(c + (int) (tam_Celda / 3), f + (int) (tam_Celda / 3), (int) (tam_Celda / 3),
                        (int) (tam_Celda / 3));
            }
            if (fila - 1 == padre.fila) {
                g2.fillRect(c + (int) (tam_Celda / 3), f, (int) (tam_Celda / 3), (int) (tam_Celda / 3));
                g2.fillRect(padre.c + (int) (tam_Celda / 3), padre.f + ((int) (tam_Celda / 3) * 2),
                        (int) (tam_Celda / 3), (int) (tam_Celda / 3));
            }

            if (columna + 1 == padre.columna) {
                g2.fillRect(c + ((int) (tam_Celda / 3) * 2), f + (int) (tam_Celda / 3), (int) (tam_Celda / 3),
                        (int) (tam_Celda / 3));
                g2.fillRect(padre.c, padre.f + (int) (tam_Celda / 3), (int) (tam_Celda / 3), (int) (tam_Celda / 3));
            }

            if (fila + 1 == padre.fila) {
                g2.fillRect(c + (int) (tam_Celda / 3), f + ((int) (tam_Celda / 3) * 2), (int) (tam_Celda / 3),
                        (int) (tam_Celda / 3));
                g2.fillRect(padre.c + (int) (tam_Celda / 3), padre.f, (int) (tam_Celda / 3), (int) (tam_Celda / 3));
            }

            if (columna - 1 == padre.columna) {
                g2.fillRect(c, f + (int) (tam_Celda / 3), (int) (tam_Celda / 3), (int) (tam_Celda / 3));
                g2.fillRect(padre.c + ((int) (tam_Celda / 3) * 2), padre.f + (int) (tam_Celda / 3),
                        (int) (tam_Celda / 3), (int) (tam_Celda / 3));
            }
        }
    }

    // Método para restablecer el estado de la celda
    public void reset_celda() {
        visitado = false;
        camino_visitado = false;
        padre = null;
        sig = new ArrayList<>();
    }

    public int getGrosor() {
        return grosor;
    }

    // Método para establecer un grosor aleatorio a la celda
    public void setGrosor() {
        this.grosor = new Random().nextInt(9) + 1;
    }
}
