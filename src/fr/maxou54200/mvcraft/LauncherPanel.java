package fr.maxou54200.mvcraft;

import static fr.theshark34.swinger.Swinger.drawFullsizedImage;
import static fr.theshark34.swinger.Swinger.getTransparentWhite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import fr.theshark34.openauth.AuthenticationException;
import fr.theshark34.openlauncherlib.launcher.util.UsernameSaver;
import fr.theshark34.swinger.Swinger;
import fr.theshark34.swinger.colored.SColoredBar;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;

@SuppressWarnings("serial")
public class LauncherPanel extends JPanel implements SwingerEventListener{
	
	private Image background = Swinger.getResource("background.png");
	
	private UsernameSaver saver = new UsernameSaver(Launcher.MV_INFOS);
	
	private JTextField usernameField = new JTextField(saver.getUsername(""));
	private JPasswordField passwordField = new JPasswordField();
	
	private STexturedButton playButton = new STexturedButton(Swinger.getResource("Jouer_Normal.png"));
	
	private SColoredBar progressBar = new SColoredBar(getTransparentWhite(100), getTransparentWhite(175));
	private JLabel infoLabel = new JLabel("Clique sur Jouer !", SwingConstants.CENTER);
	
	public LauncherPanel() {
		this.setLayout(null);
		
		usernameField.setForeground(Color.BLACK);
		usernameField.setFont(usernameField.getFont().deriveFont(15F));
		usernameField.setCaretColor(Color.BLACK);
		usernameField.setOpaque(false);
		usernameField.setBorder(null);
		usernameField.setBounds(320, 219, 250, 20);
		this.add(usernameField);
		
		
		passwordField.setForeground(Color.BLACK);
		passwordField.setFont(usernameField.getFont().deriveFont(15F));
		passwordField.setCaretColor(Color.BLACK);
		passwordField.setOpaque(false);
		passwordField.setBorder(null);
		passwordField.setBounds(320, 263, 250, 20);
		this.add(passwordField);
		
		playButton.setBounds(293, 350);
		playButton.addEventListener(this);
		this.add(playButton);
		
		progressBar.setBounds(0, 460, 906, 15);
		this.add(progressBar);
		
		infoLabel.setForeground(Color.WHITE);
		infoLabel.setFont(usernameField.getFont());
		infoLabel.setBounds(0, 440, 906, 20);
		this.add(infoLabel);
		
	}
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawFullsizedImage(g, this, background);
	}
	@SuppressWarnings("deprecation")
	@Override
	public void onEvent(SwingerEvent e) {
		if(e.getSource() == playButton) {
			setFieldsEnabled(false);
			
			if(usernameField.getText().replaceAll(" ", "").length() == 0 || passwordField.getText().replaceAll(" ", "").length() == 0){
				JOptionPane.showMessageDialog(this, "Erreur: Veuillez entrer un pseudo/email et un mot de passe valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
				setFieldsEnabled(true);
				return;
			}
			
			Thread t = new Thread() {
				@SuppressWarnings("deprecation")
				@Override
				public void run() {
					try {
						Launcher.auth(usernameField.getText(), passwordField.getText());
					} catch (AuthenticationException e) {
						JOptionPane.showMessageDialog(LauncherPanel.this, "Erreur: Impossible de se connecter: " + e.getErrorModel().getErrorMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
						setFieldsEnabled(true);
						return;
					}
					
					saver.setUsername(usernameField.getText());
					
					try {
						Launcher.update();
					} catch (Exception e) {
						Launcher.interruptThread();  
						Launcher.getErrorUtil().catchError(e, "Impossible de mettre a jour MV Craft !");
					}
					
					try {
						Launcher.launch();
					} catch (IOException e) {
						Launcher.getErrorUtil().catchError(e, "Impossible de lancer MV Craft !");
					}
				}
			};
			t.start();
			
		}
		
	}
	
	private void setFieldsEnabled(boolean enabled) {
		usernameField.setEnabled(enabled);
		passwordField.setEnabled(enabled);
		playButton.setEnabled(enabled);
	}
	
	public void setInfoText(String text) {
		infoLabel.setText(text);
	}
	public SColoredBar getProgressBar() {
		return this.progressBar;
	}

}
