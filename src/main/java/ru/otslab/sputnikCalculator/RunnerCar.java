package ru.otslab.sputnikCalculator;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationWriter;
import org.matsim.contrib.accessibility.AccessibilityConfigGroup;
import org.matsim.contrib.accessibility.AccessibilityModule;
import org.matsim.contrib.accessibility.Modes4Accessibility;
import org.matsim.contrib.accessibility.utils.AccessibilityUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

/**
 * Created by volot on 12.05.2017.
 */
public class RunnerCar {
    public static void main(String[] args) {
        double scaleCoefficient = 1.3;
        double populationSample = 1.0;
        boolean scalePopulation = true;
        boolean removePersonOnMode = true;
        String configFile = "config_horizon_2021_1_car.xml";
        Config config = ConfigUtils.loadConfig(configFile);
        //config.global().setNumberOfThreads(12);

        config.qsim().setFlowCapFactor(scaleCoefficient);
        config.qsim().setStorageCapFactor(scaleCoefficient * 2);
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        Scenario scenario = ScenarioUtils.loadScenario(config);
        Population population = scenario.getPopulation();



            List<Id<Person>> personIdList = new LinkedList<Id<Person>>();
            AgentsTripModeModifier modeModifier = new AgentsTripModeModifier(population);
            modeModifier.changeMode("pt","car");
        PopulationWriter populationWriter = new PopulationWriter(population);
        populationWriter.writeV6("CarPopulation.xml");


        //Population drawedPopulation = PopulationUtils.createPopulation(config);
        List<Id<Person>> personIdList2 = new LinkedList<Id<Person>>();

        Iterator personIterator = population.getPersons().values().iterator();
        while (personIterator.hasNext()) {
            Person person = (Person) personIterator.next();
            personIdList2.add(person.getId());
               /*
                Id<Person> toAddId = (Id<Person>) randomDrawIterator.next();
                Person toAddPerson = population.getPersons(Map<Id<Person>, args> );
                drawedPopulation.addPerson(toRemoveId);
                */
            }
            List<Id<Person>> randomDraw = pickNRandom(personIdList2, personIdList2.size() * (1 - populationSample));
            Iterator randomDrawIterator = randomDraw.iterator();
            while (randomDrawIterator.hasNext()) {
                Id<Person> toRemoveId = (Id<Person>) randomDrawIterator.next();
                log.println("Removing the person " + toRemoveId);
                population.removePerson(toRemoveId);
        }




        Controler controler = new Controler(scenario);
        controler.run();
    }
            public static List<Id<Person>> pickNRandom (List < Id < Person >> lst,double n){
                List<Id<Person>> copy = new LinkedList<Id<Person>>(lst);
                Collections.shuffle(copy);
                return copy.subList(0, (int) n);
            }
}