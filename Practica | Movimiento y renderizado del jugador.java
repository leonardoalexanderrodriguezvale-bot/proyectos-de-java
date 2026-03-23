import java.awt.*;
import java.awt.event.*;
import java.time.Year;
import javax.swing.*;

// ─── Clase Principal ───────────────────────────────────────────
public class Main {
    public static void main(String[] args) {
        new VentanaJuego();
    }
}

// ─── Jugador ───────────────────────────────────────────────────
class Jugador {
    int x, y;
    int tamanio;
    Color color;
    boolean esNPC;

    public Jugador(int x, int y, int tamanio, Color color, boolean esNPC) {
        this.x = x;
        this.y = y;
        this.tamanio = tamanio;
        this.color = color;
        this.esNPC = esNPC;
    }

    public void dibujar(Graphics g) {
        g.setColor(color);
        g.fillOval(x - tamanio / 2, y - tamanio / 2, tamanio, tamanio);
        g.setColor(Color.WHITE);
        g.drawOval(x - tamanio / 2, y - tamanio / 2, tamanio, tamanio);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.setColor(Color.WHITE);
        String label = esNPC ? "NPC" : "P1";
        g.drawString(label, x - 8, y + 4);
    }
}

// ─── Ventana del Juego ─────────────────────────────────────────
class VentanaJuego extends JFrame {
    public VentanaJuego() {
        setTitle("Proyecto Juego - Cancha de Futbol");
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
class PanelJuego extends JPanel implements KeyListener {

    private Jugador jugador;
    private Jugador npc;
    private final int VEL = 5;
    private final int CANCHA_X = 60;
    private final int CANCHA_Y = 80;
    private final int CANCHA_W = 520;
    private final int CANCHA_H = 340;

    public PanelJuego() {
        setPreferredSize(new Dimension(640, 480));
        setBackground(new Color(30, 100, 30));

        jugador = new Jugador(150, 240, 30, new Color(30, 80, 220), false);
        npc     = new Jugador(490, 240, 30, new Color(220, 40, 40), true);

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dibujarCancha(g2);
        dibujarHUD(g2);
        npc.dibujar(g2);
        jugador.dibujar(g2);
    }

    private void dibujarCancha(Graphics2D g) {
        // Pasto
        g.setColor(new Color(34, 139, 34));
        g.fillRect(CANCHA_X, CANCHA_Y, CANCHA_W, CANCHA_H);

        // Franjas
        g.setColor(new Color(40, 160, 40));
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0)
                g.fillRect(CANCHA_X + i * (CANCHA_W / 8), CANCHA_Y, CANCHA_W / 8, CANCHA_H);
        }

        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));

        // Borde
        g.drawRect(CANCHA_X, CANCHA_Y, CANCHA_W, CANCHA_H);

        // Línea central
        int centroX = CANCHA_X + CANCHA_W / 2;
        g.drawLine(centroX, CANCHA_Y, centroX, CANCHA_Y + CANCHA_H);

        // Círculo central
        int radioC = 50;
        g.drawOval(centroX - radioC, CANCHA_Y + CANCHA_H / 2 - radioC, radioC * 2, radioC * 2);
        g.fillOval(centroX - 4, CANCHA_Y + CANCHA_H / 2 - 4, 8, 8);

        // Áreas
        int areaW = 90, areaH = 160;
        int areaY = CANCHA_Y + (CANCHA_H - areaH) / 2;
        g.drawRect(CANCHA_X, areaY, areaW, areaH);
        g.drawRect(CANCHA_X + CANCHA_W - areaW, areaY, areaW, areaH);

        // Áreas pequeñas
        int areaPW = 40, areaPH = 90;
        int areaPY = CANCHA_Y + (CANCHA_H - areaPH) / 2;
        g.drawRect(CANCHA_X, areaPY, areaPW, areaPH);
        g.drawRect(CANCHA_X + CANCHA_W - areaPW, areaPY, areaPW, areaPH);

        // Porterías
        int portH = 60, portW = 12;
        int portY = CANCHA_Y + (CANCHA_H - portH) / 2;
        g.setStroke(new BasicStroke(3));
        g.drawRect(CANCHA_X - portW, portY, portW, portH);
        g.drawRect(CANCHA_X + CANCHA_W, portY, portW, portH);

        // Semicírculos
        g.setStroke(new BasicStroke(2));
        g.drawArc(CANCHA_X + areaW - 35, CANCHA_Y + CANCHA_H / 2 - 35, 70, 70, -60, 120);
        g.drawArc(CANCHA_X + CANCHA_W - areaW - 35, CANCHA_Y + CANCHA_H / 2 - 35, 70, 70, 120, 120);

        // Esquinas
        int r = 12;
        g.drawArc(CANCHA_X - r,            CANCHA_Y - r,            r*2, r*2,   0, -90);
        g.drawArc(CANCHA_X + CANCHA_W - r, CANCHA_Y - r,            r*2, r*2, 180,  90);
        g.drawArc(CANCHA_X - r,            CANCHA_Y + CANCHA_H - r, r*2, r*2,   0,  90);
        g.drawArc(CANCHA_X + CANCHA_W - r, CANCHA_Y + CANCHA_H - r, r*2, r*2,  90,  90);
    }

    private void dibujarHUD(Graphics2D g) {
        // HUD superior
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, 640, 72);

        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.setColor(Color.CYAN);
        g.drawString("Nombre: Leonardo Rodriguez Valencia", 10, 18);

        g.setColor(Color.WHITE);
        g.drawString("Grupo: 25A  |  Turno: Matutino  |  Año: " + Year.now().getValue(), 10, 36);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Jugador P1 → X: " + jugador.x + "   Y: " + jugador.y, 10, 58);

        g.setColor(new Color(180, 180, 180));
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Mover: WASD o Flechas", 460, 58);

        // HUD inferior
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(0, 450, 640, 30);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString("● Jugador P1", 10, 467);
        g.setColor(Color.RED);
        g.drawString("● NPC", 110, 467);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP)
            jugador.y = Math.max(CANCHA_Y + jugador.tamanio / 2, jugador.y - VEL);
        if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN)
            jugador.y = Math.min(CANCHA_Y + CANCHA_H - jugador.tamanio / 2, jugador.y + VEL);
        if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
            jugador.x = Math.max(CANCHA_X + jugador.tamanio / 2, jugador.x - VEL);
        if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
            jugador.x = Math.min(CANCHA_X + CANCHA_W - jugador.tamanio / 2, jugador.x + VEL);
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
