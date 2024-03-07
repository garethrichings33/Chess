package com.github.garethrichings33;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGUI extends JFrame implements ActionListener {
    Board board;
    JPanel boardPanel;

    public ChessGUI(){
        board = new Board();

        setTitle("Chess");
        setSize(500, 500);
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

        GridBagConstraints gbc;
        JLabel rowLabel;


        for(int i = 8; i >= 1; i--){
            rowLabel = new JLabel(Integer.toString(i));
            rowLabel.setFont(boardFont);
            gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.NONE;
            gbc.gridx = 0;
            gbc.gridy = 8 - i;
            panel.add(rowLabel, gbc);

            for(int j = 0; j < 8; j++){
                board.getSquare(i, j).getSquareColour();
            }
        }

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
