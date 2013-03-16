package mffs.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCube extends ModelBase
{
	public static final ModelCube INSTNACE = new ModelCube();
	private ModelRenderer cube;

	public ModelCube()
	{
		this.cube = new ModelRenderer(this, 0, 0);
		this.cube.addBox(-2F, -2F, -2F, 4, 4, 4);
		this.cube.setTextureSize(32, 32);
		this.cube.mirror = true;
	}

	public void render()
	{
		float f = 0.0625f;
		this.cube.render(f);
	}
}
