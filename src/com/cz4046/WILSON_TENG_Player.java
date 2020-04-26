package com.cz4046;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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