package com.feedthewolf.nhlpickem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by nmoccia on 1/13/2017.
 */

public class LeagueRecordUnitTest {
    private LeagueRecord mRecord;

    @Before
    public void createLeagueRecord() {
        mRecord = new LeagueRecord(1, 2, 3);
    }

    @Test
    public void leagueRecord_setWins_newValue() {
        mRecord.setWins(4);
        assertEquals(4, mRecord.getWins());
    }

    @Test
    public void leagueRecord_setLosses_newValue() {
        mRecord.setLosses(4);
        assertEquals(4, mRecord.getLosses());
    }

    @Test
    public void leagueRecord_setOt_newValue() {
        mRecord.setOt(4);
        assertEquals(4, mRecord.getOt());
    }

    @Test
    public void leagueRecord_toString_expectedFormat() {
        String toStringValue = mRecord.toString();
        assertEquals("1-2-3", toStringValue);
    }
}
