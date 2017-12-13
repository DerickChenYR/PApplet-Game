import processing.core.PApplet;
import java.util.Random;
import java.util.ArrayList;

//by Derick Chen

public class Game extends PApplet {

	float playWidth = 800;
	float playHeight = 800;
	
	ArrayList<Target> Targets = new ArrayList<Target>();
	
	private Target one;
	private Target two;
	private Target three;
	
	boolean completed = false;
	int displayScore = 0;
	int level = 1;
	int frameTimer = 0; //60 frames per second
	boolean start = false; //0 is paused.
	
	
	
	
	public void setup() {  //Initiator
		 size(Math.round(playWidth), Math.round(playHeight));    //Canvas Size
		 
		 one = new Target(playWidth, playHeight);
		 two = new Target(playWidth, playHeight);
		 three = new Target(playWidth, playHeight);
		 Targets.add(one);
		 Targets.add(two);
		 Targets.add(three);
		 smooth();
		 
		 System.out.println("Setup Complete!");
	}
	
	
	
	public void draw() { //Infinity loop
		
		if (start == false) {
			
			background(0);
			
			fill(255,255,255);
			textSize(20);
			text("Click to destroy all the targets.",200,300);
			text("Colored targets have different points.",200,340);
			text("Green Target - 4 points",200,370);
			text("Yellow Target - 6 points",200,400);
			text("Red Target - 9 points",200,430);
			text("3 Levels in total.",200,470);
			text("Score to Pass Level 1: 15 points in 10 sec",200,500);
			text("Score to Pass Level 2: 40 points with additional 11 sec",200,530);
			text("Score to Win: 80 points with additional 12 sec",200,560);
			text("Click to begin.",200,600);
			
			if (mousePressed) { //Listener for mouse clicks
				if (mouseButton == LEFT) {
					start = true;
					frameTimer = 600; //10 seconds for Level 1
				}
			}
		}
		
		else if (completed == false && start == true && frameTimer >= 0) {   // Switch to kill game and enter last screen
			
			// Keep time
			frameTimer -= 1;
			
			// Erase the background
			background(0);
		 
			for (int i = 0 ; i < Targets.size() ; i++) {
				Target thisTarget = Targets.get(i);
				
				if (thisTarget.checkAlive() != 0){
					thisTarget.moveTarget(); //Move targets
					thisTarget.changeColor(); //Change target colors
					drawTarget(Math.round(thisTarget.x), Math.round(thisTarget.y), thisTarget.targetColor[0],thisTarget.targetColor[1],thisTarget.targetColor[2]); //Show alive targets
					shootTarget(thisTarget); //Listen for target destructions
				}
		 
			}
			
			// Display text
			fill(255,255,255);
			//text("Click to destroy all the dots.",40,40);
			text("Level: " + level + "  " + "Score: " + displayScore,40,40);
			text("Time Left: " + (frameTimer / 60),40,70);
			
			
			//Level up to Lv2, 3 more targets
			if (displayScore >= 15 && level == 1) {
				level = 2;
				frameTimer += 660;
				Target four = new Target(playWidth, playHeight);
				Target five = new Target(playWidth, playHeight);
				Target six = new Target(playWidth, playHeight);
				Target seven = new Target(playWidth, playHeight);
				
				Targets.add(four);
				Targets.add(five);
				Targets.add(six);
				Targets.add(seven);
			}
			
			//Level up to Lv3, 3 more targets
			if (displayScore >= 40 && level == 2) {
				level = 3;
				frameTimer += 720;
				Target eight = new Target(playWidth, playHeight);
				Target nine = new Target(playWidth, playHeight);
				Target ten = new Target(playWidth, playHeight);
				Target eleven = new Target(playWidth, playHeight);
				Target twelve = new Target(playWidth, playHeight);
				
				Targets.add(eight);
				Targets.add(nine);
				Targets.add(ten);
				Targets.add(eleven);
				Targets.add(twelve);
			}
			
			
			//Win condition
			if (displayScore >= 80) {
				completed = true;
			}
		 }
		
		else if (completed == true) {
			text("Good Job! You Win!",250,300);
			text("Your Score: " + displayScore,250,330);
		}
		
		else if (completed == false && frameTimer < 0) {
			text("You lose! Good try.",250,300);
			text("Your Score: " + displayScore,250,330);
		}
	}
	
	public void drawTarget(int x, int y, int R, int G, int B) {
		fill(R,G,B);
		ellipse(x, y, 25, 25);
	}
	
	public void shootTarget(Target target) {
		
		int margin = 40;
		
		if (mousePressed) { //Listener for mouse clicks
			if (mouseButton == LEFT) {

				if ((Math.abs(this.mouseY - target.y) < margin) && (Math.abs(this.mouseX - target.x) < margin)) {
					target.killTarget();
					displayScore += target.score;
					System.out.println("HIT!");
					
				}
				
			}
		}
	}

}


class Target extends PApplet {
	
	static int[][] color = {{0,255,0},{255,255,0},{255,0,0}}; //Green Yellow Red

	int[] targetColor = new int[3];
	private int life = 1;
	int score = 1; //Increases as difficulty increases
	int refreshCounter = 0;
	float x;
	float y;
	float xSpeed;
	float ySpeed;
	float width; //Width of canvas
	float height; //Height of canvas
	String test = "Executed"; //Test statement
	
	public Target(float width, float height) {
		this.width = width;
		this.height = height;
		Random rand = new Random();
		
		//Randomise starting position
		this.x = rand.nextInt(Math.round(width - 0 + 1)) + 0;
		this.y = rand.nextInt(Math.round(height - 0 + 1)) + 0;
		
		//Randomise speeds
		this.xSpeed = rand.nextInt(Math.round(15 - 5 + 1)) + 5;
		this.ySpeed = rand.nextInt(Math.round(15 - 5 + 1)) + 5;
	}
	
	public int checkAlive() {
		return life;
	}
	
	public void killTarget() {
		this.life = 0;
	}
	
	public void changeColor () {
		
		int refreshLimit = 50;
		
		//Count frames
		this.refreshCounter += 1;
		
		//Refresh counter reset after a while, for colour designation
		if (this.refreshCounter == refreshLimit) {
			this.refreshCounter = 0;
			
			//Randomly pick a colour from set of colours
			Random randColor = new Random();
			int randomColorIndex = randColor.nextInt(2 - 0 + 1) + 0;
			
			switch (randomColorIndex) {
			case 0: this.score = 4; break;
			case 1: this.score = 6; break;
			case 2: this.score = 9; break;
			}
			
			//Set output colour RGB
			for (int i = 0; i < targetColor.length ; i++) {
				this.targetColor[i] = Target.color[randomColorIndex][i];
			}
		}
	}
	
	public void moveTarget() {
		// move the ball based on its speed
		this.x += this.xSpeed;

		if (this.x > this.width) {
			this.x = this.width - 10;
			this.xSpeed = -1 * this.xSpeed;
		}

		if (this.x < 0) {
			this.x = 10;
			this.xSpeed = abs(this.xSpeed);
		}

		// move the ball based on its speed
		this.y += this.ySpeed;

		if (this.y > this.height) {
			this.y = this.width - 5;
			this.ySpeed =  -1 * this.ySpeed;
		}

		if (this.y < 0) {
			this.y = 10;
			this.ySpeed = abs(this.ySpeed);
		}
		

	}
	

	

}




