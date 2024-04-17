package gui;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import logic.simulator.Simulator;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProgressBars extends JFrame {
    public ProgressBars(JProgressBar[] progressBars) {
        setTitle("Queue Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 800);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(progressBars.length, 2));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));

        for (int i = 0; i < progressBars.length; i++) {
            panel.add(new JLabel("                                          Server: " + (i + 1)));
            panel.add(progressBars[i]);
        }

        add(panel);
        setVisible(true);
    }
}
