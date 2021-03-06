package me.battleblast.animations;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import me.battleblast.BattleBlast;
/*
 * The documentation of TextureAtlas.findRegions() says:
 * ```
 * This method uses string comparison...so the result should be cached
 * rather than calling this method multiple times
 * ```
 * So this is why we use this caching class, instead of calling directly
 * `findRegions()` for the animations.
 */
public class AtlasRegionsCache {
    private static Array<TextureAtlas.AtlasRegion> smallBoomRegions;
    private static Array<TextureAtlas.AtlasRegion> tinyBoomRegions;
    private static Array<TextureAtlas.AtlasRegion> bigBoomRegions;

    public static Array<AtlasRegion> getSmallBoomRegions() {
        if (AtlasRegionsCache.smallBoomRegions == null) {
            AtlasRegionsCache.smallBoomRegions = BattleBlast.getAtlas().findRegions("boomSmall");
        }
        return AtlasRegionsCache.smallBoomRegions;
    }

    public static Array<AtlasRegion> getTinyBoomRegions() {
        if (AtlasRegionsCache.tinyBoomRegions == null) {
            AtlasRegionsCache.tinyBoomRegions = BattleBlast.getAtlas().findRegions("boomTiny");
        }
        return AtlasRegionsCache.tinyBoomRegions;
    }

    public static Array<AtlasRegion> getBigBoomRegions() {
        if (AtlasRegionsCache.bigBoomRegions == null) {
            AtlasRegionsCache.bigBoomRegions = BattleBlast.getAtlas().findRegions("boomBig");
        }
        return AtlasRegionsCache.bigBoomRegions;
    }
}
