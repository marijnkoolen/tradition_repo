package net.stemmaweb.rest;

import java.util.HashSet;
import java.util.Set;

//import javax.annotation.PostConstruct;
import javax.ws.rs.core.Application;

//import org.glassfish.jersey.server.spi.ContainerLifecycleListener;


//import net.stemmaweb.services.DatabaseService;

//import net.stemmaweb.services.GraphDatabaseServiceProvider;
//import org.neo4j.graphdb.GraphDatabaseService;

/**
 * This is the main configuration and setup class.
 * It defines which services will be published by the server
 * @author PSE FS 2015 Team2
 */

public class ApplicationConfig extends Application {
    // Get the correct path to the database location
    //private static final String DB_ENV = System.getenv("DATABASE_HOME");
    //private static final String DB_PATH = DB_ENV == null ? "/home/tagaid/stemmarest/neo4jdb" : DB_ENV;
    //private static final String DB_PATH = "/data/tagaid/neo4jdb";
    
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<>();
        s.add(Root.class);

        //s.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        //s.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        return s;
    }
    
    /*
    @PostConstruct
    public void initializeApp()
    {
        // This has been moved to ApplicationContextListener so that 
        // apache tomcat properly shuts down neo4j.
        // Connect to the database, create the root node if necessary, and leave.
        GraphDatabaseService db = new GraphDatabaseServiceProvider(DB_PATH).getDatabase();
        DatabaseService.createRootNode(db);
        registerShutdownHook(db);
    } 
    */
    
    /*
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        System.out.println("Tagaid - Stemmarest application - Registering shutdown hook from application config.");
                
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                System.out.println("Tagaid - Stemmarest application - Shutting down database from config shutdown hook.");
                graphDb.shutdown();
            }   
        } );
    }
    */
}