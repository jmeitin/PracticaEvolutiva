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
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.Label;
import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.Axis;
import org.math.plot.plots.LinePlot;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.util.LoggingFacade;

import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class MainView extends JFrame {

	private JPanel window;
	private JTextField numGenTextField;
	private JTextField crossProbabilityTextField;
	private JTextField mutationProbabilityTextField;
	private Plot2DPanel plot;
	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
	private JTextField solutionTextField;
	private final Color lightBlue = new Color(34, 235, 249);
	private final Color lightGreen = new Color(67, 249, 34);
	private final Color lightRed = new Color(249, 34, 34);
	private final Color darkBlue = new Color(25, 111, 160);
	private final Color darkGreen = new Color(39, 160, 25);
	private final Color darkRed = new Color(160, 36, 25);
	private boolean isDarkTheme = false;
	private final ViewController controller = new ViewController(this);
	private Thread controllerThread = new Thread(controller);

	// Plot
	private double[] average_fitnesses;
	private double[] best_absolute_fitnesses;
	private double[] best_fitnesses;

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

	private void showInformationDialog(String message, Exception ex) {
		JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this), message + "\n\n" + ex.getMessage(),
				"FlatLaf", JOptionPane.INFORMATION_MESSAGE);
	}

	public void setSolutionText(String solutionText)
	{
		this.solutionTextField.setText(solutionText);
	}
	
	public void updateGraph(double[] average_fitnesses, double[] best_absolute_fitnesses, double[] best_fitnesses) {
		this.average_fitnesses = average_fitnesses;
		this.best_absolute_fitnesses = best_absolute_fitnesses;
		this.best_fitnesses = best_fitnesses;
		plotData();
	}

	public void cleanPlot()
	{
		plot.removeAllPlots();
	}
	
	public void cleanPlotData()
	{
		average_fitnesses = null;
		best_fitnesses = null;
		best_absolute_fitnesses = null;
	}
	
	private void plotData() {
		if (average_fitnesses == null || best_fitnesses == null || best_absolute_fitnesses == null)
			return;

		cleanPlot();
		plot.addLinePlot("Mejor Absoluto", isDarkTheme ? lightRed : darkRed, best_absolute_fitnesses);
		plot.addLinePlot("Mejor Generación", isDarkTheme ? lightBlue : darkBlue, best_fitnesses);
		plot.addLinePlot("Media Generación", isDarkTheme ? lightGreen : darkGreen, average_fitnesses);
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		FlatLightLaf.setup();

		this.setTitle("Practica 1 - PEV");
		this.setResizable(false);
		this.setMinimumSize(new Dimension(1280, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		JPanel westPanel = new JPanel();
		getContentPane().add(westPanel, BorderLayout.WEST);
		westPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));

		JLabel genSizeLabel = new JLabel("Tamaño de la población");
		genSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		;
		JTextField genSizeTextField = new JFormattedTextField(numberFormat);
		genSizeTextField.setText("100");
		genSizeTextField.addPropertyChangeListener("value", evt -> {
			String text =  evt.getNewValue().toString();
			genSizeTextField.setText(text);
			controller.getAlgorithmData().poblation_size = Integer.parseInt(text);
		});

		JLabel numGenLabel = new JLabel("Número de generaciones");
		numGenLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		numGenTextField = new JFormattedTextField(numberFormat);
		numGenTextField.setText("100");
		numGenTextField.setColumns(10);
		numGenTextField.addPropertyChangeListener("value", evt -> {
			String text =  evt.getNewValue().toString();
			numGenTextField.setText(text);
			controller.setGenSize(Integer.parseInt(text));
		});

		JPanel selectionPanel = new JPanel();
		selectionPanel.setBorder(
				new TitledBorder(null, "Selecci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		selectionPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel selectionTypeLabel = new JLabel("Tipo de selección");
		selectionTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		selectionPanel.add(selectionTypeLabel);

		JComboBox selectionTypeComboBox = new JComboBox();
		selectionTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setSelectionType(selectionTypeComboBox.getSelectedItem().toString().toUpperCase());
			}
		});
		selectionTypeComboBox.setModel(new DefaultComboBoxModel(
				new String[] { "Ruleta", "Estocástico", "T-Determinístico", "T-Probabilístico" }));
		selectionPanel.add(selectionTypeComboBox);

		JPanel crossPanel = new JPanel();
		crossPanel.setBorder(new TitledBorder("Cruce"));
		crossPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel crossTypeLabel = new JLabel("Tipo de cruce");
		crossTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		crossPanel.add(crossTypeLabel);

		JComboBox crossTypeComboBox = new JComboBox();
		crossTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setCrossType(crossTypeComboBox.getSelectedItem().toString().toUpperCase());
			}
		});
		crossTypeComboBox.setModel(new DefaultComboBoxModel(new String[] { "Cruce Monopunto" }));
		crossPanel.add(crossTypeComboBox);

		JLabel crossProbabilityLabel = new JLabel("% Cruce");
		crossPanel.add(crossProbabilityLabel);

		crossProbabilityTextField =new JFormattedTextField(numberFormat);
		crossProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text =  evt.getNewValue().toString();
			crossProbabilityTextField.setText(text);
			controller.setMutationChance(Double.parseDouble(text));
		});
		crossProbabilityTextField.setText("60.0");
		crossPanel.add(crossProbabilityTextField);
		crossProbabilityTextField.setColumns(10);

		JPanel mutationPanel = new JPanel();
		mutationPanel.setBorder(new TitledBorder("Mutaci\u00F3n"));
		mutationPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel mutationTypeLabel = new JLabel("Tipo de mutación");
		mutationTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mutationPanel.add(mutationTypeLabel);

		JComboBox mutationTypeTextField = new JComboBox();
		mutationTypeTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setMutationType(mutationTypeTextField.getSelectedItem().toString().toUpperCase());
			}
		});
		mutationTypeTextField.setModel(new DefaultComboBoxModel(new String[] { "Mutación básica" }));
		mutationPanel.add(mutationTypeTextField);

		JLabel mutationProbabilityLabel = new JLabel("% Mutación");
		mutationPanel.add(mutationProbabilityLabel);

		mutationProbabilityTextField = new JFormattedTextField(numberFormat);
		mutationProbabilityTextField.setText("60.0");
		mutationProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text =  evt.getNewValue().toString();
			mutationProbabilityTextField.setText(text);
			controller.setMutationChance(Double.parseDouble(text));
		});
		mutationProbabilityTextField.setColumns(10);
		mutationPanel.add(mutationProbabilityTextField);

		JButton executeButton = new JButton("Ejecutar");
		executeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanPlot();
				controller.run();
			}
		});

		JButton restartButton = new JButton("Reiniciar");
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanPlot();
			}
		});

		JPanel problemPanel = new JPanel();
		problemPanel.setBorder(new TitledBorder("Problema"));
		problemPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lblSeleccionaProblema = new JLabel("Selecciona problema");
		lblSeleccionaProblema.setHorizontalAlignment(SwingConstants.LEFT);
		problemPanel.add(lblSeleccionaProblema);

		JComboBox problemSelectionComboBox = new JComboBox();
		problemSelectionComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setFunction(problemSelectionComboBox.getSelectedItem().toString().toUpperCase());
			}
		});
		problemSelectionComboBox.setModel(new DefaultComboBoxModel(new String[] { "P1 - Funcion 1", "P1 - Funcion 2",
				"P1 - Funcion 3", "P1 - Funcion 4", "P1 - Funcion 5" }));
		problemPanel.add(problemSelectionComboBox);

		JPanel themePanel = new JPanel();
		themePanel.setBorder(new TitledBorder("Tema"));
		themePanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel themeLabel = new JLabel("Selecciona Tema");
		themeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		themePanel.add(themeLabel);

		JComboBox themeComboBox = new JComboBox();
		themeComboBox.setModel(new DefaultComboBoxModel(new String[] { "Claro", "Oscuro" }));
		themeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String theme = (String) e.getItem();
					if (theme.equals("Claro")) {
						FlatLightLaf.setup();
						isDarkTheme = false;
						plotData();
					} else {
						FlatDarkLaf.setup();
						isDarkTheme = true;
						plotData();
					}

					FlatLaf.updateUI();
					window.repaint();
				}
			}
		});
		;
		themePanel.add(themeComboBox);
		GroupLayout gl_westPanel = new GroupLayout(westPanel);
		gl_westPanel.setHorizontalGroup(gl_westPanel.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
				gl_westPanel.createSequentialGroup().addGap(10).addGroup(gl_westPanel
						.createParallelGroup(Alignment.LEADING)
						.addComponent(themePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(restartButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(mutationPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(crossPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(selectionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
								Short.MAX_VALUE)
						.addComponent(numGenTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
								Short.MAX_VALUE)
						.addComponent(numGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(genSizeTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234,
								Short.MAX_VALUE)
						.addComponent(genSizeLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(executeButton, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
						.addComponent(problemPanel, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)).addGap(10)));
		gl_westPanel.setVerticalGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup().addGap(1)
						.addComponent(genSizeLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(genSizeTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(numGenLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(numGenTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(crossPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(mutationPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(problemPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(themePanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(executeButton)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(restartButton).addGap(244)));
		gl_westPanel.setAutoCreateGaps(true);
		gl_westPanel.setAutoCreateContainerGaps(true);
		westPanel.setLayout(gl_westPanel);

		JPanel eastPanel = new JPanel();
		eastPanel.setBorder(new TitledBorder(null, "Plot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(eastPanel, BorderLayout.CENTER);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

		plot = new Plot2DPanel();
		plot.plotCanvas.setAutoBounds(1);
		plot.plotCanvas.setAxisLabels(new String[] { "X", "Y" });
		//plot.addLegend("South");
		//plot.plotCanvas.setAxisLabels(new String[] { "Número de generaciones", "Valor de la función" });
		plot.plotCanvas.setBackground(UIManager.getColor("Button.light"));
		eastPanel.add(plot);
		plotData();

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
		gl_solutionPanel.setHorizontalGroup(gl_solutionPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_solutionPanel.createSequentialGroup().addContainerGap().addComponent(solutionLabel)
						.addGap(18).addComponent(solutionTextField, GroupLayout.DEFAULT_SIZE, 899, Short.MAX_VALUE)
						.addContainerGap()));
		gl_solutionPanel
				.setVerticalGroup(gl_solutionPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
						gl_solutionPanel.createSequentialGroup().addContainerGap()
								.addGroup(gl_solutionPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(solutionTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(solutionLabel, GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE))
								.addGap(11)));
		solutionPanel.setLayout(gl_solutionPanel);

		window = new JPanel();
		window.setLayout(new BorderLayout());

		this.setVisible(true);
	}
}
