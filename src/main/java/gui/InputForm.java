package gui;

import logic.generator.Generator;
import logic.scheduler.Scheduler;
import logic.scheduler.strategy.SelectionPolicy;
import logic.simulator.Simulator;
import model.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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
    private JComboBox<SelectionPolicy> strategyComboBox;

    public InputForm() {
        setTitle("Queue Management Application Input");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2));
        panel.setBackground(new Color(47, 79, 79));
        setLocationRelativeTo(null);

        panel.add(createLabel("Number of Clients (N):"));
        numberOfClientsField = new JTextField();
        numberOfClientsField.setBackground(new Color(192, 192, 192));
        numberOfClientsField.setForeground(Color.BLACK);
        numberOfClientsField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(numberOfClientsField);

        panel.add(createLabel("Number of Queues (Q):"));
        numberOfQueuesField = new JTextField();
        numberOfQueuesField.setBackground(new Color(192, 192, 192));
        numberOfQueuesField.setForeground(Color.BLACK);
        numberOfQueuesField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(numberOfQueuesField);


        panel.add(createLabel("Simulation Interval (Max):"));
        simulationIntervalField = new JTextField();
        simulationIntervalField.setBackground(new Color(192, 192, 192));
        simulationIntervalField.setForeground(Color.BLACK);
        simulationIntervalField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(simulationIntervalField);


        panel.add(createLabel("Minimum Arrival Time:"));
        minArrivalTimeField = new JTextField();
        minArrivalTimeField.setBackground(new Color(192, 192, 192));
        minArrivalTimeField.setForeground(Color.BLACK);
        minArrivalTimeField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(minArrivalTimeField);


        panel.add(createLabel("Maximum Arrival Time:"));
        maxArrivalTimeField = new JTextField();
        maxArrivalTimeField.setBackground(new Color(192, 192, 192));
        maxArrivalTimeField.setForeground(Color.BLACK);
        maxArrivalTimeField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(maxArrivalTimeField);


        panel.add(createLabel("Minimum Service Time:"));
        minServiceTimeField = new JTextField();
        minServiceTimeField.setBackground(new Color(192, 192, 192));
        minServiceTimeField.setForeground(Color.BLACK);
        minServiceTimeField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(minServiceTimeField);


        panel.add(createLabel("Maximum Service Time:"));
        maxServiceTimeField = new JTextField();
        maxServiceTimeField.setBackground(new Color(192, 192, 192));
        maxServiceTimeField.setForeground(Color.BLACK);
        maxServiceTimeField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(maxServiceTimeField);


        panel.add(createLabel("Maximum Clients Per Server:"));
        maxClientsPerServerField = new JTextField();
        maxClientsPerServerField.setBackground(new Color(192, 192, 192));
        maxClientsPerServerField.setForeground(Color.BLACK);
        maxClientsPerServerField.setFont(new Font("Arial", Font.PLAIN, 14));
        panel.add(maxClientsPerServerField);


        panel.add(createLabel("Strategy:"));
        strategyComboBox = new JComboBox<>(SelectionPolicy.values());
        strategyComboBox.setBackground(new Color(105, 105, 105));
        strategyComboBox.setForeground(Color.WHITE);
        panel.add(strategyComboBox);

        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberOfClients = Integer.parseInt(numberOfClientsField.getText());
                int numberOfQueues = Integer.parseInt(numberOfQueuesField.getText());
                int simulationInterval = Integer.parseInt(simulationIntervalField.getText());
                int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
                int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
                int minServiceTime = Integer.parseInt(minServiceTimeField.getText());
                int maxServiceTime = Integer.parseInt(maxServiceTimeField.getText());
                int maxClientsPerServer = Integer.parseInt(maxClientsPerServerField.getText());
                SelectionPolicy strategy = (SelectionPolicy) strategyComboBox.getSelectedItem();

                Generator generator = new Generator(new Random(), numberOfClients, numberOfQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, maxClientsPerServer);
                Scheduler scheduler = new Scheduler(numberOfQueues, maxClientsPerServer);
                scheduler.serversInitializer();
                scheduler.changeStrategy(strategy);
                
                Simulator simulator = new Simulator(generator, scheduler, "D:\\CODING\\Proiecte InteliJ\\TP\\Assignment2\\src\\main\\java\\logic\\logger\\log");
                simulator.run();
            }
        });

        panel.add(new JLabel());
        panel.add(submitButton);

        add(panel);
        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE); // Set the foreground color to white
        label.setFont(new Font("Arial", Font.BOLD, 14)); // Set the font to bold
        return label;
    }


    public static void main(String[] args) {
        new InputForm();
    }
}
