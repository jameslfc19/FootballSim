package uk.co.codeecho.fixture.generator;

import java.util.LinkedList;
import java.util.List;

import com.james.Utils;
import com.james.Team;

public class FixtureGenerator {

    public List<List<Fixture<Team>>> getFixtures(List<Team> teams, boolean includeReverseFixtures, int teamId) {

        int numberOfTeams = teams.size();
        
        // If odd number of teams add a "ghost".
        boolean ghost = false;
        if (numberOfTeams % 2 != 0) {
            numberOfTeams++;
            ghost = true;
        }

        // Generate the fixtures using the cyclic algorithm.
        int totalRounds = numberOfTeams - 1;
        int matchesPerRound = numberOfTeams / 2;
        List<List<Fixture<Team>>> rounds = new LinkedList<List<Fixture<Team>>>();

        for (int round = 0; round < totalRounds; round++) {
            LinkedList<Fixture<Team>> fixtures = new LinkedList<Fixture<Team>>();
            for (int match = 0; match < matchesPerRound; match++) {
                int home = (round + match) % (numberOfTeams - 1);
                int away = (numberOfTeams - 1 - match + round) % (numberOfTeams - 1);
                // Last team stays in the same place while the others
                // rotate around it.
                if (match == 0) {
                    away = numberOfTeams - 1;
                }
                fixtures.add(new Fixture<Team>(teams.get(home), teams.get(away)));
            }
            Utils.putChosenTeamAtTopOfArray(fixtures, teamId);
            rounds.add(fixtures);
        }
        
        Utils.randomiseArray(rounds);

        // Interleave so that home and away games are fairly evenly dispersed.
        List<List<Fixture<Team>>> interleaved = new LinkedList<List<Fixture<Team>>>();

        int evn = 0;
        int odd = (numberOfTeams / 2);
        for (int i = 0; i < rounds.size(); i++) {
            if (i % 2 == 0) {
                interleaved.add(rounds.get(evn++));
            } else {
                interleaved.add(rounds.get(odd++));
            }
        }

        rounds = interleaved;

        // Last team can't be away for every game so flip them
        // to home on odd rounds.
        for (int roundNumber = 0; roundNumber < rounds.size(); roundNumber++) {
            if (roundNumber % 2 == 1) {
                Fixture fixture = rounds.get(roundNumber).get(0);
                rounds.get(roundNumber).set(0, new Fixture(fixture.getAwayTeam(), fixture.getHomeTeam()));
            }
        }
        
        if(includeReverseFixtures){
            List<List<Fixture<Team>>> reverseFixtures = new LinkedList<List<Fixture<Team>>>();
            for(List<Fixture<Team>> round: rounds){
                List<Fixture<Team>> reverseRound = new LinkedList<Fixture<Team>>();
                for(Fixture<Team> fixture: round){
                    reverseRound.add(new Fixture<Team>(fixture.getAwayTeam(), fixture.getHomeTeam()));
                }
                reverseFixtures.add(reverseRound);
            }
            rounds.addAll(reverseFixtures);
        }

        return rounds;
    }
}
