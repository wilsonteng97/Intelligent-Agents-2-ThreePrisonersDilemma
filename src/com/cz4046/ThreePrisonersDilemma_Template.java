package com.cz4046;

import java.util.*;

public class ThreePrisonersDilemma_Template {

	/*
	 This Java program models the two-player Prisoner's Dilemma game.
	 We use the integer "0" to represent cooperation, and "1" to represent
	 defection.

	 Recall that in the 2-players dilemma, U(DC) > U(CC) > U(DD) > U(CD), where
	 we give the payoff for the first player in the list. We want the three-player game
	 to resemble the 2-player game whenever one player's response is fixed, and we
	 also want symmetry, so U(CCD) = U(CDC) etc. This gives the unique ordering

	 U(DCC) > U(CCC) > U(DDC) > U(CDC) > U(DDD) > U(CDD)

	 The payoffs for player 1 are given by the following matrix: */

    static int[][][] payoff = {
            {{6,3},  //payoffs when first and second players cooperate
            {3,0}},  //payoffs when first player coops, second defects
            {{8,5},  //payoffs when first player defects, second coops
            {5,2}}}; //payoffs when first and second players defect

	/*
	 So payoff[i][j][k] represents the payoff to player 1 when the first
	 player's action is i, the second player's action is j, and the
	 third player's action is k.

	 In this simulation, triples of players will play each other repeatedly in a
	 'match'. A match consists of about 100 rounds, and your score from that match
	 is the average of the payoffs from each round of that match. For each round, your
	 strategy is given a list of the previous plays (so you can remember what your
	 opponent did) and must compute the next action.  */


    abstract class Player {
        // This procedure takes in the number of rounds elapsed so far (n), and
        // the previous plays in the match, and returns the appropriate action.
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            throw new RuntimeException("You need to override the selectAction method.");
        }

        // Used to extract the name of this player class.
        final String name() {
            String result = getClass().getName();
            return result.substring(result.indexOf('$')+1);
        }
    }

    /* Here are four simple strategies: */

    class NicePlayer extends Player {
        //NicePlayer always cooperates
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return 0;
        }
    }

    class NastyPlayer extends Player {
        //NastyPlayer always defects
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return 1;
        }
    }

    class RandomPlayer extends Player {
        //RandomPlayer randomly picks his action each time
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (Math.random() < 0.5)
                return 0;  //cooperates half the time
            else
                return 1;  //defects half the time
        }
    }

    class TolerantPlayer extends Player {
        //TolerantPlayer looks at his opponents' histories, and only defects
        //if at least half of the other players' actions have been defects
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            int opponentCoop = 0;
            int opponentDefect = 0;
            for (int i=0; i<n; i++) {
                if (oppHistory1[i] == 0)
                    opponentCoop = opponentCoop + 1;
                else
                    opponentDefect = opponentDefect + 1;
            }
            for (int i=0; i<n; i++) {
                if (oppHistory2[i] == 0)
                    opponentCoop = opponentCoop + 1;
                else
                    opponentDefect = opponentDefect + 1;
            }
            if (opponentDefect > opponentCoop)
                return 1;
            else
                return 0;
        }
    }

    class FreakyPlayer extends Player {
        //FreakyPlayer determines, at the start of the match,
        //either to always be nice or always be nasty.
        //Note that this class has a non-trivial constructor.
        int action;
        FreakyPlayer() {
            if (Math.random() < 0.5)
                action = 0;  //cooperates half the time
            else
                action = 1;  //defects half the time
        }

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            return action;
        }
    }

    class T4TPlayer extends Player {
        //Picks a random opponent at each play,
        //and uses the 'tit-for-tat' strategy against them
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 0; //cooperate by default
            if (Math.random() < 0.5)
                return oppHistory1[n-1];
            else
                return oppHistory2[n-1];
        }
    }

    class WinStayLoseShift extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 0;

            int r = n - 1;
            int myLA = myHistory[r];
            int oppLA1 = oppHistory1[r];
            int oppLA2 = oppHistory2[r];

            if (payoff[myLA][oppLA1][oppLA2]>=5) return myLA;
            return oppAction(myLA);
        }

        private int oppAction(int action) {
            if (action==1) return 0;
            return 1;
        }
    }

    class Trigger extends Player {
        boolean triggered = false;
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 0;
            if (oppHistory1[n-1] + oppHistory2[n-1] == 2) triggered = true;
            if (triggered) return 1;
            return 0;
        }
    }

    class AlternatePlayer0 extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 0;
            return oppAction(myHistory[n-1]);
        }
        private int oppAction(int action) {
            if (action == 1) return 0;
            return 1;
        }
    }

    class AlternatePlayer1 extends Player {
        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            if (n==0) return 1;
            return oppAction(myHistory[n-1]);
        }
        private int oppAction(int action) {
            if (action == 1) return 0;
            return 1;
        }
    }

    class WILSON_TENG_Player extends Player {
        final String NAME = "[██] WILSON_THURMAN_TENG";
        final String MATRIC_NO = "[██] U1820540H";

        int[][][] payoff = {
                {{6, 3},     //payoffs when first and second players cooperate
                        {3, 0}},     //payoffs when first player coops, second defects
                {{8, 5},     //payoffs when first player defects, second coops
                        {5, 2}}};    //payoffs when first and second players defect

        int r;
        int[] myHist, opp1Hist, opp2Hist;
        int myScore=0, opp1Score=0, opp2Score=0;
        int opponent1Coop = 0; int opponent2Coop = 0;

        final double LENIENT_THRESHOLD = 0.705; // Used for Law [#1]
        final double STRICT_THRESHOLD = 0.750; // Used for Law [#2]

        int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
            /**
             LAWS:
             [#0] Unless I am losing, be trustworthy and unpredictable at the same time.
             [#1] Protect myself.
             [#2] Cooperate in a cooperative environment.
             [#3] If I am losing, turn it into a lose-lose situation.
             */

            // Assume environment is cooperative. Always cooperate in first round!
            if (n==0) return 0;

            // Updating class variables for use in methods.
            this.r = n - 1; // previous round index
            this.myHist = myHistory;
            this.opp1Hist = oppHistory1;
            this.opp2Hist = oppHistory2;

            // Updating Last Actions (LA) for all players.
            int myLA = myHistory[r];
            int opp1LA = oppHistory1[r];
            int opp2LA = oppHistory2[r];

            // Updating Scores for all players
            this.myScore += payoff[myLA][opp1LA][opp2LA];
            this.opp1Score += payoff[opp1LA][opp2LA][myLA];
            this.opp2Score += payoff[opp2LA][opp1LA][myLA];

            // Update opponent's cooperate record.
            if (n>0) {
                opponent1Coop += oppAction(opp1Hist[r]);
                opponent2Coop += oppAction(opp2Hist[r]);
            }
            // Calculate opponent's cooperate probability.
            double opponent1Coop_prob = opponent1Coop / opp1Hist.length;
            double opponent2Coop_prob = opponent2Coop / opp2Hist.length;

            /** [PROTECT MYSELF]: -> Law [#1]
             When it is nearing the end of the tournament at 100 rounds, if both players are known to be relatively nasty
             (cooperate less than 75% of the time). Defect to protect myself.
             */
            if ((n>100) && (opponent1Coop_prob<STRICT_THRESHOLD && opponent2Coop_prob<STRICT_THRESHOLD)) {
                // Law [#0] Added
                return actionWithNoise(1, 99);
            }

            /** [REWARD COOPERATION]: -> Law [#2]
             At any point in time before we are able to accurately decide if opponents are nasty or not. We set a lenient
             threshold (0.705) to gauge if opponents are cooperative. Additionally, we check if both opponent's last action
             was to cooperate. If yes, we will cooperate too.
             */
            if ((opp1LA+opp2LA ==0)&&(opponent1Coop_prob>LENIENT_THRESHOLD && opponent2Coop_prob>LENIENT_THRESHOLD)) {
                // Law [#0] Added
                return actionWithNoise(0, 99);
            }
            else
            /** [I WILL NOT LOSE] -> Law [#3]
             However, if opponent is not cooperative, we will check if we have the highest score.
             If we have the highest score, we are appeased and will cooperate. Else, we will defect.
             */
                return SoreLoser();
        }

        /**
         * Law [#0]: This utility method introduces noise to an agent's action, allowing it to be unpredictable.
         * @param intendedAction The agent's intended action.
         * @param percent_chance_for_intended_action The percentage chance the agent will perform it's intended action.
         * @return The agent's final action.
         */
        private int actionWithNoise(int intendedAction, int percent_chance_for_intended_action) {
            Map<Integer, Integer> map = new HashMap<Integer, Integer>() {{
                put(intendedAction, percent_chance_for_intended_action);
                put(oppAction(intendedAction), 1-percent_chance_for_intended_action);
            }};
            LinkedList<Integer> list = new LinkedList<>();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                for (int i = 0; i < entry.getValue(); i++) {
                    list.add(entry.getKey());
                }
            }
            Collections.shuffle(list);
            return list.pop();
        }

        /** Law [#3]:
         * Cooperates if agent currently has the highest score, else defect.
         * @return
         */
        private int SoreLoser() {
            if (iAmWinner()) return 0;
            return 1;
        }

        /* Function to check if agent is loser or not. Agent is a winner if it has the highest score. */
        private boolean iAmWinner() {
            if (myScore>=opp1Score && myScore>=opp2Score) {
                return true;
            }
            return false;
        }
        /* Utility method to obtain opposite action. */
        private int oppAction(int action) {
            if (action == 1) return 0;
            return 1;
        }
    }

    /* In our tournament, each pair of strategies will play one match against each other.
     This procedure simulates a single match and returns the scores. */
    double[] scoresOfMatch(Player A, Player B, Player C, int rounds) {
        int[] HistoryA = new int[0], HistoryB = new int[0], HistoryC = new int[0];
        double ScoreA = 0, ScoreB = 0, ScoreC = 0;

        for (int i=0; i<rounds; i++) {
            int PlayA = A.selectAction(i, HistoryA, HistoryB, HistoryC);
            int PlayB = B.selectAction(i, HistoryB, HistoryC, HistoryA);
            int PlayC = C.selectAction(i, HistoryC, HistoryA, HistoryB);
            ScoreA = ScoreA + payoff[PlayA][PlayB][PlayC];
            ScoreB = ScoreB + payoff[PlayB][PlayC][PlayA];
            ScoreC = ScoreC + payoff[PlayC][PlayA][PlayB];
            HistoryA = extendIntArray(HistoryA, PlayA);
            HistoryB = extendIntArray(HistoryB, PlayB);
            HistoryC = extendIntArray(HistoryC, PlayC);
        }
        double[] result = {ScoreA/rounds, ScoreB/rounds, ScoreC/rounds};
        return result;
    }

    //	This is a helper function needed by scoresOfMatch.
    int[] extendIntArray(int[] arr, int next) {
        int[] result = new int[arr.length+1];
        for (int i=0; i<arr.length; i++) {
            result[i] = arr[i];
        }
        result[result.length-1] = next;
        return result;
    }

	/* The procedure makePlayer is used to reset each of the Players
	 (strategies) in between matches. When you add your own strategy,
	 you will need to add a new entry to makePlayer, and change numPlayers.*/

    Player makePlayer(int which) {
        switch (which) {
            case 0: return new WILSON_TENG_Player();
            case 1: return new NicePlayer();
            case 2: return new NastyPlayer();
            case 3: return new RandomPlayer();
            case 4: return new TolerantPlayer();
            case 5: return new FreakyPlayer();
            case 6: return new T4TPlayer();
            // Added Strategies
            case 7: return new WinStayLoseShift();
            case 8: return new Trigger();
            case 9: return new AlternatePlayer0();
//            case 9: return new AlternatePlayer1();

        }
        throw new RuntimeException("Bad argument passed to makePlayer");
    }

    /* Finally, the remaining code actually runs the tournament. */
    public static void main (String[] args) {
        int TOURNAMENT_ROUNDS = 10000;
        int NUM_PLAYERS = 10;
        boolean PRINT_TOP_3 = false;
        boolean VERBOSE = false; // set verbose = false if you get too much text output
        int val;

        ThreePrisonersDilemma_Template instance = new ThreePrisonersDilemma_Template();
        LinkedHashMap<Integer, Integer> hashMap = new LinkedHashMap<>();
        for (int player = 0; player < NUM_PLAYERS; player++)
            hashMap.put(player, 0);

        for (int i = 0; i < TOURNAMENT_ROUNDS; i++) {
            int[] top_players = instance.runTournament(NUM_PLAYERS, VERBOSE);
            if (PRINT_TOP_3) for (int tp = 0; tp < 3; tp++) {
                System.out.println(top_players[tp]);
            }
            for (int p = 0; p < top_players.length; p++) {
                int tp = top_players[p];
                val = hashMap.get(tp);
                hashMap.put(tp, val + p + 1);
            }
        }

        hashMap = (LinkedHashMap<Integer, Integer>) sortByValue(hashMap);

        float float_tournament_rounds = (float) TOURNAMENT_ROUNDS;
        float float_val;
        LinkedHashMap<Integer, Float> newHashMap = new LinkedHashMap<>();
        for (int p=0; p<NUM_PLAYERS; p++) {
            val = hashMap.get(p);
            float_val = (float) val;
            newHashMap.put(p, float_val/float_tournament_rounds);
        }

        hashMap = (LinkedHashMap<Integer, Integer>) sortByValue(hashMap);
        newHashMap = (LinkedHashMap<Integer, Float>) sortByValue(newHashMap);
        System.out.print("[" + TOURNAMENT_ROUNDS + " TOURNAMENT_ROUNDS]");
        System.out.println(" >>> Player 0 is WILSON_TENG_Player <<<");
        System.out.println("Summed up rankings for Players 0 to " + NUM_PLAYERS + " : " + hashMap);
        System.out.println("Average rankings : \t\t\t\t\t\t " + newHashMap);
    }

    int[] runTournament(int numPlayers, boolean verbose) {
        double[] totalScore = new double[numPlayers];

        // This loop plays each triple of players against each other.
        // Note that we include duplicates: two copies of your strategy will play once
        // against each other strategy, and three copies of your strategy will play once.
        int count = 0;
        for (int i=0; i<numPlayers; i++) for (int j=i; j<numPlayers; j++) for (int k=j; k<numPlayers; k++) {

            Player A = makePlayer(i); // Create a fresh copy of each player
            Player B = makePlayer(j);
            Player C = makePlayer(k);
            int rounds = 90 + (int)Math.rint(20 * Math.random()); // Between 90 and 110 rounds
            double[] matchResults = scoresOfMatch(A, B, C, rounds); // Run match
            totalScore[i] = totalScore[i] + matchResults[0];
            totalScore[j] = totalScore[j] + matchResults[1];
            totalScore[k] = totalScore[k] + matchResults[2];
            count++;
            if (verbose)
                System.out.println("[" + count + "] " + A.name() + " scored " + matchResults[0] +
                        " points, " + B.name() + " scored " + matchResults[1] +
                        " points, and " + C.name() + " scored " + matchResults[2] + " points.");
        }
        int[] sortedOrder = new int[numPlayers];
        // This loop sorts the players by their score.
        for (int i=0; i<numPlayers; i++) {
            int j=i-1;
            for (; j>=0; j--) {
                if (totalScore[i] > totalScore[sortedOrder[j]])
                    sortedOrder[j+1] = sortedOrder[j];
                else break;
            }
            sortedOrder[j+1] = i;
        }

        // Finally, print out the sorted results.
        if (verbose) System.out.println();
        System.out.println("Tournament Results");
        for (int i=0; i<numPlayers; i++)
            System.out.println("[Player " + sortedOrder[i] + "] " + makePlayer(sortedOrder[i]).name() + ": "
                    + totalScore[sortedOrder[i]] + " points.");

        System.out.println();
        return sortedOrder;
    } // end of runTournament()
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
//        Collections.reverse(list);

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
} // end of class PrisonersDilemma
