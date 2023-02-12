package View;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JToolBar;
import java.awt.TextField;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.Label;
import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;

import org.math.plot.Plot2DPanel;

import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Font;

public class MainView extends JFrame {

	private JPanel window;
	private JTextField numGenTextField;
	private JTextField crossProbabilityTextField;
	private JTextField mutationProbabilityTextField;
	private Plot2DPanel plot;
	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
	private JTextField solutionTextField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		this.setTitle("Practica 1 - PEV");
		this.setResizable(false);
		this.setMinimumSize(new Dimension(1280,720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
				
		JPanel westPanel = new JPanel();
		getContentPane().add(westPanel, BorderLayout.WEST);
		westPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		JLabel genSizeLabel = new JLabel("Tamaño de la población");
		genSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));;
		JTextField genSizeTextField = new JFormattedTextField(numberFormat);
		genSizeTextField.setText("100");
		
		JLabel numGenLabel = new JLabel("Número de generaciones");
		numGenLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		numGenTextField = new JTextField();
		numGenTextField.setText("100");
		numGenTextField.setColumns(10);
		
		
		JPanel selectionPanel = new JPanel();
		selectionPanel.setBorder(new TitledBorder(null, "Selecci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		selectionPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel selectionTypeLabel = new JLabel("Tipo de seleección");
		selectionTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		selectionPanel.add(selectionTypeLabel);
		
		JComboBox selectionTypeComboBox = new JComboBox();
		selectionTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Ruleta", "Estocástico", "T-Determinístico", "T-Probabilístico"}));
		selectionPanel.add(selectionTypeComboBox);
		
		JPanel crossPanel = new JPanel();
		crossPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Cruce", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		crossPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel crossTypeLabel = new JLabel("Tipo de cruce");
		crossTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		crossPanel.add(crossTypeLabel);
		
		JComboBox crossTypeComboBox = new JComboBox();
		crossTypeComboBox.setModel(new DefaultComboBoxModel(new String[] {"Cruce Monopunto"}));
		crossPanel.add(crossTypeComboBox);
		
		JLabel crossProbabilityLabel = new JLabel("% Cruce");
		crossPanel.add(crossProbabilityLabel);
		
		crossProbabilityTextField = new JTextField();
		crossProbabilityTextField.setText("60.0");
		crossPanel.add(crossProbabilityTextField);
		crossProbabilityTextField.setColumns(10);
		
		JPanel mutationPanel = new JPanel();
		mutationPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Mutaci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		mutationPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel mutationTypeLabel = new JLabel("Tipo de mutación");
		mutationTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mutationPanel.add(mutationTypeLabel);
		
		JComboBox mutationTypeTextField = new JComboBox();
		mutationTypeTextField.setModel(new DefaultComboBoxModel(new String[] {"Mutación básica"}));
		mutationPanel.add(mutationTypeTextField);
		
		JLabel mutationProbabilityLabel = new JLabel("% Mutación");
		mutationPanel.add(mutationProbabilityLabel);
		
		mutationProbabilityTextField = new JTextField();
		mutationProbabilityTextField.setText("60.0");
		mutationProbabilityTextField.setColumns(10);
		mutationPanel.add(mutationProbabilityTextField);
		
		JButton executeButton = new JButton("Ejecutar");
		
		JButton restartButton = new JButton("Reiniciar");
		
		JPanel problemPanel = new JPanel();
		problemPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Problema", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		problemPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblSeleccionaProblema = new JLabel("Selecciona problema");
		lblSeleccionaProblema.setHorizontalAlignment(SwingConstants.LEFT);
		problemPanel.add(lblSeleccionaProblema);
		
		JComboBox selectionTypeComboBox_1 = new JComboBox();
		selectionTypeComboBox_1.setModel(new DefaultComboBoxModel(new String[] {"P1 - Funcion 1", "P1 - Funcion 2", "P1 - Funcion 3", "P1 - Funcion 4", "P1 - Funcion 5"}));
		problemPanel.add(selectionTypeComboBox_1);
		GroupLayout gl_westPanel = new GroupLayout(westPanel);
		gl_westPanel.setHorizontalGroup(
			gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup()
					.addGap(10)
					.addGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_westPanel.createSequentialGroup()
							.addComponent(restartButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_westPanel.createSequentialGroup()
							.addGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(mutationPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(crossPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(selectionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(numGenTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(numGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(genSizeTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(genSizeLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(executeButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
								.addComponent(problemPanel, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
							.addGap(10))))
		);
		gl_westPanel.setVerticalGroup(
			gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup()
					.addGap(1)
					.addComponent(genSizeLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(genSizeTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(numGenLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(numGenTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(crossPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(mutationPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(problemPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(executeButton)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(restartButton)
					.addGap(244))
		);
		gl_westPanel.setAutoCreateGaps(true);
		gl_westPanel.setAutoCreateContainerGaps(true);
		westPanel.setLayout(gl_westPanel);
		
		JPanel eastPanel = new JPanel();
		eastPanel.setBorder(new TitledBorder(null, "Plot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(eastPanel, BorderLayout.CENTER);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		
		plot = new Plot2DPanel();
		eastPanel.add(plot);
		
		JPanel solutionPanel = new JPanel();
		eastPanel.add(solutionPanel);
		
		JLabel solutionLabel = new JLabel("Solution:");
		solutionLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		solutionLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		solutionTextField = new JTextField();
		solutionTextField.setText("Here will be solution");
		solutionTextField.setEditable(false);
		solutionTextField.setColumns(10);
		GroupLayout gl_solutionPanel = new GroupLayout(solutionPanel);
		gl_solutionPanel.setHorizontalGroup(
			gl_solutionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_solutionPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(solutionLabel)
					.addGap(18)
					.addComponent(solutionTextField, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_solutionPanel.setVerticalGroup(
			gl_solutionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_solutionPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_solutionPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_solutionPanel.createSequentialGroup()
							.addComponent(solutionTextField, GroupLayout.PREFERRED_SIZE, 14, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_solutionPanel.createSequentialGroup()
							.addComponent(solutionLabel, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
							.addGap(11))))
		);
		solutionPanel.setLayout(gl_solutionPanel);
		
		window = new JPanel();
		window.setLayout(new BorderLayout());
	}
}
