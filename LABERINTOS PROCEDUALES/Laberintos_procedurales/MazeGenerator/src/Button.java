/*
Esta clase declara el formato general y la forma de los botones usados para las interfaces de usuario
*/

import java.awt.Color;
import javax.swing.*;

public class Button extends JButton {
    // Constructor de la clase Button
    Button(String nombre, int x, int y, int ancho, int alto, Color color) {
        // Establece el texto del botón como el nombre dado
        this.setText(nombre);
        // Establece los límites y la ubicación del botón en el panel
        this.setBounds(x, y, ancho, alto);
        this.setLocation(x, y);
        // Establece el color de fondo del botón
        this.setBackground(color);
        // Establece la posición del texto horizontal y verticalmente en el centro del botón
        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);
        // Desactiva el enfoque del botón para evitar que se destaque al recibir el foco
        this.setFocusable(false);
    }
}
