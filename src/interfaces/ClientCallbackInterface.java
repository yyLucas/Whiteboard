package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Client Callback Interface
 * @author Yang Zhang (Lucas)
 *
 */
public interface ClientCallbackInterface extends Remote {
	// parameters need to be Serializable
	public void serverCallback(String msg) throws RemoteException;

	public String getName() throws RemoteException;

	public void updateClientList(List<String> listModel) throws RemoteException;
	
	public void drawShape(String command, double x, double y) throws RemoteException;
	
	public void setColor(int color) throws RemoteException;

	public void drawShape(String string, double xCor, double yCor, double finalX, double finalY) throws RemoteException;

	public void clearCanvas() throws RemoteException;
}
