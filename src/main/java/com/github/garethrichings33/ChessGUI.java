package com.github.garethrichings33;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessGUI extends JFrame implements ActionListener {
    private GamePlay gamePlay;
    private JPanel boardPanel;
    private JPanel infoPanel;
    private JPanel buttonPanel;
    private JButton drawButton;
    private JButton concedeButton;
    private Font gameFont;
    private JButton[][] squareButtons;
    private JLabel playerToGo;
    private JLabel scoreboard;
    private String fromButton;
    private String toButton;

    public ChessGUI(){
        gamePlay = new GamePlay(this);

        gameFont = new Font("Arial", Font.PLAIN, 20);

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

        infoPanel = createInformationPanel();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(infoPanel, gbc);

        buttonPanel = createButtonPanel();
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(buttonPanel, gbc);
    }

    private JPanel createBoardPanel() {
        var panel =  new JPanel(new GridBagLayout());
        var boardFont = gameFont;
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

    private JPanel createInformationPanel(){
        GridBagConstraints gbc;
        var panel = new JPanel(new GridBagLayout());

        playerToGo = new JLabel();
        updateNextPlayerLabel();
        playerToGo.setFont(gameFont);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(playerToGo, gbc);

        scoreboard = new JLabel();
        updateScoreboard();
        scoreboard.setFont(gameFont);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 40;
        panel.add(scoreboard, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        GridBagConstraints gbc;
        var panel = new JPanel(new GridBagLayout());
        var insets = new Insets(0, 10, 0, 10);

        drawButton = new JButton();
        drawButton.setFont(gameFont);
        drawButton.setText("Draw");
        drawButton.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = insets;
        panel.add(drawButton, gbc);

        concedeButton = new JButton();
        concedeButton.setFont(gameFont);
        concedeButton.setText("Concede");
        concedeButton.addActionListener(this);
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = insets;
        panel.add(concedeButton, gbc);

        return panel;
    }

    private void updateScoreboard() {
        var players = gamePlay.getPlayers();
        String format = "%s %.0f : %.0f %s";
        if(Math.abs(players[0].getPointsWon() - Math.floor(players[0].getPointsWon())) >  0.1)
            format = "%s %.1f : %.1f %s";
        scoreboard.setText(String.format(format, players[0].getName(), players[0].getPointsWon(),
                 players[1].getPointsWon(), players[1].getName()));
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

        if(buttonPressed.equals("Draw"))
            draw();
        else if(buttonPressed.equals("Concede"))
            concede();
        else if(fromButton.equals(""))
            fromButton = buttonPressed;
        else {
            toButton = buttonPressed;
            moveManager();
        }
    }

    private void draw() {
        if(JOptionPane.showConfirmDialog(this, "Draw?", "Confirm Draw",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            gamePlay.drawGame();
        outputTurnsConfirm();
        newGame();
        updateNextPlayerLabel();
    }

    private void concede() {
        if(JOptionPane.showConfirmDialog(this, "Really Concede?", "Confirm Concession",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            gamePlay.concedeGame();
        outputTurnsConfirm();
        newGame();
        updateNextPlayerLabel();
    }

    private void moveManager() {
        var moveType = gamePlay.gameTurn(fromButton, toButton);
        if(moveType == MoveTypes.INVALID)
            invalidMoveWarning();
        else {
            placePieces();
            if (moveType == MoveTypes.CHECKMATE) {
                checkMateMessage();
                outputTurnsConfirm();
                newGame();
            }
            else if(moveType == MoveTypes.CHECK)
                checkMessage();
            updateNextPlayerLabel();
        }
        resetPressedButtons();
    }

    private void outputTurnsConfirm() {
        if(JOptionPane.showConfirmDialog(this, "Get list of turns?", "Output Turns",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
            outputTurns();
    }

    private void outputTurns() {
        JFrame outputFrame = new JFrame("Turns");
        outputFrame.setFont(gameFont);
        outputFrame.setSize(200, 400);
        outputFrame.setDefaultCloseOperation(HIDE_ON_CLOSE);
        outputFrame.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setFont(gameFont);
        textArea.setEditable(false);
        textArea.getCaret().setVisible(false);

        var turns = gamePlay.getTurns();
        String turn;
        int turnNo = 0;
        while(!turns.isEmpty()){
            turnNo++;
            turn = turns.removeFirst().toString();
            textArea.append(turnNo + ":  " + turn);
        }

        var scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setSize(new Dimension(90, 380));

        outputFrame.add(scrollPane);
        outputFrame.setVisible(true);
    }

    private void newGame() {
        gamePlay.initialiseNewGame();
        placePieces();
        updateScoreboard();
    }

    private void updateNextPlayerLabel() {
        playerToGo.setText(gamePlay.getActivePlayer().getPieceColourString() +
                " to go (" + gamePlay.getActivePlayer().getName() + ")" );
    }

    private void resetPressedButtons() {
        fromButton = "";
        toButton = "";
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

    public void checkMateMessage() {
        JOptionPane.showMessageDialog(this,
                "Checkmate! " + gamePlay.getInactivePlayer().getName() + " wins!");
    }

    public String getPlayerName(int playerNumber) {
        String name;
        name =  JOptionPane.showInputDialog("Add name of player " + playerNumber);
        if(name == null || name.length() == 0)
            name = "Player " + playerNumber;
        return name;
    }
}
