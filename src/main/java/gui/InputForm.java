package gui;

import logic.generator.Generator;
import logic.scheduler.Scheduler;
import logic.simulator.Simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class InputForm extends JFrame {
    private JTextField numberOfClientsField;
    private JTextField numberOfQueuesField;
    private JTextField simulationIntervalField;
    private JTextField minArrivalTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minServiceTimeField;
    private JTextField maxServiceTimeField;
    private JTextField maxClientsPerServerField;

    public InputForm() {
        setTitle("Queue Management Application Input");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));
        setLocationRelativeTo(null);

        panel.add(new JLabel("Number of Clients (N):"));
        numberOfClientsField = new JTextField();
        panel.add(numberOfClientsField);

        panel.add(new JLabel("Number of Queues (Q):"));
        numberOfQueuesField = new JTextField();
        panel.add(numberOfQueuesField);

        panel.add(new JLabel("Simulation Interval (Max):"));
        simulationIntervalField = new JTextField();
        panel.add(simulationIntervalField);

        panel.add(new JLabel("Minimum Arrival Time:"));
        minArrivalTimeField = new JTextField();
        panel.add(minArrivalTimeField);

        panel.add(new JLabel("Maximum Arrival Time:"));
        maxArrivalTimeField = new JTextField();
        panel.add(maxArrivalTimeField);

        panel.add(new JLabel("Minimum Service Time:"));
        minServiceTimeField = new JTextField();
        panel.add(minServiceTimeField);

        panel.add(new JLabel("Maximum Service Time:"));
        maxServiceTimeField = new JTextField();
        panel.add(maxServiceTimeField);

        panel.add(new JLabel("Maximum Clients Per Server:"));
        maxClientsPerServerField = new JTextField();
        panel.add(maxClientsPerServerField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve input data and process it
                int numberOfClients = Integer.parseInt(numberOfClientsField.getText());
                int numberOfQueues = Integer.parseInt(numberOfQueuesField.getText());
                int simulationInterval = Integer.parseInt(simulationIntervalField.getText());
                int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
                int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
                int minServiceTime = Integer.parseInt(minServiceTimeField.getText());
                int maxServiceTime = Integer.parseInt(maxServiceTimeField.getText());
                int maxClientsPerServer = Integer.parseInt(maxClientsPerServerField.getText());

                Generator generator = new Generator(new Random(), numberOfClients, numberOfQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, maxClientsPerServer);
                Scheduler scheduler = new Scheduler(numberOfQueues, maxClientsPerServer);
                Simulator simulator = new Simulator(generator, scheduler);
                simulator.run();
            }
        });

        panel.add(new JLabel());
        panel.add(submitButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new InputForm();
    }
}

