package com.crypticmushroom.planetbound.world.gen;

import java.util.Random;

import com.crypticmushroom.planetbound.init.PBBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenCaves;

public class PBGenCavesRonne extends MapGenCaves
{
	protected void generateLargeCaveNode(long caveSeed, int centerX, int centerZ, ChunkPrimer blockStorage, double randX, double randY, double randZ)
	{
		this.generateCaveNode(caveSeed, centerX, centerZ, blockStorage, randX, randY, randZ, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5D);
	}

	protected void generateCaveNode(long caveSeed, int centerX, int centerZ, ChunkPrimer blockStorage, double randX, double randY, double randZ, float caveSize, float randPI, float angleToGenerate, int loopOne, int loopEnd, double yScale)
	{
		double offsetCenterX = centerX * 16 + 8;
		double offsetCenterZ = centerZ * 16 + 8;
		float var23 = 0.0F;
		float var24 = 0.0F;
		Random caveRNG = new Random(caveSeed);

		if(loopEnd <= 0)
		{
			int rangeInBlocks = this.range * 16 - 16;
			loopEnd = rangeInBlocks - caveRNG.nextInt(rangeInBlocks / 4);
		}

		boolean shouldStop = false;

		if(loopOne == -1)
		{
			loopOne = loopEnd / 2;
			shouldStop = true;
		}

		int var27 = caveRNG.nextInt(loopEnd / 2) + loopEnd / 4;

		for(boolean var28 = caveRNG.nextInt(6) == 0; loopOne < loopEnd; ++loopOne)
		{
			double sizeVar = 1.5D + MathHelper.sin(loopOne * (float) Math.PI / loopEnd) * caveSize * 1.0F;
			double scaledSize = sizeVar * yScale;
			float cosAngle = MathHelper.cos(angleToGenerate);
			float sinAngle = MathHelper.sin(angleToGenerate);
			randX += MathHelper.cos(randPI) * cosAngle;
			randY += sinAngle;
			randZ += MathHelper.sin(randPI) * cosAngle;

			if(var28)
			{
				angleToGenerate *= 0.92F;
			}
			else
			{
				angleToGenerate *= 0.7F;
			}

			angleToGenerate += var24 * 0.1F;
			randPI += var23 * 0.1F;
			var24 *= 0.9F;
			var23 *= 0.75F;
			var24 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 2.0F;
			var23 += (caveRNG.nextFloat() - caveRNG.nextFloat()) * caveRNG.nextFloat() * 4.0F;

			if(!shouldStop && loopOne == var27 && caveSize > 1.0F && loopEnd > 0)
			{
				this.generateCaveNode(caveRNG.nextLong(), centerX, centerZ, blockStorage, randX, randY, randZ, caveRNG.nextFloat() * 0.5F + 0.5F, randPI - ((float) Math.PI / 2F), angleToGenerate / 3.0F, loopOne, loopEnd, 1.0D);
				this.generateCaveNode(caveRNG.nextLong(), centerX, centerZ, blockStorage, randX, randY, randZ, caveRNG.nextFloat() * 0.5F + 0.5F, randPI + ((float) Math.PI / 2F), angleToGenerate / 3.0F, loopOne, loopEnd, 1.0D);
				return;
			}

			if(shouldStop || caveRNG.nextInt(4) != 0)
			{
				double distX = randX - offsetCenterX;
				double distZ = randZ - offsetCenterZ;
				double var39 = loopEnd - loopOne;
				double sizeSixteen = caveSize + 2.0F + 16.0F;

				if(distX * distX + distZ * distZ - var39 * var39 > sizeSixteen * sizeSixteen)
				{
					return;
				}

				if(randX >= offsetCenterX - 16.0D - sizeVar * 2.0D && randZ >= offsetCenterZ - 16.0D - sizeVar * 2.0D && randX <= offsetCenterX + 16.0D + sizeVar * 2.0D && randZ <= offsetCenterZ + 16.0D + sizeVar * 2.0D)
				{
					int minX = MathHelper.floor(randX - sizeVar) - centerX * 16 - 1;
					int maxX = MathHelper.floor(randX + sizeVar) - centerX * 16 + 1;
					int maxY = MathHelper.floor(randY - scaledSize) - 1;
					int minY = MathHelper.floor(randY + scaledSize) + 1;
					int minZ = MathHelper.floor(randZ - sizeVar) - centerZ * 16 - 1;
					int maxZ = MathHelper.floor(randZ + sizeVar) - centerZ * 16 + 1;

					if(minX < 0)
					{
						minX = 0;
					}

					if(maxX > 16)
					{
						maxX = 16;
					}

					if(maxY < 1)
					{
						maxY = 1;
					}

					if(minY > 120)
					{
						minY = 120;
					}

					if(minZ < 0)
					{
						minZ = 0;
					}

					if(maxZ > 16)
					{
						maxZ = 16;
					}

					boolean hasHitWater = false;
					int genX;
					int genZ;

					for(genX = minX; !hasHitWater && genX < maxX; ++genX)
					{
						for(genZ = minZ; !hasHitWater && genZ < maxZ; ++genZ)
						{
							for(int genY = minY + 1; !hasHitWater && genY >= maxY - 1; --genY)
							{

								if(genY >= 0 && genY < 128)
								{
									if(isOceanBlock(blockStorage, genX, genY, genZ))
									{
										hasHitWater = true;
									}

									if(genY != maxY - 1 && genX != minX && genX != maxX - 1 && genZ != minZ && genZ != maxZ - 1)
									{
										genY = maxY;
									}
								}
							}
						}
					}

					if(!hasHitWater)
					{
						for(genX = minX; genX < maxX; ++genX)
						{
							double var59 = (genX + centerX * 16 + 0.5D - randX) / sizeVar;

							for(genZ = minZ; genZ < maxZ; ++genZ)
							{
								double var46 = (genZ + centerZ * 16 + 0.5D - randZ) / sizeVar;
								boolean hitGrass = false;

								if(var59 * var59 + var46 * var46 < 1.0D)
								{
									for(int caveY = minY - 1; caveY >= maxY; --caveY)
									{
										double var51 = (caveY + 0.5D - randY) / scaledSize;

										if(var51 > -0.7D && var59 * var59 + var51 * var51 + var46 * var46 < 20.0D)
										{
											final IBlockState blockStateAt = blockStorage.getBlockState(genX, caveY, genZ);
											Block blockAt = blockStateAt.getBlock();
											if(blockAt == PBBlocks.ronnian_sand || blockAt == PBBlocks.ronnian_grass)
											{
												hitGrass = true;
											}

											if(blockAt != null && (blockAt == PBBlocks.ronnian_stone || blockStateAt.getMaterial() == Material.GROUND || blockStateAt.getMaterial() == Material.GRASS))
											{
												if(var59 * var59 + var51 * var51 + var46 * var46 < 0.85D)
												{
													final IBlockState state = (caveY < 10 ? Blocks.LAVA : Blocks.AIR).getDefaultState();
													blockStorage.setBlockState(genX, caveY, genZ, state);
												}

												if(hitGrass && blockStorage.getBlockState(genX, caveY - 1, genZ).getBlock() == PBBlocks.ronnian_dirt)
												{
													IBlockState blockState = this.world.getBiome(new BlockPos(genX + centerX * 16, 0, genZ + centerZ * 16)).topBlock;
													blockStorage.setBlockState(genX, caveY - 1, genZ, blockState);
												}
											}
										}
									}
								}
							}
						}

						if(shouldStop)
						{
							break;
						}
					}
				}
			}
		}
	}

	@Override
	protected void recursiveGenerate(World par1World, int genX, int genZ, int centerX, int centerZ, ChunkPrimer blockStorage)
	{
		int numberOfCaves = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);

		if(this.rand.nextInt(15) != 0)
		{
			numberOfCaves = 0;
		}

		for(int i = 0; i < numberOfCaves; ++i)
		{
			double randX = genX * 16 + this.rand.nextInt(16);
			double randY = this.rand.nextInt(this.rand.nextInt(120) + 8);
			double randZ = genZ * 16 + this.rand.nextInt(16);
			int numberOfNormalNodes = 1;

			if(this.rand.nextInt(4) == 0)
			{
				this.generateLargeCaveNode(this.rand.nextLong(), centerX, centerZ, blockStorage, randX, randY, randZ);
				numberOfNormalNodes += this.rand.nextInt(4);
			}

			for(int j = 0; j < numberOfNormalNodes; ++j)
			{
				float randPi = this.rand.nextFloat() * (float) Math.PI * 2.0F;
				float randEight = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
				float caveSize = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

				if(this.rand.nextInt(10) == 0)
				{
					caveSize *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
				}

				this.generateCaveNode(this.rand.nextLong(), centerX, centerZ, blockStorage, randX, randY, randZ, caveSize, randPi, randEight, 0, 0, 1.0D);
			}
		}
	}

	private boolean isOceanBlock(ChunkPrimer data, int x, int y, int z)
	{
		IBlockState state = data.getBlockState(x, y, z);
		return state.getBlock() == Blocks.WATER;
	}
}
