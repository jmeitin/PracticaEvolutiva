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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
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
import java.text.Format;

public class MainView extends JFrame {

	class SelectableType {
		private String name;
		private boolean enabled;

		public SelectableType(String name, boolean enabled) {
			this.name = name;
			this.enabled = enabled;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public boolean equals(Object other) {
			return this.name == other.toString();
		}
	}

	private JPanel window;
	private JTextField numGenTextField;
	private JTextField crossProbabilityTextField;
	private JTextField mutationProbabilityTextField;
	JComboBox crossTypeComboBox;
	private Plot2DPanel plot;
	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
	DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
	private JTextField solutionTextField;
	private final Color LIGHT_BLUE = new Color(34, 235, 249);
	private final Color LIGHT_GREEN = new Color(67, 249, 34);
	private final Color LIGHT_RED = new Color(249, 34, 34);
	private final Color DARK_BLUE = new Color(25, 111, 160);
	private final Color DARK_GREEN = new Color(39, 160, 25);
	private final Color DARK_RED = new Color(160, 36, 25);
	private boolean isDarkTheme = false;
	private final ViewController controller = new ViewController(this);
	private Thread controllerThread = new Thread(controller);

	// Plot
	private double[] average_fitnesses;
	private double[] best_absolute_fitnesses;
	private double[] best_fitnesses;
	private JFormattedTextField elitismProbabilityTextField;

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

	public void setSolutionText(String solutionText) {
		this.solutionTextField.setText(solutionText);
	}

	public void updateGraph(double[] average_fitnesses, double[] best_absolute_fitnesses, double[] best_fitnesses) {
		this.average_fitnesses = average_fitnesses;
		this.best_absolute_fitnesses = best_absolute_fitnesses;
		this.best_fitnesses = best_fitnesses;
		plotData();
	}

	public void cleanPlot() {
		plot.removeAllPlots();
		plot.removeLegend();
		plot.addLegend("SOUTH");
	}

	public void cleanPlotData() {
		average_fitnesses = null;
		best_fitnesses = null;
		best_absolute_fitnesses = null;
	}

	private void plotData() {
		if (average_fitnesses == null || best_fitnesses == null || best_absolute_fitnesses == null)
			return;

		cleanPlot();

		plot.addLinePlot("Mejor Absoluto", isDarkTheme ? LIGHT_RED : DARK_RED, best_absolute_fitnesses);
		plot.addLinePlot("Mejor Generación", isDarkTheme ? LIGHT_BLUE : DARK_BLUE, best_fitnesses);
		plot.addLinePlot("Media Generación", isDarkTheme ? LIGHT_GREEN : DARK_GREEN, average_fitnesses);
	}

	// Method to enable/disable options "Aritmético" y "BLX-α"
	private void enableCrossTypeOptions(boolean enabled) {
		ComboBoxModel<SelectableType> model = (ComboBoxModel<SelectableType>) crossTypeComboBox.getModel();
		SelectableType selectedItem = (SelectableType) crossTypeComboBox.getSelectedItem();

		for (int i = 0; i < model.getSize(); i++) {
			SelectableType crossType = model.getElementAt(i);
			if (crossType.equals("Aritmético") || crossType.equals("BLX-α")) {
				crossType.setEnabled(enabled);

				// Check if the selected element is now disabled. If so, search the next enabled
				// item and set it
				// This avoids the user enabling "Aritmetico", then going to other functions
				if (crossType.equals(selectedItem) && !enabled) {
					for (int j = 0; j < model.getSize(); j++) {
						SelectableType next = model.getElementAt(j);
						if (next.isEnabled()) {
							crossTypeComboBox.setSelectedItem(next);
							break;
						}
					}
				}
			}
		}
		
		// Just in case the selection changed, update the controller
		controller.setCrossType(((SelectableType) crossTypeComboBox.getSelectedItem()).toString().toUpperCase());
		crossTypeComboBox.repaint();
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
			String text = evt.getNewValue().toString();
			genSizeTextField.setText(text);
			controller.getAlgorithmData().poblation_size = Integer.parseInt(text);
		});

		JLabel numGenLabel = new JLabel("Número de generaciones");
		numGenLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		numGenTextField = new JFormattedTextField(numberFormat);
		numGenTextField.setText("100");
		numGenTextField.setColumns(10);
		numGenTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
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
		selectionTypeComboBox.setModel(new DefaultComboBoxModel(new String[] { "Ruleta", "T-Determinístico",
				"T-Probabilístico", "Estocástico", "Truncamiento", "Restos" }));
		selectionPanel.add(selectionTypeComboBox);

		JPanel crossPanel = new JPanel();
		crossPanel.setBorder(new TitledBorder("Cruce"));
		crossPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel crossTypeLabel = new JLabel("Tipo de cruce");
		crossTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		crossPanel.add(crossTypeLabel);

		crossTypeComboBox = new JComboBox();
		crossTypeComboBox.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent event) {
		        if (event.getStateChange() == ItemEvent.SELECTED) {
		            SelectableType selectedType = (SelectableType) crossTypeComboBox.getSelectedItem();
		            if (!selectedType.isEnabled()) {
		            	// If the option is disabled, select the first enabled option
		            	// Sadly we couldn't find other way to make the item not selectable
		                ComboBoxModel<SelectableType> model = crossTypeComboBox.getModel();
		                for (int i = 0; i < model.getSize(); i++) {
		                    SelectableType type = model.getElementAt(i);
		                    if (type.isEnabled()) {
		                        crossTypeComboBox.setSelectedItem(type);
		                        break;
		                    }
		                }
		            } else {
		                controller.setCrossType(selectedType.toString().toUpperCase());
		            }
		        }
		    }
		});
		crossTypeComboBox.setModel(new DefaultComboBoxModel<>(
				new SelectableType[] { new SelectableType("Monopunto", true), new SelectableType("Uniforme", true),
						new SelectableType("Aritmético", false), new SelectableType("BLX-α", false) }));
		crossTypeComboBox.setRenderer(new ListCellRenderer<SelectableType>() {
			private final DefaultListCellRenderer DEFAULT_RENDERER = new DefaultListCellRenderer();
			private final Color DISABLED_COLOR = Color.LIGHT_GRAY;

			@Override
			public Component getListCellRendererComponent(JList<? extends SelectableType> list, SelectableType value,
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel renderer = (JLabel) DEFAULT_RENDERER.getListCellRendererComponent(list, value, index, isSelected,
						cellHasFocus);
				if (!list.isEnabled() || !value.isEnabled())
					renderer.setForeground(DISABLED_COLOR);
				else
					renderer.setForeground(list.getForeground());

				return renderer;
			}
		});
		crossPanel.add(crossTypeComboBox);

		JLabel crossProbabilityLabel = new JLabel("% Cruce");
		crossPanel.add(crossProbabilityLabel);

		crossProbabilityTextField = new JFormattedTextField(decimalFormat);
		crossProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			controller.setMutationChance(Double.parseDouble(text));
		});
		crossProbabilityTextField.setText("60,0");
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
		mutationTypeTextField.setModel(new DefaultComboBoxModel(new String[] { "Básica" }));
		mutationPanel.add(mutationTypeTextField);

		JLabel mutationProbabilityLabel = new JLabel("% Mutación");
		mutationPanel.add(mutationProbabilityLabel);

		mutationProbabilityTextField = new JFormattedTextField(decimalFormat);
		mutationProbabilityTextField.setText("5,0");
		mutationProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
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
		problemSelectionComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selectedFunction = e.getItem().toString();
					// enabled or disable options "Aritmético" y "BLX-α"
					if (selectedFunction.equals("P1 - Funcion 4B")) {
						enableCrossTypeOptions(true);
					} else {
						enableCrossTypeOptions(false);
					}
				}

				controller.setFunction(e.getItem().toString().toUpperCase());
			}
		});
		problemSelectionComboBox.setModel(new DefaultComboBoxModel(new String[] { "P1 - Funcion 1", "P1 - Funcion 2",
				"P1 - Funcion 3", "P1 - Funcion 4A", "P1 - Funcion 4B", "P1 - Funcion 5" }));
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

		JPanel elitismPanel = new JPanel();
		elitismPanel.setBorder(new TitledBorder(null, "Elitismo", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		elitismPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel elitismProbabilityLabel = new JLabel("% Elitismo");
		elitismPanel.add(elitismProbabilityLabel);

		elitismProbabilityTextField = new JFormattedTextField(decimalFormat);
		elitismProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			controller.setElitism(Double.parseDouble(text));
		});
		elitismProbabilityTextField.setText("0,0");
		elitismProbabilityTextField.setColumns(10);
		elitismPanel.add(elitismProbabilityTextField);

		JLabel toleranceLabel = new JLabel("Tolerancia");
		toleranceLabel.setFont(new Font("Tahoma", Font.BOLD, 11));

		JFormattedTextField toleranceTextField = new JFormattedTextField(decimalFormat);
		toleranceTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			controller.setTolerance(Double.parseDouble(text));
		});
		toleranceTextField.setText("0,025");
		toleranceTextField.setColumns(10);
		GroupLayout gl_westPanel = new GroupLayout(westPanel);
		gl_westPanel.setHorizontalGroup(gl_westPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_westPanel.createSequentialGroup().addGap(10).addGroup(gl_westPanel
						.createParallelGroup(Alignment.TRAILING)
						.addComponent(restartButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(executeButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(themePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(toleranceTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 282,
								Short.MAX_VALUE)
						.addComponent(problemPanel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(elitismPanel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(mutationPanel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(crossPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(selectionPanel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(numGenTextField, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(numGenLabel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(genSizeTextField, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
						.addComponent(genSizeLabel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE).addComponent(
								toleranceLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))
						.addGap(10)));
		gl_westPanel.setVerticalGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup().addGap(1)
						.addComponent(genSizeLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(genSizeTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(numGenLabel)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(numGenTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(toleranceLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(toleranceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(selectionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(crossPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(mutationPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addGap(1)
						.addComponent(elitismPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(problemPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(themePanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(executeButton)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(restartButton).addGap(168)));
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
		plot.plotCanvas.setBackground(UIManager.getColor("Button.light"));
		plot.plotLegend.setBackground(UIManager.getColor("Button.light"));
		plot.plotToolBar.setBackground(UIManager.getColor("Button.light"));
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
