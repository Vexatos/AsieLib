package pl.asie.lib.tweak.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vexatos
 */
public class RayTracer {
	private static RayTracer instance = new RayTracer();

	public static RayTracer instance() {
		if(instance == null)
			instance = new RayTracer();
		return instance;
	}

	private Entity target = null;

	public void fire(EntityLivingBase entity, double distance) {
		if(entity.worldObj.isRemote) {
			return;
		}
		this.target = this.rayTrace(entity, distance, 0);
	}

	public Entity getTarget() {
		return this.target;
	}

	public Entity rayTrace(EntityLivingBase entity, double distance, float par3) {
		Entity target;
		final Vec3 position = entity.getPosition(par3);
		if(entity.getEyeHeight() != 0.12F) {
			position.yCoord += entity.getEyeHeight();
		}

		Vec3 look = entity.getLook(par3);

		for(double i = 1.0; i < distance; i += 0.2) {
			Vec3 search = position.addVector(look.xCoord * i, look.yCoord * i, look.zCoord * i);
			AxisAlignedBB searchBox = AxisAlignedBB.getBoundingBox(
				search.xCoord - 0.1, search.yCoord - 0.1, search.zCoord - 0.1,
				search.xCoord + 0.1, search.yCoord + 0.1, search.zCoord + 0.1);
			MovingObjectPosition blockCheck = entity.worldObj.rayTraceBlocks(
				Vec3.createVectorHelper(position.xCoord, position.yCoord, position.zCoord), search, false);
			if(blockCheck != null && blockCheck.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				double d1 = position.distanceTo(blockCheck.hitVec);
				double d2 = position.distanceTo(search);
				if(position.distanceTo(blockCheck.hitVec)
					< position.distanceTo(search)) {
					return null;
				}
			}

			target = getArthropod(entity, position, search, look, searchBox, 0.1);
			if(target != null) {
				return target;
			}
		}
		return null;
	}

	private Entity getArthropod(EntityLivingBase base, Vec3 position, Vec3 search, Vec3 look, AxisAlignedBB searchBox, double v) {
		ArrayList<Entity> entityList = new ArrayList<Entity>();
		List entityObjects = base.worldObj.getEntitiesWithinAABB(Entity.class, searchBox);
		for(Object o : entityObjects) {
			if(o instanceof Entity && o != base && ((Entity) o).canBeCollidedWith()) {
				entityList.add(((Entity) o));
			}
		}
		if(entityList.size() <= 0) {
			return null;
		}
		Entity entity = null;
		if(entityList.size() > 1) {
			for(Entity e : entityList) {
				if(entity == null || position.distanceTo(Vec3.createVectorHelper(e.posX, e.posY, e.posZ))
					< position.distanceTo(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ))) {
					entity = e;
				}
			}
			/*Vec3 newSearch = search.addVector(-v / 2.0 * look.xCoord, -v / 2.0 * look.yCoord, -v / 2.0 * look.zCoord);
			AxisAlignedBB newSearchBox = AxisAlignedBB.getBoundingBox(
				newSearch.xCoord - v / 2.0, newSearch.yCoord - v / 2.0, newSearch.zCoord - v / 2.0,
				newSearch.xCoord + v / 2.0, newSearch.yCoord + v / 2.0, newSearch.zCoord + v / 2.0);
			return getArthropod(world, newSearch, look, newSearchBox, v / 2.0);*/
		} else {
			entity = entityList.get(0);
		}
		return entity;
	}
}
