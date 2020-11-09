package tramcity.client.ui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mysql.cj.xdevapi.JsonArray;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Random;


public class CityMap extends JPanel {


	Point[] points;
	JSONArray paths;

	private Random randomGenerator;
	Double os = 1.0;

	public CityMap(Point[] listPoint, Double offset, JSONArray listPath) {
		this.setBackground(Color.DARK_GRAY);
		randomGenerator = new Random();
		points =  listPoint;
		paths = listPath;
		os = offset;

	}
	/**
	 * @return a Color with random values for alpha, red, green, and blue values
	 */
	private Color getRandomColor() {
		float alpha = randomGenerator.nextFloat();
		float red = randomGenerator.nextFloat();
		float green = randomGenerator.nextFloat();
		float blue = randomGenerator.nextFloat();
		return new Color(red, green, blue, alpha);
	}


	public void paint(Graphics g) {
		try {



			Graphics2D g2 = (Graphics2D) g;
//			final BufferedImage imgBg = ImageIO.read( this.getClass().getResourceAsStream("/img/map.jpg"));
//			g2.drawImage(imgBg.getScaledInstance(600, 400,java.awt.Image.SCALE_SMOOTH), 0, 0, null);//(400, 300, 5, 5);
	//		g2.drawOval(5, 5, (int) (600*os) ,(int) ( 400*os));
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(60, 179, 113));
			g2.fillOval(0, 0, this.getWidth()-1,this.getHeight()-1);
			
			if (points != null){       
				for (int i = 0; i < points.length; i++) {
					try {
						final BufferedImage img =ImageIO.read( this.getClass().getResourceAsStream("/img/maker.png")); 
						g2.drawImage(img, (int) (points[i].x*os)-5, (int) (points[i].y*os)-12, null);//(400, 300, 5, 5);
					} catch (Exception e) {
						// TODO: handle exception
						g2.drawOval((int) (points[i].x*os), (int) (points[i].y*os), 5, 5);
					}
				}
			}
			if(paths != null) {
				for (int i = 0; i < paths.length(); i++) {
					try {
						JSONObject path = paths.getJSONObject(i);
						int line = (int) path.getInt("Line");
						switch (line) {
						case 1:

							g2.setColor( Color.yellow );
							break;
						case 2:

							g2.setColor( Color.blue );
							break;

						default:
							g2.setColor( Color.red );
							break;
						}
						// X Start, Y Start, X End, Y End
						g2.drawLine ( (int)(path.getInt("FromX")*os), (int) (path.getInt("FromY")*os), (int)(path.getInt("ToX")*os),(int) (path.getInt("ToY")*os ));
					}
					catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		//path
		//		Path2D.Float path = new Path2D.Float();
		//
		//		Point p = points[0];
		//		path.moveTo(p.getX(), p.getY());
		//		for (int i = 1; i < points.length; i++) {
		//			p = points[i];
		//			path.lineTo(p.getX(), p.getY());
		//		}
		//		g2.draw(path);
		//		Color color = g2.getColor();
		//		Stroke stroke = g2.getStroke();
		//		g2.setColor(Color.BLACK);
		//		g2.setStroke(new BasicStroke(1));
		//		g2.draw(new Ellipse2D.Double(p.getX() - 4, p.getY() - 4, 8, 8));
		//		g2.setColor(color);
		//		g2.setStroke(stroke);

	}
}