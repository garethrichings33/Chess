package com.github.garethrichings33;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGUI extends JFrame implements ActionListener {
    private Board board;
    private JPanel boardPanel;
    private JButton[][] squareButtons;

    public ChessGUI(){
        board = new Board();

        setTitle("Chess");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        addPanels();

        setVisible(true);
    }

    private void addPanels() {
        GridBagConstraints gbc;

        boardPanel = createBoardPanel();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(boardPanel, gbc);
    }

    private JPanel createBoardPanel() {
        var panel =  new JPanel(new GridBagLayout());
        var boardFont = new Font("Arial", Font.PLAIN, 20);
        var squareSize = new Dimension(70, 70);
        squareButtons = new JButton[8][8];

        GridBagConstraints gbc;
        JLabel rowLabel;
        JLabel columnLabel;
        JButton square;

        for(int i = 0; i < 8; i++){
            rowLabel = new JLabel(Integer.toString(8 - i));
            rowLabel.setFont(boardFont);
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.ipadx = 20;
            panel.add(rowLabel, gbc);

            for(int j = 0; j < 8; j++){
                square = new JButton();
                square.addActionListener(this);
                square.setPreferredSize(squareSize);
                if(board.getSquare(i, j).getSquareColour() == SquareColour.BLACK)
                    square.setBackground(Color.BLACK);
                else
                    square.setBackground(Color.WHITE);
                gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = j + 1;
                gbc.gridy = i;
                gbc.insets = new Insets(-2,-2,-2,-2);
                panel.add(square, gbc);

                if(i == 7 && j == 4)
                    square.setIcon(board.getSquare(7,4).getPiece().getPieceIcon());

                squareButtons[i][j] = square;
            }
        }

        for(int i = 1; i < 9; i++) {
            String label = Character.toString((char)(64+i));
            columnLabel = new JLabel(label);
            columnLabel.setFont(boardFont);
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = i;
            gbc.gridy = 8;
            gbc.ipady = 20;
            panel.add(columnLabel,gbc);
        }

//        addPieces();
        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private void addPieces(){
        Icon icon;
//        for(int i = 0; i < 8; i++)
//            for(int j = 0; j < 8; j++){
//                icon = board.getSquare(i,j).getPiece().getPieceIcon();
//                if(icon != null)
//                    squareButtons[i][j].setIcon(icon);
//            }
                icon = board.getSquare(7,4).getPiece().getPieceIcon();
                squareButtons[7][4].setIcon(icon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
