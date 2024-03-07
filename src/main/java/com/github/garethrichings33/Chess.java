package com.github.garethrichings33;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;

public class Chess
{
    public static void main( String[] args )
    {
        try{
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        }catch(Exception ex){
            System.err.println("Failed to initialise theme. Using fallback.");
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ChessGUI();
            }
        });
    }
}
