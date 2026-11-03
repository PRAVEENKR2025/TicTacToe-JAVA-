import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe extends JFrame {
    CardLayout cl = new CardLayout();
    JPanel container = new JPanel(cl);
    JButton[] buttons = new JButton[9];
    JLabel status = new JLabel("Player X's Turn", SwingConstants.CENTER);
    JLabel scoreLabel = new JLabel("X: 0  |  O: 0  |  Draws: 0", SwingConstants.CENTER);
    
    char turn = 'X';
    boolean over = false;
    int moves = 0, xWins = 0, oWins = 0, draws = 0;
    
    Color bg = new Color(20, 24, 33), pnl = new Color(33, 38, 51), xCol = new Color(255, 75, 110), oCol = new Color(0, 212, 255);

    public TicTacToe() {
        setTitle("Tic-Tac-Toe Arena");
        setSize(400, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 1. Cover Page (Matching your design)
        JPanel cover = new JPanel(new GridBagLayout());
        cover.setBackground(bg);
        JPanel box = new JPanel(); box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS)); box.setOpaque(false);
        
        JLabel icon = new JLabel("✕   ○", SwingConstants.CENTER); icon.setFont(new Font("Segoe UI", Font.BOLD, 42)); icon.setForeground(xCol); icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER); title.setFont(new Font("Segoe UI", Font.BOLD, 26)); title.setForeground(Color.WHITE); title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("<html><center>Experience classic strategy with smart bot AI & sleek visuals</center></html>", SwingConstants.CENTER); sub.setForeground(new Color(150, 160, 180)); sub.setAlignmentX(Component.CENTER_ALIGNMENT); sub.setBorder(BorderFactory.createEmptyBorder(8, 20, 20, 20));
        
        JButton enter = new JButton("🎮 Enter Arena"); enter.setFont(new Font("Segoe UI", Font.BOLD, 15)); enter.setForeground(Color.WHITE); enter.setBackground(new Color(80, 110, 255)); enter.setFocusPainted(false); enter.setAlignmentX(Component.CENTER_ALIGNMENT);
        enter.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        enter.addActionListener(e -> cl.show(container, "GAME"));

        box.add(icon); box.add(Box.createVerticalStrut(10)); box.add(title); box.add(sub); box.add(Box.createVerticalStrut(5)); box.add(enter);
        cover.add(box);

        // 2. Game Page
        JPanel game = new JPanel(new BorderLayout(10, 10));
        game.setBackground(bg); game.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        // Top Panel: Scores & Status
        JPanel topPanel = new JPanel(new GridLayout(2, 1, 0, 5)); topPanel.setOpaque(false);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 14)); scoreLabel.setForeground(new Color(200, 200, 220));
        status.setFont(new Font("Segoe UI", Font.BOLD, 17)); status.setForeground(xCol);
        topPanel.add(scoreLabel); topPanel.add(status);
        game.add(topPanel, BorderLayout.NORTH);

        // 3x3 Grid Board
        JPanel board = new JPanel(new GridLayout(3, 3, 6, 6)); board.setBackground(bg);
        for (int i = 0; i < 9; i++) {
            final int idx = i;
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Segoe UI", Font.BOLD, 48));
            buttons[i].setFocusPainted(false);
            buttons[i].setBackground(pnl);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].setBorder(BorderFactory.createLineBorder(new Color(60, 70, 95), 2));
            
            buttons[i].addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { if(buttons[idx].getText().isEmpty() && !over) buttons[idx].setBackground(new Color(50, 60, 85)); }
                public void mouseExited(MouseEvent e) { if(buttons[idx].getText().isEmpty() && !over) buttons[idx].setBackground(pnl); }
            });
            buttons[i].addActionListener(e -> play(idx));
            board.add(buttons[i]);
        }
        game.add(board, BorderLayout.CENTER);

        // Bottom Controls
        JPanel bottom = new JPanel(new GridLayout(1, 2, 8, 0)); bottom.setOpaque(false);
        JButton menu = new JButton("MAIN MENU"); menu.setForeground(Color.WHITE); menu.setBackground(new Color(75, 85, 120)); menu.setFocusPainted(false);
        JButton reset = new JButton("NEXT ROUND"); reset.setForeground(Color.WHITE); reset.setBackground(new Color(80, 110, 255)); reset.setFocusPainted(false);
        
        menu.addActionListener(e -> { resetScores(); cl.show(container, "COVER"); });
        reset.addActionListener(e -> resetBoard());
        
        bottom.add(menu); bottom.add(reset);
        game.add(bottom, BorderLayout.SOUTH);

        container.add(cover, "COVER");
        container.add(game, "GAME");
        add(container);
        setVisible(true);
    }

    void play(int i) {
        if (over || !buttons[i].getText().isEmpty()) return;
        buttons[i].setText(String.valueOf(turn));
        buttons[i].setForeground(turn == 'X' ? xCol : oCol);
        moves++;

        if (checkWin()) {
            status.setText("🎉 Player " + turn + " Wins!");
            status.setForeground(new Color(46, 204, 113));
            if (turn == 'X') xWins++; else oWins++;
            updateScoreText();
            over = true;
            return;
        }
        if (moves == 9) {
            status.setText("🤝 It's a Draw!");
            status.setForeground(Color.YELLOW);
            draws++;
            updateScoreText();
            over = true;
            return;
        }
        turn = (turn == 'X') ? 'O' : 'X';
        status.setText("Player " + turn + "'s Turn");
        status.setForeground(turn == 'X' ? xCol : oCol);
    }

    boolean checkWin() {
        int[][] w = {{0,1,2},{3,4,5},{6,7,8},{0,3,6},{1,4,7},{2,5,8},{0,4,8},{2,4,6}};
        for (int[] l : w) {
            if (!buttons[l[0]].getText().isEmpty() && buttons[l[0]].getText().equals(buttons[l[1]].getText()) && buttons[l[1]].getText().equals(buttons[l[2]].getText())) return true;
        }
        return false;
    }

    void updateScoreText() {
        scoreLabel.setText("X: " + xWins + "  |  O: " + oWins + "  |  Draws: " + draws);
    }

    void resetBoard() {
        for (JButton b : buttons) { b.setText(""); b.setBackground(pnl); }
        turn = 'X'; over = false; moves = 0;
        status.setText("Player X's Turn"); status.setForeground(xCol);
    }

    void resetScores() {
        xWins = 0; oWins = 0; draws = 0;
        updateScoreText();
        resetBoard();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}