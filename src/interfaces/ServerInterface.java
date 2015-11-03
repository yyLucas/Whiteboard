package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Server Interface
 * @author Yang Zhang (Lucas)
 *
 */
public interface ServerInterface extends Remote {
	public void send(ClientCallbackInterface clientCallback, String msg) throws RemoteException;
	public void unregisterClientCallback(ClientCallbackInterface clientCallback) throws RemoteException;
	public void register(ClientCallbackInterface currentClient) throws RemoteException;
	public void sendPrivate(ClientCallbackInterface clientCallback, String selectedUser, String text) throws RemoteException;
	public void sendDrawingCommand(ClientCallbackInterface currentClient, String shape, double x, double y) throws RemoteException;
	public void sendDrawingColor(ClientCallbackInterface currentClient, int color) throws RemoteException;
	public void sendDrawingCommand(ClientCallbackInterface clientCallback, String string, double xCor, double yCor, double finalX, double finalY) throws RemoteException;
	public void sendClearCommand(ClientCallbackInterface clientCallback) throws RemoteException;
}
