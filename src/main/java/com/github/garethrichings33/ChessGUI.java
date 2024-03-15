package com.github.garethrichings33;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGUI extends JFrame implements ActionListener {
    private GamePlay gamePlay;
    private JPanel boardPanel;
    private JButton[][] squareButtons;
    String fromButton;
    String toButton;

    public ChessGUI(){
        gamePlay = new GamePlay(this);

        setTitle("Chess");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        addPanels();

        fromButton = "";
        toButton = "";

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
        JButton squareButton;

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
                squareButton = new JButton();
                squareButton.setActionCommand(Integer.toString(i) + Integer.toString(j));
                squareButton.addActionListener(this);
                squareButton.setPreferredSize(squareSize);
                if(gamePlay.getSquare(i, j).getSquareColour() == SquareColour.BLACK)
                    squareButton.setBackground(new Color(153, 46, 39));
                else
                    squareButton.setBackground(Color.WHITE);
                gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.NONE;
                gbc.gridx = j + 1;
                gbc.gridy = i;
                gbc.insets = new Insets(-2,-2,-2,-2);
                panel.add(squareButton, gbc);

                squareButtons[i][j] = squareButton;
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

        placePieces();
        panel.revalidate();
        panel.repaint();
        return panel;
    }

    private void placePieces(){
        for(int i = 0; i < 8; i++)
            for(int j = 0; j < 8; j++)
                if(gamePlay.getSquare(i, j).getPiece() != null)
                    squareButtons[i][j].setIcon(new ImageIcon(gamePlay.getSquare(i, j).getPiece().getPieceIcon()));
                else
                    squareButtons[i][j].setIcon(null);
    }

    @Override
    public void actionPerformed(ActionEvent action) {
        var buttonPressed = action.getActionCommand();
        MoveTypes moveType;

        if(fromButton.equals(""))
            fromButton = buttonPressed;
        else {
            toButton = buttonPressed;
            moveType = gamePlay.movePiece(fromButton, toButton);
            if(moveType == MoveTypes.INVALID)
                invalidMoveWarning();
            else {
                placePieces();
                if(moveType == MoveTypes.CHECK)
                    checkMessage();
            }
            fromButton = "";
            toButton = "";
        }
    }

    public String promotePawn(int[] fromCoordinates, int[] toCoordinates) {
        String[] options = {"Queen", "Bishop", "Knight", "Rook"};
        return JOptionPane.showInputDialog(this, "Choose piece",
                "Queen", JOptionPane.PLAIN_MESSAGE, null, options, options[0]).toString();
    }

    private void invalidMoveWarning() {
        JOptionPane.showMessageDialog(this,
                "Invalid move. Please try again.");
    }

    public void checkMessage() {
        JOptionPane.showMessageDialog(this,
                "Check");
    }
}
