import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;


public class Testweka {

    public static void main (String[] args) throws Exception {
        BufferedReader reader;
        reader = new BufferedReader(new FileReader("/mnt/myfol/workspaces/M2INFO/data_mining/TP/weka_files/apprentissage.arff"));
        Instances train = new Instances(reader);
        train.setClassIndex(train.numAttributes() - 1);
        ArrayList<String> trancheAge = new ArrayList<String>(3);
        trancheAge.add("<34.33");
        trancheAge.add("34.33-50.66");
        trancheAge.add(">50.66");
        Attribute age = new Attribute("age", trancheAge);

        ArrayList<String> sex = new ArrayList<String>(2);
        sex.add("MALE");
        sex.add("FEMALE");
        Attribute sexe = new Attribute("sex", sex);

        ArrayList<String> regions = new ArrayList<String>(4);
        regions.add("INNER_CITY");
        regions.add("TOWN");
        regions.add("RURAL");
        regions.add("SUBURBAN");
        Attribute region = new Attribute("region", regions);

        ArrayList<String> revenus = new ArrayList<String>(3);
        revenus.add("<24386.17");
        revenus.add("24386.17-43758.13");
        revenus.add(">43758.13");
        Attribute income = new Attribute("income", revenus);

        ArrayList<String> situation = new ArrayList<String>(2);
        situation.add("NO");
        situation.add("YES");
        Attribute married = new Attribute("married", situation);

        ArrayList<String> child = new ArrayList<String>(4);
        child.add("0");
        child.add("1");
        child.add("2");
        child.add("3");
        Attribute children = new Attribute("children", child);

        ArrayList<String> hascar = new ArrayList<String>(2);
        hascar.add("NO");
        hascar.add("YES");
        Attribute car = new Attribute("car", hascar);

        ArrayList<String> act = new ArrayList<String>(2);
        act.add("NO");
        act.add("YES");
        Attribute save_act = new Attribute("save_act", act);

        ArrayList<String> c_act = new ArrayList<String>(2);
        c_act.add("NO");
        c_act.add("YES");
        Attribute current_act = new Attribute("current_act", c_act);

        ArrayList<String> mortgag = new ArrayList<String>(2);
        mortgag.add("NO");
        mortgag.add("YES");
        Attribute mortgage = new Attribute("mortgage", mortgag);

        ArrayList<String> prets = new ArrayList<String>(2);
        prets.add("NO");
        prets.add("YES");
        Attribute pret = new Attribute("pret", prets);

        reader = new BufferedReader(new FileReader("/mnt/myfol/workspaces/M2INFO/data_mining/TP/weka_files/apprentissage.arff"));
        Instances test = new Instances(reader);
        test.setClassIndex(train.numAttributes() - 1);

        Instance newClient = new DenseInstance(11);
        newClient = test.instance(test.numInstances() - 1);

        newClient.setValue(0, ">50.66");
        newClient.setValue(1, "MALE");
        newClient.setValue(2, "RURAL");
        newClient.setValue(3, ">43758.13");
        newClient.setValue(4, "YES");
        newClient.setValue(5, "2");
        newClient.setValue(6, "YES");
        newClient.setValue(7, "YES");
        newClient.setValue(8, "YES");
        newClient.setValue(9, "YES");
        newClient.setValue(10, "NO");

        // Print the instance
        System.out.println("The instance: " + newClient);
        test.add(newClient);
        ArffSaver savefile = new ArffSaver();
        savefile.setInstances(test);
        savefile.setFile(new File("/mnt/myfol/workspaces/M2INFO/data_mining/TP/weka_files/apprentissage.arff"));
        savefile.writeBatch();
        reader.close();
        J48 classifieur = new J48();
        classifieur.buildClassifier(train);
        Instances labeled = new Instances(test);
        double clsLabel = classifieur.classifyInstance(test.instance(test.numInstances() - 1));
        String valeurPredite = test.classAttribute().value((int) clsLabel);

        if (valeurPredite.equals("NON")) {
            System.out.println("Desole! le client ne peut pas avoir un pret");
        } else {
            System.out.println("Le client peut avoir un pret!");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("/mnt/myfol/workspaces/M2INFO/data_mining/TP/weka_files/apprentissage.arff"));
        writer.write(labeled.toString());
    }
}
