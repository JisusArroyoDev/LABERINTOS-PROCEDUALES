/*
   Esta clase se encarga de controlar los atributos de la ventana que corre el programa,
   tales como el título de la ventana, que aparezca centrado, y que no se pueda modificar
   el tamaño de la misma.
*/
import javax.swing.*;

public class Frame extends JFrame
{
    public Panel panel;

    // Definición de constantes para el ancho y alto de la ventana
    public final int Anchoventana 	= 1000;
    public final int Altoventana 	= 600;

    // Constructor de la clase myFrame
    Frame()
    {
        // Configura las propiedades del marco de la ventana
        this.setTitle("GENERADOR Y SOLUCIONADOR DE LABERINTO");  // Establece el título de la ventana
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Establece la operación de cierre de la ventana
        this.setSize(Anchoventana, Altoventana);  // Establece el tamaño de la ventana
        this.setResizable(false);  // Evita que la ventana sea redimensionable

        // Crea un nuevo panel personalizado con las dimensiones de la ventana
        panel = new Panel(Anchoventana, Altoventana);
        
        // Agrega el panel a la ventana
        this.add(panel);
        
        // Ajusta automáticamente el tamaño de la ventana según su contenido
        this.pack();
        
        // Desactiva el administrador de diseño predeterminado
        this.setLayout(null);
        
        // Hace visible la ventana
        this.setVisible(true);
        
        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);
    }
}