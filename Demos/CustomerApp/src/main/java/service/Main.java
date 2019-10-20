package service;
import io.helidon.config.Config;
import io.helidon.microprofile.config.MpConfig;
import io.helidon.microprofile.server.Server;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Main {
    public static void main(String[] args) {
    	// Used for dependency injection
        Weld weld = new Weld();
        WeldContainer cdiContainer = weld.initialize();

        Server server = Server.builder()
                .addApplication(CustomerApp.class)
                .cdiContainer(cdiContainer)
                // using a customized helidon config instance (in this case the default...)
                .config(MpConfig.builder().config(Config.create()).build())
                .host("localhost")
                // use 0 for a random free port
                .port(9090)
                .build();

        server.start();

        String endpoint = "http://" + server.host() + ":" + server.port();
        System.out.println("API       " + endpoint + "/api/customers");
    }
}
