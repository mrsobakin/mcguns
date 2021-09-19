package net.sbkn.customthings;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.MovingObjectPosition;
import net.minecraft.server.v1_14_R1.MovingObjectPositionBlock;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import org.bukkit.util.Vector;

public class GrenadeProjectile extends EntitySnowball {
  public GrenadeProjectile(World var0, EntityLiving var1, float force) {
    super(var0, var1);
    
    Vec3D v = var1.getLookDirection();
    
    setMot(v.x * force, v.y * force, v.z * force);
    
    Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)CustomThings.instance, () -> { if (isAlive()) detonate();  }100L);
  }



  
  protected void a(MovingObjectPosition var0) {
    if (var0.getType() != MovingObjectPosition.EnumMovingObjectType.ENTITY && 
      var0.getType() == MovingObjectPosition.EnumMovingObjectType.BLOCK) {
      MovingObjectPositionBlock positionBlock = (MovingObjectPositionBlock)var0;
      
      if (CraftBlock.at((GeneratorAccess)getWorld(), positionBlock.getBlockPosition()).isPassable())
        return; 
      Vector v = new Vector((getMot()).x, (getMot()).y, (getMot()).z);
      
      if (v.length() < 0.1D) {
        setNoGravity(true);
        setMot(new Vec3D(0.0D, 0.0D, 0.0D));
      } else {
        BlockFace hitFace = CraftBlock.notchToBlockFace(positionBlock.getDirection());
        
        Vector n = hitFace.getDirection();
        
        Vector r = v.subtract(n.multiply(v.dot(n) * 2.0D)).multiply(0.4F);
        
        this.impulse = true;
        this.velocityChanged = true;
        setMot(new Vec3D(r.getX(), r.getY(), r.getZ()));
        
        setPosition(this.lastX, this.lastY, this.lastZ);
      } 
    } 
  }

  
  public void detonate() {
    getWorld().createExplosion((Entity)this, this.locX, this.locY, this.locZ, 3.0F, false, Explosion.Effect.NONE);
    die();
  }
}
