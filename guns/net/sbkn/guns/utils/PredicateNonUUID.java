package net.sbkn.guns.utils;

import java.util.UUID;
import java.util.function.Predicate;
import org.bukkit.entity.Entity;

public class PredicateNonUUID
  implements Predicate<Entity> {
  final UUID uuid;
  
  public PredicateNonUUID(UUID uuid) {
    this.uuid = uuid;
  }
  
  public boolean test(Entity entity) {
    return (entity.getUniqueId() != this.uuid);
  }
}
