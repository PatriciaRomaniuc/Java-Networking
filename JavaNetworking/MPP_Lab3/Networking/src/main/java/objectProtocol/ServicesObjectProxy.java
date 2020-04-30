package objectProtocol;

import domain.Excursie;
import domain.Rezervare;
import domain.User;
import observer.applicationObserver;
import persistance.PersistanceException;
import service.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesObjectProxy implements IService {
    private String host;
    private Integer port;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;

    private applicationObserver client;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesObjectProxy(String host,int p){
        this.host = host;
        port = p;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    @Override
    public void login(User user, applicationObserver applicationObserver) throws PersistanceException {
        initializeConnection();
        sendRequest(new loginRequest(user));
        Response response = readResponse();
        if(response instanceof OkResponse){
            System.out.println("ok");
            this.client=applicationObserver;
        }
        if(response instanceof ErrorResponse){
            ErrorResponse errorResponse = (ErrorResponse) response;
            throw new PersistanceException(errorResponse.getError());
        }
    }

    @Override
    public void logout(String username) throws PersistanceException {
        if(connection != null){
            sendRequest(new logoutRequest(username));
            System.out.println("logout succes");
            Response response=readResponse();
            closeConnection();

        }
    }

    @Override
    public List<Excursie> findAllExcursii() throws PersistanceException {
        sendRequest(new getExcursiiRequest());
        Response response = readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new PersistanceException(err.getError());
        }
        getExcursiiResponse response1 = (getExcursiiResponse) response;
        return response1.getList();
    }

    @Override
    public List<Excursie> getExcursiiTableFiltru(String obiectiv, String dupa, String inainte) throws PersistanceException {
        sendRequest(new getExcursiiFilteredRequest(obiectiv,dupa,inainte));
        Response response = readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new PersistanceException(err.getError());
        }
        getExcursiiFilteredResponse response1 = (getExcursiiFilteredResponse) response;
        return response1.getList();
    }

    @Override
    public Excursie findOneExcursie(int id) throws PersistanceException {
        sendRequest(new findExcursieRequest(id));
        Response response = readResponse();
        if(response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new PersistanceException(err.getError());
        }
        findExcursieResponse response1 = (findExcursieResponse) response;
        return response1.getExcursie1();
    }

    @Override
    public void updateExcursie( Excursie excursie) throws PersistanceException {
        sendRequest(new updateExcursieRequest(excursie));
        Response response = readResponse();
        if(response instanceof OkResponse){
            System.out.println("modificat");
        }
        else {
            ErrorResponse response1 = (ErrorResponse) response;
            System.out.println("Nu s-a putut modifica" + response1.getError());
        }
    }


    private void handleUpdate(Response response){
        if(response instanceof updateGetAllExcursii){
            updateGetAllExcursii response1 = (updateGetAllExcursii) response;
            List<Excursie> excursies = response1.getList();
            try{
                client.updateTrips(excursies);

            }
            catch (PersistanceException exc){
                exc.printStackTrace();
            }
        }
    }
    @Override
    public void addRezervare(Rezervare rezervare) throws PersistanceException {
        sendRequest(new adaugaRezervareRequest(rezervare));
        Response response = readResponse();
        if(response instanceof OkResponse){
            System.out.println("adaugat");
        }
        else {
            ErrorResponse response1 = (ErrorResponse) response;
            System.out.println("Nu s-a putut adauga" + response1.getError());
        }
    }

    private void initializeConnection() throws PersistanceException {
        try {
            connection=new Socket(host,port);
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendRequest(Request request)throws PersistanceException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new PersistanceException("Error sending object"+e);
        }

    }
    private Response readResponse() throws PersistanceException {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response=input.readObject();
                    System.out.println("response received ");
                    if (response instanceof updateGetAllExcursii){
                        handleUpdate((updateGetAllExcursii)response);
                    }
                    else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
