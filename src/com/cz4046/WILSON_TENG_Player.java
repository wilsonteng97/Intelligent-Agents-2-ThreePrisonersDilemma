package com.cz4046;

class WILSON_TENG_Player extends Player {
    String name = "[██] WILSON_TENG_Player";
    static final int NO_OF_ROUNDS = 100;

    int p1_defects = 0;
    int p2_defects = 0;

    int observeRounds = 30;

    double p1_grudge = 0.0;
    double p2_grudge = 0.0;

    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        //RandomPlayer randomly picks his action each time
        if (n != 0) {
            this.p1_defects += oppHistory1[n - 1];
            this.p2_defects += oppHistory2[n - 1];
        } else {
            return 0;
        }

        if (NO_OF_ROUNDS-n-1 < 3) return 1; // defect last 3 rounds

        if (n < observeRounds) { // T4T
            if (Math.random() < 0.5)
                return oppHistory1[n-1];
            else
                return oppHistory2[n-1];
        }

        if (oppHistory1[n - 1] == 1) {
            p1_grudge += 0.5;
            p1_grudge %= 3;
        }
        if (oppHistory2[n - 1] == 1) {
            p2_grudge += 0.5;
            p2_grudge %= 3;
        }

        if (p1_grudge>p2_grudge) {
            if (p1_grudge>0) {
                p1_grudge -= 1.5;
                return 1;
            }
        } else {
            if (p2_grudge>0) {
                p2_grudge -= 1.5;
                return 1;
            }
        }

        return 0;
    }

    // FIXME! >>> REMOVE THIS METHOD BEFORE SUBMISSION.
    @Override
    String name() {
        String result = this.name;
        return result.substring(result.indexOf('$')+1);
    }
}
