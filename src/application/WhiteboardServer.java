package application;

import interfaces.ClientCallbackInterface;
import interfaces.ServerConstants;
import interfaces.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Main server application, handle client requests and pass messages to other clients.
 * @author Yang Zhang (Lucas)
 *
 */

public class WhiteboardServer implements ServerInterface
{
	ArrayList <ClientCallbackInterface> clients = new ArrayList<ClientCallbackInterface>();
	List<String> list = new ArrayList<String>();
	//ObservableList<String> clientList = FXCollections.observableList(list);
	
	public static void main(String[] args)
	{
		if (System.getSecurityManager() == null) {
            SecurityManager securityManager = new SecurityManager();
                        
            System.setProperty("java.security.policy", "rmi.policy");

			System.setSecurityManager(securityManager);
        }
        try {
        	ServerInterface proxy = new WhiteboardServer();
        	ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(proxy, 0);
            
            // create a new RMI Registry and bind to user port 50000
            LocateRegistry.createRegistry(50000);
            
            // get a reference to the registry
            Registry registry = LocateRegistry.getRegistry(50000);           
            
            registry.rebind(ServerConstants.name, stub);
            System.out.println(ServerConstants.name+" bound");
        } catch (Exception e) {
            System.err.println(ServerConstants.name+" exception:");
            e.printStackTrace();
        }
	}

	@Override
	public void send(ClientCallbackInterface currentClient, String msg) throws RemoteException {
		System.err.println(currentClient.getName() + " : " + msg);
		
		for(ClientCallbackInterface client: clients)
		{
			if (!client.equals(currentClient)) {
				client.serverCallback(currentClient.getName() + " : " + msg);
			}
		}
	}
	
	@Override
	public void register(ClientCallbackInterface currentClient) throws RemoteException {
		System.err.println("Client " + currentClient.getName()
				+ " logging on.");
		clients.add(currentClient);
		//System.out.println(currentClient.getName());
		list.add(currentClient.getName());

		for (ClientCallbackInterface client : clients) {
			client.updateClientList(list);
			if (client.getName().equals(currentClient.getName())) {
				client.serverCallback("Welcome login : "+ currentClient.getName());
			}
			else{
				client.serverCallback(currentClient.getName() + " enters the room.");
			}
		}
	}
	
	@Override
	public void sendPrivate(ClientCallbackInterface currentClient, String selectedUser, String msg) throws RemoteException {
		for(ClientCallbackInterface client: clients)
		{
			if (client.getName().equals(selectedUser)) {
				client.serverCallback(currentClient.getName() + " : " + msg);
			}
		}
		
	}

	@Override
	public void unregisterClientCallback(ClientCallbackInterface clientCallback) throws RemoteException {
		System.err.println("Client " + clientCallback.getName()
				+ " logging off.");
		
		list.remove(clientCallback.getName());

		for (ClientCallbackInterface client : clients) {
			client.updateClientList(list);
			if (client.getName().equals(clientCallback.getName())) {
				client.serverCallback("Welcome login : "+ clientCallback.getName());
			}
			else
				client.serverCallback(clientCallback.getName() + " enters the room.");
		}
		clients.remove(clientCallback);
		
	}

	@Override
	public void sendDrawingCommand(ClientCallbackInterface currentClient,
			String shape, double x, double y) throws RemoteException {
		// TODO Auto-generated method stub
		System.err.println("Client " + currentClient.getName()
				+ " ("+ shape + "," + x + "," + y + ")");
		for(ClientCallbackInterface client: clients)
		{
			if (!client.equals(currentClient)) {
				client.drawShape(shape, x, y);
			}
		}
		
	}

	@Override
	public void sendDrawingColor(ClientCallbackInterface currentClient,
			int color) throws RemoteException {
		// TODO Auto-generated method stub
		System.err.println("Client " + currentClient.getName()
				+ " " + color);
		for(ClientCallbackInterface client: clients)
		{
			if (!client.equals(currentClient)) {
				client.setColor(color);
			}
		}
	}

	@Override
	public void sendDrawingCommand(ClientCallbackInterface clientCallback,
			String string, double xCor, double yCor, double finalX,
			double finalY) throws RemoteException {
		// TODO Auto-generated method stub
		for(ClientCallbackInterface client: clients)
		{
			if (!client.equals(clientCallback)) {
				client.drawShape(string, xCor, yCor, finalX, finalY);
			}
		}
		
	}

	@Override
	public void sendClearCommand(ClientCallbackInterface clientCallback)
			throws RemoteException {
		// TODO Auto-generated method stub
		for(ClientCallbackInterface client: clients)
		{
			if (!client.equals(clientCallback)) {
				client.clearCanvas();
			}
		}
	}

	

}
