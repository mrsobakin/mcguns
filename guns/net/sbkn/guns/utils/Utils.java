package net.sbkn.guns.utils;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;



public class Utils
{
  public static boolean isRayBBIntersects(BoundingBox bb, Vector origin, Vector direction) {
    Vector dirfrac = new Vector(1.0D / direction.getX(), 1.0D / direction.getY(), 1.0D / direction.getZ());
    
    float t1 = (float)((bb.getMinX() - origin.getX()) * dirfrac.getX());
    float t2 = (float)((bb.getMaxX() - origin.getX()) * dirfrac.getX());
    float t3 = (float)((bb.getMinY() - origin.getY()) * dirfrac.getY());
    float t4 = (float)((bb.getMaxY() - origin.getY()) * dirfrac.getY());
    float t5 = (float)((bb.getMinZ() - origin.getZ()) * dirfrac.getZ());
    float t6 = (float)((bb.getMaxZ() - origin.getZ()) * dirfrac.getZ());
    
    float tmin = Math.max(Math.max(Math.min(t1, t2), Math.min(t3, t4)), Math.min(t5, t6));
    float tmax = Math.min(Math.min(Math.max(t1, t2), Math.max(t3, t4)), Math.max(t5, t6));
    
    if (tmax < 0.0F) {
      return false;
    }
    
    return (tmin <= tmax);
  }
  
  public static Vector loc2vec(Location loc) {
    return new Vector(loc.getX(), loc.getY(), loc.getZ());
  }
  
  public static Location vec2loc(World w, Vector v) {
    return new Location(w, v.getX(), v.getY(), v.getZ());
  }
  
  public static int log2(int N) {
    return (int)(Math.log(N) / Math.log(2.0D));
  }
  
  public static Vector reflectAlongNormal(Vector v, Vector n) {
    return v.subtract(n.multiply(v.dot(n) * 2.0D));
  }
}
