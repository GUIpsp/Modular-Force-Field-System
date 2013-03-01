package mffs.common;

public enum ForceFieldType
{
	Camouflage(2), Default(1), Zapper(3), Area(1), Containment(1);
	
	int cost;

	private ForceFieldType(int costmodi)
	{
		this.cost = costmodi;
	}
}