/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.examples.nqueensga.swingui;

import org.optaplanner.core.impl.move.Move;
import org.optaplanner.core.impl.solution.Solution;
import org.optaplanner.examples.common.swingui.SolutionPanel;
import org.optaplanner.examples.common.swingui.TangoColorFactory;
import org.optaplanner.examples.nqueensga.domain.NQueens;
import org.optaplanner.examples.nqueensga.domain.Queen;
import org.optaplanner.examples.nqueensga.domain.Row;
import org.optaplanner.examples.nqueensga.solver.move.RowChangeMove;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * TODO this code is highly unoptimized
 */
public class NQueensPanel extends SolutionPanel {

    public static final String LOGO_PATH = "/org/optaplanner/examples/nqueens/swingui/nqueensLogo.png";
    private static final String QUEEN_IMAGE_PATH = "/org/optaplanner/examples/nqueens/swingui/queenImage.png";

    private ImageIcon queenImageIcon;

    public NQueensPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.BLACK);
        queenImageIcon = new ImageIcon(getClass().getResource(QUEEN_IMAGE_PATH));
    }

    private NQueens getNQueens() {
        return (NQueens) solutionBusiness.getSolution();
    }

    public void resetPanel(Solution solution) {
        removeAll();
        repaint(); // When GridLayout doesn't fill up all the space
        NQueens nQueens = (NQueens) solution;
        int n = nQueens.getN();
        if (n > 100) {
            JLabel tooBigToShowLabel = new JLabel("The dataset is too big to show.");
            tooBigToShowLabel.setForeground(Color.WHITE);
            add(tooBigToShowLabel);
            return;
        }
        List<Queen> queenList = nQueens.getQueenList();
        setLayout(new GridLayout(n, n));
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                Queen queen = queenList.get(column);
                if (queen.getColumn().getIndex() != column) {
                    throw new IllegalStateException("The queenList is not in the expected order.");
                }
                String toolTipText = "row " + row + ", column " + column;
                if (queen.getRow() != null && queen.getRow().getIndex() == row) {
                    JButton button = new JButton(new QueenAction(queen));
                    button.setMinimumSize(new Dimension(20, 20));
                    button.setPreferredSize(new Dimension(20, 20));
                    button.setToolTipText(toolTipText);
                    add(button);
                } else {
                    JPanel panel = new JPanel();
                    panel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(TangoColorFactory.ALUMINIUM_6),
                            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
                    Color background = (((row + column) % 2) == 0) ? Color.WHITE : TangoColorFactory.ALUMINIUM_3;
                    panel.setBackground(background);
                    panel.setToolTipText(toolTipText);
                    add(panel);
                }
            }
        }
    }

    private class QueenAction extends AbstractAction {

        private Queen queen;

        public QueenAction(Queen queen) {
            super(null, queenImageIcon);
            this.queen = queen;
        }

        public void actionPerformed(ActionEvent e) {
            List<Row> rowList = getNQueens().getRowList();
            JPanel messagePanel = new JPanel(new BorderLayout());
            messagePanel.add(new JLabel("Move to row: "), BorderLayout.WEST);
            JComboBox rowListField = new JComboBox(rowList.toArray());
            rowListField.setSelectedItem(queen.getRow());
            messagePanel.add(rowListField, BorderLayout.CENTER);
            int result = JOptionPane.showConfirmDialog(NQueensPanel.this.getRootPane(), messagePanel,
                    "Queen in column " + queen.getColumn().getIndex(),
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                Row toRow = (Row) rowListField.getSelectedItem();
                Move move = new RowChangeMove(queen, toRow);
                solutionBusiness.doMove(move);
                solverAndPersistenceFrame.resetScreen();
            }
        }

    }

}
