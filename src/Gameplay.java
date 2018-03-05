import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Map;

import javax.swing.Timer;

import javax.swing.JPanel;

public class Gameplay extends JPanel implements KeyListener, ActionListener{  //KeyListener for arrow keys, ActionListener for ball
	private boolean play = false;
	private int score = 0;
	private int totalBricks = 21;
	
	private Timer timer;
	
	//ball speed
	private int delay = 5;
	
	//x-axis starting position of paddle
	private int playerX = 310;
	
	//x and y axis of ball starting position
	private int ballX = 124;
	private int ballY = 350;
	
	//initial direction of ball
	private int ballXDir = -1;
	private int ballYDir = -2;
	
	private MapGenerator tiles;
	
	public Gameplay() {
		tiles = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	//to draw shapes
	public void paint(Graphics g) {
		//background
		g.setColor(Color.black);
		//rectangle for background
		g.fillRect(1, 1, 692, 592);
		
		//drawing tiles
		tiles.draw((Graphics2D)g);
		
		//score
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString(Integer.toString(score), 590, 30);
		
		//borders
		g.setColor(Color.yellow);
		//rectangles for border
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//paddle
		g.setColor(Color.green);
		g.fillRect(playerX, 550, 100, 8);
		
		//ball
		g.setColor(Color.yellow);
		g.fillOval(ballX, ballY, 20, 20);
		
		if(totalBricks<=0) {
			play = false;
			ballXDir = 0;
			ballYDir = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You Won!!", 280, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 260, 380);
		}
		
		if(ballY>570) {
			play = false;
			ballXDir = 0;
			ballYDir = 0;
			
			g.setColor(Color.red);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over!", 280, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Score: "+ score, 300, 340);
			
			g.setFont(new Font("serif", Font.BOLD, 20));
			g.drawString("Press Enter to Restart", 260, 380);
			
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) { //from ActionListener
		timer.start();
		
		//bounce off paddle
		if(play) {
			if(new Rectangle(ballX, ballY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYDir = - ballYDir;
			}
			
			A: for(int i=0; i<tiles.map.length; i++) {
				for(int j=0; j<tiles.map[0].length; j++) {
					if(tiles.map[i][j]>0) {
						int brickX = j*tiles.brickWidth+80;
						int brickY = i*tiles.brickHeight+50;
						int brickWidth = tiles.brickWidth;
						int brickHeight = tiles.brickHeight;
						
						//create rectangle around tiles
						Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						//create rectangle around ball
						Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);
						
						if(ballRect.intersects(brickRect)) {
							tiles.setBrickValue(0, i, j);
							totalBricks--;
							score+=5;
							
							if(ballX + 19 <=brickRect.x || ballX + 1 >=brickRect.x + brickRect.width) {
								ballXDir = -ballXDir;
							} else {
								ballYDir = -ballYDir;
							}
							
							break A;
						}
					}
				}
			}
			
			ballX+=ballXDir;
			ballY+=ballYDir;
			
			//bounce off left border
			if(ballX<0) {
				ballXDir = -ballXDir;
			}
			//bounce off top
			if(ballY<0) {
				ballYDir = -ballYDir;
			}
			//bounce off right border
			if(ballX>670) {
				ballXDir = -ballXDir;
			}
		}
		
		repaint(); //draw everything again
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//if right arrow key pressed
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}
			
		}
		//if left arrow key pressed
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballX = 120;
				ballY = 350;
				ballXDir = -1;
				ballYDir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				tiles = new MapGenerator(3, 7);
				
				repaint();
			}
		}
		
	}
	
	public void moveRight() {
		play = true;
		playerX+=10;
	}
	
	public void moveLeft() {
		play = true;
		playerX-=10;
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
