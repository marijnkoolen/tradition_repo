package net.stemmaweb.services;

import java.util.Iterator;
import net.stemmaweb.model.ReadingModel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.Iterators; // Neo4j 3.x
//import org.neo4j.helpers.collection.IteratorUtil; // Neo4j 2.x


/**
 * Created by Marijn Koolen on 27/01/17.
 */


public class RankCalculation {
    
    private final GraphDatabaseService db;
    
    public RankCalculation() {
        GraphDatabaseServiceProvider dbServiceProvider = new GraphDatabaseServiceProvider();
        db = dbServiceProvider.getDatabase();
    }
    
    public Long getMax_rank(String traditionId) {
        Result result;
        Long max_rank = null;
        try (Transaction tx = db.beginTx()) {
            result = db.execute("match (m) where m.tradition_id = \"" + traditionId + "\" and m.is_end=true return m");
            Iterator<Node> nodes = result.columnAs("m");
            for (Node node : Iterators.asIterable(nodes))
            //for (Node node : IteratorUtil.asIterable(nodes))
            {
                ReadingModel m = new ReadingModel(node);
                max_rank = m.getRank();
            }
            
            tx.success();
            
        } catch (Exception e) {
            return null;
        }

        return max_rank;
    }

}
