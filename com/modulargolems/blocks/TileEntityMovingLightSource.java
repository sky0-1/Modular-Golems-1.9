package com.modulargolems.blocks;

import java.util.List;

import com.modulargolems.entity.ModularGolem;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityMovingLightSource extends TileEntity implements ITickable
{    
	protected AxisAlignedBB aabb = Block.NULL_AABB;

	public TileEntityMovingLightSource() {}

	/**
	 * This controls whether the tile entity gets replaced whenever the block state 
	 * is changed. Normally only want this when block actually is replaced.
	 */
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return (oldState.getBlock() != newSate.getBlock());
	}

	@Override
	public void update()
	{
		List<ModularGolem> entityList = worldObj.getEntitiesWithinAABB(ModularGolem.class, this.getAABBToCheck(this.worldObj, this.getPos()));

		// if no golem was found, delete this tile entity and block
		if(entityList.isEmpty())
		{
			selfDestruct();
		}
		else // if golems WERE found, make sure they have a light level
		{
			int maxLight = 0;
			for(ModularGolem g : entityList)
			{
				int l = g.getLightLevel();
				if(l > maxLight) maxLight = l;
			}
			// if no light level was high enough, selfDestruct
			if(maxLight == 0)
			{
				selfDestruct();
			}
		}
	} 

	protected void selfDestruct()
	{
		this.getWorld().removeTileEntity(getPos());
		this.getWorld().setBlockToAir(getPos());
	}

	protected AxisAlignedBB getAABBToCheck(World worldIn, BlockPos pos)
	{
		if(this.aabb == Block.NULL_AABB)
		{
			this.aabb = new AxisAlignedBB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 1D, (double)pos.getY() + 1D, (double)pos.getZ() + 1D);
		}
		return this.aabb;	
	}
}
