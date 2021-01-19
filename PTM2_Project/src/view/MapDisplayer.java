package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import command.ConnectCommand;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import server_side.MyClientHandler;
import server_side.MySerialServer;
//import command.ConnectCommand;
//import test.DataReaderServer;

public class MapDisplayer extends Canvas {
	double[][] mapHights;
	// int airPlaneCol, airPlaneRow;
	double cellDistance;
	double datumPointX, datumPointY;
	int mapRows = 0;
	int mapCols = 0;
	double maxHight;
	double mapWidth;
	double mapHeight;
	double cellWidth, cellHeight;
	double planePosX, planePosY;
	double destPointX, destPointY;
	String lst;
	static int numOfTimesEnterd = 0;
	Socket theServer = null;
	MySerialServer mss = null;
	File chosen;

	public void intiallizesetMapDisplayer() {
		mapHights = null;
		cellDistance = 0;
		datumPointX = 0;
		datumPointY = 0;
		mapRows = 0;
		mapCols = 0;
		maxHight = 0;
		mapWidth = 0;
		mapHeight = 0;
		cellWidth = 0;
		cellHeight = 0;
		planePosX = 0;
		planePosY = 0;
		destPointX = 0;
		destPointY = 0;
		lst = null;
	}

	public void drawPathOnMap() {
		GraphicsContext gc = getGraphicsContext2D();
		String[] moves = lst.split(",");
		double currPositionX = planePosX;
		double currPositionY = planePosY;
		for (int i = 0; i < moves.length - 1; i++) {
			// Image pathPhoto = new Image(new FileInputStream("./resources/circle.jpg"));
			Image pathPhoto = new Image(getClass().getResourceAsStream("/images/circle.jpg"));

			if (moves[i].equals("Up")) {
				currPositionY = currPositionY - 1;
				gc.drawImage(pathPhoto, currPositionX * cellWidth, currPositionY * cellHeight, cellWidth, cellHeight);
			}
			if (moves[i].equals("Down")) {
				currPositionY = currPositionY + 1;
				gc.drawImage(pathPhoto, currPositionX * cellWidth, currPositionY * cellHeight, cellWidth, cellHeight);
			}
			if (moves[i].equals("Left")) {
				currPositionX = currPositionX - 1;
				gc.drawImage(pathPhoto, currPositionX * cellWidth, currPositionY * cellHeight, cellWidth, cellHeight);
			}
			if (moves[i].equals("Right")) {
				currPositionX = currPositionX + 1;
				gc.drawImage(pathPhoto, currPositionX * cellWidth, currPositionY * cellHeight, cellWidth, cellHeight);
			}
		}
	}

	public void setMapDisplayer(File file) {
		try {
			chosen = file;
			ArrayList<String[]> mapLst = new ArrayList<String[]>();
			@SuppressWarnings("resource")
			Scanner CSVreader = new Scanner(new BufferedReader(new FileReader(file)));
			String[] datumPoints = CSVreader.nextLine().split(",");
			datumPointX = Double.parseDouble(datumPoints[0]);
			datumPointY = Double.parseDouble(datumPoints[1]);
			String[] cellDistanceStr = CSVreader.nextLine().split(",");
			cellDistance = Double.parseDouble(cellDistanceStr[0]);
			maxHight = 0;

			while (CSVreader.hasNextLine()) {
				String[] line = CSVreader.nextLine().split(",");
				mapLst.add(line);
				mapCols = line.length;
				mapRows++;
			}
			int count = 0;

			mapHights = new double[mapRows][mapCols];
			for (int i = 0; i < mapRows; i++) {
				String[] line = mapLst.get(i);
				for (int j = 0; j < mapCols; j++) {

					double value = Double.parseDouble(line[j]);
					mapHights[i][j] = value;
					count++;
					if (value > maxHight) {
						maxHight = value;
					}
				}
			}
			reDraw();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void drawPlane(double posX, double posY) {
		int corX = (int) (posX / cellWidth);
		int corY = (int) (posY / cellHeight) * -1;

		planePosX = corX;
		planePosY = corY;
		// Image img = new Image(new FileInputStream("./resources/a.png"));
		Image img = new Image(getClass().getResourceAsStream("/images/a.png"));

		GraphicsContext gc = getGraphicsContext2D();
		gc.drawImage(img, corX * cellWidth, corY * cellHeight, cellWidth, cellHeight); // draw plane
	}

	public void drawAirplaneMoves() {
		Timer myTimer = new Timer();
		myTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Double lat = ConnectCommand.getFromServer("get /position/latitude-deg");
				Double lon = ConnectCommand.getFromServer("get /position/longitude-deg");
				double x = (lon - datumPointX + cellDistance) / cellDistance;
				double y = ((lat - datumPointY + cellDistance) / cellDistance) * -1;
//				System.out.println("x: " + x + " y: " + y);
			}
		}, 1000, 250);
	}

	public void markDestByMouse(double posX, double posY) {
		int corX = (int) (posX / cellWidth);
		int corY = (int) (posY / cellHeight);
		this.destPointX = corX;
		this.destPointY = corY;
		try {
//			Image img = new Image(new FileInputStream("./resources/destination.jpg"));
			Image img = new Image(getClass().getResourceAsStream("/images/destination.jpg"));

			GraphicsContext gc = getGraphicsContext2D();
			gc.drawImage(img, corX * cellWidth, corY * cellHeight, cellWidth, cellHeight); // draw the dest
			if (numOfTimesEnterd >= 1) {
				if (theServer != null && mss != null) {
					theServer.close();
					mss.stopServer();
				}
				reDraw();

				mss = new MySerialServer(5403);
				mss.start(5403, new MyClientHandler(this));
				try {
					theServer = new Socket("127.0.0.1", 5403);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reDraw() {
		if (mapHights != null) {
			double red = 0;
			double green = 0;
			mapWidth = getWidth();
			mapHeight = getHeight();
			cellWidth = mapWidth / mapCols;
			cellHeight = mapHeight / mapRows;

			GraphicsContext gc = getGraphicsContext2D();

			for (int i = 0; i < mapRows; i++) {
				for (int j = 0; j < mapCols; j++) {
					if (mapHights[i][j] <= maxHight * 0.5) {
						red = 255;
						green = mapHights[i][j] * (255 / maxHight) * 2;
						if (green > 1.0) {
							green = 1.0;
						}
					} else {
						red = Math.abs(255 - ((mapHights[i][j] - (maxHight / 2)) * (255 / maxHight) * 2));
						green = 255;
					}
					gc.setFill(new Color(red / 255, green / 255, 0, 1));
					gc.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
				}
			}
			drawPlane(this.datumPointX, this.datumPointY);
		}
	}

	public double getPlanePosX() {
		return planePosX;
	}

	public void setPlanePosX(int planePosX) {
		this.planePosX = planePosX;
	}

	public double getPlanePosY() {
		return planePosY;
	}

	public void setPlanePosY(int planePosY) {
		this.planePosY = planePosY;
	}

	public double[][] getMapHights() {
		return mapHights;
	}

	public void setMapHights(double[][] mapHights) {
		this.mapHights = mapHights;
	}

	public double getDatumPointX() {
		return datumPointX;
	}

	public void setDatumPointX(double datumPointX) {
		this.datumPointX = datumPointX;
	}

	public double getDatumPointY() {
		return datumPointY;
	}

	public void setDatumPointY(double datumPointY) {
		this.datumPointY = datumPointY;
	}

	public double getDestPointX() {
		return destPointX;
	}

	public void setDestPointX(double destPointX) {
		this.destPointX = destPointX;
	}

	public double getDestPointY() {
		return destPointY;
	}

	public void setDestPointY(double destPointY) {
		this.destPointY = destPointY;
	}

	public String getLst() {
		return lst;
	}

	public void setLst(String ls) {
		this.lst = ls;
	}

	public int getMapRows() {
		return mapRows;
	}

	public void setMapRows(int mapRows) {
		this.mapRows = mapRows;
	}

	public int getMapCols() {
		return mapCols;
	}

	public void setMapCols(int mapCols) {
		this.mapCols = mapCols;
	}

}