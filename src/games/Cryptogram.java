package games;

import lib.G;
import lib.Window;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.security.Key;
import java.util.ArrayList;

public class Cryptogram extends Window {
    public static final int  W = 18, H = 50; //Cell SIze
    public static Cell.List cells = new Cell.List();
    public static final int dCode = 18, dGuess = 40, xM = 100, yM = 100, SPACE = 20, LineGap = 45;
    public static Font font = new Font("Veradana", Font.PLAIN, 20);
    public Cryptogram() {
        super("Cryptogram", 1000, 700);
        Cell.init();
        loadQuote("NOW IS THE TIME FOR ALL GOOD MAN");
//        Cell c = new Cell(Pair.alpha[2]);
//        new Cell(Pair.alpha[9]);
//        c.p.guess = 'W';
//        Cell.selected = c;
    }

    public void paintComponent(Graphics g){
        G.backRect.fill(g, Color.WHITE);
//        g.setColor(Color.RED);
//        g.fillRect(100, 100, 100, 100);
        g.setFont(font);
        cells.show(g);
    }

    public void keyTyped (KeyEvent ke) {
        char c = ke.getKeyChar();
        if (c >= 'a' && c <= 'z') {
            c = (char) (c - 'a' + 'A');
        }
        if (Cell.selected != null) {
            Cell.selected.p.guess = (c >= 'A' && c <= 'Z') ? c : ' ';
        }
        repaint();
    }

    public void keyPressed(KeyEvent ke) {
        int vk = ke.getKeyCode();
        if (vk == KeyEvent.VK_LEFT) {
//            System.out.println("LEFT");
            Cell.select(Cell.selNdx() - 1);
        }
        if (vk == KeyEvent.VK_RIGHT) {
//            System.out.println("RIGHT");
            Cell.select(Cell.selNdx() + 1);
        }
        repaint();
    }

    public static void main(String[] args) {
        PANEL = new Cryptogram();
        launch();
    }

    public void loadQuote(String q) {
        Cell.init();
        Pair.init();
        for (int i = 0; i < q.length(); i++) {
            char c = q.charAt(i);
            int iAlpha = c - 'A';
            if (c >= 'A' && c <= 'Z') {
                new Cell(Pair.alpha[iAlpha]);
            } else {
                Cell.nextLoc.x += SPACE;
                if (Cell.nextLoc.x > 900) {
                    Cell.nextLoc.set(Cell.nextLine.x, Cell.nextLine.y);
                    Cell.nextLine.y += LineGap;
                }
            }
        }
        Cell.selected = cells.get(0);
    }


    //----------------Cell-------------------
    public static class Cell {
        public static G.V size = new G.V(W, H);
        public static G.V nextLoc = new G.V(), nextLine = new G.V();
        public G.V loc;
        public Pair p;
        public int ndx;  // index of cell
        public static Cell selected = null;
        public Cell(Pair p) {
            this.p = p;
            this.loc = new G.V(nextLoc);
            nextLoc.x += SPACE;
            ndx = cells.size();
            cells.add(this);
        }

        public static void init() {nextLoc.set(xM, yM); nextLine.set(xM, yM + LineGap);}

        public static void select(int n) {
            if (n >= 0 && n < cells.size()) {
                selected = cells.get(n);
            }
        }

        public static int selNdx() {
            return selected.ndx;
        }
        public void show(Graphics g) {
            if (this == selected) {
                g.setColor(Color.RED);
                g.fillRect(loc.x, loc.y, size.x, size.y);
            }
            g.setColor(Color.BLACK);
            g.drawString("" + p.code, loc.x, loc.y + dCode);
            g.drawString("" + p.guess, loc.x, loc.y + dGuess);
        }
        // ---------------List---------------
        public static class List extends ArrayList<Cell> {
            public void show(Graphics g) {
                for (Cell c : this) {c.show(g);}
            }
        }
    }

    //----------------Pair-------------------
    public static class Pair {
        public char actual, code, guess;
        public static Pair[] alpha = new Pair[26];
        static {
            for (int i = 0; i < 26; i++) {
                alpha[i] = new Pair((char)('A' + i));
            }
        }
        public Pair(char c) {
            actual = c; code = c;guess =' ';
        }

        public static void init() {
            for (int i = 0; i < 26; i++) {
                Pair p = alpha[i];
                p.guess = ' ';
                Pair x = alpha[G.rnd(26)];
                char c = p.code; p.code = x.code; x.code = c; //swap
            }
        }
    }
}
