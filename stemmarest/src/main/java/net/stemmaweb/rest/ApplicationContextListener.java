/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stemmaweb.rest;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.stemmaweb.services.DatabaseService;
import net.stemmaweb.services.GraphDatabaseServiceProvider;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author marijn
 */
public class ApplicationContextListener implements ServletContextListener {
    
    //private static final String DB_ENV = System.getenv("DATABASE_HOME");
    //private static final String DB_PATH = DB_ENV == null ? "/var/lib/stemmarest" : DB_ENV;
    private final File DB_PATH = new File("/data/tagaid/neo4jdb");
    
    private final ServletContext context = null;

    @Override
    public void contextInitialized(final ServletContextEvent event) {

        GraphDatabaseService db = new GraphDatabaseServiceProvider(DB_PATH).getDatabase();
        final ServletContext context = event.getServletContext();
        context.setAttribute("neo4j", db);
        DatabaseService.createRootNode(db);
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        final ServletContext context = event.getServletContext();
        
        try {
            GraphDatabaseService db = (GraphDatabaseService) context.getAttribute("neo4j");
            if (db != null) {

                db.shutdown();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
