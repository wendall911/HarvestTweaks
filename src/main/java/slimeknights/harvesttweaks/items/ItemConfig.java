package slimeknights.harvesttweaks.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import slimeknights.harvesttweaks.config.ConfigFile;
import slimeknights.mantle.configurate.objectmapping.Setting;
import slimeknights.mantle.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ItemConfig extends ConfigFile {

  // key = Item, value = toolclass + level
  @Setting
  Map<String, Map<Item, Integer>> tools = new HashMap<>();

  public ItemConfig() {
    super("Tools");
  }

  @Override
  protected int getConfigVersion() {
    return 1;
  }

  @Override
  public void insertDefaults() {
    if(tools.isEmpty()) {
      for(Item item : Item.REGISTRY) {
        ItemStack stack = new ItemStack(item);
        Set<String> classes = item.getToolClasses(stack);
        classes.forEach(toolclass -> {
          try {
            tools.computeIfAbsent(toolclass, x -> new HashMap<>()).computeIfAbsent(item, s -> {
              setNeedsSaving();
              return item.getHarvestLevel(stack, toolclass, null, null);
            });
          } catch(Exception e) {
            // in case something happens in getHarvestLevel
          }
        });
      }
    }
  }
}
