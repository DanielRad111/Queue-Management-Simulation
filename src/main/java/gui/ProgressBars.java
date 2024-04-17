package gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProgressBars extends JFrame {
    public ProgressBars(JProgressBar[] progressBars) {
        setTitle("Queue Progress");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(getParent());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(progressBars.length, 1));

        for (JProgressBar progressBar : progressBars) {
            panel.add(progressBar);
        }

        add(panel);
        setVisible(true);
    }
}
