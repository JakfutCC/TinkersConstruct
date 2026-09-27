package tconstruct.util;

import net.minecraft.world.World;

import com.cardinalstar.cubicchunks.world.ICubicWorld;
import com.cardinalstar.cubicchunks.world.cube.ICubeProvider;

import cpw.mods.fml.common.Loader;

public final class CubicPlacementBounds {

    private CubicPlacementBounds() {}

    public static boolean canPlace(World world, int x, int y, int z, boolean solid) {
        boolean vanillaResult = y != 255 || !solid;
        if (!Loader.isModLoaded("cubicchunks")) return vanillaResult;
        return CubicBounds.available(world, x, z, 0, y, (long) y + (solid ? 1 : 0), vanillaResult);
    }

    public static boolean canGrow(World world, int x, int y, int z, int height, boolean seekHeight) {
        boolean vanillaResult = y >= 1 && (long) y + height + 1 <= 256;
        if (seekHeight || !Loader.isModLoaded("cubicchunks")) return vanillaResult;
        return CubicBounds.available(world, x, z, 4, (long) y - 1, (long) y + height, vanillaResult);
    }

    private static final class CubicBounds {

        private static boolean available(World world, int x, int z, int radius, long minY, long maxY,
                boolean vanillaResult) {
            if (!(world.getChunkProvider() instanceof ICubeProvider)) return vanillaResult;
            ICubicWorld cubic = (ICubicWorld) world;
            if (minY < cubic.getMinHeight() || maxY >= cubic.getMaxHeight() || minY > maxY) return false;
            if ((long) x - radius < Integer.MIN_VALUE || (long) x + radius > Integer.MAX_VALUE
                    || (long) z - radius < Integer.MIN_VALUE
                    || (long) z + radius > Integer.MAX_VALUE)
                return false;
            return world.checkChunksExist(x - radius, (int) minY, z - radius, x + radius, (int) maxY, z + radius);
        }
    }
}
