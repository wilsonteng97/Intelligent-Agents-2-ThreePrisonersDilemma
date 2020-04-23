package com.cz4046;

class WILSON_TENG_Player extends Player {
    String name = "[██] WILSON_TENG_Player";

    int selectAction(int n, int[] myHistory, int[] oppHistory1, int[] oppHistory2) {
        //RandomPlayer randomly picks his action each time
        if (Math.random() < 0.5)
            return 0;  //cooperates half the time
        else
            return 1;  //defects half the time
    }

    // FIXME! >>> REMOVE THIS METHOD BEFORE SUBMISSION.
    @Override
    String name() {
        String result = this.name;
        return result.substring(result.indexOf('$')+1);
    }
}
