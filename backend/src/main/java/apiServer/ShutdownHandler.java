package apiServer;

import database.DatabaseConnectorPool;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ShutdownHandler {
    @PreDestroy
    public void destroy() {
        System.out.println("goodbye");
        DatabaseConnectorPool.closeDataSource();
    }

}


// https://stackoverflow.com/questions/4727536/how-do-i-stop-a-process-running-in-intellij-such-that-it-calls-the-shutdown-hook