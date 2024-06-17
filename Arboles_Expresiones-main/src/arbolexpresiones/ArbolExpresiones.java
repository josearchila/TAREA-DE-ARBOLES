package arbolexpresiones;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

class Nodo {
    String valor;
    Nodo izquierdo, derecho;

    public Nodo(String valor) {
        this.valor = valor;
        this.izquierdo = this.derecho = null;
    }
}

public class ArbolExpresiones extends JPanel {
    private Nodo raiz;

    public ArbolExpresiones(Nodo raiz) {
        this.raiz = raiz;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        dibujarNodo(g, getWidth() / 2, 30, raiz, getWidth() / 4);
    }

    private void dibujarNodo(Graphics g, int x, int y, Nodo nodo, int offsetX) {
        g.drawOval(x, y, 30, 30);
        g.drawString(nodo.valor, x + 10, y + 20);

        if (nodo.izquierdo != null) {
            int xLeft = x - offsetX;
            int yLeft = y + 50;
            g.drawLine(x + 15, y + 30, xLeft + 15, yLeft);
            dibujarNodo(g, xLeft, yLeft, nodo.izquierdo, offsetX / 2);
        }

        if (nodo.derecho != null) {
            int xRight = x + offsetX;
            int yRight = y + 50;
            g.drawLine(x + 15, y + 30, xRight + 15, yRight);
            dibujarNodo(g, xRight, yRight, nodo.derecho, offsetX / 2);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingresa una expresión aritmética:");
        String expresion = scanner.nextLine();
        scanner.close();

        Nodo raiz = construirArbol(expresion);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Tamaño más grande
        frame.setLocationRelativeTo(null); // Centrar la ventana
        frame.add(new ArbolExpresiones(raiz));
        frame.setVisible(true);
    }

    private static Nodo construirArbol(String expresion) {
        expresion = expresion.replaceAll("\\s+", ""); // Eliminar espacios en blanco

        // Si la expresión está entre paréntesis, eliminar los paréntesis externos
        if (expresion.startsWith("(") && expresion.endsWith(")")) {
            expresion = expresion.substring(1, expresion.length() - 1);
        }

        // Buscar el operador principal (el último operador con la menor precedencia)
        int indiceOperadorPrincipal = -1;
        int nivelParentesis = 0;
        int precedenciaMinima = Integer.MAX_VALUE;

        for (int i = expresion.length() - 1; i >= 0; i--) {
            char c = expresion.charAt(i);
            if (c == '(') {
                nivelParentesis--;
            } else if (c == ')') {
                nivelParentesis++;
            } else if (nivelParentesis == 0 && (c == '+' || c == '-' || c == '*' || c == '/')) {
                int precedencia = obtenerPrecedencia(c);
                if (precedencia <= precedenciaMinima) {
                    precedenciaMinima = precedencia;
                    indiceOperadorPrincipal = i;
                }
            }
        }

        // Si no se encontró ningún operador, la expresión es un solo número
        if (indiceOperadorPrincipal == -1) {
            return new Nodo(expresion);
        }

        // Construir el nodo con el operador principal
        Nodo nodo = new Nodo(expresion.substring(indiceOperadorPrincipal, indiceOperadorPrincipal + 1));

        // Recursivamente construir los subárboles izquierdo y derecho
        nodo.izquierdo = construirArbol(expresion.substring(0, indiceOperadorPrincipal));
        nodo.derecho = construirArbol(expresion.substring(indiceOperadorPrincipal + 1));

        return nodo;
    }

    private static int obtenerPrecedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }
}


