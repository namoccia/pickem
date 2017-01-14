package com.feedthewolf.nhlpickem;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by nmoccia on 1/13/2017.
 */

@RunWith(AndroidJUnit4.class)
public class LeagueRecordInstrumentedTest {
    private LeagueRecord mRecord;

    @Before
    public void createLeagueRecord() {
        mRecord = new LeagueRecord(1, 2, 3);
    }

    @Test
    public void testLeagueRecordParcelable() {
        // Obtain a Parcel object and write the parcelable object to it:
        Parcel parcel = Parcel.obtain();
        mRecord.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        LeagueRecord createdFromParcel = LeagueRecord.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getWins(), is(mRecord.getWins()));
        assertThat(createdFromParcel.getLosses(), is(mRecord.getLosses()));
        assertThat(createdFromParcel.getOt(), is(mRecord.getOt()));
    }
}
