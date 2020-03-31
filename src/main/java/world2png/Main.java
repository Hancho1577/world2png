package world2png;

import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.scheduler.AsyncTask;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends PluginBase {

	private int X;
	private int Y;
	private int Size;

	@Override
	public void onEnable() {
		saveDefaultConfig();
		X = getConfig().getInt("x_size");
		Y = getConfig().getInt("y_size");
		Size = getConfig().getInt("size");
		getServer().getScheduler().scheduleAsyncTask(this, new AsyncTask() {
			@Override
			public void onRun() {
				getLogger().info("\u00A7aGenerating world.png...");

				BufferedImage image;
				Position spawn = getServer().getDefaultLevel().getSpawnLocation();
				Level level = getServer().getDefaultLevel();
				int rX = 0;
				int rY = 0;

				image = new BufferedImage(X * Size, Y * Size, BufferedImage.TYPE_INT_RGB);
				Graphics2D graphics = image.createGraphics();

				for (int x = 0; x != X; x++) {
					for (int y = 0; y != Y; y++) {
						graphics.setColor(new Color(
								level.getMapColorAt(spawn.getFloorX() - (X / 2) + x, spawn.getFloorZ() - (Y / 2) + y)
										.getRGB()));

						graphics.fillRect(rX, rY, Size, Size);
						rY += Size;
					}
					rY = 0;
					rX += Size;
					if ((x * 100 / X) % 5 == 0)
						getLogger().info("\u00A7a" + (x * 100 / X) + "% done");
				}
				try {
					File file = new File(getDataFolder() + "/world.png");
					ImageIO.write(image, "png", file);
					getLogger().info("\u00A7aworld.png saved!");
				} catch (IOException e) {
				}
			}
		});
	}
}