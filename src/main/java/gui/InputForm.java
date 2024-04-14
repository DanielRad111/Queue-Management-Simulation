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
    private JTextArea simulationArea;
    private JScrollPane simulationScrollPane;
    private JPanel resultsPanel;
    private JPanel inputForm;
    private JLabel averageWaitingTimeLabel;
    private JLabel averageServiceLabel;
    private JLabel peakHourLabel;
    private JTextField numberOfClientsField;
    private JTextField numberOfQueuesField;
    private JTextField simulationIntervalField;
    private JTextField minArrivalTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minServiceTimeField;
    private JTextField maxServiceTimeField;
    private JComboBox<SelectionPolicy> strategyComboBox;
    private JButton startButton;

    public InputForm() {
        setTitle("Queue Management Application Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        simulationArea = new JTextArea();
        simulationArea.setEditable(false);

        simulationScrollPane = new JScrollPane(simulationArea);
        simulationScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        simulationScrollPane.setBackground(new Color(47, 79, 79));
        simulationScrollPane.setForeground(new Color(47, 79, 79));
        add(simulationScrollPane, BorderLayout.CENTER);

        inputForm = createInputPanel();
        add(inputForm, BorderLayout.NORTH);

        resultsPanel = createResultPanel();
        add(resultsPanel, BorderLayout.SOUTH);

        startButton = new JButton("Start");
        startButton.setBackground(new Color(70, 130, 180));
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numberOfClients = Integer.parseInt(numberOfClientsField.getText());
                int numberOfQueues = Integer.parseInt(numberOfQueuesField.getText());
                int simulationInterval = Integer.parseInt(simulationIntervalField.getText());
                int minArrivalTime = Integer.parseInt(minArrivalTimeField.getText());
                int maxArrivalTime = Integer.parseInt(maxArrivalTimeField.getText());
                int minServiceTime = Integer.parseInt(minServiceTimeField.getText());
                int maxServiceTime = Integer.parseInt(maxServiceTimeField.getText());
                SelectionPolicy strategy = (SelectionPolicy) strategyComboBox.getSelectedItem();

                Generator generator = new Generator(new Random(), numberOfClients, numberOfQueues, simulationInterval, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
                Scheduler scheduler = new Scheduler(numberOfQueues);
                scheduler.serversInitializer();
                scheduler.changeStrategy(strategy);

                Simulator simulator = new Simulator(generator, scheduler, "/Users/danielrad/Desktop/QZ/Coding/TP/Assignment2/src/main/java/logic/logger/log");
                Thread t = new Thread(simulator);
                t.start();
            }
        });
        inputForm.add(new JLabel());
        inputForm.add(startButton);

        setVisible(true);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 2));
        panel.setBackground(new Color(47, 79, 79));

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

        panel.add(createLabel("Strategy:"));
        strategyComboBox = new JComboBox<>(SelectionPolicy.values());
        strategyComboBox.setBackground(new Color(192, 192, 192));
        strategyComboBox.setForeground(Color.BLACK);
        panel.add(strategyComboBox);

        return panel;
    }

    private JPanel createResultPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.setBackground(new Color(47, 79, 79));
        averageWaitingTimeLabel = createLabel("Average Waiting Time:");
        averageServiceLabel = createLabel("Average Service Time:");
        peakHourLabel = createLabel("Peak Hour:");
        panel.add(averageWaitingTimeLabel);
        panel.add(new JLabel());
        panel.add(averageServiceLabel);
        panel.add(new JLabel());
        panel.add(peakHourLabel);
        panel.add(new JLabel());

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE); // Set the foreground color to white
        label.setFont(new Font("Arial", Font.BOLD, 14)); // Set the font to bold
        return label;
    }

    public void appendLog(String text) {
        simulationArea.append(text + "\n");
        simulationArea.setCaretPosition(simulationArea.getDocument().getLength());
    }

    public void setAvgWaitingTime(double avgWaitingTime) {
        averageWaitingTimeLabel.setText("Average waiting time: " + avgWaitingTime);
    }

    public void setAvgServiceTime(double avgServiceTime) {
        averageServiceLabel.setText("Average service time: " + avgServiceTime);
    }

    public void setPeakHour(int peakHour) {
        peakHourLabel.setText("Peak hour: " + peakHour);
    }

    public int getNumberOfClients() {
        return Integer.parseInt(numberOfClientsField.getText());
    }

    public int getNumberOfServers() {
        return Integer.parseInt(numberOfQueuesField.getText());
    }

    public int getTimeLimit() {
        return Integer.parseInt(maxServiceTimeField.getText());
    }

    public int getMinArrivalTime() {
        return Integer.parseInt(minArrivalTimeField.getText());
    }

    public int getMaxArrivalTime() {
        return Integer.parseInt(maxArrivalTimeField.getText());
    }

    public int getMinServiceTime() {
        return Integer.parseInt(minServiceTimeField.getText());
    }

    public int getMaxServiceTime() {
        return Integer.parseInt(maxServiceTimeField.getText());
    }
}
