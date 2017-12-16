package fr.polytech.tp3.morpion.gui;

import fr.polytech.tp3.morpion.game.*;
import fr.polytech.tp3.morpion.game.exceptions.CellFullException;
import fr.polytech.tp3.morpion.game.matrix.Matrix;
import fr.polytech.tp3.morpion.game.matrix.MatrixListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Frame extends JFrame {
	
	private Game game = new Game();
	private Matrix<ArrangedButton> buttons = new Matrix<>(game.getGrid().getNbColumns(), game.getGrid().getNbRows(), new ArrangedButton());
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu m_file = new JMenu(System.getProperty("program.name"));
	private JMenuItem mi_exit = new JMenuItem("Exit");
	
	private JSplitPane sp_main = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private JPanel p_buttons = new JPanel();
	private JPanel p_info = new JPanel();
	private JPanel p_p1info = new JPanel();
	private JPanel p_p2info = new JPanel();
	
	private JTextField tf_p1name = new JTextField("p1");
	private JTextField tf_p2name = new JTextField("p2");
	private JLabel l_p1nbWin = new JLabel("0");
	private JLabel l_p2nbWin = new JLabel("0");
	private JLabel l_p1token = new JLabel();
	private JLabel l_p2token = new JLabel();
	private JLabel l_nbGame = new JLabel("0");
	
	private final String tokenString = "%s, it\'s your turn!";
	
	public Frame() {
		game.setListener(new GameListener() {
			@Override
			public void onStateChanged(EState oldState, EState newState) {
				switch (newState)
				{
					case INITIALIZING:
						break;
					case PLAYING:
						break;
					case PAUSING:
						break;
					case P1WON:
						win(game.getP1());
						break;
					case P2WON:
						win(game.getP2());
						break;
					case DRAW:
						draw();
						break;
				}
			}
			
			@Override
			public void onTokenChanged(ECell oldToken, ECell newToken) {
				switch (newToken)
				{
					case EMPTY:
						// If it empty, the program chooses the player.
						game.setToken(ECell.CROSS);
					case CROSS:
						l_p1token.setText(String.format(tokenString, game.getP1().getName()));
						l_p2token.setText("");
						break;
					case CIRCLE:
						l_p1token.setText("");
						l_p2token.setText(String.format(tokenString, game.getP2().getName()));
						break;
				}
			}
			
			@Override
			public void onNbGameChanged(int nbGame) {
				l_nbGame.setText(Integer.toString(nbGame));
			}
		});
		
		game.getGrid().setListener(new MatrixListener() {
			@Override
			public void OnCellChanged(int x, int y, Object value) {
				try {
					if (buttons.get(x, y) != null)
						buttons.get(x, y).setText(ECell.class.cast(value).toString());
				} catch (ClassCastException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		game.getP1().setPlayerListener(new PlayerListener() {
			@Override
			public void onNameChanged(String name) { }
			@Override
			public void onTypeChanged(ECell type) { }
			@Override
			public void onNbWinChanged(int nbWin) {
				System.out.println("P1> nbWin = " + nbWin);
				l_p1nbWin.setText(Integer.toString(nbWin));
			}
			@Override
			public void onNbGameChanged(int nbGame) { }
		});
		
		game.getP2().setPlayerListener(new PlayerListener() {
			@Override
			public void onNameChanged(String name) { }
			@Override
			public void onTypeChanged(ECell type) { }
			@Override
			public void onNbWinChanged(int nbWin) {
				System.out.println("P2> nbWin = " + nbWin);
				l_p2nbWin.setText(Integer.toString(nbWin));
			}
			@Override
			public void onNbGameChanged(int nbGame) { }
		});
		
		p_buttons.setLayout(new GridLayout(game.getGrid().getNbColumns(), game.getGrid().getNbRows(), 5, 5));
		
		for (int i = 0, imax = game.getGrid().getNbColumns(); i < imax; i++)
		{
			for (int j = 0, jmax = game.getGrid().getNbRows(); j < jmax; j++)
			{
				buttons.set(i, j, new ArrangedButton(ECell.EMPTY.toString(), i, j));
				buttons.get(i, j).addActionListener((ActionEvent e) -> {
					try {
						ArrangedButton source = ArrangedButton.class.cast(e.getSource());
						game.play(source.getCoordinates());
					} catch (ClassCastException ex) {
						ex.printStackTrace();
					} catch (CellFullException ignored) { }
				});
				p_buttons.add(buttons.get(i, j)).setLocation(i, j);
			}
		}
		
		m_file.add(mi_exit);
		
		menuBar.add(m_file);
		
		tf_p1name.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				warn();
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				warn();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				warn();
			}
			
			private void warn() {
				game.getP1().setName(tf_p1name.getText());
			}
		});
		
		tf_p2name.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				warn();
			}
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				warn();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				warn();
			}
			
			private void warn() {
				game.getP2().setName(tf_p2name.getText());
			}
		});
		
		p_p1info.setLayout(new GridLayout(4, 1, 5, 5));
		p_p1info.add(l_p1token);
		p_p1info.add(tf_p1name);
		p_p1info.add(new JLabel("Number of wins: "));
		p_p1info.add(l_p1nbWin);
		
		p_p2info.setLayout(new GridLayout(4, 1, 5, 5));
		p_p2info.add(l_p2token);
		p_p2info.add(tf_p2name);
		p_p2info.add(new JLabel("Number of wins: "));
		p_p2info.add(l_p2nbWin);
		
		p_info.setLayout(new GridLayout(2, 2, 5, 5));
		p_info.add(p_p1info);
		p_info.add(p_p2info);
		p_info.add(new JLabel("Number of games: "));
		p_info.add(l_nbGame);
		
		sp_main.add(p_buttons);
		sp_main.add(new JScrollPane(p_info));
		sp_main.setDividerLocation(300);
		
		// Setting up the initial conditions:
		game.getListener().onTokenChanged(game.getToken(), game.getToken());
		game.newGame();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.setTitle(System.getProperty("program.name"));
		this.setSize(640, 480);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setJMenuBar(menuBar);
		this.getContentPane().add(sp_main, BorderLayout.CENTER);
		
		SwingUtilities.updateComponentTreeUI(this);
		
		this.setVisible(true);
	}
	
	private void win(Player p) {
		JOptionPane.showMessageDialog(this, "Congratulation " + p.getName() + "!", "Congratualtion", JOptionPane.PLAIN_MESSAGE);
		game.newGame();
	}
	
	private void draw() {
		JOptionPane.showMessageDialog(this, "Draw!", "Draw", JOptionPane.PLAIN_MESSAGE);
		game.newGame();
	}
}
