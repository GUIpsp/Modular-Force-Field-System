package mffs.api;

import java.util.Set;

public interface IFortronCapacitor extends IFortronFrequency
{
	public Set<IFortronFrequency> getLinkedDevices();

	public int getTransmissionRange();

	public int getTransmissionRate();
}
