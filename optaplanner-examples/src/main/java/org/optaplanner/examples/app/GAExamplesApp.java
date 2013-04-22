package org.optaplanner.examples.app;


import org.optaplanner.examples.cloudbalancingga.app.CloudBalancingApp;
import org.optaplanner.examples.cloudbalancingga.swingui.CloudBalancingPanel;
import org.optaplanner.examples.common.app.CommonApp;
import org.optaplanner.examples.common.swingui.TangoColorFactory;
import org.optaplanner.examples.nqueensga.app.NQueensApp;
import org.optaplanner.examples.nqueensga.swingui.NQueensPanel;
import org.optaplanner.examples.tspga.app.TspApp;
import org.optaplanner.examples.tspga.swingui.TspPanel;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GAExamplesApp extends JFrame {
    public static void main(String[] args) {
        CommonApp.fixateLookAndFeel();
        GAExamplesApp optaPlannerExamplesApp = new GAExamplesApp();
        optaPlannerExamplesApp.pack();
        optaPlannerExamplesApp.setLocationRelativeTo(null);
        optaPlannerExamplesApp.setVisible(true);
    }

    private JTextArea descriptionTextArea;

    public GAExamplesApp() {
        super("OptaPlanner examples");
        setContentPane(createContentPane());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Container createContentPane() {
        JPanel contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JLabel titleLabel = new JLabel("Which example do you want to see?", JLabel.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(20.0f));
        contentPane.add(titleLabel, BorderLayout.NORTH);
        JScrollPane examplesScrollPane = new JScrollPane(createExamplesPanel());
        examplesScrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        examplesScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        contentPane.add(examplesScrollPane, BorderLayout.CENTER);
        contentPane.add(createDescriptionPanel(), BorderLayout.SOUTH);
        return contentPane;
    }

    private JPanel createExamplesPanel() {
        JPanel examplesPanel = new JPanel();
        examplesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        GroupLayout layout = new GroupLayout(examplesPanel);
        examplesPanel.setLayout(layout);
        JPanel basicExamplesPanel = createBasicExamplesPanel();
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(basicExamplesPanel));
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(basicExamplesPanel));
        return examplesPanel;
    }

    private JPanel createBasicExamplesPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Basic examples");
        titledBorder.setTitleColor(TangoColorFactory.CHAMELEON_3);
        panel.setBorder(BorderFactory.createCompoundBorder(titledBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(createExampleButton("N queens",
                "Place queens on a chessboard.\n\n" +
                        "No 2 queens must be able to attack each other.",
                NQueensPanel.LOGO_PATH, new Runnable() {
            public void run() {
                new NQueensApp().init(GAExamplesApp.this, false);
            }
        }));
        panel.add(createExampleButton("Cloud balancing",
                "Assign processes to computers.\n\n" +
                        "Each computer must have enough hardware to run all of it's processes.\n" +
                        "Each computer used inflicts a maintenance cost.",
                CloudBalancingPanel.LOGO_PATH, new Runnable() {
            public void run() {
                new CloudBalancingApp().init(GAExamplesApp.this, false);
            }
        }));
        panel.add(createExampleButton("Traveling salesman",
                "Official competition name: TSP - Traveling salesman problem\n" +
                        "Determine the order in which to visit all cities.\n\n" +
                        "Find the shortest route to visit all cities.",
                TspPanel.LOGO_PATH, new Runnable() {
            public void run() {
                new TspApp().init(GAExamplesApp.this, false);
            }
        }));
        return panel;
    }

    private JButton createDisabledExampleButton(final String title, final String description, String iconResource) {
        JButton exampleButton = createExampleButton(title, description, iconResource, null);
        exampleButton.setEnabled(false);
        return exampleButton;
    }

    private JButton createExampleButton(final String title, final String description, String iconResource,
                                        final Runnable runnable) {
        ImageIcon icon = iconResource == null ? null : new ImageIcon(getClass().getResource(iconResource));
        JButton button = new JButton(new AbstractAction(title, icon) {
            public void actionPerformed(ActionEvent e) {
                runnable.run();
            }
        });
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                descriptionTextArea.setText(description);
            }

            public void mouseExited(MouseEvent e) {
                descriptionTextArea.setText("");
            }

        });
        return button;
    }

    private JPanel createDescriptionPanel() {
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(new JLabel("Description"), BorderLayout.NORTH);
        descriptionTextArea = new JTextArea(8, 80);
        descriptionTextArea.setEditable(false);
        descriptionPanel.add(new JScrollPane(descriptionTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        return descriptionPanel;
    }

}
