import Dependency.DependencyGraph;
import Infra.CandidateGKey;
import Loader.DBPediaLoader;
import Miner.GKMiner;
import Summary.SummaryGraph;
import Util.Config;
import Util.Helper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class testDBPedia {


    public static void main(String []args) throws FileNotFoundException {

        long wallClockStart=System.currentTimeMillis();

        System.out.println("Test GKMier algorithm for the DBPedia dataset");

        Config.parse(args[0]);

        System.out.println(Arrays.toString(Config.dataPaths.toArray()));
        System.out.println(Arrays.toString(Config.typesPaths.toArray()));

        System.out.println("Loading the dataset.");
        long startTime=System.currentTimeMillis();
        DBPediaLoader dbpedia = new DBPediaLoader(Config.typesPaths, Config.dataPaths);
        Helper.printWithTime("Loading time: ", System.currentTimeMillis()-startTime);

        System.out.println("Creating Summary Graph.");
        startTime=System.currentTimeMillis();
        SummaryGraph summaryGraph=new SummaryGraph(dbpedia.getGraph());
        summaryGraph.summary();
        Helper.printWithTime("Summary Graph (total time): ", System.currentTimeMillis()-startTime);

        DependencyGraph dependencyGraph = new DependencyGraph();

        System.out.println("Mining graph keys.");
        startTime=System.currentTimeMillis();

        GKMiner miner = new GKMiner(dbpedia.getGraph(), summaryGraph,dependencyGraph,Config.type,Config.delta,Config.k, true);
        miner.mine();

        Helper.printWithTime("Mining time: ", System.currentTimeMillis()-startTime);

        if(Config.saveKeys)
        {
            HashMap<String, ArrayList<CandidateGKey>> gKeys = miner.getAllGKeys();
            Helper.saveGKeys(Config.type,gKeys);
        }

        Helper.printWithTime("Total wall clock time: ", System.currentTimeMillis()-wallClockStart);
    }

}
