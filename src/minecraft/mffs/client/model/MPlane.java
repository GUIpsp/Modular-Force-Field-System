package mffs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class MPlane extends ModelBase
{
	public static final MPlane INSTNACE = new MPlane();
	private ModelRenderer cube;

	public MPlane()
	{
		this.cube = new ModelRenderer(this, 0, 0);
		int size = 16;
		this.cube.addBox(-size / 8, -size / 2, -size / 2, size / 6, size, size);
		this.cube.setTextureSize(112, 70);
		this.cube.mirror = true;
	}

	public void render()
	{
		float f = 0.0625f;
		this.cube.render(f);
	}
}
