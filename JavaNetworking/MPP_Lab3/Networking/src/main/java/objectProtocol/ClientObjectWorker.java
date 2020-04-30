package objectProtocol;

import domain.Excursie;
import domain.User;
import objectProtocol.*;
import observer.applicationObserver;
import persistance.PersistanceException;
import service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientObjectWorker implements Runnable, applicationObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService serv,Socket con) {
        server = serv;
        connection = con;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try{
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if(response != null){
                    sendResponse((Response) response);
                }
            }catch (IOException | ClassNotFoundException | PersistanceException exc){
                exc.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        try{
            input.close();
            output.close();
            connection.close();
        } catch (IOException exc){
            System.out.println("error "+exc);
        }
    }

    @Override
    public void updateTrips(List<Excursie> arrayList) throws PersistanceException {
        try {
            sendResponse(new updateGetAllExcursii(arrayList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Response handleRequest(Request request) throws PersistanceException {
        Response response = null;
        if(request instanceof loginRequest){
            System.out.println("Login request");
            loginRequest request1 = (loginRequest) request;
            User user = request1.getUser();
            try{
                server.login(user,this);
                return new OkResponse();
            } catch (PersistanceException e){
                return new ErrorResponse(e.getMessage());
            }
        }

        if(request instanceof logoutRequest){
            System.out.println("Logout request");
            logoutRequest request1 = (logoutRequest) request;
            try {
                server.logout(request1.getUsername());
                connected=false;
                return new OkResponse();

            } catch (PersistanceException e) {

            }
        }
        if(request instanceof getExcursiiRequest){
            System.out.println("Get trips request");
            try{
                List<Excursie> list = server.findAllExcursii();
                return new getExcursiiResponse(list);
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        if(request instanceof getExcursiiFilteredRequest){
            getExcursiiFilteredRequest request1 = (getExcursiiFilteredRequest) request;
            try{
                List<Excursie> all = server.getExcursiiTableFiltru(request1.getObiectiv(),request1.getDupaOra().toString(),request1.getInainteDe().toString());
                return new getExcursiiFilteredResponse(all);
            } catch (PersistanceException e){
                return new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof findExcursieRequest){
            try{
                Excursie exc = server.findOneExcursie(((findExcursieRequest) request).getIdExc());
                return new findExcursieResponse(exc);
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        if(request instanceof updateExcursieRequest)
        {
            try{
                updateExcursieRequest request1 = (updateExcursieRequest) request;
                server.updateExcursie(request1.getExcursie1());
                return new OkResponse();
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        if(request instanceof adaugaRezervareRequest){
            try{
                server.addRezervare(((adaugaRezervareRequest) request).getRezervare());
                return new OkResponse();
            }
            catch (PersistanceException exc){
                return new ErrorResponse(exc.getMessage());
            }
        }
        return null;
    }
    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response");
        output.writeObject(response);
        output.flush();
    }
}
