package utils;

import objectProtocol.ClientObjectWorker;
import service.IService;

import java.net.Socket;

public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IService service;
    public ObjectConcurrentServer(Integer port, IService service) {
        super(port);
        this.service = service;
        System.out.println("ObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(service, client);
        Thread tw=new Thread(worker);
        return tw;
    }
}
