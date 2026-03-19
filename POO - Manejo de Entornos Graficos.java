import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.time.Year;
import javax.swing.JFrame;
import javax.swing.JPanel;

// ─── Clase Principal ───────────────────────────────────────────
public class Main {
    public static void main(String[] args) {
        new VentanaJuego();
    }
}

// ─── Ventana del Juego ─────────────────────────────────────────
class VentanaJuego extends JFrame {
    public VentanaJuego() {
        setTitle("Proyecto Juego - Base Grafica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        PanelJuego panel = new PanelJuego();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

// ─── Panel del Juego ───────────────────────────────────────────
class PanelJuego extends JPanel {
    public PanelJuego() {
        setPreferredSize(new Dimension(640, 480));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ── Título ──────────────────────────────────────────────
        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("Base grafica del juego", 180, 60);

        // ── Línea separadora ────────────────────────────────────
        g.setColor(Color.GRAY);
        g.drawLine(50, 80, 590, 80);

        // ── Datos del alumno ────────────────────────────────────
        g.setFont(new Font("Arial", Font.PLAIN, 18));

        g.setColor(Color.WHITE);
        g.drawString("Nombre Alumno:", 50, 130);
        g.setColor(Color.YELLOW);
        g.drawString("Leonardo   Rodriguez   Valencia", 230, 130);

        g.setColor(Color.WHITE);
        g.drawString("Grupo:", 50, 180);
        g.setColor(Color.YELLOW);
        g.drawString("25A", 230, 180);

        g.setColor(Color.WHITE);
        g.drawString("Turno:", 50, 230);
        g.setColor(Color.YELLOW);
        g.drawString("Matutino", 230, 230);

        g.setColor(Color.WHITE);
        g.drawString("Año de ejecucion:", 50, 280);
        g.setColor(Color.YELLOW);
        g.drawString(String.valueOf(Year.now().getValue()), 230, 280);
    }
}
