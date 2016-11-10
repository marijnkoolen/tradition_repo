/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stemmaweb.rest;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.stemmaweb.services.DatabaseService;
import net.stemmaweb.services.GraphDatabaseServiceProvider;

import org.apache.log4j.Logger;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 *
 * @author marijn
 */
public class ApplicationContextListener implements ServletContextListener {
    
    private static final String DB_ENV = System.getenv("DATABASE_HOME");
    private static final String DB_PATH = DB_ENV == null ? "/var/lib/stemmarest" : DB_ENV;
    final static Logger logger = Logger.getLogger(ApplicationContextListener.class);
    private ServletContext context = null;
    
    public void contextDestroyed(ServletContextEvent event) {
        //Output a simple message to the server's console
        try {
            GraphDatabaseService db = new GraphDatabaseServiceProvider().getDatabase();
            db.shutdown();
            logger.debug("This is debug: db shut down properly");
        } catch (Exception e) {
            logger.debug("This is debug: shut down error");
            logger.error("failed!", e);
            e.printStackTrace();
        }
        this.context = null;
    }
    
  public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();
        //Output a simple message to the server's console
        logger.debug("This is debug - Listener: context initialized");
        GraphDatabaseService db = new GraphDatabaseServiceProvider(DB_PATH).getDatabase();
        DatabaseService.createRootNode(db);
        
        
    }

    
}
