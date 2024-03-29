package net.skygrind.skyblock.schematic;

import com.islesmc.modules.api.API;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.data.DataException;
import com.sk89q.worldedit.schematic.SchematicFormat;
import net.skygrind.skyblock.SkyBlock;
import org.bukkit.World;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by Matt on 2017-02-10.
 */
public class SchematicLoader {
    private File schematicDir;

    public SchematicLoader() {

    }

    public void pasteSchematic(String file, World world, int x, int y, int z) throws DataException, IOException, MaxChangedBlocksException {
        File schematic = new File(SkyBlock.getPlugin().getModuleDir() + "/schematics", file);
        if (schematic.exists()) {
            Vector origin = new Vector(x, y, z);


            BukkitWorld bukkitWorld = new BukkitWorld(world);
            EditSession editSession = new EditSession(bukkitWorld, 100000);
            CuboidClipboard clipboard = SchematicFormat.MCEDIT.load(schematic);

            System.out.println(y);

            clipboard.paste(editSession, BukkitUtil.toVector(origin), true);
            return;
        }
        API.getPlugin().getLogger().log(Level.SEVERE, "Schematic {0} does not exist", file);
    }
}
