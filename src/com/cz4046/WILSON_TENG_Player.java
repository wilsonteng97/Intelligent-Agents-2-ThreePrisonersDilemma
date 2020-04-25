package com.cz4046;

class WILSON_TENG_Player extends Player {
    String name = "[██] WILSON_TENG_Player";
    // FIXME! >>> REMOVE THIS METHOD BEFORE SUBMISSION.
    @Override
    String name() {
        String result = this.name;
        return result.substring(result.indexOf('$')+1);
    }

    int[][][] payoff = {
            {{6, 3},     //payoffs when first and second players cooperate
                    {3, 0}},     //payoffs when first player coops, second defects
            {{8, 5},     //payoffs when first player defects, second coops
                    {5, 2}}};    //payoffs when first and second players defect

    int prev_round = 0;
    int this_round;
    int[] myHist, oppHist1, oppHist2;
    int freaky_action;
    int myLA, oppLA1, oppLA2;
    int myLLA, oppLLA1, oppLLA2;
    boolean triggerWSLS = false;
    int WSLS_Count = 0;
    int r;
    boolean trigger = false;
    int myLastScore, oppLastScore1, oppLastScore2;
    int myLLScore, oppLLScore1, oppLLScore2;
    int trigger_count = 0;

    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        if (n < 2) return SoftT4TPlayer();
        if (trigger) {
//            trigger_count -= 1;
//            if (trigger_count<10)
            return actionWithNoise(1, 0);
        }


        this.this_round = n;
        this.prev_round = n - 1;
        this.myHist = myHistory;
        this.oppHist1 = oppHistory1;
        this.oppHist2 = oppHistory2;

        this.r = prev_round;
        this.myLA = myHistory[r];
        this.oppLA1 = oppHistory1[r];
        this.oppLA2 = oppHistory2[r];

        this.myLLA = myHistory[r-1];
        this.oppLLA1 = oppHistory1[r-1];
        this.oppLLA2 = oppHistory2[r-1];

        this.myLastScore = payoff[myLA][oppLA1][oppLA2];
        this.oppLastScore1 = payoff[oppLA1][oppLA2][myLA];
        this.oppLastScore2 = payoff[oppLA2][oppLA1][myLA];

        this.myLLScore = payoff[myLLA][oppLLA1][oppLLA2];
        this.oppLLScore1 = payoff[oppLLA1][oppLLA2][myLLA];
        this.oppLLScore2 = payoff[oppLLA2][oppLLA1][myLLA];

//            if (triggerWSLS == true) return WinStayLoseShift();

//            if (n<10) return HardT4TPlayer();

        int opponentCoop = 0;
        int opponentDefect = 0;
        for (int i = 0; i < n; i++) {
            if (oppHistory1[i] == 0)
                opponentCoop = opponentCoop + 1;
            else
                opponentDefect = opponentDefect + 1;
        }
        for (int i = 0; i < n; i++) {
            if (oppHistory2[i] == 0)
                opponentCoop = opponentCoop + 1;
            else
                opponentDefect = opponentDefect + 1;
        }
        if (opponentDefect > opponentCoop) {
//            if (myLastScore<oppLastScore1 && myLastScore<oppLastScore2 && myLLScore<oppLLScore1 && myLLScore<oppLLScore2) {
//                trigger = true;
//                trigger_count = 5;
//                return 1;
//            }
            trigger_count+=1;
            if (trigger_count==3) {
                trigger = true;
                System.out.println("LALALALALALAL");
            }
            return 1;
        } else
            trigger_count = 0;
            return 0;
    }

    private int SoftT4TPlayer() {
        if (this_round==0) return 0;
        if ((oppHist1[r] == 0) || (oppHist2[r] == 0))
            return 0;
        else
            return 1;
    }

    private int HardT4TPlayer() {
        if (this_round==0) return 0;
        if ((oppHist1[r]==0) && (oppHist2[r]==0))
            return 0;
        else
            return 1;

//            if (Math.random() < 0.5)
//                return oppHist1[r];
//            else
//                return oppHist2[r];
    }

    private int oppAction(int action) {
        if (action == 1) return 0;
        return 1;
    }
    private int WinStayLoseShift() {
        WSLS_Count += 1;
        if (WSLS_Count==5) {
            triggerWSLS = false;
            WSLS_Count = 0;
        }
        if (payoff[myLA][oppLA1][oppLA2] + payoff[myLLA][oppLLA1][oppLLA2] >= 10) return myLA;
        return oppAction(myLA);
    }

    private int actionWithNoise(int intendedAction, double prob) {
        if (Math.random() < prob)
            return oppAction(intendedAction);  //opp of intended action
        else
        return intendedAction;  //intended action
    }
}