package com.crypticmushroom.planetbound.world.gen;

import java.util.Random;

import com.crypticmushroom.planetbound.init.PBBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;

//TODO: Replace with new tree generator. Currently using basic tree generator
public class PBEmberwoodTreeGen extends WorldGenTrees {

	public PBEmberwoodTreeGen(boolean notify) {
		super(notify);
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int height = rand.nextInt(4) + 6;
		boolean allClear = true;

		if(pos.getY() >= 1 && pos.getY() + height + 1 <= 256) {
			int cy;
			byte width;
			int cz;
			Block blockID;

			for(cy = pos.getY(); cy <= pos.getY() + 1 + height; ++cy) {
				width = 1;

				if(cy == pos.getY()) {
					width = 0;
				}

				if(cy >= pos.getY() + 1 + height - 2) {
					width = 2;
				}

				for(int cx = pos.getX() - width; cx <= pos.getX() + width && allClear; ++cx) {
					for(cz = pos.getZ() - width; cz <= pos.getZ() + width && allClear; ++cz) {
						if(cy >= 0 && cy < 256) {
							BlockPos cPos = new BlockPos(cx, cy, cz);

							IBlockState block = world.getBlockState(cPos);
							blockID = block.getBlock();

							if(blockID != Blocks.AIR &&
									!blockID.isLeaves(block, world, cPos) &&
									blockID != Blocks.GRASS &&
									blockID != Blocks.DIRT &&
									blockID != PBBlocks.ronnian_grass &&
									blockID != PBBlocks.ronnian_dirt &&
									blockID != PBBlocks.ronnian_coarse_dirt &&
									!blockID.isWood(world, cPos)) {
								allClear = false;
							}
						}else {
							allClear = false;
						}
					}
				}
			}

			if(!allClear) {
				return false;
			}else {
				Block blockUsing = world.getBlockState(pos.down()).getBlock();

				if((blockUsing == PBBlocks.ronnian_grass || blockUsing == PBBlocks.ronnian_dirt
						|| blockUsing == PBBlocks.ronnian_coarse_dirt) && pos.getY() < 256 - height - 1) {
					setBlockAndNotifyAdequately(world, pos.down(), PBBlocks.ronnian_dirt.getDefaultState());
					width = 3;
					byte var18 = 0;
					int treeWidth;
					int tx;
					int var15;

					for(cz = pos.getY() - width + height; cz <= pos.getY() + height; ++cz) {
						int number = cz - (pos.getY() + height);
						treeWidth = var18 + 1 - number / 2;

						for(tx = pos.getX() - treeWidth; tx <= pos.getX() + treeWidth; ++tx) {
							var15 = tx - pos.getX();

							for(int tz = pos.getZ() - treeWidth; tz <= pos.getZ() + treeWidth; ++tz) {
								int var17 = tz - pos.getZ();

								BlockPos tPos = new BlockPos(tx, cz, tz);

								IBlockState state = world.getBlockState(tPos);

								if((Math.abs(var15) != treeWidth || Math.abs(var17) != treeWidth
										|| rand.nextInt(2) != 0 && number != 0) &&
										state.getBlock().canBeReplacedByLeaves(state, world, tPos)) {
									this.setBlockAndNotifyAdequately(world, tPos,
											PBBlocks.emberwood_leaves.getDefaultState());
								}
							}
						}
					}

					for(cz = 0; cz < height; ++cz) {
						BlockPos cPos = pos.up(cz);
						IBlockState block = world.getBlockState(cPos);
						blockID = block.getBlock();

						if(blockID == Blocks.AIR || blockID.isLeaves(block, world, cPos)) {
							this.setBlockAndNotifyAdequately(world, cPos, PBBlocks.emberwood.getDefaultState());
						}
					}

					return true;
				}else {
					return false;
				}
			}
		}else {
			return false;
		}
	}
}
