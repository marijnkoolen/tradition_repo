package net.stemmaweb.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import net.stemmaweb.rest.ERelations;
import net.stemmaweb.services.DatabaseService;
import net.stemmaweb.services.RankCalculation;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author PSE FS 2015 Team2
 */

@XmlRootElement
@JsonInclude(Include.NON_NULL)
public class TraditionModel {

    @SuppressWarnings("unused")
    private enum Direction {
        LR,  // left-to-right text
        RL,  // right-to-left text
        BI,  // bidirectional text
    }

    // Properties
    private String id;
    private String name;
    private String language;
    private Direction direction;
    private Boolean is_public;
    private Integer stemweb_jobid;
    private String owner;
    private Long max_rank;

    // Derived from relationships
    private ArrayList<String> witnesses;
    private ArrayList<String> reltypes;

    public TraditionModel() {}

    public TraditionModel(Node node) {
        try (Transaction tx = node.getGraphDatabase().beginTx()) {
            setId(node.getProperty("id").toString());
            if (node.hasProperty("name"))
                setName(node.getProperty("name").toString());
            if (node.hasProperty("language"))
                setLanguage(node.getProperty("language").toString());
            if (node.hasProperty("direction"))
                setDirection(node.getProperty("direction").toString());
            if (node.hasProperty("is_public"))
                setIs_public((Boolean) node.getProperty("is_public"));
            if (node.hasProperty("stemweb_jobid"))
                setStemweb_jobid(Integer.valueOf(node.getProperty("stemweb_jobid").toString()));

            Relationship ownerRel = node.getSingleRelationship(ERelations.OWNS_TRADITION,
                    org.neo4j.graphdb.Direction.INCOMING);
            if( ownerRel != null ) {
                setOwner(ownerRel.getStartNode().getProperty("id").toString());
            }
            
            // add max rank (rank of is_end node) so maximum reading length can
            // be derived.
            setMax_rank();

            witnesses = new ArrayList<>();
            DatabaseService.getRelated(node, ERelations.HAS_WITNESS).forEach(
                    x -> witnesses.add(x.getProperty("sigil").toString()));
            // For now this is hard-coded
            reltypes = new ArrayList<>(Arrays.asList("grammatical", "spelling", "other", "punctuation",
                    "lexical", "orthographic", "uncertain"));

            tx.success();
        }
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getDirection() { return direction == null ? "" : direction.toString(); }
    public void setDirection(String direction) {
        if (!direction.equals(""))
            this.direction = Direction.valueOf(direction);
    }
    public Boolean getIs_public() { return is_public; }
    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public Integer getStemweb_jobid () { return stemweb_jobid; }
    public void setStemweb_jobid (int stemweb_jobid ) { this.stemweb_jobid = stemweb_jobid; }
    
    public Long getMax_rank () {
        return max_rank;
    }
    public void setMax_rank () {
        RankCalculation rankCalc = new RankCalculation();
        max_rank = rankCalc.getMax_rank(id);
    }
}
