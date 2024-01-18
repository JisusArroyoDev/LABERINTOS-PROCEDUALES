/*
Esta clase contiene todas las funciones que corresponden 
a la generación y solución del laberinto por los dos métodos disponibles
*/
import java.awt.*;
import java.util.*;
import java.util.Queue;

public class Laberinto {

    // Variables miembro para el tamaño del laberinto, tamaño de celda, grilla, etc.
    private final int SIZE_LAB;
    private int SIZE_CELDA;
    private final int w;
    private Celda[][] grillas;
    private Celda actual;
    private Celda sig;
    private Stack<Celda> pila_visitados;
    private boolean ejecutando = false;
    private int cont_visitados = 0;
    public boolean solucionado = false;
    private ArrayList<Celda> caminos_encontrados;

    private Celda inicio, fin;
    private Queue<Celda> cola_visitados;
    private Stack<Celda> visitadosP;

    // Constructor que recibe el tamaño del laberinto y el tamaño de las celdas
    Laberinto(int tam_laberinto, int tam_celda) {
        SIZE_LAB = tam_laberinto;
        SIZE_CELDA = tam_celda;
        w = (int) (SIZE_LAB / SIZE_CELDA);
        init();
    }

    // Método para inicializar las variables y generar el laberinto
    private void init() {
        grillas = new Celda[w][w];
        for (int fila = 0; fila < w; fila++) {
            for (int columna = 0; columna < w; columna++) {
                grillas[fila][columna] = new Celda(fila, columna, SIZE_CELDA);
            }
        }

        actual = grillas[0][0];
        actual.visitado = true;
        pila_visitados = new Stack<Celda>();
        ejecutando = true;
    }

    // Método para dibujar la cuadrícula en el panel de dibujo
    public void dibujar_laberinto(Graphics g) {
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                grillas[i][j].marcar_visitado(g, new Color(0, 0, 255, 100));
                grillas[i][j].dibujar_celda(g);
            }
        }
    }

    // Método para realizar la animación de generación del laberinto
    public void algoritmo_laberinto(Graphics g) {
        if (ejecutando) {
            if (cont_visitados <= w * w) {
                actual.marcar_visitado(g, new Color(0, 255, 0, 100));
            }
            actualizar();
        }
    }

    // Método de actualización para la generación del laberinto
    private void actualizar() {
        if (!ejecutando) {
            return;
        }

        sig = vecino_aleatorio(actual);

        if (sig != null) {
            pila_visitados.push(sig);
            cont_visitados++;
            sig.visitado = true;
            romper_pared(actual, sig);
            actual = sig;
        } else {
            while (!tiene_Vecino(actual)) {
                if (!pila_visitados.empty()) {
                    actual = pila_visitados.pop();
                } else {
                    ejecutando = false;
                    cont_visitados++;
                    return;
                }
            }
            actualizar();
        }
    }

    // Método para dibujar el laberinto de forma instantánea
    public void laberinto_instantaneo() {
        Stack<Celda> lista_visitados = new Stack<>();
        actual.visitado = true;
        lista_visitados.push(actual);
        while (!lista_visitados.isEmpty()) {
            actual = lista_visitados.pop();
            if (tiene_Vecino(actual)) {
                lista_visitados.push(actual);
                sig = vecino_aleatorio(actual);
                romper_pared(actual, sig);
                sig.visitado = true;
                lista_visitados.push(sig);
            }
        }
        ejecutando = false;
    }

    // Método para dibujar el buscador de caminos en el laberinto
    public void buscador_de_camino(Graphics g, int mode) {
        if (mode == 0) {
            algoritmo_BFS(g);
        } else if (mode == 1) {
            algoritmo_DFS(g);
        }

        for (Celda x : caminos_encontrados) {
            x.dibujar_camino(g, Color.pink);
        }

        inicio.marcar_visitado(g, new Color(252, 118, 106));
        fin.marcar_visitado(g, new Color(91, 132, 177));

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                grillas[i][j].dibujar_celda(g);
            }
        }
    }

    // Método para verificar si se ha terminado la generación o resolución del laberinto
    public boolean flag_solucionado() {
        return !ejecutando;
    }

    // Algoritmo de búsqueda en anchura (BFS)
    private void algoritmo_BFS(Graphics g) {
        sig = un_Vecino(actual);
        recorrido_deAaB(actual, sig);
        if (sig == fin) {
            solucionado = true;
            rastrear_camino();
            return;
        }

        if (sig != fin && sig != null) {
            cola_visitados.offer(sig);
            sig.camino_visitado = true;
            sig.visitado = true;
            sig.dibujar_camino(g, Color.RED);
        } else {
            if (!cola_visitados.isEmpty()) {
                actual = cola_visitados.poll();
            } else {
                return;
            }
        }
    }

    // Algoritmo de búsqueda en profundidad (DFS)
    private void algoritmo_DFS(Graphics g) {
        sig = un_Vecino(actual);
        recorrido_deAaB(actual, sig);
        if (sig == fin) {
            solucionado = true;
            rastrear_camino();
            return;
        }

        if (sig != fin && sig != null) {
            visitadosP.push(sig);
            sig.camino_visitado = true;
            sig.visitado = true;
            sig.dibujar_camino(g, Color.RED);
        } else {
            if (!visitadosP.isEmpty()) {
                actual = visitadosP.pop();
            } else {
                return;
            }
        }
    }

    // Realiza el rastreo del camino desde el punto final hasta el inicio
    private void rastrear_camino() {
        caminos_encontrados.add(fin);
        Celda tempParent = fin.padre;
        while (tempParent != inicio) {
            caminos_encontrados.add(tempParent);
            tempParent = tempParent.padre;
        }
    }

    // Registra el recorrido desde A hasta B
    private void recorrido_deAaB(Celda A, Celda B) {
        if (B != null) {
            B.padre = A;
        }
        if (A != null) {
            A.sig.add(B);
        }
    }

    // Inicializa los puntos de inicio y fin aleatoriamente en la matriz de grillas
    public void inicializar_InicioyFin() {
        inicio = grillas[new Random().nextInt(w)][new Random().nextInt(w)];
        fin = grillas[new Random().nextInt(w)][new Random().nextInt(w)];

        if (inicio == fin) {
            fin = grillas[new Random().nextInt(w)][new Random().nextInt(w)];
        }

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < w; j++) {
                grillas[i][j].visitado = false;
            }
        }

        inicio.visitado = true;
        fin.visitado = true;
        cola_visitados = new LinkedList<Celda>();
        visitadosP = new Stack<Celda>();
        caminos_encontrados = new ArrayList<Celda>();
        actual = inicio;
        sig = actual;
    }

    // Obtiene un vecino no visitado de la celda actual
    public Celda un_Vecino(Celda celda_actual) {
        ArrayList<Celda> vecinos = new ArrayList<>();

        if (celda_actual.fila - 1 >= 0 && !grillas[celda_actual.fila - 1][celda_actual.columna].camino_visitado && !celda_actual.Paredes[0]) {
            vecinos.add(grillas[celda_actual.fila - 1][celda_actual.columna]);
        }

        if (celda_actual.columna + 1 < w && !grillas[celda_actual.fila][celda_actual.columna + 1].camino_visitado && !celda_actual.Paredes[1]) {
            vecinos.add(grillas[celda_actual.fila][celda_actual.columna + 1]);
        }

        if (celda_actual.fila + 1 < w && !grillas[celda_actual.fila + 1][celda_actual.columna].camino_visitado && !celda_actual.Paredes[2]) {
            vecinos.add(grillas[celda_actual.fila + 1][celda_actual.columna]);
        }

        if (celda_actual.columna - 1 >= 0 && !grillas[celda_actual.fila][celda_actual.columna - 1].camino_visitado && !celda_actual.Paredes[3]) {
            vecinos.add(grillas[celda_actual.fila][celda_actual.columna - 1]);
        }

        if (vecinos.isEmpty()) {
            return null;
        }

        return vecinos.get(0);
    }

    // Obtiene un vecino aleatorio de la celda actual
    public Celda vecino_aleatorio(Celda celda_actual) {
        ArrayList<Celda> vecinos = new ArrayList<>();

        if (celda_actual.fila - 1 >= 0 && !grillas[celda_actual.fila - 1][celda_actual.columna].visitado) {
            vecinos.add(grillas[celda_actual.fila - 1][celda_actual.columna]);
        }

        if (celda_actual.columna + 1 < w && !grillas[celda_actual.fila][celda_actual.columna + 1].visitado) {
            vecinos.add(grillas[celda_actual.fila][celda_actual.columna + 1]);
        }

        if (celda_actual.fila + 1 < w && !grillas[celda_actual.fila + 1][celda_actual.columna].visitado) {
            vecinos.add(grillas[celda_actual.fila + 1][celda_actual.columna]);
        }

        if (celda_actual.columna - 1 >= 0 && !grillas[celda_actual.fila][celda_actual.columna - 1].visitado) {
            vecinos.add(grillas[celda_actual.fila][celda_actual.columna - 1]);
        }

        if (vecinos.isEmpty()) {
            return null;
        }

        return vecinos.get(new Random().nextInt(vecinos.size()));
    }

    // Verifica si la celda actual tiene algún vecino no visitado
    public boolean tiene_Vecino(Celda celda_actual) {
        return vecino_aleatorio(celda_actual) != null;
    }

    // Rompe la pared entre dos celdas adyacentes
    public void romper_pared(Celda cellA, Celda cellB) {
        if (cellA.fila == cellB.fila + 1) {
            cellA.Paredes[0] = false; // Pared superior de cellA
            cellB.Paredes[2] = false; // Pared inferior de cellB
            return;
        }

        if (cellA.fila == cellB.fila - 1) {
            cellA.Paredes[2] = false; // Pared inferior de cellA
            cellB.Paredes[0] = false; // Pared superior de cellB
            return;
        }

        if (cellA.columna == cellB.columna + 1) {
            cellA.Paredes[3] = false; // Pared izquierda de cellA
            cellB.Paredes[1] = false; // Pared derecha de cellB
            return;
        }

        if (cellA.columna == cellB.columna - 1) {
            cellA.Paredes[1] = false; // Pared derecha de cellA
            cellB.Paredes[3] = false; // Pared izquierda de cellB
            return;
        }
    }

    // Reinicia el estado del laberinto a su estado inicial
    public void reset_laberinto() {
        for (int fila = 0; fila < w; fila++) {
            for (int columna = 0; columna < w; columna++) {
                grillas[fila][columna].reset_celda();
            }
        }
        solucionado = false;
    }

    // Obtiene el tamaño de la celda
    public int get_tam_celda() {
        return SIZE_CELDA;
    }

    // Establece el tamaño de la celda
    public void set_tam_celda(int tam_celda) {
        SIZE_CELDA = tam_celda;
    }
}
