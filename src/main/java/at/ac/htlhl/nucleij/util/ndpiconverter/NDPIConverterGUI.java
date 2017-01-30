/* 
NDPI to OME-TIFF Converter
Tool for converting a selected image of a NDPI file to an OME-TIFF.

Copyright (C) 2012  Matthias Baldauf & Andreas Dander

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.ac.htlhl.nucleij.util.ndpiconverter;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.ColorUIResource;

import loci.formats.tiff.TiffCompression;

import org.libjpegturbo.turbojpeg.TJDecompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ac.htlhl.nucleij.util.ndpiconverter.utilities.CustomBackgroundPanel;
import at.ac.htlhl.nucleij.util.ndpiconverter.utilities.CustomRoundedBorder;
import at.ac.htlhl.nucleij.util.ndpiconverter.utilities.CustomToolBarButton;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.MultiStepRescaleOp;

/**
 * Provides the graphical user interface for the NDPI to OME-TIFF Converter.
 * 
 * @author Matthias Baldauf
 * @see NDPIConverterCLI
 * 
 */
public class NDPIConverterGUI extends JFrame implements ActionListener, PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private JPanel sidemenu;
	private JPanel cards;
	private JPanel welcomeCard;
	private JPanel settingsCard;
	private JPanel convertCard;
	private JPanel helpCard;
	private CustomBackgroundPanel imageThumbPanel;
	private CustomBackgroundPanel selectedImgThumbPanel;
	private CardLayout cardLayout;
	private JFileChooser ndpiFileChooser;
	private JFileChooser outputDirectoryChooser;
	private JLabel filenameLabel;
	private JLabel filesizeLabel;
	private JLabel numImagesLabel;
	private JLabel makeLabel;
	private JLabel modelLabel;
	private JLabel softwareLabel;
	private JLabel datetimeLabel;
	private JLabel outputDirLabel;
	private JLabel progressMessageLabel;
	private JLabel exportTitleLabel;
	private JLabel exportOutputLabel;
	private JLabel ometiffTitleLabel;
	private JLabel ometiffOutputLabel;
	private JPanel imagesPanel;
	private JButton startConversionButton;
	private JButton startExportButton;
	private JRadioButton storageButtonStrips;
	private JRadioButton outputDirButtonChoose;
	private JRadioButton bigTiffButton;
	private JProgressBar progressBar;
	private JDialog readingDialog;
	private JDialog convertingDialog;
	private JDialog exportingDialog;
	private JDialog aboutDialog;
	private File ndpiFile;
	private ImageConverter converterWorker;
	private ArrayList<ImageInfo> imgInfos;
	private BufferedImage imgThumbnail;
	private BufferedImage macroThumbnail;
	private BufferedImage mapThumbnail;
	private int selectedImageIndex;
	private static final int FRAME_WIDTH = 1250;
	private static final int FRAME_HEIGHT = 740;
	/**
	 * Maximum number of bytes for standard TIFF format (4 GB minus 128 KB for
	 * TIFF IFD).
	 */
	private static final BigInteger STANDARD_TIFF_MAX_BYTES = new BigInteger(Integer.toString(Integer.MAX_VALUE - 128 * 1024));
	private static final Logger LOGGER = LoggerFactory.getLogger(NDPIConverterGUI.class);
	private ConversionSettings settings = ConversionSettings.getInstance();

	/**
	 * Creates a blank NDPI to OME-TIFF Converter GUI with the specified title.
	 * 
	 * @param title
	 *            the title for the GUI
	 */
	public NDPIConverterGUI(String title) {
		super(title);
	}

	/**
	 * Sets up and displays the GUI. Furthermore, it will be checked if
	 * libjpeg-turbo (speeds up JPEG-decompression) is installed.
	 */
	private static void createAndShowGUI() {
		LOGGER.info(Constants.TOOL_TITLE + " " + Constants.TOOL_VERSION);

		NDPIConverterGUI mainFrame = new NDPIConverterGUI(Constants.TOOL_TITLE);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setIconImage((new ImageIcon(mainFrame.getClass().getResource(Constants.WINDOW_IMAGE))).getImage());
		mainFrame.setMinimumSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));

		Container content = mainFrame.getContentPane();
		content.setBackground(Color.WHITE);

		// Add components
		mainFrame.addComponentsToPane(mainFrame.getContentPane());
		mainFrame.pack();

		// Center window
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - mainFrame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - mainFrame.getHeight()) / 2);
		mainFrame.setLocation(x, y);

		mainFrame.setVisible(true);

		// Check if libjpeg-turbo is installed
		// If libjpeg-turbo is not installed, Java built-in JPEG-decoder for
		// decompression of JPEG-images will be used
		try {
			new TJDecompressor();
			ConversionSettings.getInstance().setLibjpegTurboAvailable(true);
		} catch (UnsatisfiedLinkError e) {
			mainFrame
					.displayInfoMessage(
							"libjpeg-turbo not found",
							"<b>libjpeg-turbo was not found on your system</b><br>Therefore, Java built-in JPEG-decoder will be used<br><br><p style='text-align:justify'>This converter is capable of using libjpeg-turbo for accelerated JPEG-decompression. It is highly recommended to install libjpeg-turbo as it enables up to ten times faster JPEG-decompression as Java built-in decompression.</p>");
			ConversionSettings.getInstance().setLibjpegTurboAvailable(false);
		} catch (Exception e) {
			mainFrame.displayErrorMessage("libjpeg-turbo error", "<b>libjpeg-turbo could not be started</b><br>Therefore, Java built-in JPEG-decoder will be used<br><br>Error message: " + e.toString());
			ConversionSettings.getInstance().setLibjpegTurboAvailable(false);
		}
	}

	/**
	 * Adds the components to the specified container.
	 * 
	 * @param pane
	 *            the container to which the components will be added
	 */
	private void addComponentsToPane(final Container pane) {
		createSideMenu();

		// Create the panel that contains the cards
		cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		createWelcomeCard();
		cards.add(welcomeCard, "Welcome Card");
		createConvertCard();
		cards.add(convertCard, "Convert Card");
		createSettingsCard();
		cards.add(settingsCard, "Settings Card");
		createHelpCard();
		cards.add(helpCard, "Help Card");

		// Create all static dialogs
		createAboutDialog();
		createReadingNDPIDialog();
		createExportingImageDialog();

		pane.add(sidemenu, BorderLayout.WEST);
		pane.add(cards, BorderLayout.CENTER);

		// Set glass pane (darken background during analysis, export and conversion of images)
		this.getRootPane().setGlassPane(new JComponent() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				g.setColor(new Color(0, 0, 0, 100));
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		});
	}

	/**
	 * Creates the side menu which contains the logo, the menu buttons, the
	 * version number and a button for accessing further information about the
	 * NDPI to OME-TIFF Converter.
	 */
	private void createSideMenu() {
		// Create sidemenu panel which serves as a container for all components of the sidemenu
		sidemenu = new JPanel(new GridBagLayout());
		sidemenu.setBackground(Constants.LIGHT_GREY_COLOR);
		GridBagConstraints gbcSideMenu = new GridBagConstraints();
		gbcSideMenu.anchor = GridBagConstraints.NORTH;
		gbcSideMenu.fill = GridBagConstraints.HORIZONTAL;

		// Add the logo
		JLabel logoLabel = new JLabel(new ImageIcon(getClass().getResource(Constants.LOGO_IMAGE)));
		gbcSideMenu.insets = new Insets(0, 0, 5, 0);
		sidemenu.add(logoLabel, gbcSideMenu);

		// Create the toolbar which contains the buttons to switch
		// between the "convert", "settings" or "help" card
		JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
		toolbar.setFloatable(false);
		toolbar.setAlignmentY(0);
		toolbar.setBackground(Constants.LIGHT_GREY_COLOR);
		toolbar.setBorderPainted(false);
		toolbar.setLayout(new GridBagLayout());
		GridBagConstraints gbcToolbar = new GridBagConstraints();
		gbcToolbar.insets = new Insets(0, 0, 5, 0);
		CustomToolBarButton convertButton = new CustomToolBarButton(new ImageIcon(getClass().getResource(Constants.TOOLBAR_IMAGE_CONVERT)), new ImageIcon(getClass().getResource(Constants.TOOLBAR_IMAGE_CONVERT_HOVER)));
		convertButton.setActionCommand("convert");
		convertButton.addActionListener(this);
		toolbar.add(convertButton, gbcToolbar);
		CustomToolBarButton settingsButton = new CustomToolBarButton(new ImageIcon(getClass().getResource(Constants.TOOLBAR_IMAGE_SETTINGS)), new ImageIcon(getClass().getResource(Constants.TOOLBAR_IMAGE_SETTINGS_HOVER)));
		settingsButton.setActionCommand("settings");
		settingsButton.addActionListener(this);
		gbcToolbar.gridy = 1;
		toolbar.add(settingsButton, gbcToolbar);
		CustomToolBarButton helpButton = new CustomToolBarButton(new ImageIcon(getClass().getResource(Constants.TOOLBAR_IMAGE_HELP)), new ImageIcon(getClass().getResource(Constants.TOOLBAR_IMAGE_HELP_HOVER)));
		helpButton.setActionCommand("help");
		helpButton.addActionListener(this);
		gbcToolbar.gridy = 2;
		toolbar.add(helpButton, gbcToolbar);
		gbcSideMenu.gridy = 1;
		gbcSideMenu.weighty = 1;
		gbcSideMenu.insets = new Insets(0, 0, 0, 0);
		sidemenu.add(toolbar, gbcSideMenu);

		// Create a tool-info panel which shows the version of the tool
		// and a "About this tool" button to get further information about the tool
		JPanel toolInfoPanel = new CustomBackgroundPanel(Constants.FOOTER_IMAGE, false);
		toolInfoPanel.setBackground(Constants.LIGHT_GREY_COLOR);
		toolInfoPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcInfoPanel = new GridBagConstraints();
		gbcInfoPanel.anchor = GridBagConstraints.CENTER;
		gbcInfoPanel.insets = new Insets(3, 0, 0, 0);
		JLabel toolVersionLabel = new JLabel(Constants.TOOL_VERSION);
		toolVersionLabel.setFont(toolVersionLabel.getFont().deriveFont((float) 11));
		toolInfoPanel.add(toolVersionLabel, gbcInfoPanel);
		JButton aboutButton = new JButton("About this tool");
		aboutButton.setBackground(Constants.DARK_GREEN_COLOR);
		aboutButton.setPreferredSize(new Dimension(150, 25));
		aboutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Display "About this tool" dialog
				aboutDialog.setVisible(true);
			}
		});
		gbcInfoPanel.gridy = 1;
		toolInfoPanel.add(aboutButton, gbcInfoPanel);
		gbcSideMenu.gridy = 2;
		gbcSideMenu.weighty = 0;
		sidemenu.add(toolInfoPanel, gbcSideMenu);
	}

	/**
	 * Creates the welcome card which contains the welcome text.
	 */
	private void createWelcomeCard() {
		// Create welcome card which serves as a container for all components of this card	
		welcomeCard = new JPanel(new GridBagLayout());
		welcomeCard.setBackground(Color.WHITE);
		welcomeCard.setBorder(new CustomRoundedBorder(25, Constants.LIGHT_GREY_COLOR));
		GridBagConstraints gbcWelcomeCard = new GridBagConstraints();
		gbcWelcomeCard.insets = new Insets(0, 50, 0, 50);
		gbcWelcomeCard.weightx = 1;
		gbcWelcomeCard.fill = GridBagConstraints.HORIZONTAL;

		// Create welcome panel which shows the welcome text
		JPanel welcomePanel = new CustomBackgroundPanel(Constants.WELCOME_BACKGROUND_IMAGE, true);
		welcomePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcWelcomePanel = new GridBagConstraints();
		gbcWelcomePanel.insets = new Insets(15, 25, 5, 25);
		gbcWelcomePanel.anchor = GridBagConstraints.NORTH;
		welcomePanel.setPreferredSize(new Dimension(500, 170));
		welcomePanel.setBorder(new CustomRoundedBorder(25, Color.WHITE));
		JLabel welcomeLabel = new JLabel("Welcome");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont((float) 22));
		welcomePanel.add(welcomeLabel, gbcWelcomePanel);
		JLabel descriptionLabel = new JLabel(
				"<html><p style='text-align:justify'>With this tool you can open Hamamatsu NDPI-files, view the contents of the file, convert a selected image to a standard-compliant OME-TIFF and export images. Various output settings allows you to customize the output file.</p></html>");
		descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		descriptionLabel.setFont(descriptionLabel.getFont().deriveFont((float) 13));
		gbcWelcomePanel.gridy = 1;
		gbcWelcomePanel.weighty = 1;
		gbcWelcomePanel.weightx = 1;
		gbcWelcomePanel.fill = GridBagConstraints.HORIZONTAL;
		welcomePanel.add(descriptionLabel, gbcWelcomePanel);
		JLabel startLabel = new JLabel("Click on 'Convert' to start");
		startLabel.setHorizontalAlignment(SwingConstants.CENTER);
		startLabel.setFont(startLabel.getFont().deriveFont((float) 13));
		startLabel.setFont(startLabel.getFont().deriveFont(Font.BOLD));
		gbcWelcomePanel.insets = new Insets(0, 25, 15, 25);
		gbcWelcomePanel.gridy = 2;
		gbcWelcomePanel.weighty = 0;
		gbcWelcomePanel.weightx = 0;
		welcomePanel.add(startLabel, gbcWelcomePanel);
		welcomeCard.add(welcomePanel, gbcWelcomeCard);
	}

	/**
	 * Creates the convert card which contains all elements to
	 * <ul>
	 * <li>open a NDPI file,</li>
	 * <li>view the contents of the file,</li>
	 * <li>select an image,</li>
	 * <li>view information about the selected image and to</li>
	 * <li>start conversion or export of the selected image.</li>
	 * </ul>
	 */
	private void createConvertCard() {
		// Create convert card which serves as a container for all components of this card
		convertCard = new JPanel(new GridBagLayout());
		convertCard.setBackground(Color.WHITE);
		convertCard.setBorder(new CustomRoundedBorder(25, Constants.LIGHT_GREY_COLOR));
		GridBagConstraints gbcConvertCard = new GridBagConstraints();
		gbcConvertCard.insets = new Insets(25, 25, 0, 25);
		gbcConvertCard.anchor = GridBagConstraints.NORTHWEST;
		gbcConvertCard.fill = GridBagConstraints.HORIZONTAL;
		gbcConvertCard.weightx = 1;
		gbcConvertCard.gridwidth = 2;

		// Create title panel
		JPanel titlePanel = new CustomBackgroundPanel(Constants.TITLE_BACKGROUND_IMAGE, true);
		titlePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(5, 25, 5, 25);
		gbcTitle.anchor = GridBagConstraints.NORTH;
		titlePanel.setMaximumSize(new Dimension(titlePanel.getWidth(), 40));
		titlePanel.setBorder(new CustomRoundedBorder(22, Color.WHITE));
		JLabel welcomeLabel = new JLabel("Convert");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setFont(welcomeLabel.getFont().deriveFont((float) 22));
		titlePanel.add(welcomeLabel, gbcTitle);
		convertCard.add(titlePanel, gbcConvertCard);

		// Create file panel which serves as a container for a button to open NDPI file,
		// general information about the NDPI file and an image thumbnail of the NDPI file
		JPanel selectFilePanel = new JPanel(new GridBagLayout());
		selectFilePanel.setBackground(Color.WHITE);
		GridBagConstraints gbcSelectFile = new GridBagConstraints();
		gbcSelectFile.anchor = GridBagConstraints.NORTHWEST;
		gbcSelectFile.fill = GridBagConstraints.BOTH;
		gbcSelectFile.insets = new Insets(5, 5, 5, 1);
		JButton openFileButton = new JButton("Open NDPI file");
		openFileButton.setToolTipText("Click to open NDPI file");
		openFileButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		openFileButton.setHorizontalTextPosition(SwingConstants.CENTER);
		openFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open NDPI file chooser
				openNDPIFile();
			}
		});
		openFileButton.setPreferredSize(new Dimension(140, openFileButton.getHeight()));
		openFileButton.setIcon(new ImageIcon(getClass().getResource(Constants.FILE_ICON)));
		openFileButton.addActionListener(this);
		selectFilePanel.add(openFileButton, gbcSelectFile);

		// Create file-info panel which shows general information about the selected NDPI file
		JPanel fileInfoPanel = new JPanel(new GridBagLayout());
		fileInfoPanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
		fileInfoPanel.setBackground(Constants.LIGHT_GREY_COLOR);
		GridBagConstraints gbcFileInfo = new GridBagConstraints();
		gbcFileInfo.insets = new Insets(5, 10, 2, 10);
		gbcFileInfo.anchor = GridBagConstraints.NORTHWEST;
		gbcFileInfo.fill = GridBagConstraints.HORIZONTAL;
		gbcFileInfo.weightx = 1;
		gbcFileInfo.gridwidth = 4;
		filenameLabel = new JLabel("no file selected");
		filenameLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Constants.DARK_GREY_COLOR), BorderFactory.createEmptyBorder(0, 0, 2, 0)));
		filenameLabel.setOpaque(true);
		filenameLabel.setFont(filenameLabel.getFont().deriveFont((float) 13));
		filenameLabel.setFont(filenameLabel.getFont().deriveFont(Font.BOLD));
		fileInfoPanel.add(filenameLabel, gbcFileInfo);
		JLabel filesizeTitleLabel = new JLabel("File size:");
		filesizeTitleLabel.setFont(filesizeTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcFileInfo.insets = new Insets(2, 10, 2, 0);
		gbcFileInfo.fill = GridBagConstraints.NONE;
		gbcFileInfo.gridwidth = 1;
		gbcFileInfo.weightx = 0;
		gbcFileInfo.gridy = 1;
		gbcFileInfo.gridx = 0;
		fileInfoPanel.add(filesizeTitleLabel, gbcFileInfo);
		filesizeLabel = new JLabel("-");
		gbcFileInfo.weightx = 0.4;
		gbcFileInfo.gridx = 1;
		fileInfoPanel.add(filesizeLabel, gbcFileInfo);
		JLabel makeTitleLabel = new JLabel("Make:");
		makeTitleLabel.setFont(makeTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcFileInfo.weightx = 0;
		gbcFileInfo.gridx = 2;
		fileInfoPanel.add(makeTitleLabel, gbcFileInfo);
		makeLabel = new JLabel("-");
		gbcFileInfo.weightx = 0.6;
		gbcFileInfo.gridx = 3;
		fileInfoPanel.add(makeLabel, gbcFileInfo);
		JLabel numImagesTitleLabel = new JLabel("Images:");
		numImagesTitleLabel.setFont(numImagesTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcFileInfo.weightx = 0;
		gbcFileInfo.gridy = 2;
		gbcFileInfo.gridx = 0;
		fileInfoPanel.add(numImagesTitleLabel, gbcFileInfo);
		numImagesLabel = new JLabel("-");
		gbcFileInfo.gridx = 1;
		fileInfoPanel.add(numImagesLabel, gbcFileInfo);
		JLabel modelTitleLabel = new JLabel("Model:");
		modelTitleLabel.setFont(modelTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcFileInfo.gridx = 2;
		fileInfoPanel.add(modelTitleLabel, gbcFileInfo);
		modelLabel = new JLabel("-");
		gbcFileInfo.gridx = 3;
		fileInfoPanel.add(modelLabel, gbcFileInfo);
		JLabel softwareTitleLabel = new JLabel("Software:");
		softwareTitleLabel.setFont(softwareTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcFileInfo.insets = new Insets(2, 10, 5, 0);
		gbcFileInfo.gridy = 3;
		gbcFileInfo.gridx = 0;
		fileInfoPanel.add(softwareTitleLabel, gbcFileInfo);
		softwareLabel = new JLabel("-");
		gbcFileInfo.gridx = 1;
		fileInfoPanel.add(softwareLabel, gbcFileInfo);
		JLabel datetimeTitleLabel = new JLabel("Date & Time:");
		datetimeTitleLabel.setFont(datetimeTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcFileInfo.gridx = 2;
		fileInfoPanel.add(datetimeTitleLabel, gbcFileInfo);
		datetimeLabel = new JLabel("-");
		gbcFileInfo.gridx = 3;
		fileInfoPanel.add(datetimeLabel, gbcFileInfo);
		gbcSelectFile.insets = new Insets(7, 5, 7, 5);
		gbcSelectFile.gridx = 1;
		gbcSelectFile.weightx = 1;
		selectFilePanel.add(fileInfoPanel, gbcSelectFile);

		// Create thumbnail panel which shows an image thumbnail of the NDPI file
		imageThumbPanel = new CustomBackgroundPanel(Constants.THUMBNAIL_IMAGE, false);
		imageThumbPanel.setToolTipText("Thumbnail of the NDPI file");
		imageThumbPanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
		imageThumbPanel.setPreferredSize(new Dimension(150, 68));
		imageThumbPanel.setMinimumSize(imageThumbPanel.getPreferredSize());
		gbcSelectFile.insets = new Insets(7, 3, 7, 5);
		gbcSelectFile.gridx = 2;
		gbcSelectFile.weightx = 0;
		selectFilePanel.add(imageThumbPanel, gbcSelectFile);

		// Add file panel to the convert card
		gbcConvertCard.gridy = 1;
		gbcConvertCard.insets = new Insets(25, 25, 0, 25);
		convertCard.add(selectFilePanel, gbcConvertCard);

		// Create header panel which shows the column names for the
		// table-like presentation of all images of the NDPI file
		JPanel headerPanel = new JPanel(new BorderLayout());
		headerPanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
		headerPanel.setBackground(Constants.MEDIUM_GREY_COLOR);
		JPanel subheaderPanel = new JPanel(new GridBagLayout());
		subheaderPanel.setOpaque(false);
		GridBagConstraints gbcDescription = new GridBagConstraints();
		gbcDescription.anchor = GridBagConstraints.WEST;
		gbcDescription.fill = GridBagConstraints.BOTH;
		gbcDescription.insets = new Insets(6, 10, 6, 2);
		gbcDescription.weightx = 1;
		JLabel imageTypeLabel = new JLabel("Type");
		imageTypeLabel.setFont(imageTypeLabel.getFont().deriveFont(Font.BOLD));
		imageTypeLabel.setPreferredSize(new Dimension(60, 18));
		subheaderPanel.add(imageTypeLabel, gbcDescription);
		JLabel imageSizeLabel = new JLabel("Image size (w x h)");
		imageSizeLabel.setFont(imageSizeLabel.getFont().deriveFont(Font.BOLD));
		imageSizeLabel.setPreferredSize(new Dimension(125, 18));
		gbcDescription.insets = new Insets(6, 2, 6, 2);
		gbcDescription.gridx = 1;
		subheaderPanel.add(imageSizeLabel, gbcDescription);
		JLabel imageMagLabel = new JLabel("Magnification");
		imageMagLabel.setFont(imageMagLabel.getFont().deriveFont(Font.BOLD));
		imageMagLabel.setPreferredSize(new Dimension(80, 18));
		gbcDescription.gridx = 2;
		subheaderPanel.add(imageMagLabel, gbcDescription);
		JLabel imageFormatLabel = new JLabel("File format");
		imageFormatLabel.setFont(imageFormatLabel.getFont().deriveFont(Font.BOLD));
		imageFormatLabel.setPreferredSize(new Dimension(70, 18));
		gbcDescription.gridx = 3;
		subheaderPanel.add(imageFormatLabel, gbcDescription);
		JLabel imageFilesizeLabel = new JLabel("File size");
		imageFilesizeLabel.setFont(imageFilesizeLabel.getFont().deriveFont(Font.BOLD));
		imageFilesizeLabel.setPreferredSize(new Dimension(100, 18));
		gbcDescription.insets = new Insets(6, 2, 6, 10);
		gbcDescription.gridx = 4;
		subheaderPanel.add(imageFilesizeLabel, gbcDescription);
		headerPanel.add(subheaderPanel, BorderLayout.CENTER);
		JLabel selectedImageLabel = new JLabel("Selected image", SwingConstants.CENTER);
		selectedImageLabel.setFont(selectedImageLabel.getFont().deriveFont(Font.BOLD));
		selectedImageLabel.setPreferredSize(new Dimension(158, 18));
		headerPanel.add(selectedImageLabel, BorderLayout.EAST);

		// Add file panel to the convert card
		gbcConvertCard.insets = new Insets(25, 32, 0, 30);
		gbcConvertCard.gridwidth = 2;
		gbcConvertCard.gridy = 2;
		gbcConvertCard.weighty = 0;
		gbcConvertCard.fill = GridBagConstraints.HORIZONTAL;
		convertCard.add(headerPanel, gbcConvertCard);

		// Create images panel which servers as a container for
		// all images of the NDPI file in a table-like presentation
		imagesPanel = new JPanel();
		imagesPanel.setBackground(Constants.LIGHT_GREY_COLOR);
		imagesPanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
		JScrollPane scrollImages = new JScrollPane(imagesPanel);
		scrollImages.setBorder(null);
		gbcConvertCard.insets = new Insets(0, 32, 25, 4);
		gbcConvertCard.gridwidth = 1;
		gbcConvertCard.gridy = 3;
		gbcConvertCard.weighty = 1;
		gbcConvertCard.fill = GridBagConstraints.BOTH;
		convertCard.add(imagesPanel, gbcConvertCard);

		// Create panel which shows information about selected image
		// and contains buttons to start conversion or export of the selected image
		JPanel selectedImagePanel = new JPanel(new GridBagLayout());
		selectedImagePanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
		selectedImagePanel.setBackground(Constants.LIGHT_GREY_COLOR);
		selectedImagePanel.setPreferredSize(new Dimension(150, 380));
		selectedImagePanel.setMinimumSize(selectedImagePanel.getPreferredSize());
		GridBagConstraints gbcSelectedImg = new GridBagConstraints();
		gbcSelectedImg.anchor = GridBagConstraints.NORTHWEST;
		gbcSelectedImg.insets = new Insets(8, 8, 5, 8);
		gbcSelectedImg.weightx = 1;
		gbcSelectedImg.fill = GridBagConstraints.BOTH;
		selectedImgThumbPanel = new CustomBackgroundPanel(Constants.THUMBNAIL_IMAGE, false);
		selectedImgThumbPanel.setToolTipText("Thumbnail of the selected image");
		selectedImgThumbPanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
		selectedImgThumbPanel.setPreferredSize(new Dimension(140, 80));
		selectedImgThumbPanel.setMinimumSize(selectedImgThumbPanel.getPreferredSize());
		selectedImagePanel.add(selectedImgThumbPanel, gbcSelectedImg);
		exportTitleLabel = new JLabel();
		exportTitleLabel.setFont(exportTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcSelectedImg.insets = new Insets(25, 8, 0, 8);
		gbcSelectedImg.gridy = 1;
		selectedImagePanel.add(exportTitleLabel, gbcSelectedImg);
		exportOutputLabel = new JLabel();
		exportOutputLabel.setFont(exportOutputLabel.getFont().deriveFont((float) 11));
		gbcSelectedImg.insets = new Insets(0, 8, 0, 8);
		gbcSelectedImg.gridy = 2;
		selectedImagePanel.add(exportOutputLabel, gbcSelectedImg);
		startExportButton = new JButton("Export image");
		startExportButton.setIcon(new ImageIcon(getClass().getResource(Constants.EXPORT_ICON)));
		startExportButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		startExportButton.setHorizontalTextPosition(SwingConstants.CENTER);
		startExportButton.setToolTipText("Export selected image");
		startExportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Start exporting the selected image
				processSelectedImage(false);
			}
		});
		startExportButton.setEnabled(false);
		startExportButton.addActionListener(this);
		startExportButton.setVisible(false);
		gbcSelectedImg.insets = new Insets(2, 6, 2, 6);
		gbcSelectedImg.gridy = 3;
		selectedImagePanel.add(startExportButton, gbcSelectedImg);
		gbcSelectedImg.weighty = 1;
		gbcSelectedImg.gridy = 4;
		selectedImagePanel.add(new JLabel(), gbcSelectedImg);
		ometiffTitleLabel = new JLabel();
		ometiffTitleLabel.setFont(ometiffTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcSelectedImg.insets = new Insets(0, 8, 0, 8);
		gbcSelectedImg.gridy = 5;
		gbcSelectedImg.weighty = 0;
		selectedImagePanel.add(ometiffTitleLabel, gbcSelectedImg);
		ometiffOutputLabel = new JLabel();
		ometiffOutputLabel.setFont(ometiffOutputLabel.getFont().deriveFont((float) 11));
		gbcSelectedImg.gridy = 6;
		selectedImagePanel.add(ometiffOutputLabel, gbcSelectedImg);
		startConversionButton = new JButton("Convert image");
		startConversionButton.setIcon(new ImageIcon(getClass().getResource(Constants.CONVERT_ICON)));
		startConversionButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		startConversionButton.setHorizontalTextPosition(SwingConstants.CENTER);
		startConversionButton.setToolTipText("Convert selected image to an OME-TIFF");
		startConversionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Start converting the selected image
				processSelectedImage(true);
			}
		});
		startConversionButton.setEnabled(false);
		startConversionButton.addActionListener(this);
		startConversionButton.setVisible(false);
		gbcSelectedImg.insets = new Insets(2, 6, 8, 6);
		gbcSelectedImg.gridy = 7;
		selectedImagePanel.add(startConversionButton, gbcSelectedImg);
		gbcConvertCard.insets = new Insets(0, 4, 25, 30);
		gbcConvertCard.gridx = 1;
		gbcConvertCard.weightx = 0;
		convertCard.add(selectedImagePanel, gbcConvertCard);

		// Create NDPI file chooser
		ndpiFileChooser = new JFileChooser();
		ndpiFileChooser.setDialogTitle("Open NDPI file");
		ndpiFileChooser.setAcceptAllFileFilterUsed(false);
		ndpiFileChooser.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".ndpi") || f.isDirectory();
			}

			@Override
			public String getDescription() {
				return "NDPI files (*.npdi)";
			}
		});
	}

	/**
	 * Creates the settings card which contains all elements to customize the
	 * OME-TIFF. The following settings are available:
	 * <ul>
	 * <li>Output directory of the exported image or OME-TIFF</li>
	 * <li>Downscaling and downscaling algorithm</li>
	 * <li>Split color channels</li>
	 * <li>Image data organization (strips or tiles)</li>
	 * <li>Compression</li>
	 * <li>TIFF file format</li>
	 * </ul>
	 */
	private void createSettingsCard() {
		// Create settings card which serves as a container for all components of this card
		settingsCard = new JPanel(new GridBagLayout());
		settingsCard.setBackground(Color.WHITE);
		settingsCard.setBorder(new CustomRoundedBorder(25, Constants.LIGHT_GREY_COLOR));
		settingsCard.setPreferredSize(convertCard.getPreferredSize());
		GridBagConstraints gbcSettingsCard = new GridBagConstraints();
		gbcSettingsCard.insets = new Insets(25, 25, 0, 25);
		gbcSettingsCard.anchor = GridBagConstraints.NORTHWEST;
		gbcSettingsCard.fill = GridBagConstraints.HORIZONTAL;
		gbcSettingsCard.weightx = 1;

		// Create title panel
		JPanel titlePanel = new CustomBackgroundPanel(Constants.TITLE_BACKGROUND_IMAGE, true);
		titlePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(5, 25, 5, 25);
		gbcTitle.anchor = GridBagConstraints.NORTH;
		titlePanel.setMaximumSize(new Dimension(titlePanel.getWidth(), 40));
		titlePanel.setBorder(new CustomRoundedBorder(22, Color.WHITE));
		JLabel settingsLabel = new JLabel("Settings");
		settingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		settingsLabel.setFont(settingsLabel.getFont().deriveFont((float) 22));
		titlePanel.add(settingsLabel, gbcTitle);
		settingsCard.add(titlePanel, gbcSettingsCard);

		// Create settings panel which serves as a container for the settings panels
		JPanel settingsPanel = new JPanel(new GridBagLayout());
		settingsPanel.setBackground(Color.WHITE);
		JScrollPane settingsScrollPane = new JScrollPane(settingsPanel);
		settingsScrollPane.setBorder(null);
		GridBagConstraints gbcScroll = new GridBagConstraints();
		gbcScroll.insets = new Insets(0, 0, 2, 0);
		gbcScroll.anchor = GridBagConstraints.NORTHWEST;
		gbcScroll.fill = GridBagConstraints.HORIZONTAL;
		gbcScroll.weightx = 1;

		// Create output directory panel
		JPanel outputDirPanel = new JPanel();
		outputDirPanel.setBackground(Color.WHITE);
		outputDirPanel.setBorder(BorderFactory.createTitledBorder("Output directory"));
		outputDirPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcOutputDir = new GridBagConstraints();
		gbcOutputDir.anchor = GridBagConstraints.NORTHWEST;
		ButtonGroup outputDirSelectionGroup = new ButtonGroup();
		JRadioButton outputDirButtonSubdirectory = new JRadioButton("Subdirectory");
		outputDirButtonSubdirectory.setPreferredSize(new Dimension(145, (int) outputDirButtonSubdirectory.getPreferredSize().getHeight()));
		outputDirButtonSubdirectory.setBackground(Color.WHITE);
		if (settings.isStoreInSubdirectorySelected())
			outputDirButtonSubdirectory.setSelected(true);
		outputDirButtonSubdirectory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				settings.setStoreInSubdirectorySelected(true);
			}
		});
		outputDirSelectionGroup.add(outputDirButtonSubdirectory);
		gbcOutputDir.insets = new Insets(2, 5, 2, 5);
		outputDirPanel.add(outputDirButtonSubdirectory, gbcOutputDir);
		gbcOutputDir.insets = new Insets(3, 5, 2, 5);
		gbcOutputDir.gridx = 1;
		gbcOutputDir.weightx = 1;
		JLabel infoSubdirectoryLabel = new JLabel("Store OME-TIFFs in subdirectory '" + Constants.SUBDIRECTORY_CONVERTED + "' and exported images in subdirectory '" + Constants.SUBDIRECTORY_EXPORTED + "' of the current file");
		outputDirPanel.add(infoSubdirectoryLabel, gbcOutputDir);
		outputDirButtonChoose = new JRadioButton("Choose directory");
		outputDirButtonChoose.setBackground(Color.WHITE);
		if (!settings.isStoreInSubdirectorySelected())
			outputDirButtonChoose.setSelected(true);
		outputDirButtonChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Do not store exported/converted images in subdirectory of NDPI file
				settings.setStoreInSubdirectorySelected(false);
				settings.setOutputDirectory(outputDirLabel.getText());
			}
		});
		outputDirSelectionGroup.add(outputDirButtonChoose);
		gbcOutputDir.insets = new Insets(2, 5, 2, 5);
		gbcOutputDir.gridx = 0;
		gbcOutputDir.gridy = 1;
		gbcOutputDir.weightx = 0;
		outputDirPanel.add(outputDirButtonChoose, gbcOutputDir);
		gbcOutputDir.insets = new Insets(3, 5, 2, 5);
		gbcOutputDir.gridx = 1;
		gbcOutputDir.weightx = 1;
		JLabel infoChooseDirLabel = new JLabel("Store OME-TIFFs and exported images in the following directory:");
		outputDirPanel.add(infoChooseDirLabel, gbcOutputDir);
		outputDirLabel = new JLabel(new java.io.File("").getAbsolutePath());
		if (!settings.isStoreInSubdirectorySelected())
			outputDirLabel.setText(settings.getOutputDirectory());
		outputDirLabel.setFont(outputDirLabel.getFont().deriveFont(Font.ITALIC));
		gbcOutputDir.gridy = 2;
		outputDirPanel.add(outputDirLabel, gbcOutputDir);
		JButton chooseDirButton = new JButton("Choose output directory");
		chooseDirButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Open output directory chooser
				specifyOutputDirectory();
			}
		});
		chooseDirButton.setPreferredSize(new Dimension(210, 33));
		chooseDirButton.setIcon(new ImageIcon(getClass().getResource(Constants.DIRECTORY_ICON)));
		chooseDirButton.addActionListener(this);
		gbcOutputDir.gridy = 3;
		outputDirPanel.add(chooseDirButton, gbcOutputDir);
		settingsPanel.add(outputDirPanel, gbcScroll);

		// Create output directory chooser
		outputDirectoryChooser = new JFileChooser(new File(outputDirLabel.getText()));
		outputDirectoryChooser.setDialogTitle("Choose output directory");
		outputDirectoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		outputDirectoryChooser.setAcceptAllFileFilterUsed(false);

		// Create downscaling panel
		JPanel downscalingPanel = new JPanel(new GridBagLayout());
		downscalingPanel.setBackground(Color.WHITE);
		downscalingPanel.setBorder(BorderFactory.createTitledBorder("Downscaling"));
		GridBagConstraints gbcDownscale = new GridBagConstraints();
		gbcDownscale.anchor = GridBagConstraints.NORTHWEST;
		JCheckBox downscaleCheckBox = new JCheckBox("Downscale image to half size with the following interpolation algorithm:");
		downscaleCheckBox.setSelected(settings.isDownscalingSelected());
		downscaleCheckBox.setBackground(Color.WHITE);
		downscaleCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (settings.isDownscalingSelected()) {
					// Downscaling not selected
					settings.setDownscalingSelected(false);
					// Update text of currently selected image
					updateSelectedImage();
				} else {
					// Downscaling selected
					settings.setDownscalingSelected(true);
					if (settings.getImgDataOrganization() == ConversionSettings.TILES) {
						// Downscaled images have to be stored as striped OME-TIFFs
						displayInfoMessage("Image data organization", "Downscaled images have to be stored as striped OME-TIFFs.\nThe image data organization will be changed to strips.");
						settings.setImgDataOrganization(ConversionSettings.STRIPS);
						storageButtonStrips.setSelected(true);
					}
					// Update text of currently selected image
					updateSelectedImage();
				}
			}
		});
		gbcDownscale.insets = new Insets(2, 5, 2, 5);
		downscalingPanel.add(downscaleCheckBox, gbcDownscale);
		JComboBox downscaleList = new JComboBox(settings.getAvailableDownscalingAlgorithms());
		downscaleList.setSelectedItem(settings.getDownscalingAlgorithmAsString());
		downscaleList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Update downscaling algorithm
				String algorithmName = (String) ((JComboBox) e.getSource()).getSelectedItem();
				settings.setDownscalingAlgorithm(algorithmName);
			}
		});
		gbcDownscale.insets = new Insets(0, 5, 2, 5);
		gbcDownscale.gridx = 1;
		gbcDownscale.weightx = 1;
		downscalingPanel.add(downscaleList, gbcDownscale);
		gbcScroll.gridy = 1;
		settingsPanel.add(downscalingPanel, gbcScroll);

		// Create color-splitting panel
		JPanel colorSplitPanel = new JPanel(new GridBagLayout());
		ButtonGroup splitColorsSelectionGroup = new ButtonGroup();
		colorSplitPanel.setBackground(Color.WHITE);
		colorSplitPanel.setBorder(BorderFactory.createTitledBorder("Split color channels"));
		GridBagConstraints gbcColorSplit = new GridBagConstraints();
		gbcColorSplit.anchor = GridBagConstraints.NORTHWEST;
		JRadioButton splitColorsYes = new JRadioButton("Yes");
		splitColorsYes.setBackground(Color.WHITE);
		splitColorsYes.setPreferredSize(new Dimension(145, (int) splitColorsYes.getPreferredSize().getHeight()));
		if (settings.isMultipageOMETiffSelected())
			splitColorsYes.setSelected(true);
		splitColorsYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Color-splitting selected
				settings.setMultipageOMETiffSelected(true);
			}
		});
		splitColorsSelectionGroup.add(splitColorsYes);
		gbcColorSplit.insets = new Insets(2, 5, 2, 5);
		colorSplitPanel.add(splitColorsYes, gbcColorSplit);
		JLabel infoSplitColorsLabel = new JLabel("<html>Create a multipage OME-TIFF with three grayscale images (one image for each RGB color channel)<br>This setting is required to create a standard-compliant OME-TIFF</html>");
		gbcColorSplit.insets = new Insets(3, 5, 2, 5);
		gbcColorSplit.gridx = 1;
		gbcColorSplit.weightx = 1;
		colorSplitPanel.add(infoSplitColorsLabel, gbcColorSplit);
		JRadioButton splitColorsNo = new JRadioButton("No");
		splitColorsNo.setBackground(Color.WHITE);
		if (!settings.isMultipageOMETiffSelected())
			splitColorsNo.setSelected(true);
		splitColorsNo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Color-splitting not selected
				settings.setMultipageOMETiffSelected(false);
			}
		});
		splitColorsSelectionGroup.add(splitColorsNo);
		gbcColorSplit.insets = new Insets(2, 5, 2, 5);
		gbcColorSplit.weightx = 0;
		gbcColorSplit.gridx = 0;
		gbcColorSplit.gridy = 1;
		colorSplitPanel.add(splitColorsNo, gbcColorSplit);
		JLabel infoNoSplitColorsLabel = new JLabel("Create an OME-TIFF containing a single RGB image");
		gbcColorSplit.insets = new Insets(3, 5, 2, 5);
		gbcColorSplit.gridx = 1;
		gbcColorSplit.weightx = 1;
		colorSplitPanel.add(infoNoSplitColorsLabel, gbcColorSplit);
		gbcScroll.gridy = 2;
		settingsPanel.add(colorSplitPanel, gbcScroll);

		// Create image data organization panel
		JPanel imgDataOrganizationPanel = new JPanel(new GridBagLayout());
		ButtonGroup pixelStorageSelectionGroup = new ButtonGroup();
		imgDataOrganizationPanel.setBackground(Color.WHITE);
		imgDataOrganizationPanel.setBorder(BorderFactory.createTitledBorder("Image data organization"));
		GridBagConstraints gbcImgDataOrganization = new GridBagConstraints();
		gbcImgDataOrganization.anchor = GridBagConstraints.NORTHWEST;
		storageButtonStrips = new JRadioButton("Strips");
		storageButtonStrips.setBackground(Color.WHITE);
		storageButtonStrips.setPreferredSize(new Dimension(145, (int) storageButtonStrips.getPreferredSize().getHeight()));
		if (settings.getImgDataOrganization() == ConversionSettings.STRIPS)
			storageButtonStrips.setSelected(true);
		storageButtonStrips.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Striped OME-TIFF selected
				settings.setImgDataOrganization(ConversionSettings.STRIPS);
				// Update text of currently selected image
				updateSelectedImage();
			}
		});
		pixelStorageSelectionGroup.add(storageButtonStrips);
		gbcImgDataOrganization.insets = new Insets(2, 5, 2, 5);
		imgDataOrganizationPanel.add(storageButtonStrips, gbcImgDataOrganization);
		JLabel infoStripsLabel = new JLabel("Image data get organized into strips");
		gbcImgDataOrganization.insets = new Insets(3, 5, 2, 5);
		gbcImgDataOrganization.gridx = 1;
		gbcImgDataOrganization.weightx = 1;
		imgDataOrganizationPanel.add(infoStripsLabel, gbcImgDataOrganization);
		JRadioButton storageButtonTiles = new JRadioButton("Tiles");
		storageButtonTiles.setBackground(Color.WHITE);
		if (settings.getImgDataOrganization() == ConversionSettings.TILES)
			storageButtonTiles.setSelected(true);
		storageButtonTiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Tiled OME-TIFF selected
				if (settings.isDownscalingSelected()) {
					// Downscaled images have to be stored as striped OME-TIFFs
					displayInfoMessage("Image data organization", "Downscaled images have to be stored as striped OME-TIFFs.\nThe image data organization will be changed to strips.");
					settings.setImgDataOrganization(ConversionSettings.STRIPS);
					storageButtonStrips.setSelected(true);
					// Update text of currently selected image
					updateSelectedImage();
				} else {
					settings.setImgDataOrganization(ConversionSettings.TILES);
					// Update text of currently selected image
					updateSelectedImage();
				}
			}
		});
		pixelStorageSelectionGroup.add(storageButtonTiles);
		gbcImgDataOrganization.insets = new Insets(2, 5, 2, 5);
		gbcImgDataOrganization.weightx = 0;
		gbcImgDataOrganization.gridx = 0;
		gbcImgDataOrganization.gridy = 1;
		imgDataOrganizationPanel.add(storageButtonTiles, gbcImgDataOrganization);
		JLabel infoTilesLabel = new JLabel("Image data get organized into tiles");
		gbcImgDataOrganization.insets = new Insets(3, 5, 2, 5);
		gbcImgDataOrganization.gridx = 1;
		gbcImgDataOrganization.weightx = 1;
		imgDataOrganizationPanel.add(infoTilesLabel, gbcImgDataOrganization);
		gbcScroll.gridy = 3;
		settingsPanel.add(imgDataOrganizationPanel, gbcScroll);

		// Create compression panel
		JPanel compressionPanel = new JPanel(new GridBagLayout());
		ButtonGroup compressionSelectionGroup = new ButtonGroup();
		compressionPanel.setBackground(Color.WHITE);
		compressionPanel.setBorder(BorderFactory.createTitledBorder("Compression"));
		GridBagConstraints gbcCompression = new GridBagConstraints();
		gbcCompression.anchor = GridBagConstraints.NORTHWEST;
		JRadioButton comprButtonUncompressed = new JRadioButton("Uncompressed");
		comprButtonUncompressed.setBackground(Color.WHITE);
		comprButtonUncompressed.setPreferredSize(new Dimension(145, (int) comprButtonUncompressed.getPreferredSize().getHeight()));
		if (settings.getCompressionAlgorithm() == TiffCompression.UNCOMPRESSED.getCode())
			comprButtonUncompressed.setSelected(true);
		comprButtonUncompressed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// No compression selected
				settings.setCompressionAlgorithm(TiffCompression.UNCOMPRESSED.getCode());
			}
		});
		compressionSelectionGroup.add(comprButtonUncompressed);
		gbcCompression.insets = new Insets(2, 5, 2, 5);
		compressionPanel.add(comprButtonUncompressed, gbcCompression);
		JLabel infoUncompressedLabel = new JLabel("Fastest way for converting images, largest file size");
		gbcCompression.insets = new Insets(3, 5, 2, 5);
		gbcCompression.gridx = 1;
		gbcCompression.weightx = 1;
		compressionPanel.add(infoUncompressedLabel, gbcCompression);
		JRadioButton comprButtonLZW = new JRadioButton("LZW");
		comprButtonLZW.setBackground(Color.WHITE);
		if (settings.getCompressionAlgorithm() == TiffCompression.LZW.getCode())
			comprButtonLZW.setSelected(true);
		comprButtonLZW.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// LZW compression selected
				settings.setCompressionAlgorithm(TiffCompression.LZW.getCode());
			}
		});
		compressionSelectionGroup.add(comprButtonLZW);
		gbcCompression.insets = new Insets(2, 5, 2, 5);
		gbcCompression.gridx = 0;
		gbcCompression.gridy = 1;
		gbcCompression.weightx = 0;
		compressionPanel.add(comprButtonLZW, gbcCompression);
		JLabel infoLZWLabel = new JLabel("Fast and lossless compression, compression-rate is about 2:1");
		gbcCompression.insets = new Insets(3, 5, 2, 5);
		gbcCompression.gridx = 1;
		gbcCompression.weightx = 1;
		compressionPanel.add(infoLZWLabel, gbcCompression);
		JRadioButton comprButtonDeflate = new JRadioButton("Deflate");
		comprButtonDeflate.setBackground(Color.WHITE);
		if (settings.getCompressionAlgorithm() == TiffCompression.DEFLATE.getCode())
			comprButtonDeflate.setSelected(true);
		comprButtonDeflate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Deflate compression selected
				settings.setCompressionAlgorithm(TiffCompression.DEFLATE.getCode());
			}
		});
		compressionSelectionGroup.add(comprButtonDeflate);
		gbcCompression.insets = new Insets(2, 5, 2, 5);
		gbcCompression.gridx = 0;
		gbcCompression.gridy = 2;
		gbcCompression.weightx = 0;
		compressionPanel.add(comprButtonDeflate, gbcCompression);
		JLabel infoDeflateLabel = new JLabel("Lossless ZIP-compression, more efficient than LZW but slower");
		gbcCompression.insets = new Insets(3, 5, 2, 5);
		gbcCompression.gridx = 1;
		gbcCompression.weightx = 1;
		compressionPanel.add(infoDeflateLabel, gbcCompression);
		JRadioButton comprButtonDeflateAdobe = new JRadioButton("Adobe Deflate");
		comprButtonDeflateAdobe.setBackground(Color.WHITE);
		if (settings.getCompressionAlgorithm() == TiffCompression.PROPRIETARY_DEFLATE.getCode())
			comprButtonDeflateAdobe.setSelected(true);
		comprButtonDeflateAdobe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Adobe Deflate compression selected
				settings.setCompressionAlgorithm(TiffCompression.PROPRIETARY_DEFLATE.getCode());
			}
		});
		compressionSelectionGroup.add(comprButtonDeflateAdobe);
		gbcCompression.insets = new Insets(2, 5, 2, 5);
		gbcCompression.gridx = 0;
		gbcCompression.gridy = 3;
		gbcCompression.weightx = 0;
		compressionPanel.add(comprButtonDeflateAdobe, gbcCompression);
		JLabel infoDeflateAdobeLabel = new JLabel("Same compression as Deflate, but other compression-tag");
		gbcCompression.insets = new Insets(3, 5, 2, 5);
		gbcCompression.gridx = 1;
		gbcCompression.weightx = 1;
		compressionPanel.add(infoDeflateAdobeLabel, gbcCompression);
		JRadioButton comprButtonJPEG2000 = new JRadioButton("JPEG 2000");
		comprButtonJPEG2000.setBackground(Color.WHITE);
		if (settings.getCompressionAlgorithm() == TiffCompression.JPEG_2000.getCode())
			comprButtonJPEG2000.setSelected(true);
		comprButtonJPEG2000.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// JPEG 2000 compression selected
				settings.setCompressionAlgorithm(TiffCompression.JPEG_2000.getCode());
			}
		});
		compressionSelectionGroup.add(comprButtonJPEG2000);
		gbcCompression.insets = new Insets(2, 5, 2, 5);
		gbcCompression.gridx = 0;
		gbcCompression.gridy = 4;
		gbcCompression.weightx = 0;
		compressionPanel.add(comprButtonJPEG2000, gbcCompression);
		JLabel infoJPEG2000Label = new JLabel("Lossless JPEG 2000 compression, smallest file size, rarely supported by image viewers");
		gbcCompression.insets = new Insets(3, 5, 2, 5);
		gbcCompression.gridx = 1;
		gbcCompression.weightx = 1;
		compressionPanel.add(infoJPEG2000Label, gbcCompression);
		gbcScroll.gridy = 4;
		settingsPanel.add(compressionPanel, gbcScroll);

		// Create TIFF file format panel
		JPanel tiffFormatPanel = new JPanel(new GridBagLayout());
		ButtonGroup tiffFormatSelectionGroup = new ButtonGroup();
		tiffFormatPanel.setBackground(Color.WHITE);
		tiffFormatPanel.setBorder(BorderFactory.createTitledBorder("TIFF file format"));
		GridBagConstraints gbcTiffFormat = new GridBagConstraints();
		gbcTiffFormat.anchor = GridBagConstraints.NORTHWEST;
		JRadioButton standardTiffButton = new JRadioButton("TIFF");
		standardTiffButton.setBackground(Color.WHITE);
		standardTiffButton.setPreferredSize(new Dimension(145, (int) standardTiffButton.getPreferredSize().getHeight()));
		if (!settings.isBigTiffSelected())
			standardTiffButton.setSelected(true);
		standardTiffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Standard TIFF file format selected
				settings.setBigTiffSelected(false);
			}
		});
		tiffFormatSelectionGroup.add(standardTiffButton);
		gbcTiffFormat.insets = new Insets(2, 5, 2, 5);
		tiffFormatPanel.add(standardTiffButton, gbcTiffFormat);
		JLabel infoTiffLabel = new JLabel("Standard TIFF file format with a file size limitation of 4 GByte (2^32 bits)");
		gbcTiffFormat.insets = new Insets(3, 5, 2, 5);
		gbcTiffFormat.gridx = 1;
		gbcTiffFormat.weightx = 1;
		tiffFormatPanel.add(infoTiffLabel, gbcTiffFormat);
		bigTiffButton = new JRadioButton("BigTIFF");
		bigTiffButton.setBackground(Color.WHITE);
		if (settings.isBigTiffSelected())
			bigTiffButton.setSelected(true);
		bigTiffButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// BigTIFF file format selected
				settings.setBigTiffSelected(true);
			}
		});
		tiffFormatSelectionGroup.add(bigTiffButton);
		gbcTiffFormat.insets = new Insets(2, 5, 2, 5);
		gbcTiffFormat.gridx = 0;
		gbcTiffFormat.gridy = 1;
		gbcTiffFormat.weightx = 0;
		tiffFormatPanel.add(bigTiffButton, gbcTiffFormat);
		JLabel infoBigTiffLabel = new JLabel("BigTIFF file format which overcomes the 4 GByte limitation");
		gbcTiffFormat.insets = new Insets(3, 5, 2, 5);
		gbcTiffFormat.gridx = 1;
		gbcTiffFormat.weightx = 1;
		tiffFormatPanel.add(infoBigTiffLabel, gbcTiffFormat);
		gbcScroll.weighty = 1;
		gbcScroll.gridy = 5;
		settingsPanel.add(tiffFormatPanel, gbcScroll);

		// Add settings panel to the settings card
		gbcSettingsCard.insets = new Insets(25, 25, 25, 25);
		gbcSettingsCard.gridy = 1;
		gbcSettingsCard.weighty = 1;
		gbcSettingsCard.fill = GridBagConstraints.BOTH;
		settingsCard.add(settingsScrollPane, gbcSettingsCard);
	}

	/**
	 * Creates the help card which contains a quick guide how to convert and
	 * export images of NDPI files.
	 */
	private void createHelpCard() {
		// Create help card which serves as a container for all components of this card
		helpCard = new JPanel(new GridBagLayout());
		helpCard.setBackground(Color.WHITE);
		helpCard.setBorder(new CustomRoundedBorder(25, Constants.LIGHT_GREY_COLOR));
		helpCard.setPreferredSize(convertCard.getPreferredSize());
		GridBagConstraints gbcHelpCard = new GridBagConstraints();
		gbcHelpCard.insets = new Insets(25, 25, 0, 25);
		gbcHelpCard.anchor = GridBagConstraints.NORTHWEST;
		gbcHelpCard.fill = GridBagConstraints.HORIZONTAL;
		gbcHelpCard.weightx = 1;

		// Create title panel
		JPanel titlePanel = new CustomBackgroundPanel(Constants.TITLE_BACKGROUND_IMAGE, true);
		titlePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(5, 25, 5, 25);
		gbcTitle.anchor = GridBagConstraints.NORTH;
		titlePanel.setMaximumSize(new Dimension(titlePanel.getWidth(), 40));
		titlePanel.setBorder(new CustomRoundedBorder(22, Color.WHITE));
		JLabel helpLabel = new JLabel("Help");
		helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		helpLabel.setFont(helpLabel.getFont().deriveFont((float) 22));
		titlePanel.add(helpLabel, gbcTitle);
		helpCard.add(titlePanel, gbcHelpCard);

		// Create help panel which serves as a container
		// for the website button and the help text
		JPanel helpPanel = new JPanel(new GridBagLayout());
		helpPanel.setBackground(Color.WHITE);
		GridBagConstraints gbcHelpPanel = new GridBagConstraints();
		gbcHelpPanel.insets = new Insets(0, 0, 20, 0);
		gbcHelpPanel.anchor = GridBagConstraints.NORTHWEST;
		gbcHelpPanel.fill = GridBagConstraints.BOTH;
		gbcHelpPanel.weightx = 1;

		JLabel websiteLabel = new JLabel("<html><p style='text-align:center'><b>Please visit the following website for further information about this tool:</b><br>" + Constants.TOOL_WEBSITE + "</p></html>");
		websiteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		websiteLabel.setBorder(BorderFactory.createTitledBorder(""));
		websiteLabel.setToolTipText("Click to open the website");
		websiteLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (Desktop.isDesktopSupported()) {
					try {
						Desktop.getDesktop().browse(new URI(Constants.TOOL_WEBSITE));
					} catch (Exception ex) {
						displayErrorMessage("Could not open website", "Could not open the website " + Constants.TOOL_WEBSITE);
					}
				} else {
					displayErrorMessage("Could not open website", "Could not open the website " + Constants.TOOL_WEBSITE);
				}

			}
		});
		helpPanel.add(websiteLabel, gbcHelpPanel);

		JTextPane convertHelpTextPane = new JTextPane();
		convertHelpTextPane.setPreferredSize(new Dimension(400, (int) convertHelpTextPane.getPreferredSize().getHeight()));
		convertHelpTextPane.setEditable(false);
		convertHelpTextPane.setContentType("text/html");
		convertHelpTextPane.setBorder(BorderFactory.createTitledBorder(""));
		JScrollPane convertHelpScrollPane = new JScrollPane(convertHelpTextPane);
		convertHelpScrollPane.setBorder(BorderFactory.createEmptyBorder());
		try {
			convertHelpTextPane.read(getClass().getResourceAsStream(Constants.HELP_FILE), null);
		} catch (Exception e) {
			LOGGER.error("Failed to load help file '" + Constants.HELP_FILE + "':", e);
			convertHelpTextPane.setText("Failed to load help file '" + Constants.HELP_FILE + "'!");
		}
		gbcHelpPanel.insets = new Insets(0, 0, 0, 0);
		gbcHelpPanel.gridy = 1;
		gbcHelpPanel.weighty = 1;
		helpPanel.add(convertHelpScrollPane, gbcHelpPanel);

		// Add text pane to the help card
		gbcHelpCard.insets = new Insets(25, 25, 25, 25);
		gbcHelpCard.gridy = 1;
		gbcHelpCard.weighty = 1;
		gbcHelpCard.fill = GridBagConstraints.BOTH;
		helpCard.add(helpPanel, gbcHelpCard);
	}

	/**
	 * Creates a dialog which shows further information about the NDPI to
	 * OME-TIFF converter.
	 */
	private void createAboutDialog() {
		aboutDialog = new JDialog(this, "About this tool", true);
		aboutDialog.setLayout(new GridBagLayout());
		GridBagConstraints gbcInfoDialog = new GridBagConstraints();
		gbcInfoDialog.anchor = GridBagConstraints.CENTER;
		JLabel toolTitleLabel = new JLabel(Constants.TOOL_TITLE);
		toolTitleLabel.setFont(toolTitleLabel.getFont().deriveFont((float) 16));
		toolTitleLabel.setFont(toolTitleLabel.getFont().deriveFont(Font.BOLD));
		aboutDialog.add(toolTitleLabel, gbcInfoDialog);
		gbcInfoDialog.gridy = 1;
		aboutDialog.add(new JLabel(Constants.TOOL_PURPOSE), gbcInfoDialog);
		gbcInfoDialog.gridy = 2;
		aboutDialog.add(new JLabel(Constants.TOOL_VERSION), gbcInfoDialog);
		gbcInfoDialog.insets = new Insets(10, 0, 0, 0);
		gbcInfoDialog.gridy = 3;
		aboutDialog.add(new JLabel(Constants.TOOL_COPYRIGHT), gbcInfoDialog);
		gbcInfoDialog.insets = new Insets(0, 0, 0, 0);
		gbcInfoDialog.gridy = 4;
		aboutDialog.add(new JLabel(Constants.TOOL_WEBSITE), gbcInfoDialog);
		gbcInfoDialog.insets = new Insets(25, 0, 0, 0);
		gbcInfoDialog.gridy = 5;
		aboutDialog.add(new JLabel(Constants.TOOL_LICENSE), gbcInfoDialog);
		JTextArea licenseTextArea = new JTextArea(Constants.TOOL_LICENSE_TEXT);
		licenseTextArea.setEditable(false);
		gbcInfoDialog.insets = new Insets(0, 10, 0, 10);
		gbcInfoDialog.gridy = 6;
		gbcInfoDialog.fill = GridBagConstraints.HORIZONTAL;
		aboutDialog.add(licenseTextArea, gbcInfoDialog);
		gbcInfoDialog.insets = new Insets(15, 0, 0, 0);
		gbcInfoDialog.gridy = 7;
		gbcInfoDialog.fill = GridBagConstraints.NONE;
		aboutDialog.add(new JLabel("This tool uses the following icon sets"), gbcInfoDialog);
		JTextArea iconsTextArea = new JTextArea(Constants.TOOL_USED_ICONSETS);
		iconsTextArea.setEditable(false);
		gbcInfoDialog.insets = new Insets(0, 10, 0, 10);
		gbcInfoDialog.gridy = 8;
		gbcInfoDialog.fill = GridBagConstraints.HORIZONTAL;
		aboutDialog.add(iconsTextArea, gbcInfoDialog);
		gbcInfoDialog.insets = new Insets(15, 0, 0, 0);
		gbcInfoDialog.gridy = 9;
		gbcInfoDialog.fill = GridBagConstraints.NONE;
		aboutDialog.add(new JLabel("This tool uses the following packages/software"), gbcInfoDialog);
		JTextArea packagesTextArea = new JTextArea(Constants.TOOL_USED_PACKAGES);
		packagesTextArea.setEditable(false);
		gbcInfoDialog.insets = new Insets(0, 10, 15, 10);
		gbcInfoDialog.gridy = 10;
		gbcInfoDialog.fill = GridBagConstraints.HORIZONTAL;
		aboutDialog.add(packagesTextArea, gbcInfoDialog);
		aboutDialog.setResizable(false);
		aboutDialog.pack();
		aboutDialog.setLocationRelativeTo(this);
	}

	/**
	 * Creates a dialog which informs the user that the selected NDPI file is
	 * being analyzed.
	 */
	private void createReadingNDPIDialog() {
		readingDialog = new JDialog(this, null, true);
		JPanel readingPanel = new JPanel(new GridBagLayout());
		readingPanel.setBackground(Color.WHITE);
		readingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		GridBagConstraints gbcReading = new GridBagConstraints();
		gbcReading.insets = new Insets(5, 25, 5, 25);
		gbcReading.anchor = GridBagConstraints.CENTER;
		readingPanel.add(new JLabel(new ImageIcon(getClass().getResource(Constants.PROCESSING_IMAGE))), gbcReading);
		gbcReading.gridy = 1;
		readingPanel.add(new JLabel("Analyzing selected NDPI file..."), gbcReading);
		readingDialog.add(readingPanel, BorderLayout.CENTER);
		readingDialog.setSize(350, 120);
		readingDialog.setLocationRelativeTo(this);
		readingDialog.setUndecorated(true);
	}

	/**
	 * Creates a dialog which informs the user that the selected image is being
	 * exported.
	 */
	private void createExportingImageDialog() {
		exportingDialog = new JDialog(this, null, true);
		JPanel exportingPanel = new JPanel(new GridBagLayout());
		exportingPanel.setBackground(Color.WHITE);
		exportingPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		GridBagConstraints gbcExporting = new GridBagConstraints();
		gbcExporting.insets = new Insets(5, 25, 5, 25);
		gbcExporting.anchor = GridBagConstraints.CENTER;
		exportingPanel.add(new JLabel(new ImageIcon(getClass().getResource(Constants.PROCESSING_IMAGE))), gbcExporting);
		gbcExporting.gridy = 1;
		exportingPanel.add(new JLabel("Exporting selected image..."), gbcExporting);
		exportingDialog.add(exportingPanel, BorderLayout.CENTER);
		exportingDialog.setSize(350, 120);
		exportingDialog.setLocationRelativeTo(this);
		exportingDialog.setUndecorated(true);
	}

	/**
	 * Creates a dialog which informs the user that the selected image is being
	 * converted to an OME-TIFF. Furthermore, information about the input file
	 * and output file are shown as well as a progress-bar and a cancel-button
	 * to stop the conversion.
	 * 
	 * @param imgInfo
	 *            the {@link ImageInfo} object. It contains all important
	 *            information about the selected image.
	 */
	private void createConvertingDialog(ImageInfo imgInfo) {
		convertingDialog = new JDialog(this, null, true);
		JPanel conversionPanel = new JPanel(new GridBagLayout());
		conversionPanel.setBackground(Constants.LIGHT_GREY_COLOR);
		conversionPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		GridBagConstraints gbcConversion = new GridBagConstraints();
		gbcConversion.insets = new Insets(10, 25, 10, 25);
		gbcConversion.anchor = GridBagConstraints.NORTHWEST;
		gbcConversion.weightx = 1;
		gbcConversion.fill = GridBagConstraints.HORIZONTAL;

		// Create title panel
		JPanel titlePanel = new CustomBackgroundPanel(Constants.TITLE_BACKGROUND_IMAGE, true);
		titlePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(5, 25, 5, 25);
		gbcTitle.anchor = GridBagConstraints.NORTH;
		titlePanel.setMaximumSize(new Dimension(conversionPanel.getWidth(), 28));
		titlePanel.setBorder(new CustomRoundedBorder(22, Color.WHITE));
		JLabel helpLabel = new JLabel("Converting selected NDPI image to OME-TIFF");
		helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
		helpLabel.setFont(helpLabel.getFont().deriveFont((float) 16));
		titlePanel.add(helpLabel, gbcTitle);
		conversionPanel.add(titlePanel, gbcConversion);

		// Create input file panel which shows information about the input image
		JPanel inputFilePanel = new JPanel(new GridBagLayout());
		inputFilePanel.setBorder(BorderFactory.createTitledBorder(new CustomRoundedBorder(10, Constants.LIGHT_GREY_COLOR), "Input file"));
		GridBagConstraints gbcInput = new GridBagConstraints();
		gbcInput.insets = new Insets(5, 10, 2, 10);
		gbcInput.anchor = GridBagConstraints.NORTHWEST;
		JLabel inputFilenameTitleLabel = new JLabel("Filename:");
		inputFilenameTitleLabel.setFont(inputFilenameTitleLabel.getFont().deriveFont(Font.BOLD));
		inputFilenameTitleLabel.setPreferredSize(new Dimension(135, (int) inputFilenameTitleLabel.getPreferredSize().getHeight()));
		inputFilePanel.add(inputFilenameTitleLabel, gbcInput);
		JLabel inputFilenameLabel = new JLabel(ndpiFile.getName());
		gbcInput.weightx = 1;
		gbcInput.gridx = 1;
		inputFilePanel.add(inputFilenameLabel, gbcInput);
		JLabel inputFileDimensionTitleLabel = new JLabel("Image size (w x h):");
		inputFileDimensionTitleLabel.setFont(inputFileDimensionTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcInput.insets = new Insets(2, 10, 5, 10);
		gbcInput.weightx = 0;
		gbcInput.gridx = 0;
		gbcInput.gridy = 1;
		inputFilePanel.add(inputFileDimensionTitleLabel, gbcInput);
		JLabel inputFileDimensionLabel = new JLabel(NumberFormat.getIntegerInstance().format(imgInfo.getWidth()) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getHeight()));
		gbcInput.weightx = 1;
		gbcInput.gridx = 1;
		inputFilePanel.add(inputFileDimensionLabel, gbcInput);
		gbcConversion.gridy = 1;
		conversionPanel.add(inputFilePanel, gbcConversion);

		// Create output file panel which shows information about the output image (OME-TIFF)
		JPanel outputFilePanel = new JPanel(new GridBagLayout());
		outputFilePanel.setBorder(BorderFactory.createTitledBorder(new CustomRoundedBorder(10, Constants.LIGHT_GREY_COLOR), "Output file"));
		GridBagConstraints gbcOutput = new GridBagConstraints();
		gbcOutput.insets = new Insets(5, 10, 2, 10);
		gbcOutput.anchor = GridBagConstraints.NORTHWEST;
		JLabel outputFileTitleLabel = new JLabel("Filename:");
		outputFileTitleLabel.setFont(outputFileTitleLabel.getFont().deriveFont(Font.BOLD));
		outputFileTitleLabel.setPreferredSize(new Dimension(135, (int) outputFileTitleLabel.getPreferredSize().getHeight()));
		outputFilePanel.add(outputFileTitleLabel, gbcOutput);
		JLabel outputFileLabel = new JLabel(imgInfo.getOMETiffFilename());
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(outputFileLabel, gbcOutput);
		JLabel ouputDirTitleLabel = new JLabel("Output directory:");
		ouputDirTitleLabel.setFont(ouputDirTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcOutput.insets = new Insets(2, 10, 2, 10);
		gbcOutput.weightx = 0;
		gbcOutput.gridx = 0;
		gbcOutput.gridy = 1;
		outputFilePanel.add(ouputDirTitleLabel, gbcOutput);
		JLabel ouputDirLabel = new JLabel(settings.getOutputDirectory());
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(ouputDirLabel, gbcOutput);
		JLabel outputFileDimensionTitleLabel = new JLabel("Image size (w x h):");
		outputFileDimensionTitleLabel.setFont(outputFileDimensionTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcOutput.weightx = 0;
		gbcOutput.gridx = 0;
		gbcOutput.gridy = 2;
		outputFilePanel.add(outputFileDimensionTitleLabel, gbcOutput);
		JLabel outputFileDimensionLabel = new JLabel();
		String text;
		if (settings.isDownscalingSelected()) {
			text = NumberFormat.getIntegerInstance().format(imgInfo.getDownscaledWidth(settings.getDownscalingFactor())) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getDownscaledHeight(settings.getDownscalingFactor()));
			text += " (downscaled, " + settings.getDownscalingAlgorithmAsString() + ")";
		} else {
			text = NumberFormat.getIntegerInstance().format(imgInfo.getWidth()) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getHeight());
		}
		outputFileDimensionLabel.setText(text);
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(outputFileDimensionLabel, gbcOutput);
		JLabel ouputCompressionTitleLabel = new JLabel("Compression:");
		ouputCompressionTitleLabel.setFont(ouputCompressionTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcOutput.weightx = 0;
		gbcOutput.gridx = 0;
		gbcOutput.gridy = 3;
		outputFilePanel.add(ouputCompressionTitleLabel, gbcOutput);
		JLabel ouputCompressionLabel = new JLabel(settings.getCompressionAlgorithmAsString());
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(ouputCompressionLabel, gbcOutput);
		JLabel ouputBigTiffTitleLabel = new JLabel("BigTIFF:");
		ouputBigTiffTitleLabel.setFont(ouputBigTiffTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcOutput.weightx = 0;
		gbcOutput.gridx = 0;
		gbcOutput.gridy = 4;
		outputFilePanel.add(ouputBigTiffTitleLabel, gbcOutput);
		JLabel ouputBigTiffLabel = new JLabel("no");
		if (settings.isBigTiffSelected())
			ouputBigTiffLabel.setText("yes");
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(ouputBigTiffLabel, gbcOutput);

		JLabel ouputImgDataOrgTitleLabel = new JLabel("Image data organization:");
		ouputImgDataOrgTitleLabel.setFont(ouputImgDataOrgTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcOutput.weightx = 0;
		gbcOutput.gridx = 0;
		gbcOutput.gridy = 5;
		outputFilePanel.add(ouputImgDataOrgTitleLabel, gbcOutput);
		JLabel ouputImgDataOrgLabel = new JLabel((settings.isMultipageOMETiffSelected() ? "Multipage OME-TIFF" : "Singlepage OME-TIFF") + ", " + settings.getImgDataOrganizationAsString());
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(ouputImgDataOrgLabel, gbcOutput);

		JLabel ouputStripSizeTitleLabel = new JLabel();
		if (settings.getImgDataOrganization() == ConversionSettings.STRIPS)
			ouputStripSizeTitleLabel.setText("Strip size (w x h):");
		else if (settings.getImgDataOrganization() == ConversionSettings.TILES)
			ouputStripSizeTitleLabel.setText("Tile size (w x h):");
		ouputStripSizeTitleLabel.setFont(ouputStripSizeTitleLabel.getFont().deriveFont(Font.BOLD));
		gbcOutput.insets = new Insets(2, 10, 10, 10);
		gbcOutput.weightx = 0;
		gbcOutput.gridx = 0;
		gbcOutput.gridy = 6;
		outputFilePanel.add(ouputStripSizeTitleLabel, gbcOutput);
		JLabel ouputStripSizeLabel = new JLabel();
		if (settings.getImgDataOrganization() == ConversionSettings.STRIPS) {
			if (settings.isDownscalingSelected())
				ouputStripSizeLabel.setText(NumberFormat.getIntegerInstance().format(imgInfo.getStripWidth() / 2) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getStripHeight() / 2));
			else
				ouputStripSizeLabel.setText(NumberFormat.getIntegerInstance().format(imgInfo.getStripWidth()) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getStripHeight()));
		} else if (settings.getImgDataOrganization() == ConversionSettings.TILES) {
			ouputStripSizeLabel.setText(NumberFormat.getIntegerInstance().format(imgInfo.getTileWidth()) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getTileHeight()));
		}
		gbcOutput.weightx = 1;
		gbcOutput.gridx = 1;
		outputFilePanel.add(ouputStripSizeLabel, gbcOutput);
		gbcConversion.gridy = 2;
		conversionPanel.add(outputFilePanel, gbcConversion);

		// Create a progress message label which informs the user about the conversion progress
		progressMessageLabel = new JLabel(" ");
		progressMessageLabel.setFont(progressMessageLabel.getFont().deriveFont(Font.BOLD));
		progressMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gbcConversion.insets = new Insets(10, 25, 2, 25);
		gbcConversion.anchor = GridBagConstraints.CENTER;
		gbcConversion.gridy = 3;
		conversionPanel.add(progressMessageLabel, gbcConversion);

		// Create a progress bar which informs the user about the conversion progress
		progressBar = new JProgressBar();
		progressBar.setString("0 %");
		progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(conversionPanel.getWidth(), 30));
		gbcConversion.insets = new Insets(2, 25, 10, 25);
		gbcConversion.gridy = 4;
		conversionPanel.add(progressBar, gbcConversion);

		// Create a button to cancel the conversion
		JButton cancelConversion = new JButton("Cancel conversion");
		cancelConversion.setIcon(new ImageIcon(getClass().getResource(Constants.CANCEL_IMAGE)));
		cancelConversion.setPreferredSize(new Dimension(200, 35));
		cancelConversion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Cancel SwingWorker conversion thread
				converterWorker.cancel(false);
			}
		});
		gbcConversion.gridy = 5;
		gbcConversion.fill = GridBagConstraints.NONE;
		conversionPanel.add(cancelConversion, gbcConversion);

		convertingDialog.add(BorderLayout.CENTER, conversionPanel);
		convertingDialog.setSize(680, 490);
		convertingDialog.setLocationRelativeTo(this);
		convertingDialog.setUndecorated(true);
	}

	/**
	 * Catches all action events from the menu buttons and shows the selected
	 * card.
	 * 
	 * @param e
	 *            the {@link ActionEvent} which caused the call of this method
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("convert")) {
			// Show the convert card
			cardLayout.show(cards, "Convert Card");
		} else if (e.getActionCommand().equals("settings")) {
			// Show the settings card
			cardLayout.show(cards, "Settings Card");
		} else if (e.getActionCommand().equals("help")) {
			// Show the help card
			cardLayout.show(cards, "Help Card");
		}
	}

	/**
	 * Opens the NDPI file chooser, analyzes the selected NDPI file and displays
	 * information about the NDPI file.
	 */
	private void openNDPIFile() {
		// Open the file chooser to select and read a NDPI-file
		int returnVal = ndpiFileChooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			ndpiFile = ndpiFileChooser.getSelectedFile();
			LOGGER.info("Selected NDPI file: " + ndpiFile.getName());
			startConversionButton.setEnabled(false);

			// Display filename and file size
			filenameLabel.setText(ndpiFile.getName());
			double fileSize = (double) ndpiFile.length() / (1024 * 1024);
			filesizeLabel.setText((new DecimalFormat(".##")).format(fileSize) + " MB");

			// Clear all information about previously opened NDPI file
			displayNDPIFileInfo("-", "-", "-", "-", "-");
			clearNDPIContent();
			clearNDPIThumbnails();

			// Analyze the NDPI file (get information about containing images)
			imgInfos = new ArrayList<ImageInfo>();
			ImageInfoRetriever worker = new ImageInfoRetriever(this, ndpiFile, imgInfos);
			worker.execute();
			this.getRootPane().getGlassPane().setVisible(true);
			readingDialog.setVisible(true);

			// Create thumbnails (image, maco and map) of the NDPI file
			createNDPIThumbnails();

			// Show image thumbnail of the NDPI file
			displayNDPIThumbnail();

			// Show information about images contained in the NDPI file
			displayNDPIContent();
		}
	}

	/**
	 * Opens the directory chooser to specify the output directory for exported
	 * and converted images.
	 */
	private void specifyOutputDirectory() {
		// Open the directory chooser to set the output directory
		int returnVal = outputDirectoryChooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			outputDirButtonChoose.setSelected(true);
			settings.setStoreInSubdirectorySelected(false);
			settings.setOutputDirectory(outputDirectoryChooser.getSelectedFile().getAbsolutePath());
			outputDirLabel.setText(outputDirectoryChooser.getSelectedFile().getAbsolutePath());
		}
	}

	/**
	 * Processes the selected image by either converting or exporting the
	 * selected image.
	 * 
	 * @param convertImage
	 *            whether selected image should be converted ({@code true}), or
	 *            whether selected image should be exported ({@code false})
	 */
	private void processSelectedImage(boolean convertImage) {
		// Get the image information object of the selected image
		ImageInfo imgInfo = imgInfos.get(selectedImageIndex);

		if (imgInfo == null) {
			LOGGER.error("Can't process selected image: file information object not found");
			displayErrorMessage("File information not found", "<b>Can't process selected image</b><br>File information object not found");
		} else {

			if (convertImage) {
				// Check if output file might be larger than 4 GB
				BigInteger selectedImageWidth = new BigInteger(settings.isDownscalingSelected() ? Integer.toString(imgInfo.getDownscaledWidth(settings.getDownscalingFactor())) : Integer.toString(imgInfo.getWidth()));
				BigInteger selectedImageHeight = new BigInteger(settings.isDownscalingSelected() ? Integer.toString(imgInfo.getDownscaledHeight(settings.getDownscalingFactor())) : Integer.toString(imgInfo.getHeight()));
				BigInteger fileSizeEstimation = selectedImageWidth.multiply(selectedImageHeight);
				// Multiply by 3 because there are 3 color channels (RBG)
				fileSizeEstimation = fileSizeEstimation.multiply(new BigInteger("3"));
				if (fileSizeEstimation.compareTo(STANDARD_TIFF_MAX_BYTES) == 1 && !settings.isBigTiffSelected()) {
					LOGGER.warn("Output file size might be larger than 4 GB (when uncompressed)");
					int reply = JOptionPane.showConfirmDialog(this, "The output file for the selected image might be larger than 4 GB (when uncompressed).\n"
							+ "Therefore, the BigTIFF file format should be used.\n\nDo you want to use BigTIFF for this image?", "Output file size > 4 GB", JOptionPane.YES_NO_OPTION);

					if (reply == JOptionPane.YES_OPTION) {
						// Switch TIFF file format to BigTIFF
						settings.setBigTiffSelected(true);
						bigTiffButton.setSelected(true);
						LOGGER.info("Set TIFF file format to BigTIFF");
					}
				}
			}

			// Check if selected NDPI file exists
			if (!ndpiFile.exists()) {
				LOGGER.error("The selected NDPI file '" + ndpiFile.getName() + "' could not be found");
				displayErrorMessage("NDPI file not found", "<b>The selected NDPI file could not be found</b><br>" + ndpiFile.getName());
				return;
			}

			// Check if image should be stored in a subdirectory of the selected NDPI file
			if (settings.isStoreInSubdirectorySelected()) {
				String absPath = ndpiFile.getAbsolutePath();
				settings.setOutputDirectory(absPath.substring(0, absPath.lastIndexOf(File.separator)), convertImage);
			}

			// Check if output directory exists
			if (!new File(settings.getOutputDirectory()).exists()) {
				try {
					if ((new File(settings.getOutputDirectory())).mkdir()) {
						LOGGER.info("Output directory sucessfully created:");
						LOGGER.info(settings.getOutputDirectory());
					} else {
						LOGGER.error("Output directory could not be created");
						displayErrorMessage("Failed to create output directory", "<b>The output directory could not be created</b><br>" + settings.getOutputDirectory());
						return;
					}
				} catch (Exception e) {
					LOGGER.error("Failed to create output directory:", e);
					displayErrorMessage("Failed to create output directory", "<b>The output directory could not be created</b><br>" + settings.getOutputDirectory() + "<br>Error message: " + e.toString());
					return;
				}
			}

			// Check if output file already exists
			String outputFilename = (convertImage ? imgInfo.getOMETiffFilename() : imgInfo.getExportFilename());
			File outFile = new File(settings.getOutputDirectory() + System.getProperty("file.separator") + outputFilename);
			if (outFile.exists()) {
				LOGGER.warn("Output file already exists:");
				LOGGER.warn(outputFilename);
				int reply = JOptionPane.showConfirmDialog(this, "The output file already exists:\n" + outputFilename + "\n\nShould it be overwritten?", "Output file already exists", JOptionPane.YES_NO_OPTION);

				// Check if file should be overwritten
				if (reply == JOptionPane.YES_OPTION) {
					try {
						outFile.delete();
						LOGGER.info("Output file '" + outputFilename + "' gets overwritten and was therefore deleted");
					} catch (Exception e) {
						LOGGER.error("Could not delete existing output file '" + outputFilename + "':", e);
						displayErrorMessage("Could not delete existing output file", "Could not delete existing output file '" + outputFilename + "'.\n" + e.toString());
						return;
					}
				} else {
					LOGGER.info("Output file '" + outputFilename + "' won't get overwritten");
					return;
				}
			}

			if (convertImage) {
				createConvertingDialog(imgInfo);

				// Create a new SwingWorker thread for converting the selected image to an OME-TIFF
				converterWorker = new ImageConverter(this, ndpiFile, imgInfo);
				converterWorker.addPropertyChangeListener(this);
				converterWorker.execute();
				this.getRootPane().getGlassPane().setVisible(true);
				convertingDialog.setVisible(true);
			} else {
				// Create a new SwingWorker thread for exporting the selected image
				ImageExporter exporterWorker = new ImageExporter(this, ndpiFile, imgInfo);
				exporterWorker.execute();
				this.getRootPane().getGlassPane().setVisible(true);
				exportingDialog.setVisible(true);
			}

		}
	}

	/**
	 * Updates the currently selected image in the GUI. This is necessary
	 * because the text has to be updated if some output settings were changed
	 * (for example the downscaling option).
	 */
	private void updateSelectedImage() {
		for (int c = 0; c < imagesPanel.getComponentCount(); c++) {
			if (imagesPanel.getComponent(c).getBackground().equals(Constants.LIGHT_TURQUOISE_COLOR))
				displaySelectedImage(c);
		}
	}

	/**
	 * Displays the selected image with an image thumbnail, buttons to export
	 * and/or convert the selected image and further informations in the convert
	 * card.
	 * 
	 * @param imgIndex
	 *            the index of the image of the NDPI file to display
	 */
	private void displaySelectedImage(int imgIndex) {
		// Get image information object of the selected image
		ImageInfo info = imgInfos.get(imgIndex);
		selectedImageIndex = imgIndex;

		// Set up downscaling to create image thumbnail
		// (image should fit in the thumbnail panel)
		MultiStepRescaleOp resc = new MultiStepRescaleOp(DimensionConstrain.createMaxDimension(selectedImgThumbPanel.getWidth(), selectedImgThumbPanel.getHeight()), RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		BufferedImage thumbnail = null;
		// Check if image (in smallest resolution), macro or mask was found
		if (info.getCompressionScheme().equals("JPEG") && info.getImageType().equals("Image")) {
			if (imgThumbnail != null)
				thumbnail = resc.filter(imgThumbnail, null);
		} else if (info.getImageType().equals("Macro")) {
			if (macroThumbnail != null)
				thumbnail = resc.filter(macroThumbnail, null);
		} else if (info.getImageType().equals("Map")) {
			if (mapThumbnail != null)
				thumbnail = resc.filter(mapThumbnail, null);
		}

		// Display thumbnail image
		if (thumbnail != null)
			selectedImgThumbPanel.setImage(thumbnail);
		else
			selectedImgThumbPanel.setImage(Constants.THUMBNAIL_IMAGE);

		// Create short description of the output file (if exported)
		String exportTitleText;
		// If TIFF file format: change file format to 'BMP'
		// because map-images (only map-images are TIFF-images) can only be exported as 'BMP' images		
		exportTitleText = info.getCompressionScheme().equals("TIFF") ? "BMP" : info.getCompressionScheme();
		exportTitleLabel.setText(exportTitleText);
		String exportOutputText = "<html>";
		exportOutputText += NumberFormat.getIntegerInstance().format(info.getWidth()) + " x " + NumberFormat.getIntegerInstance().format(info.getHeight());
		exportOutputText += "<br>";
		exportOutputText += info.getFileSizeFormated();
		exportOutputText += "</html>";
		exportOutputLabel.setText(exportOutputText);

		startExportButton.setEnabled(true);
		startExportButton.setVisible(true);

		// Create short description of the output file (if converted to OME-TIFF)
		ometiffTitleLabel.setText("OME-TIFF");
		String ometiffOutputText = "<html>";
		if (info.isConvertible()) {
			startConversionButton.setEnabled(true);
			if (settings.isDownscalingSelected()) {
				ometiffOutputText += "downscaled<br>";
				ometiffOutputText += NumberFormat.getIntegerInstance().format(info.getDownscaledWidth(settings.getDownscalingFactor())) + " x " + NumberFormat.getIntegerInstance().format(info.getDownscaledHeight(settings.getDownscalingFactor()));
			} else {
				ometiffOutputText += NumberFormat.getIntegerInstance().format(info.getWidth()) + " x " + NumberFormat.getIntegerInstance().format(info.getHeight());
			}
			ometiffOutputText += "<br>";
			if (settings.getImgDataOrganization() == ConversionSettings.STRIPS)
				ometiffOutputText += "Strip size: ";
			else if (settings.getImgDataOrganization() == ConversionSettings.TILES)
				ometiffOutputText += "Tile size: ";

			if (settings.getImgDataOrganization() == ConversionSettings.STRIPS) {
				if (settings.isDownscalingSelected())
					ometiffOutputText += NumberFormat.getIntegerInstance().format(info.getDownscaledWidth(settings.getDownscalingFactor())) + " x " + NumberFormat.getIntegerInstance().format((info.getStripHeight() / 2));
				else
					ometiffOutputText += NumberFormat.getIntegerInstance().format(info.getStripWidth()) + " x " + NumberFormat.getIntegerInstance().format(info.getStripHeight());
			} else if (settings.getImgDataOrganization() == ConversionSettings.TILES) {
				ometiffOutputText += NumberFormat.getIntegerInstance().format(info.getTileWidth()) + " x " + NumberFormat.getIntegerInstance().format(info.getTileHeight());
			}
		} else {
			startConversionButton.setEnabled(false);
			ometiffOutputText += "this image can't be converted to an OME-TIFF";
		}
		ometiffOutputText += "</html>";
		ometiffOutputLabel.setText(ometiffOutputText);
		startConversionButton.setVisible(true);
	}

	/**
	 * Clears the displayed contents of the NDPI file in the GUI by removing all
	 * images from the table-like presentation and resetting the detailed view
	 * of a selected image. This is necessary if a new NDPI file gets opened.
	 */
	private void clearNDPIContent() {
		// Remove all images from the table-like presentation
		imagesPanel.removeAll();
		imagesPanel.revalidate();
		imagesPanel.repaint();

		// Reset the detailed view of a selected image
		selectedImgThumbPanel.setImage(Constants.THUMBNAIL_IMAGE);
		exportTitleLabel.setText("");
		exportOutputLabel.setText("");
		ometiffTitleLabel.setText("");
		ometiffOutputLabel.setText("");
		startExportButton.setEnabled(false);
		startExportButton.setVisible(false);
		startConversionButton.setEnabled(false);
		startConversionButton.setVisible(false);
	}

	/**
	 * Displays the content of the NDPI file in the GUI by presenting all images
	 * of the NDPI file in a table-like manner.
	 */
	private void displayNDPIContent() {
		clearNDPIContent();

		if (imgInfos != null) {
			imagesPanel.setLayout(new GridLayout(imgInfos.size(), 1));
			imagesPanel.setBorder(null);
			imagesPanel.setBackground(Color.WHITE);

			// Loop through all images of the NDPI file and add them to the images panel
			for (ImageInfo imgInfo : imgInfos) {
				JPanel imgInfoPanel = new JPanel(new GridBagLayout());
				imgInfoPanel.setBorder(new CustomRoundedBorder(10, Color.WHITE));
				imgInfoPanel.setBackground(Constants.LIGHT_GREY_COLOR);
				GridBagConstraints gbcImageInfo = new GridBagConstraints();
				gbcImageInfo.anchor = GridBagConstraints.WEST;
				gbcImageInfo.insets = new Insets(6, 10, 6, 2);
				gbcImageInfo.weightx = 1;
				gbcImageInfo.fill = GridBagConstraints.HORIZONTAL;
				imgInfoPanel.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						// Color background of the selected image panel and
						// display further information about the selected image
						JPanel infoPanel = (JPanel) me.getSource();
						if (!infoPanel.getBackground().equals(Constants.LIGHT_TURQUOISE_COLOR)) {
							infoPanel.setBackground(Constants.LIGHT_TURQUOISE_COLOR);
							for (int c = 0; c < imagesPanel.getComponentCount(); c++) {
								if (!imagesPanel.getComponent(c).equals(infoPanel))
									imagesPanel.getComponent(c).setBackground(Constants.LIGHT_GREY_COLOR);
								else
									displaySelectedImage(c);
							}
						}
					}

					public void mouseEntered(MouseEvent me) {
						// Add highlight to current image panel
						JPanel infoPanel = (JPanel) me.getSource();
						if (!infoPanel.getBackground().equals(Constants.LIGHT_TURQUOISE_COLOR))
							infoPanel.setBackground(Constants.MOUSE_OVER_COLOR);
					}

					public void mouseExited(MouseEvent me) {
						// Remove highlight from current image panel
						JPanel infoPanel = (JPanel) me.getSource();
						if (!infoPanel.getBackground().equals(Constants.LIGHT_TURQUOISE_COLOR))
							infoPanel.setBackground(Constants.LIGHT_GREY_COLOR);
					}
				});
				JLabel imageTitleLabel = new JLabel(imgInfo.getImageType());
				imageTitleLabel.setPreferredSize(new Dimension(60, 18));
				imgInfoPanel.add(imageTitleLabel, gbcImageInfo);
				JLabel imageSizeLabel = new JLabel(NumberFormat.getIntegerInstance().format(imgInfo.getWidth()) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getHeight()));
				imageSizeLabel.setPreferredSize(new Dimension(125, 18));
				gbcImageInfo.insets = new Insets(6, 2, 6, 2);
				gbcImageInfo.gridx = 1;
				imgInfoPanel.add(imageSizeLabel, gbcImageInfo);
				JLabel imageMagLabel = new JLabel(imgInfo.getMagnification());
				imageMagLabel.setPreferredSize(new Dimension(80, 18));
				gbcImageInfo.gridx = 2;
				imgInfoPanel.add(imageMagLabel, gbcImageInfo);
				JLabel imageCompressionLabel = new JLabel(imgInfo.getCompressionScheme());
				imageCompressionLabel.setPreferredSize(new Dimension(70, 18));
				gbcImageInfo.gridx = 3;
				imgInfoPanel.add(imageCompressionLabel, gbcImageInfo);
				JLabel imageFilesizeLabel = new JLabel(imgInfo.getFileSizeFormated());
				imageFilesizeLabel.setPreferredSize(new Dimension(100, 18));
				gbcImageInfo.insets = new Insets(6, 2, 6, 10);
				gbcImageInfo.gridx = 4;
				imgInfoPanel.add(imageFilesizeLabel, gbcImageInfo);

				imagesPanel.add(imgInfoPanel);
			}
		}
	}

	/**
	 * Updates the conversion progress bar by catching the
	 * {@link PropertyChangeEvent} from the conversion thread.
	 * 
	 * @param evt
	 *            the {@link PropertyChangeEvent} with the new value for the
	 *            progress bar
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress" == evt.getPropertyName()) {
			int progress = (Integer) evt.getNewValue();
			progressBar.setValue(progress);
			progressBar.setString(Integer.toString(progress) + " %");
		}
	}

	/**
	 * Disposes the reading dialog. This is done when analysis of the NDPI file
	 * is completed.
	 */
	public void disposeReadingDialog() {
		readingDialog.dispose();
		this.getRootPane().getGlassPane().setVisible(false);
	}

	/**
	 * Disposes the converting dialog. This is done when conversion of the
	 * selected image is completed.
	 */
	public void disposeConvertingDialog() {
		convertingDialog.dispose();
		this.getRootPane().getGlassPane().setVisible(false);
	}

	/**
	 * Disposes the exporting dialog. This is done when export of the selected
	 * image is completed.
	 */
	public void disposeExportingDialog() {
		exportingDialog.dispose();
		this.getRootPane().getGlassPane().setVisible(false);
	}

	/**
	 * Displays the NDPI file information in the GUI. This is done when analysis
	 * of the NDPI file is completed.
	 * 
	 * @param numberOfImages
	 *            the number of images in the NDPI file
	 * @param make
	 *            the make of the slide scanner
	 * @param model
	 *            the model of the slide scanner
	 * @param software
	 *            the name of the software used to create the NDPI file
	 * @param dateTime
	 *            the date and time of the scan
	 */
	public void displayNDPIFileInfo(String numberOfImages, String make, String model, String software, String dateTime) {
		numImagesLabel.setText(numberOfImages);
		makeLabel.setText(make);
		modelLabel.setText(model);
		softwareLabel.setText(software);
		datetimeLabel.setText(dateTime);
	}

	/**
	 * Clears all created thumbnail images of the NDPI file. Furthermore, the
	 * shown thumbnail image of the NDPI file in the GUI gets reseted.
	 */
	public void clearNDPIThumbnails() {
		imgThumbnail = null;
		macroThumbnail = null;
		mapThumbnail = null;
		imageThumbPanel.setImage(Constants.THUMBNAIL_IMAGE);
	}

	/**
	 * Creates the thumbnail images of the NDPI file to show them in the GUI if
	 * the corresponding image gets selected. Thumbnail images will be created
	 * for image types "Image", "Macro" and "Map".
	 */
	public void createNDPIThumbnails() {
		ImageInfo imgInfo = null;
		ImageInfo macroInfo = null;
		ImageInfo mapInfo = null;

		for (ImageInfo info : imgInfos) {
			// Check if image (in smallest resolution), macro or mask was found
			if (info.getCompressionScheme().equals("JPEG") && info.getImageType().equals("Image"))
				imgInfo = info;
			else if (info.getImageType().equals("Macro"))
				macroInfo = info;
			else if (info.getImageType().equals("Map"))
				mapInfo = info;
		}

		RandomAccessFile in = null;
		try {
			in = new RandomAccessFile(ndpiFile, "r");

			// Set up downscaling (image should fit in the thumbnail panel)
			MultiStepRescaleOp resc = new MultiStepRescaleOp(DimensionConstrain.createMaxDimension(imageThumbPanel.getWidth(), imageThumbPanel.getHeight()), RenderingHints.VALUE_INTERPOLATION_BICUBIC);

			byte[] image;
			BufferedImage buffImg;

			// Read and scale down the image (in smallest resolution)
			if (imgInfo != null) {
				image = new byte[(int) imgInfo.getFileSize()];
				in.seek(imgInfo.getOffsetImage());
				in.readFully(image);
				buffImg = ImageIO.read(new ByteArrayInputStream(image));
				imgThumbnail = resc.filter(buffImg, null);
			} else {
				LOGGER.error("Can't create image thumbnail: image information object not found");
				displayErrorMessage("Can't create image thumbnail", "Can't create image thumbnail: image information object not found");
			}

			// Read and scale down the macro (overall picture of the scanned slide)
			if (macroInfo != null) {
				image = new byte[(int) macroInfo.getFileSize()];
				in.seek(macroInfo.getOffsetImage());
				in.readFully(image);
				buffImg = ImageIO.read(new ByteArrayInputStream(image));
				macroThumbnail = resc.filter(buffImg, null);
			}

			// Read and scale down the map (might be the scanned area on the slide)	
			if (mapInfo != null) {
				image = new byte[(int) mapInfo.getFileSize()];
				in.seek(mapInfo.getOffsetImage());
				in.readFully(image);
				buffImg = new BufferedImage(mapInfo.getWidth(), mapInfo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
				buffImg.getRaster().setDataElements(0, 0, mapInfo.getWidth(), mapInfo.getHeight(), image);
				mapThumbnail = resc.filter(buffImg, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Can't create thumbnails of the NDPI-file. The images could not be processed:", e);
			displayErrorMessage("Can't create thumbnails", "Can't create thumbnails of the NDPI-file: The images could not be processed");
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Displays the thumbnail image of the NDPI file in the GUI.
	 */
	public void displayNDPIThumbnail() {
		if (imgThumbnail != null)
			imageThumbPanel.setImage(imgThumbnail);
	}

	/**
	 * Displays an error message with the given title and text.
	 * 
	 * @param messageTitle
	 *            the message title
	 * @param messageText
	 *            the message text
	 */
	public void displayErrorMessage(String messageTitle, String messageText) {
		JOptionPane.showMessageDialog(this, new JLabel("<html><body style='width:345px'>" + messageText + "</body></html>"), messageTitle, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Displays an info message with the given title and text.
	 * 
	 * @param messageTitle
	 *            the message title
	 * @param messageText
	 *            the message text
	 */
	public void displayInfoMessage(String messageTitle, String messageText) {
		JOptionPane.showMessageDialog(this, new JLabel("<html><body style='width:345px'>" + messageText + "</body></html>"), messageTitle, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Updates the progress message in the conversion dialog when a selected
	 * image gets converted to an OME-TIFF.
	 * 
	 * @param progressMsg
	 *            the progress message to display in the conversion dialog
	 */
	public void updateConversionProgress(String progressMsg) {
		progressMessageLabel.setText(progressMsg);
	}

	/**
	 * Application entry point for NDPI to OME-TIFF Convert with graphical user
	 * interface. Checks if update for installed Java is necessary and if Look
	 * and Feel can be set to Nimbus. If these preconditions are fulfilled, the
	 * GUI will be shown.
	 * 
	 * @param args
	 *            the command-line arguments (will be ignored)
	 */
	public static void main(String[] args) {
		// Check if installed Java Version is Version 6 Update 10 (1.6.0_10) or greater
		try {
			String version = System.getProperty("java.version");
			int major = Integer.parseInt(version.split("\\.")[0]);
			int minor = Integer.parseInt(version.split("\\.")[1]);
			int update = Integer.parseInt((version.split("\\.")[2]).split("\\_")[1]);
			if (major < 1 || (major == 1 && minor < 6) || (major == 1 && minor == 6 && update < 10)) {
				JOptionPane.showMessageDialog(null, "<html><b>NDPI to OME-TIFF Converter</b><br><span style='font-weight:normal'>Please update Java to Version 6 Update 10 or greater<br><br>Installed: Java Version " + minor + " Update " + update
						+ " (" + version + ")</span></html>", "Outdated Java Version", JOptionPane.WARNING_MESSAGE);
				System.exit(1);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "<html><b>NDPI to OME-TIFF Converter</b><br><span style='font-weight:normal'>Could not determine Java Version</span></html>", ">Could not determine Java Version", JOptionPane.WARNING_MESSAGE);
		}

		try {
			boolean nimbusFound = false;
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					nimbusFound = true;

					// Customize Nimbus Look and Feel
					LookAndFeel lnf = UIManager.getLookAndFeel().getClass().newInstance();
					UIDefaults uiDefaults = lnf.getDefaults();
					uiDefaults.put("control", new ColorUIResource(Constants.LIGHT_GREY_COLOR));
					uiDefaults.put("FileChooser.background", new ColorUIResource(Constants.LIGHT_GREY_COLOR));
					uiDefaults.put("nimbusFocus", new ColorUIResource(Constants.DARK_TURQUOISE_COLOR));
					uiDefaults.put("nimbusOrange", new ColorUIResource(Constants.DARK_GREEN_COLOR));
					uiDefaults.put("nimbusSelectionBackground", new ColorUIResource(Constants.DARK_TURQUOISE_COLOR));
					UIManager.getLookAndFeel().uninitialize();
					UIManager.setLookAndFeel(lnf);
					break;
				}
			}

			if (nimbusFound) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						createAndShowGUI();
					}
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Could not set Java Look and Feel to Nimbus.\nPlease update to Java Version 6 Update 10 or greater.", "NDPI to OME-TIFF Converter", JOptionPane.ERROR_MESSAGE);
		}
	}

}