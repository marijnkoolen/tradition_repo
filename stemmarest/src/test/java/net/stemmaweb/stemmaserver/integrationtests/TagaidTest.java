/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.stemmaweb.stemmaserver.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import net.stemmaweb.model.TraditionModel;
import net.stemmaweb.rest.ERelations;
import net.stemmaweb.rest.Nodes;
import net.stemmaweb.rest.Root;
import net.stemmaweb.services.DatabaseService;
import net.stemmaweb.services.GraphDatabaseServiceProvider;
import net.stemmaweb.parser.GraphMLParser;
import net.stemmaweb.stemmaserver.JerseyTestServerFactory;

import net.stemmaweb.stemmaserver.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.*;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.test.framework.JerseyTest;
import net.stemmaweb.rest.Tradition;
import org.neo4j.test.TestGraphDatabaseFactory;

/**
 *
 * @author marijn
 */
public class TagaidTest {
    private String tradId;
    private long maxRank;

    private GraphDatabaseService db;

    /*
     * JerseyTest is the test environment to Test api calls it provides a
     * grizzly http service
     */
    private JerseyTest jerseyTest;
    private GraphMLParser importResource;

    @Before
    public void setUp() throws Exception {

        db = new GraphDatabaseServiceProvider(new TestGraphDatabaseFactory()
                .newImpermanentDatabase())
                .getDatabase();

        /*
         * Populate the test database with the root node and a user with id 1
         */
        DatabaseService.createRootNode(db);
        try (Transaction tx = db.beginTx()) {
            Node rootNode = db.findNode(Nodes.ROOT, "name", "Root node");

            Node node = db.createNode(Nodes.USER);
            node.setProperty("id", "1");
            node.setProperty("role", "admin");

            rootNode.createRelationshipTo(node, ERelations.SYSTEMUSER);
            tx.success();
        }

        // Create a JerseyTestServer for the necessary REST API calls
        Root webResource = new Root();
        jerseyTest = JerseyTestServerFactory.newJerseyTestServer()
                .addResource(webResource)
                .create();
        jerseyTest.setUp();

        /**
         * create a tradition inside the test DB
         */
        try {
            //String fileName = "src/TestFiles/testTradition.xml";
            //String fileName = "src/TestFiles/Sapientia.xml";
            String fileName = "src/TestFiles/Sapientia.xml";
            tradId = createTraditionFromFile("Sapientia", "LR", "1", fileName, "graphml");
        } catch (FileNotFoundException e) {
            assertTrue(false);
        }
    }

    private String createTraditionFromFile(String tName, String tDir, String userId, String fName,
                                           String fType) throws FileNotFoundException {

        FormDataMultiPart form = new FormDataMultiPart();
        if (fType != null) form.field("filetype", fType);
        if (tName != null) form.field("name", tName);
        if (tDir != null) form.field("direction", tDir);
        if (userId != null) form.field("userId", userId);
        if (fName != null) {
            FormDataBodyPart fdp = new FormDataBodyPart("file",
                    new FileInputStream(fName),
                    MediaType.APPLICATION_OCTET_STREAM_TYPE);
            form.bodyPart(fdp);
        }
        ClientResponse jerseyResult = jerseyTest.resource()
                .path("/tradition")
                .type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .put(ClientResponse.class, form);
        assertEquals(Response.Status.CREATED.getStatusCode(), jerseyResult.getStatus());
        String tradId = Util.getValueFromJson(jerseyResult, "tradId");
        assert(tradId.length() != 0);
        return  tradId;
    }

    /*
    ** Test if subgraph request returns edges that span the requested range
    */
    @Test
    public void getTraditionInfoTest() {

        ClientResponse response = jerseyTest.resource()
                .path("/tradition/" + tradId)
                .get(ClientResponse.class);

        Tradition tradition = new Tradition(tradId);

        Response result = tradition.getTraditionInfo();
        TraditionModel tradInfo = (TraditionModel) result.getEntity();
        assertEquals(tradInfo.getName(), "Sapientia");
        maxRank = tradInfo.getMax_rank();
        assertEquals(maxRank, 2117L);
    }
    
    /**
     * Shut down the jersey server
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        jerseyTest.tearDown();
        db.shutdown();
    }

}
