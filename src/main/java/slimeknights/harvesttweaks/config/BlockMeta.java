package slimeknights.harvesttweaks.config;

import com.google.common.reflect.TypeToken;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

import slimeknights.mantle.configurate.ConfigurationNode;
import slimeknights.mantle.configurate.objectmapping.ObjectMappingException;
import slimeknights.mantle.configurate.objectmapping.serialize.TypeSerializer;

public class BlockMeta {

  public static final TypeSerializer<BlockMeta> SERIALIZER = new TypeSerializer<BlockMeta>() {
    @Override
    public BlockMeta deserialize(TypeToken<?> typeToken, ConfigurationNode configurationNode)
        throws ObjectMappingException {
      String val = configurationNode.getString();
      String[] parts = val.split(":");

      Block block = Block.REGISTRY.getObject(new ResourceLocation(parts[0], parts[1]));
      if(block == Blocks.AIR) {
        throw new ObjectMappingException("Block not found: " + val);
      }
      int meta = -1;
      if(parts.length > 2) {
        meta = Integer.valueOf(parts[2]);
      }

      return new BlockMeta(block, meta);
    }

    @Override
    public void serialize(TypeToken<?> typeToken, BlockMeta blockMeta, ConfigurationNode configurationNode)
        throws ObjectMappingException {
      String val = blockMeta.block.getRegistryName().toString();
      if(blockMeta.metadata > -1) {
        val += ":" + blockMeta.metadata;
      }
      configurationNode.setValue(val);
    }
  };

  public Block block;
  public int metadata;

  public BlockMeta() {
  }

  public BlockMeta(Block block, int metadata) {
    this.block = block;
    this.metadata = metadata;
  }

  public static BlockMeta of(IBlockState state) {
    return new BlockMeta(state.getBlock(), state.getBlock().getMetaFromState(state));
  }


  @Override
  public boolean equals(Object o) {
    if(this == o) {
      return true;
    }
    if(o == null || getClass() != o.getClass()) {
      return false;
    }

    BlockMeta blockMeta = (BlockMeta) o;

    if(metadata != blockMeta.metadata) {
      return false;
    }
    return block != null ? block.equals(blockMeta.block) : blockMeta.block == null;

  }

  @Override
  public int hashCode() {
    int result = block != null ? block.hashCode() : 0;
    result = 31 * result + metadata;
    return result;
  }

}
