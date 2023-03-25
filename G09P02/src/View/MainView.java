package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.JTextComponent;

import org.math.plot.Plot2DPanel;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import View.Slider.RangeSlider;

public class MainView extends JFrame {
	private static final long serialVersionUID = 1L;

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
	private JTextArea solutionTextField;
	private final Color LIGHT_BLUE = new Color(34, 235, 249);
	private final Color LIGHT_GREEN = new Color(67, 249, 34);
	private final Color LIGHT_RED = new Color(249, 34, 34);
	private final Color DARK_BLUE = new Color(25, 111, 160);
	private final Color DARK_GREEN = new Color(39, 160, 25);
	private final Color DARK_RED = new Color(160, 36, 25);
	private boolean isDarkTheme = false;
	private final ViewController controller = new ViewController(this);

	// Plot
	private double[] average_fitnesses;
	private double[] best_absolute_fitnesses;
	private double[] best_fitnesses;
	private JFormattedTextField elitismProbabilityTextField;
	private JTextComponent genSizeTextField;
	private JFormattedTextField toleranceTextField;
	private JComboBox selectionTypeComboBox;
	private JComboBox mutationTypeComboBox;
	private JComboBox problemSelectionComboBox;
	private JFormattedTextField dimensionsTextField;
	private JLabel lblDimensions;

	private DefaultComboBoxModel p1Model;
	private DefaultComboBoxModel p2Model;
	private DefaultComboBoxModel p1Mutations;
	private DefaultComboBoxModel p2Mutations;
	private JProgressBar progressBar;
	private RangeSlider populationSlider;
	private RangeSlider mutationSlider;
	private RangeSlider crossSlider;

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

	/***
	 * Shows a dialog bog with an "ok" button
	 * 
	 * @param message
	 * @param ex
	 */
	@SuppressWarnings("unused")
	private void showInformationDialog(String message, Exception ex) {
		JOptionPane.showMessageDialog(SwingUtilities.windowForComponent(this), message + "\n\n" + ex.getMessage(),
				"FlatLaf", JOptionPane.INFORMATION_MESSAGE);
	}

	/***
	 * Sets the solution text of the view
	 * 
	 * @param solutionText The text you want to appear in the view
	 */
	public void setSolutionText(String solutionText) {
		this.solutionTextField.setText(solutionText);
	}

	/***
	 * Updates the view of the graph with new data
	 * 
	 * @param average_fitnesses       Average fitness of the poblation
	 * @param best_absolute_fitnesses Best absolute fitness of the poblation
	 * @param best_fitnesses          Best fitness of the poblation in each
	 *                                iteration
	 */
	public void updateGraph(double[] average_fitnesses, double[] best_absolute_fitnesses, double[] best_fitnesses) {
		this.average_fitnesses = average_fitnesses;
		this.best_absolute_fitnesses = best_absolute_fitnesses;
		this.best_fitnesses = best_fitnesses;
		plotData();
	}

	/***
	 * Cleans the graph view
	 */
	public void cleanPlot() {
		plot.removeAllPlots();
		plot.removeLegend();
		plot.addLegend("SOUTH");
	}

	/***
	 * Cleans the plot data
	 */
	public void cleanPlotData() {
		average_fitnesses = null;
		best_fitnesses = null;
		best_absolute_fitnesses = null;
	}

	/***
	 * Uses the current data to redraw the graph
	 */
	private void plotData() {
		if (average_fitnesses == null || best_fitnesses == null || best_absolute_fitnesses == null)
			return;

		cleanPlot();

		plot.addLinePlot("Mejor Absoluto", isDarkTheme ? LIGHT_RED : DARK_RED, best_absolute_fitnesses);
		plot.addLinePlot("Mejor Generación", isDarkTheme ? LIGHT_BLUE : DARK_BLUE, best_fitnesses);
		plot.addLinePlot("Media Generación", isDarkTheme ? LIGHT_GREEN : DARK_GREEN, average_fitnesses);
	}

	/***
	 * Method to enable/disable options "Aritmético" y "BLX-α"
	 * 
	 * @param enabled Determines wheter enable or disable options "Aritmético" y
	 *                "BLX-α"
	 */
	private void enableCrossTypeOptions(boolean enabled) {
		ComboBoxModel<SelectableType> model = (ComboBoxModel<SelectableType>) crossTypeComboBox.getModel();
		SelectableType selectedItem = (SelectableType) crossTypeComboBox.getSelectedItem();

		for (int i = 0; i < model.getSize(); i++) {
			SelectableType crossType = model.getElementAt(i);
			if (crossType.name.equals("Aritmético") || crossType.name.equals("BLX-α")) {
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

	public void setProgressBarPercentage(int percentage) {
		this.progressBar.setValue(percentage);
	}

	/**
	 * Sets the controller data to match the view data.
	 */
	private void initController() {
		controller.setPoblationSize(Integer.parseInt(genSizeTextField.getText()));
		controller.setGenSize(Integer.parseInt(numGenTextField.getText()));
		controller.setTolerance(Double.parseDouble(toleranceTextField.getText().replace(',', '.')));
		controller.setSelectionType((selectionTypeComboBox.getSelectedItem()).toString().toUpperCase());
		controller.setCrossType((crossTypeComboBox.getSelectedItem()).toString().toUpperCase());
		controller.setCrossChance(Double.parseDouble(crossProbabilityTextField.getText().replace(',', '.')));
		controller.setMutationType((mutationTypeComboBox.getSelectedItem()).toString().toUpperCase());
		controller.setMutationChance(Double.parseDouble(mutationProbabilityTextField.getText().replace(',', '.')));
		controller.setElitism(Double.parseDouble(elitismProbabilityTextField.getText().replace(',', '.')));
		controller.setFunction((problemSelectionComboBox.getSelectedItem()).toString().toUpperCase());
		controller.setDimensions(Integer.parseInt(dimensionsTextField.getText()));
		controller.setMutationRangeChance(mutationSlider.getValue(), mutationSlider.getUpperValue());
		controller.setCrossRangeChance(crossSlider.getValue(), crossSlider.getUpperValue());
		controller.setPoblationSizeRange(populationSlider.getValue(), populationSlider.getUpperValue());
	}

	private int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	private double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	private void setModelsForP1() {
		if (crossTypeComboBox != null)
			crossTypeComboBox.setModel(p1Model);

		if (mutationTypeComboBox != null) {
			mutationTypeComboBox.setModel(p1Mutations);
			controller.setMutationType(mutationTypeComboBox.getSelectedItem().toString().toUpperCase());
		}
	}

	private void setModelsForP2() {
		if (crossTypeComboBox != null)
			crossTypeComboBox.setModel(p2Model);

		if (mutationTypeComboBox != null) {
			mutationTypeComboBox.setModel(p2Mutations);
			controller.setMutationType(mutationTypeComboBox.getSelectedItem().toString().toUpperCase());
		}
	}

	private boolean slider_mode = false;

	private void toggleSliderMode() {
		slider_mode = !slider_mode;

		mutationSlider.setVisible(slider_mode);
		populationSlider.setVisible(slider_mode);
		crossSlider.setVisible(slider_mode);

		genSizeTextField.setVisible(!slider_mode);
		crossProbabilityTextField.setVisible(!slider_mode);
		mutationProbabilityTextField.setVisible(!slider_mode);
		
		controller.setSliderMode(slider_mode);
	}

	/**
	 * Create the frame. Most of the code was generated with windowbuildertool. Some
	 * snippets (cell renderer, jformattedclass) where added manually to fit our
	 * needs.
	 */
	public MainView() {
		FlatLightLaf.setup();

		this.setTitle("Practica 1 - PEV");
		this.setResizable(true);
		this.setMinimumSize(new Dimension(512, 256));
		this.setSize(new Dimension(1280, 720));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		JPanel westPanel = new JPanel();
		getContentPane().add(westPanel, BorderLayout.WEST);
		westPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));

		JLabel genSizeLabel = new JLabel("Tamaño de la población");
		genSizeLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		;
		genSizeTextField = new JFormattedTextField(numberFormat);
		genSizeTextField.setText("100");
		genSizeTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			final int result = clamp(Integer.parseInt(text), 10, 100000);
			genSizeTextField.setText(Integer.toString(result));
			controller.setPoblationSize(result);
		});

		JLabel numGenLabel = new JLabel("Número de generaciones");
		numGenLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		numGenTextField = new JFormattedTextField(numberFormat);
		numGenTextField.setText("100");
		numGenTextField.setColumns(10);
		numGenTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			final int result = clamp(Integer.parseInt(text), 10, 100000);
			numGenTextField.setText(Integer.toString(result));
			controller.setGenSize(result);
		});

		JPanel selectionPanel = new JPanel();
		selectionPanel.setBorder(
				new TitledBorder(null, "Selecci\u00F3n", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		selectionPanel.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel selectionTypeLabel = new JLabel("Tipo de selección");
		selectionTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
		selectionPanel.add(selectionTypeLabel);

		selectionTypeComboBox = new JComboBox();
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

		JLabel crossTypeLabel = new JLabel("Tipo de cruce");
		crossTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);

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
		p1Model = new DefaultComboBoxModel<>(new SelectableType[] { new SelectableType("Monopunto", true),
				new SelectableType("Multipunto", true), new SelectableType("Uniforme", true),
				new SelectableType("Aritmético", false), new SelectableType("BLX-α", false) });
		p2Model = new DefaultComboBoxModel<>(new SelectableType[] { new SelectableType("PMX", true),
				new SelectableType("OX", true), new SelectableType("OX-PP", true), new SelectableType("OX-PO", true),
				new SelectableType("CX", true), new SelectableType("ERX", true), new SelectableType("CO", true), new SelectableType("ORIGINAL", true) });
		setModelsForP1();
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

		JLabel crossProbabilityLabel = new JLabel("% Cruce");

		crossProbabilityTextField = new JFormattedTextField(decimalFormat);
		crossProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			final double result = clamp(Double.parseDouble(text), 0.0, 100.0);
			crossProbabilityTextField.setText(Double.toString(result).replace('.', ','));
			controller.setCrossChance(result);
		});
		crossProbabilityTextField.setText("60,0");
		crossProbabilityTextField.setColumns(10);

		JPanel mutationPanel = new JPanel();
		mutationPanel.setBorder(new TitledBorder("Mutaci\u00F3n"));

		JLabel mutationTypeLabel = new JLabel("Tipo de mutación");
		mutationTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);

		mutationTypeComboBox = new JComboBox();
		mutationTypeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.setMutationType(mutationTypeComboBox.getSelectedItem().toString().toUpperCase());
			}
		});

		this.p1Mutations = new DefaultComboBoxModel(new String[] { "Básica" });
		this.p2Mutations = new DefaultComboBoxModel(
				new String[] { "Intercambio", "Inversion", "Insercion", "Heuristica", "Original" });
		mutationTypeComboBox.setModel(p2Mutations);

		JLabel mutationProbabilityLabel = new JLabel("% Mutación");

		mutationProbabilityTextField = new JFormattedTextField(decimalFormat);
		mutationProbabilityTextField.setText("5,0");
		mutationProbabilityTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			final double result = clamp(Double.parseDouble(text), 0.0, 100.0);
			mutationProbabilityTextField.setText(Double.toString(result).replace('.', ','));
			controller.setMutationChance(result);
		});
		mutationProbabilityTextField.setColumns(10);

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
				controller.stop();
			}
		});

		JButton sliderButton = new JButton("Alternar Slider");
		sliderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				toggleSliderMode();
			}
		});

		JPanel problemPanel = new JPanel();
		problemPanel.setBorder(new TitledBorder("Problema"));

		JLabel lblSeleccionaProblema = new JLabel("Selecciona problema");
		lblSeleccionaProblema.setHorizontalAlignment(SwingConstants.LEFT);

		problemSelectionComboBox = new JComboBox();
		problemSelectionComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String selectedFunction = e.getItem().toString();
					if (e.getItem().equals("P2"))
						setModelsForP2();
					else
						setModelsForP1();
					// enabled or disable options "Aritmético" y "BLX-α"
					enableCrossTypeOptions(selectedFunction.equals("P1 - Funcion 4B"));
					boolean dimensionsVisible = selectedFunction.equals("P1 - Funcion 4B")
							|| selectedFunction.equals("P1 - Funcion 4A");
					if (lblDimensions != null) {

						lblDimensions.setVisible(dimensionsVisible);
						dimensionsTextField.setVisible(dimensionsVisible);
					}
				}

				controller.setFunction(e.getItem().toString().toUpperCase());
			}
		});
		problemSelectionComboBox.setModel(new DefaultComboBoxModel(new String[] { "P1 - Funcion 1", "P1 - Funcion 2",
				"P1 - Funcion 3", "P1 - Funcion 4A", "P1 - Funcion 4B", "P2" }));
		problemSelectionComboBox.setSelectedIndex(5);

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
			final double result = clamp(Double.parseDouble(text), 0.0, 100.0);
			elitismProbabilityTextField.setText(Double.toString(result).replace('.', ','));
			controller.setElitism(result);
		});
		elitismProbabilityTextField.setText("0,0");
		elitismProbabilityTextField.setColumns(10);
		elitismPanel.add(elitismProbabilityTextField);

		JLabel toleranceLabel = new JLabel("Tolerancia");
		toleranceLabel.setFont(new Font("Tahoma", Font.BOLD, 11));

		toleranceTextField = new JFormattedTextField(decimalFormat);
		toleranceTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			final double result = clamp(Double.parseDouble(text), 0.001, 1.0);
			toleranceTextField.setText(Double.toString(result).replace('.', ','));
			controller.setTolerance(result);
		});
		toleranceTextField.setText("0,025");
		toleranceTextField.setColumns(10);

		populationSlider = new RangeSlider();
		populationSlider.setMinimum(500);
		populationSlider.setMinorTickSpacing(500);
		populationSlider.setValue(2000);
		populationSlider.setToolTipText("");
		populationSlider.setSnapToTicks(true);
		populationSlider.setPaintLabels(true);
		populationSlider.setPaintTicks(true);
		populationSlider.setMajorTickSpacing(1000);
		populationSlider.setMaximum(5000);
		populationSlider.setUpperValue(1000);
		populationSlider.setVisible(false);
		populationSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				final int min = populationSlider.getValue();
				final int max = populationSlider.getUpperValue();
				controller.setPoblationSizeRange(min, max);
			}
		});

		GroupLayout gl_westPanel = new GroupLayout(westPanel);
		gl_westPanel.setHorizontalGroup(gl_westPanel.createParallelGroup(Alignment.LEADING).addGroup(Alignment.TRAILING,
				gl_westPanel.createSequentialGroup().addGroup(gl_westPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_westPanel.createSequentialGroup().addGap(10).addGroup(gl_westPanel
								.createParallelGroup(Alignment.LEADING)
								.addComponent(populationSlider, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(problemPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(sliderButton, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addComponent(restartButton, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addComponent(executeButton, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addComponent(themePanel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addComponent(toleranceTextField, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addComponent(elitismPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(mutationPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(crossPanel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
								.addComponent(selectionPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(numGenTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(numGenLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(genSizeTextField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(genSizeLabel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 282,
										Short.MAX_VALUE)
								.addComponent(toleranceLabel, GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE))))
						.addGap(10)));
		gl_westPanel.setVerticalGroup(gl_westPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_westPanel.createSequentialGroup().addGap(1)
						.addComponent(genSizeLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(genSizeTextField, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(populationSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(numGenLabel).addPreferredGap(ComponentPlacement.RELATED)
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
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(crossPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(mutationPanel, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE)
						.addGap(1)
						.addComponent(elitismPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(problemPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(themePanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(executeButton)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(restartButton)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(sliderButton).addGap(51)));

		mutationSlider = new RangeSlider();
		mutationSlider.setFont(new Font("Tahoma", Font.PLAIN, 8));
		mutationSlider.setValue(5);
		mutationSlider.setUpperValue(15);
		mutationSlider.setPaintLabels(true);
		mutationSlider.setMajorTickSpacing(10);
		mutationSlider.setMaximum(40);
		mutationSlider.setVisible(false);
		mutationSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				final double min = mutationSlider.getValue();
				final double max = mutationSlider.getUpperValue();
				controller.setMutationRangeChance(min, max);
			}
		});

		GroupLayout gl_mutationPanel = new GroupLayout(mutationPanel);
		gl_mutationPanel.setHorizontalGroup(gl_mutationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mutationPanel.createSequentialGroup()
						.addGroup(gl_mutationPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_mutationPanel.createSequentialGroup()
										.addComponent(mutationTypeLabel, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(mutationTypeComboBox, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_mutationPanel.createSequentialGroup()
										.addComponent(mutationProbabilityLabel, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(mutationProbabilityTextField, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(mutationSlider, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_mutationPanel.setVerticalGroup(gl_mutationPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mutationPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_mutationPanel.createSequentialGroup()
								.addGroup(gl_mutationPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(mutationTypeLabel, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(mutationTypeComboBox, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_mutationPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(mutationProbabilityLabel, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(mutationProbabilityTextField, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(mutationSlider, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)))));
		mutationPanel.setLayout(gl_mutationPanel);

		crossSlider = new RangeSlider();
		crossSlider.setFont(new Font("Tahoma", Font.PLAIN, 8));
		crossSlider.setValue(30);
		crossSlider.setUpperValue(60);
		crossSlider.setPaintLabels(true);
		crossSlider.setMajorTickSpacing(25);
		crossSlider.setVisible(false);
		crossSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				final double min = crossSlider.getValue();
				final double max = crossSlider.getUpperValue();
				controller.setCrossRangeChance(min, max);
			}
		});
		
		GroupLayout gl_crossPanel = new GroupLayout(crossPanel);
		gl_crossPanel.setHorizontalGroup(gl_crossPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_crossPanel
				.createSequentialGroup()
				.addGroup(gl_crossPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_crossPanel.createSequentialGroup()
								.addComponent(crossTypeLabel, GroupLayout.PREFERRED_SIZE, 135,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(crossTypeComboBox, GroupLayout.PREFERRED_SIZE, 135,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_crossPanel.createSequentialGroup()
								.addComponent(crossProbabilityLabel, GroupLayout.PREFERRED_SIZE, 135,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(crossProbabilityTextField, GroupLayout.PREFERRED_SIZE, 135,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(crossSlider, GroupLayout.PREFERRED_SIZE, 135,
										GroupLayout.PREFERRED_SIZE)))));
		gl_crossPanel.setVerticalGroup(gl_crossPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_crossPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_crossPanel.createSequentialGroup()
								.addGroup(gl_crossPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(crossTypeLabel, GroupLayout.PREFERRED_SIZE, 22,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(crossTypeComboBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_crossPanel.createParallelGroup(Alignment.LEADING)
										.addComponent(crossProbabilityLabel, GroupLayout.PREFERRED_SIZE, 22,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(crossProbabilityTextField, GroupLayout.PREFERRED_SIZE, 22,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(crossSlider, GroupLayout.PREFERRED_SIZE, 22,
												GroupLayout.PREFERRED_SIZE)))));
		crossPanel.setLayout(gl_crossPanel);

		lblDimensions = new JLabel("Dimensiones");
		lblDimensions.setHorizontalAlignment(SwingConstants.LEFT);
		lblDimensions.setVisible(false);

		dimensionsTextField = new JFormattedTextField(numberFormat);
		dimensionsTextField.setText("2");
		dimensionsTextField.setVisible(false);
		dimensionsTextField.addPropertyChangeListener("value", evt -> {
			String text = evt.getNewValue().toString();
			int dimensions = Math.max(Integer.parseInt(text), 1);
			dimensionsTextField.setText(Integer.toString(dimensions));
			controller.setDimensions(dimensions);
		});
		GroupLayout gl_problemPanel = new GroupLayout(problemPanel);
		gl_problemPanel.setHorizontalGroup(gl_problemPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_problemPanel.createSequentialGroup()
						.addGroup(gl_problemPanel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_problemPanel.createSequentialGroup()
										.addComponent(lblSeleccionaProblema, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(problemSelectionComboBox, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_problemPanel.createSequentialGroup()
										.addComponent(lblDimensions, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(dimensionsTextField, GroupLayout.PREFERRED_SIZE, 135,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(206, Short.MAX_VALUE)));
		gl_problemPanel.setVerticalGroup(gl_problemPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_problemPanel
				.createSequentialGroup()
				.addGroup(gl_problemPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSeleccionaProblema, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(problemSelectionComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGroup(gl_problemPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDimensions, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
						.addComponent(dimensionsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))));
		problemPanel.setLayout(gl_problemPanel);
		gl_westPanel.setAutoCreateGaps(true);
		gl_westPanel.setAutoCreateContainerGaps(true);
		westPanel.setLayout(gl_westPanel);

		JPanel eastPanel = new JPanel();
		eastPanel.setBorder(new TitledBorder(null, "Plot", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(eastPanel, BorderLayout.CENTER);
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		eastPanel.add(progressBar);

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

		solutionTextField = new JTextArea();
		solutionTextField.setText("Here will be solution");
		solutionTextField.setEditable(false);
		solutionTextField.setColumns(10);
		solutionTextField.setLineWrap(true);
		solutionTextField.setWrapStyleWord(true);
		Border border = BorderFactory.createLineBorder(Color.black);
		Border margin = BorderFactory.createEmptyBorder(5, 8, 5, 8);
		Border compound = BorderFactory.createCompoundBorder(border, margin);
		solutionTextField.setBorder(compound);
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
		initController();
	}
}
