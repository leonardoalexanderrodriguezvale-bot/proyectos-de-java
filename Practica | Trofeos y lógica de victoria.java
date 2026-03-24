import java.awt.*;
import java.awt.event.*;
import java.time.Year;
import java.util.Random;
import javax.swing.*;

// ─── Clase Principal ───────────────────────────────────────────
public class Main {
    public static void main(String[] args) {
        new VentanaJuego();
    }
}

// ─── Trofeo ────────────────────────────────────────────────────
class Trofeo {
    int x, y;
    int trofeosRecogidos = 0;
    private static final int RADIO = 10;
    private static final int TOTAL = 5;
    private Random rand = new Random();

    // Límites de la cancha (para reposicionar dentro)
    private final int CX = 80, CY = 100, CW = 480, CH = 300;

    public Trofeo() {
        moverAleatoriamente();
    }

    public void moverAleatoriamente() {
        x = CX + 20 + rand.nextInt(CW - 40);
        y = CY + 20 + rand.nextInt(CH - 40);
    }

    public void dibujar(Graphics2D g) {
        // Moneda dorada
        g.setColor(new Color(255, 215, 0));
        g.fillOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);
        g.setColor(new Color(180, 140, 0));
        g.setStroke(new BasicStroke(2));
        g.drawOval(x - RADIO, y - RADIO, RADIO * 2, RADIO * 2);
        g.setColor(new Color(255, 255, 150));
        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.drawString("★", x - 7, y + 5);
    }

    public boolean colisionaCon(Jugador j) {
        int dx = x - j.x;
        int dy = y - j.y;
        return Math.sqrt(dx * dx + dy * dy) < (RADIO + j.tamanio / 2);
    }

    public boolean verificarYMover(Jugador j) {
        if (colisionaCon(j)) {
            trofeosRecogidos++;
            moverAleatoriamente();
            return true;
        }
        return false;
    }

    public boolean victoriaAlcanzada() {
        return trofeosRecogidos >= TOTAL;
    }

    public int getRecogidos() { return trofeosRecogidos; }
    public int getTotal()     { return TOTAL; }

    public void reiniciar() {
        trofeosRecogidos = 0;
        moverAleatoriamente();
    }
}

// ─── Jugador ───────────────────────────────────────────────────
class Jugador {
    int x, y;
    int tamanio;
    Color color;
    boolean esNPC;

    public Jugador(int x, int y, int tamanio, Color color, boolean esNPC) {
        this.x = x; this.y = y;
        this.tamanio = tamanio;
        this.color = color;
        this.esNPC = esNPC;
    }

    public void dibujar(Graphics g) {
        g.setColor(color);
        g.fillOval(x - tamanio/2, y - tamanio/2, tamanio, tamanio);
        g.setColor(Color.WHITE);
        g.drawOval(x - tamanio/2, y - tamanio/2, tamanio, tamanio);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.setColor(Color.WHITE);
        g.drawString(esNPC ? "NPC" : "P1", x - 8, y + 4);
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

    static final int ESTADO_JUEGO    = 0;
    static final int ESTADO_VICTORIA = 1;
    private int estado = ESTADO_JUEGO;

    private Jugador jugador;
    private Jugador npc;
    private Trofeo  trofeo;

    private final int VEL      = 5;
    private final int CANCHA_X = 80;
    private final int CANCHA_Y = 100;
    private final int CANCHA_W = 480;
    private final int CANCHA_H = 300;

    public PanelJuego() {
        setPreferredSize(new Dimension(640, 480));
        setBackground(new Color(20, 80, 20));

        jugador = new Jugador(140, 250, 28, new Color(30, 80, 220), false);
        npc     = new Jugador(500, 250, 28, new Color(220, 40, 40),  true);
        trofeo  = new Trofeo();

        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (estado == ESTADO_VICTORIA) {
            dibujarPantallaVictoria(g2);
        } else {
            dibujarCancha(g2);
            trofeo.dibujar(g2);
            npc.dibujar(g2);
            jugador.dibujar(g2);
            dibujarHUD(g2);
        }
    }

    // ── Cancha simple ──────────────────────────────────────────
    private void dibujarCancha(Graphics2D g) {
        // Pasto
        g.setColor(new Color(34, 139, 34));
        g.fillRect(CANCHA_X, CANCHA_Y, CANCHA_W, CANCHA_H);

        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2));

        // Borde
        g.drawRect(CANCHA_X, CANCHA_Y, CANCHA_W, CANCHA_H);

        // Línea central
        int cx = CANCHA_X + CANCHA_W / 2;
        g.drawLine(cx, CANCHA_Y, cx, CANCHA_Y + CANCHA_H);

        // Círculo central
        g.drawOval(cx - 40, CANCHA_Y + CANCHA_H/2 - 40, 80, 80);
        g.fillOval(cx - 4, CANCHA_Y + CANCHA_H/2 - 4, 8, 8);

        // Portería izquierda
        int pH = 80, pW = 15;
        int pY = CANCHA_Y + (CANCHA_H - pH) / 2;
        g.setStroke(new BasicStroke(3));
        g.drawRect(CANCHA_X - pW, pY, pW, pH);

        // Portería derecha
        g.drawRect(CANCHA_X + CANCHA_W, pY, pW, pH);

        // Área izquierda
        g.setStroke(new BasicStroke(2));
        g.drawRect(CANCHA_X, CANCHA_Y + (CANCHA_H - 140)/2, 70, 140);

        // Área derecha
        g.drawRect(CANCHA_X + CANCHA_W - 70, CANCHA_Y + (CANCHA_H - 140)/2, 70, 140);
    }

    // ── HUD ────────────────────────────────────────────────────
    private void dibujarHUD(Graphics2D g) {
        // Fondo superior
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, 640, 90);

        g.setFont(new Font("Arial", Font.BOLD, 13));
        g.setColor(Color.CYAN);
        g.drawString("Nombre: Leonardo Rodriguez Valencia", 10, 20);

        g.setColor(Color.WHITE);
        g.drawString("Grupo: 25A  |  Turno: Matutino  |  Año: " + Year.now().getValue(), 10, 40);

        g.setColor(Color.YELLOW);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("P1 → X: " + jugador.x + "   Y: " + jugador.y, 10, 62);

        g.setColor(new Color(255, 215, 0));
        g.drawString("★ Trofeos: " + trofeo.getRecogidos() + " / " + trofeo.getTotal(), 10, 82);

        g.setColor(new Color(180, 180, 180));
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Mover: WASD / Flechas", 460, 62);

        // Fondo inferior
        g.setColor(new Color(0, 0, 0, 130));
        g.fillRect(0, 450, 640, 30);
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.drawString("● Jugador P1", 10, 467);
        g.setColor(Color.RED);
        g.drawString("● NPC", 110, 467);
        g.setColor(new Color(255, 215, 0));
        g.drawString("★ Trofeo (se mueve al tocarlo)", 180, 467);
    }

    // ── Pantalla Victoria ──────────────────────────────────────
    private void dibujarPantallaVictoria(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 210));
        g.fillRect(0, 0, 640, 480);

        g.setColor(new Color(20, 60, 20));
        g.fillRoundRect(120, 110, 400, 260, 30, 30);
        g.setColor(new Color(255, 215, 0));
        g.setStroke(new BasicStroke(3));
        g.drawRoundRect(120, 110, 400, 260, 30, 30);

        g.setFont(new Font("Arial", Font.BOLD, 44));
        g.setColor(new Color(255, 215, 0));
        g.drawString("¡VICTORIA!", 190, 185);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.setColor(Color.WHITE);
        g.drawString("★  Trofeos: " + trofeo.getRecogidos() + " / " + trofeo.getTotal(), 210, 240);

        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.setColor(Color.CYAN);
        g.drawString("Leonardo Rodriguez Valencia", 200, 285);
        g.setColor(Color.WHITE);
        g.drawString("Grupo: 25A  |  Turno: Matutino", 205, 310);

        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.setColor(new Color(180, 255, 180));
        g.drawString("Presiona  R  para jugar de nuevo", 185, 350);
    }

    // ── Teclado ────────────────────────────────────────────────
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (estado == ESTADO_VICTORIA) {
            if (k == KeyEvent.VK_R) reiniciar();
            return;
        }

        if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP)
            jugador.y = Math.max(CANCHA_Y + jugador.tamanio/2, jugador.y - VEL);
        if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN)
            jugador.y = Math.min(CANCHA_Y + CANCHA_H - jugador.tamanio/2, jugador.y + VEL);
        if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT)
            jugador.x = Math.max(CANCHA_X + jugador.tamanio/2, jugador.x - VEL);
        if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT)
            jugador.x = Math.min(CANCHA_X + CANCHA_W - jugador.tamanio/2, jugador.x + VEL);

        // Verificar colisión y mover trofeo si fue tocado
        if (trofeo.verificarYMover(jugador)) {
            if (trofeo.victoriaAlcanzada()) {
                estado = ESTADO_VICTORIA;
            }
        }

        repaint();
    }

    private void reiniciar() {
        jugador.x = 140;
        jugador.y = 250;
        estado = ESTADO_JUEGO;
        trofeo.reiniciar();
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
