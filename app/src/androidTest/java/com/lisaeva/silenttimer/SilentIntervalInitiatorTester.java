//package com.lisaeva.silenttimer;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.media.AudioManager;
//import androidx.annotation.Nullable;
//import androidx.test.core.app.ApplicationProvider;
//import com.lisaeva.silenttimer.localdata.SilentIntervalList;
//import com.lisaeva.silenttimer.localdata.SilentIntervalListAccess;
//import com.lisaeva.silenttimer.model.SilentInterval;
//import com.lisaeva.silenttimer.persistence.SharedPreferenceUtil;
//import org.joda.time.DateTime;
//import org.joda.time.DateTimeConstants;
//import org.joda.time.DateTimeUtils;
//import org.joda.time.format.DateTimeFormatter;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.junit.MockitoJUnitRunner;
//import java.lang.reflect.Field;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//
//// com.android.tools.r8.a: MethodHandle.invoke and MethodHandle.invokeExact are only supported starting with Android O (--min-api 26)
//
//@RunWith(MockitoJUnitRunner.class)
//public class SilentIntervalInitiatorTester {
//
//    private static Context context;
//
//    private AlarmManager alarmManager;
//    private AudioManager audioManager;
//    private SharedPreferenceUtil mSharedPreferenceUtil;
//
//
//    private SilentIntervalReceiver receiver;
//
//    private SilentInterval[] intervals;
//
//    SilentIntervalList silMock;
//    ArgumentCaptor<String> uriCapture = ArgumentCaptor.forClass(String.class);
//
//
//    @Before
//    public void setup() {
//        context = ApplicationProvider.getApplicationContext();
//        receiver = new SilentIntervalReceiver();
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//
//        DateTimeUtils.setCurrentMillisSystem();
//
//        silMock = mock(SilentIntervalList.class);
//
//        try {
//            Field field = SilentIntervalListAccess.class.getDeclaredField("INSTANCE");
//            field.setAccessible(true);
//            field.set(new SilentIntervalList(context), silMock);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @After
//    public void cleanup() {
//        DateTimeUtils.setCurrentMillisSystem();
//    }
//
//    // 1 interval, start-time -1h, end-time +1h, no repeat
//    @Test
//    public void test1() {
//        int rc;
//        Set<String> pref;
//
//        Set<String> savedIds = mSharedPreferenceUtil.getStartedIntervalUUIDs();
//        Set<String> savedHandles = mSharedPreferenceUtil.getActivePendingIntentsMap().keySet();
//        Set<String> nonEmptySet = new HashSet<>();
//        nonEmptySet.add("string");
//        mSharedPreferenceUtil.setStartedIntervalUUIDs(new HashSet<>());
//
//        DateTime clock = DateTime.now();
//        DateTimeUtils.setCurrentMillisFixed(clock.getMillis());
//
//        DateTime s = clock.minusHours(1);
//        DateTime e = clock.plusHours(1);
//        roll(s,e);
//
//        String ss = formatter.print(s);
//        String se = formatter.print(e);
//        String id = UUID.randomUUID().toString();
//        Intent intent = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id, false, ss, se, null);
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc);
//        assertNotNull(PendingIntent.getBroadcast(context, rc, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss, intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se, intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_DEACTIVATE, intent.getAction());
//        assertEquals(AudioManager.RINGER_MODE_SILENT, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//
//        // increment clock +1h
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(1).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//
//        assertNull(PendingIntent.getBroadcast(context, rc, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size());
//
//        preferenceEditor.putStringSet(prefStartedIdsTag, savedIds).commit();
//    }
//
//    // 1 interval, start-time +1h, end-time +2h, no repeat
//    @Test
//    public void test2() {
//        int rc1, rc2, rc3;
//        Set<String> pref;
//
//        Set<String> savedIds = preferences.getStringSet(prefStartedIdsTag, new HashSet<>());
//        Set<String> savedHandles = handlesPreferences.getAll().keySet();
//        Set<String> nonEmptySet = new HashSet<>();
//        nonEmptySet.add("string");
//        preferenceEditor.putStringSet(prefStartedIdsTag, new HashSet<>()).commit();
//
//        DateTime clock = DateTime.now();
//        DateTimeUtils.setCurrentMillisFixed(clock.getMillis());
//
//        DateTime s = clock.plusHours(1);
//        DateTime e = clock.plusHours(2);
//        roll(s,e);
//
//        String ss = formatter.print(s);
//        String se = formatter.print(e);
//        String id = UUID.randomUUID().toString();
//        Intent intent = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id, false, ss, se, null);
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc1 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc1);
//        assertNotNull(PendingIntent.getBroadcast(context, rc1, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss, intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se, intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.size() == savedHandles.size()+1);
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//
//        // set clock +1h
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(1).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc2 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc2);
//        assertNull(PendingIntent.getBroadcast(context, rc1, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc2, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss, intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se, intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_DEACTIVATE, intent.getAction());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.size() == savedHandles.size()+1);
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//
//        // set clock +2h
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(2).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc3 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc3);
//        assertNull(PendingIntent.getBroadcast(context, rc2, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNull(PendingIntent.getBroadcast(context, rc3, intent, PendingIntent.FLAG_NO_CREATE));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.size() == savedHandles.size());
//        assertTrue(pref.containsAll(savedHandles));
//
//        preferenceEditor.putStringSet(prefStartedIdsTag, savedIds).commit();
//    }
//
//    // 1 interval, start-time -2h, end-time -1h, no repeat
//    @Test
//    public void test3() {
//        int rc1,rc2,rc3;
//        Set<String> pref;
//
//        Set<String> savedIds = preferences.getStringSet(prefStartedIdsTag, new HashSet<String>());
//        Set<String> savedHandles = handlesPreferences.getAll().keySet();
//        Set<String> nonEmptySet = new HashSet<>();
//        nonEmptySet.add("string");
//        preferenceEditor.putStringSet(prefStartedIdsTag, new HashSet<>()).commit();
//
//        DateTime clock = DateTime.now();
//        DateTimeUtils.setCurrentMillisFixed(clock.getMillis());
//
//        DateTime s = clock.minusHours(2);
//        DateTime e = clock.minusHours(1);
//        roll(s,e);
//
//        String ss = formatter.print(s);
//        String se = formatter.print(e);
//        String id = UUID.randomUUID().toString();
//        Intent intent = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id, false, ss, se, null);
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc1 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc1);
//        assertNotNull(PendingIntent.getBroadcast(context, rc1, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(formatter.print(s.plusDays(1)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(e.plusDays(1)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.getRingerMode());
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size() +1);
//
//        // set clock +1day -2h
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(1).minusHours(2).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc2 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc2);
//        assertNull(PendingIntent.getBroadcast(context, rc1, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc2, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(formatter.print(s.plusDays(1)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(e.plusDays(1)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_DEACTIVATE, intent.getAction());
//        assertEquals(AudioManager.RINGER_MODE_SILENT, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size()==1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size()+1);
//        assertTrue(pref.contains(id));
//
//        // set clock +1day -1h
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(1).minusHours(1).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc3 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc3);
//        assertNull(PendingIntent.getBroadcast(context, rc2, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNull(PendingIntent.getBroadcast(context, rc3, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size());
//
//        preferenceEditor.putStringSet(prefStartedIdsTag, savedIds).commit();
//    }
//
//    @Test
//    public void test4() {
//        int rc11, rc12;
//        int rc21, rc22;
//        Set<String> pref;
//
//        Set<String> savedIds = preferences.getStringSet(prefStartedIdsTag, new HashSet<String>());
//        preferenceEditor.putStringSet(prefStartedIdsTag, new HashSet<>()).commit();
//        Set<String> nonEmptySet = new HashSet<>();
//        nonEmptySet.add("string");
//        Set<String> savedHandles = handlesPreferences.getAll().keySet();
//
//        DateTime clock = DateTime.now();
//        DateTimeUtils.setCurrentMillisFixed(clock.getMillis());
//
//        DateTime s1 = clock.plusMinutes(30);
//        DateTime e1 = clock.plusHours(1).plusMinutes(30);
//        roll(s1,e1);
//
//        DateTime s2 = clock.plusHours(1);
//        DateTime e2 = clock.plusHours(2);
//        roll(s2,e2);
//
//        // (intent1) 30min -> 90min
//        String ss1 = formatter.print(s1);
//        String se1 = formatter.print(e1);
//        String id1 = UUID.randomUUID().toString();
//        Intent intent1 = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id1, false, ss1, se1, null);
//
//        // (intent2) 60min -> 120min
//        String ss2 = formatter.print(s2);
//        String se2 = formatter.print(e2);
//        String id2 = UUID.randomUUID().toString();
//        Intent intent2 = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id2, false, ss2, se2, null);
//
//        // ------
//        receiver.onReceive(context, intent1);
//        rc11 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc11);
//        assertNotNull(PendingIntent.getBroadcast(context, rc11, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss1, intent1.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se1, intent1.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent1.getAction());
//        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.getRingerMode());
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.size() == savedHandles.size() +1);
//
//        // ------
//        receiver.onReceive(context, intent2);
//        rc21 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc21);
//        assertNotNull(PendingIntent.getBroadcast(context, rc11, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc21, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss2, intent2.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se2, intent2.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent2.getAction());
//        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.getRingerMode());
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == savedHandles.size() +2);
//
//        // set clock to +30min
//        DateTimeUtils.setCurrentMillisFixed(clock.plusMinutes(30).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent1);
//        rc12 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc12);
//        assertNull(PendingIntent.getBroadcast(context, rc11, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc12, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc21, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss1, intent1.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se1, intent1.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_DEACTIVATE, intent1.getAction());
//        assertEquals(AudioManager.RINGER_MODE_SILENT, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.size()==1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == savedHandles.size() +2);
//
//        // set clock to +60min
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(1).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent2);
//        rc22 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc22);
//        assertNull(PendingIntent.getBroadcast(context, rc21, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(null, PendingIntent.getBroadcast(context, rc12, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(null, PendingIntent.getBroadcast(context, rc22, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(ss2, intent2.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(se2, intent2.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_DEACTIVATE, intent2.getAction());
//        assertEquals(AudioManager.RINGER_MODE_SILENT, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == 2);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == savedHandles.size() +2);
//
//        // set clock to +90min
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(1).plusMinutes(30).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent1);
//
//        assertNull(PendingIntent.getBroadcast(context, rc12, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc22, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(AudioManager.RINGER_MODE_SILENT, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == savedHandles.size() +1);
//
//        // set clock to +120min
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(2).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent2);
//
//        assertNull(PendingIntent.getBroadcast(context, rc22, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.getRingerMode());
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size());
//
//        preferenceEditor.putStringSet(prefStartedIdsTag, savedIds).commit();
//    }
//
//    @Test
//    public void test5() {
//        int rc1, rc2, rc3, rc4, rc5, rc6, rc7, rc8, rc9, rc10, rc11, rc12, rc13;
//        Set<String> pref;
//
//        Set<String> savedIds = preferences.getStringSet(prefStartedIdsTag, new HashSet<>());
//        Set<String> savedHandles = handlesPreferences.getAll().keySet();
//        Set<String> nonEmptySet = new HashSet<>();
//        nonEmptySet.add("string");
//        preferenceEditor.putStringSet(prefStartedIdsTag, new HashSet<>()).commit();
//
//        DateTime clock = DateTime.now().withDayOfWeek(DateTimeConstants.THURSDAY);
//        DateTimeUtils.setCurrentMillisFixed(clock.getMillis());
//
//        DateTime s = clock.toDateTime();
//        int offset = 4;
//
//        DateTime sMon = s.withDayOfWeek(DateTimeConstants.MONDAY);
//        DateTime sWed = s.withDayOfWeek(DateTimeConstants.WEDNESDAY);
//        DateTime sFri = s.withDayOfWeek(DateTimeConstants.FRIDAY);
//        DateTime sSun = s.withDayOfWeek(DateTimeConstants.SUNDAY);
//
//        DateTime eMon = sMon.plusHours(offset);
//        DateTime eWed = sWed.plusHours(offset);
//        DateTime eFri = sFri.plusHours(offset);
//        DateTime eSun = sSun.plusHours(offset);
//
//        String iMon = SilentInterval.formatInterval(formatter.print(sMon), formatter.print(eMon));
//        String iWed = SilentInterval.formatInterval(formatter.print(sWed), formatter.print(eWed));
//        String iFri = SilentInterval.formatInterval(formatter.print(sFri), formatter.print(eFri));
//        String iSun = SilentInterval.formatInterval(formatter.print(sSun), formatter.print(eSun));
//
//        String iMon7 = SilentInterval.formatInterval(formatter.print(sMon.plusDays(7)), formatter.print(eMon.plusDays(7)));
//        String iWed7 = SilentInterval.formatInterval(formatter.print(sWed.plusDays(7)), formatter.print(eWed.plusDays(7)));
//        String iFri7 = SilentInterval.formatInterval(formatter.print(sFri.plusDays(7)), formatter.print(eFri.plusDays(7)));
//        String iSun7 = SilentInterval.formatInterval(formatter.print(sSun.plusDays(7)), formatter.print(eSun.plusDays(7)));
//
//        String iMon14 = SilentInterval.formatInterval(formatter.print(sMon.plusDays(14)), formatter.print(eMon.plusDays(14)));
//        String iWed14 = SilentInterval.formatInterval(formatter.print(sWed.plusDays(14)), formatter.print(eWed.plusDays(14)));
//        String iFri14 = SilentInterval.formatInterval(formatter.print(sFri.plusDays(14)), formatter.print(eFri.plusDays(14)));
//        String iSun14 = SilentInterval.formatInterval(formatter.print(sSun.plusDays(14)), formatter.print(eSun.plusDays(14)));
//
//        ArrayList<String> weekdays = new ArrayList<>();
//        weekdays.add(iMon);
//        weekdays.add(iWed);
//        weekdays.add(iFri);
//        weekdays.add(iSun);
//
//        String id = UUID.randomUUID().toString();
//        Intent intent = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id, true, null, null, weekdays);
//
//        // clock on Thursday ------
//        receiver.onReceive(context, intent);
//        rc1 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc1);
//        assertNotNull(PendingIntent.getBroadcast(context, rc1, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(formatter.print(sFri), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eFri), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iFri, weekdays.get(0));
//        assertEquals(iSun, weekdays.get(1));
//        assertEquals(iMon7, weekdays.get(2));
//        assertEquals(iWed7, weekdays.get(3));
//
//        // clock on Friday (start)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(1).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc2 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc2);
//        assertNull(PendingIntent.getBroadcast(context, rc1, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc2, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_STOP, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sFri), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eFri), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iFri, weekdays.get(0));
//        assertEquals(iSun, weekdays.get(1));
//        assertEquals(iMon7, weekdays.get(2));
//        assertEquals(iWed7, weekdays.get(3));
//
//        // clock on Friday (end)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(1).plusHours(offset).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc3 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc3);
//        assertNull(PendingIntent.getBroadcast(context, rc2, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc3, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sSun), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eSun), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iSun, weekdays.get(0));
//        assertEquals(iMon7, weekdays.get(1));
//        assertEquals(iWed7, weekdays.get(2));
//        assertEquals(iFri7, weekdays.get(3));
//
//        // clock on Sunday (start)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(3).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc4 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc4);
//        assertNull(PendingIntent.getBroadcast(context, rc3, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc4, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_STOP, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sSun), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eSun), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iSun, weekdays.get(0));
//        assertEquals(iMon7, weekdays.get(1));
//        assertEquals(iWed7, weekdays.get(2));
//        assertEquals(iFri7, weekdays.get(3));
//
//        // clock on Sunday (end)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(3).plusHours(offset).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc5 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc5);
//        assertNull(PendingIntent.getBroadcast(context, rc4, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc5, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sMon.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eMon.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iMon7, weekdays.get(0));
//        assertEquals(iWed7, weekdays.get(1));
//        assertEquals(iFri7, weekdays.get(2));
//        assertEquals(iSun7, weekdays.get(3));
//
//        // clock on Monday (start)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(4).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc6 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc6);
//        assertNull(PendingIntent.getBroadcast(context, rc5, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc6, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_STOP, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sMon.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eMon.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iMon7, weekdays.get(0));
//        assertEquals(iWed7, weekdays.get(1));
//        assertEquals(iFri7, weekdays.get(2));
//        assertEquals(iSun7, weekdays.get(3));
//
//        // clock on Monday (end)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(4).plusHours(offset).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc7 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc7);
//        assertNull(PendingIntent.getBroadcast(context, rc6, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc7, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sWed.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eWed.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iWed7, weekdays.get(0));
//        assertEquals(iFri7, weekdays.get(1));
//        assertEquals(iSun7, weekdays.get(2));
//        assertEquals(iMon14, weekdays.get(3));
//
//        // clock on Wednesday (start)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(6).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc8 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc8);
//        assertNull(PendingIntent.getBroadcast(context, rc7, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc8, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_STOP, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sWed.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eWed.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iWed7, weekdays.get(0));
//        assertEquals(iFri7, weekdays.get(1));
//        assertEquals(iSun7, weekdays.get(2));
//        assertEquals(iMon14, weekdays.get(3));
//
//        // clock on Wednesday (end)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(6).plusHours(offset).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc9 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc9);
//        assertNull(PendingIntent.getBroadcast(context, rc8, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc9, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sFri.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eFri.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iFri7, weekdays.get(0));
//        assertEquals(iSun7, weekdays.get(1));
//        assertEquals(iMon14, weekdays.get(2));
//        assertEquals(iWed14, weekdays.get(3));
//
//        // clock on Friday (start)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(8).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc10 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc10);
//        assertNull(PendingIntent.getBroadcast(context, rc9, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc10, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_STOP, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sFri.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eFri.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iFri7, weekdays.get(0));
//        assertEquals(iSun7, weekdays.get(1));
//        assertEquals(iMon14, weekdays.get(2));
//        assertEquals(iWed14, weekdays.get(3));
//
//        // clock on Friday (end)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(8).plusHours(offset).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc11 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc11);
//        assertNull(PendingIntent.getBroadcast(context, rc10, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc11, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sSun.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eSun.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iSun7, weekdays.get(0));
//        assertEquals(iMon14, weekdays.get(1));
//        assertEquals(iWed14, weekdays.get(2));
//        assertEquals(iFri14, weekdays.get(3));
//
//        // clock on Sunday (start)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(10).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc12 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc12);
//        assertNull(PendingIntent.getBroadcast(context, rc11, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc12, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_STOP, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sSun.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eSun.plusDays(7)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == 1);
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iSun7, weekdays.get(0));
//        assertEquals(iMon14, weekdays.get(1));
//        assertEquals(iWed14, weekdays.get(2));
//        assertEquals(iFri14, weekdays.get(3));
//
//        // clock on Sunday (end)
//        DateTimeUtils.setCurrentMillisFixed(clock.plusDays(10).plusHours(offset).getMillis());
//
//        // ------
//        receiver.onReceive(context, intent);
//        rc13 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc13);
//        assertNull(PendingIntent.getBroadcast(context, rc12, intent, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc13, intent, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent.getAction());
//        assertTrue(intent.getBooleanExtra(SilentIntervalReceiver.EXTRA_REPEAT, false));
//        assertEquals(formatter.print(sMon.plusDays(14)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(formatter.print(eMon.plusDays(14)), intent.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id));
//        assertTrue(pref.size() == savedHandles.size() +1);
//        weekdays = intent.getStringArrayListExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS);
//        assertEquals(4, weekdays.size());
//        assertEquals(iMon14, weekdays.get(0));
//        assertEquals(iWed14, weekdays.get(1));
//        assertEquals(iFri14, weekdays.get(2));
//        assertEquals(iSun14, weekdays.get(3));
//
//        // manual stop
//        intent.setAction(SilentIntervalReceiver.ACTION_DEACTIVATE);
//
//        // ------
//        receiver.onReceive(context, intent);
//
//        assertNull(PendingIntent.getBroadcast(context, rc13, intent, PendingIntent.FLAG_NO_CREATE));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size());
//
//        preferenceEditor.putStringSet(prefStartedIdsTag, savedIds).commit();
//    }
//
//    @Test
//    public void test6() {
//        int rc1, rc2, rc3;
//        Set<String> pref;
//
//        Set<String> savedIds = preferences.getStringSet(prefStartedIdsTag, new HashSet<>());
//        Set<String> savedHandles = handlesPreferences.getAll().keySet();
//        Set<String> nonEmptySet = new HashSet<>();
//        nonEmptySet.add("string");
//        preferenceEditor.putStringSet(prefStartedIdsTag, new HashSet<>()).commit();
//
//        DateTime clock = DateTime.now();
//        DateTimeUtils.setCurrentMillisFixed(clock.getMillis());
//
//        String s1, s2, s3, e1, e2, e3;
//        s1 = s2 = s3 = formatter.print(clock.plusHours(1));
//        e1 = e2 = e3 = formatter.print(clock.plusHours(2));
//
//        String id1 = UUID.randomUUID().toString();
//        String id2 = UUID.randomUUID().toString();
//        String id3 = UUID.randomUUID().toString();
//
//        Intent intent1 = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id1, false, s1, e1,null);
//        Intent intent2 = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id2, false, s2, e2, null);
//        Intent intent3 = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, id3, false, s3, e3, null);
//
//        // ------
//        receiver.onReceive(context, intent1);
//        rc1 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc1);
//        assertNotNull(PendingIntent.getBroadcast(context, rc1, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent1.getAction());
//        assertEquals(s1, intent1.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(e1, intent1.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        assertTrue(preferences.getStringSet(prefStartedIdsTag, nonEmptySet).isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.size() == savedHandles.size() +1);
//
//        // ------
//        receiver.onReceive(context, intent2);
//        rc2 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc2);
//        assertNotNull(PendingIntent.getBroadcast(context, rc1, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc2, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent2.getAction());
//        assertEquals(s2, intent2.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(e2, intent2.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.size() == savedHandles.size() +2);
//
//        // ------
//        receiver.onReceive(context, intent3);
//        rc3 = preferences.getInt(prefRequestCodeTag, -1);
//
//        assertNotEquals(-1, rc3);
//        assertNotEquals(rc1, rc3);
//        assertNotEquals(rc2, rc3);
//        assertNotNull(PendingIntent.getBroadcast(context, rc1, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc2, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc3, intent3, PendingIntent.FLAG_NO_CREATE));
//        assertEquals(SilentIntervalReceiver.ACTION_START, intent3.getAction());
//        assertEquals(s3, intent3.getStringExtra(SilentIntervalReceiver.EXTRA_START_TIME));
//        assertEquals(e3, intent3.getStringExtra(SilentIntervalReceiver.EXTRA_END_TIME));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id1));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.contains(id3));
//        assertTrue(pref.size() == savedHandles.size() +3);
//
//        // ------
//        Intent canceled1 = new Intent(SilentIntervalReceiver.ACTION_DEACTIVATE);
//        Intent canceled2 = new Intent(SilentIntervalReceiver.ACTION_DEACTIVATE);
//        Intent canceled3 = new Intent(SilentIntervalReceiver.ACTION_DEACTIVATE);
//        canceled1.replaceExtras(intent1.getExtras());
//        canceled2.replaceExtras(intent2.getExtras());
//        canceled3.replaceExtras(intent3.getExtras());
//
//        // set clock +1h
//        DateTimeUtils.setCurrentMillisFixed(clock.plusHours(1).getMillis());
//
//        // ------
//        receiver.onReceive(context, canceled1);
//
//        assertNull(PendingIntent.getBroadcast(context, rc1, intent1, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc2, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc3, intent3, PendingIntent.FLAG_NO_CREATE));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id2));
//        assertTrue(pref.contains(id3));
//        assertTrue(pref.size() == savedHandles.size() +2);
//
//        // ------
//        receiver.onReceive(context, canceled2);
//
//        assertNull(PendingIntent.getBroadcast(context, rc2, intent2, PendingIntent.FLAG_NO_CREATE));
//        assertNotNull(PendingIntent.getBroadcast(context, rc3, intent3, PendingIntent.FLAG_NO_CREATE));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.contains(id3));
//        assertTrue(pref.size() == savedHandles.size() +1);
//
//        // ------
//        receiver.onReceive(context, canceled3);
//
//        assertNull(PendingIntent.getBroadcast(context, rc3, intent3, PendingIntent.FLAG_NO_CREATE));
//        pref = preferences.getStringSet(prefStartedIdsTag, nonEmptySet);
//        assertTrue(pref.isEmpty());
//        pref = handlesPreferences.getAll().keySet();
//        assertTrue(pref.containsAll(savedHandles));
//        assertTrue(pref.size() == savedHandles.size());
//
//        preferenceEditor.putStringSet(prefStartedIdsTag, savedIds).commit();
//    }
//
//    @Ignore
//    public void testPendingIntent() {
//        Intent intent = getIntent(SilentIntervalReceiver.ACTION_ACTIVATE, "-15", false, "", "", null);
//        PendingIntent pi0 = PendingIntent.getBroadcast(context, -10, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        PendingIntent pi1 = PendingIntent.getBroadcast(context, -10, intent, PendingIntent.FLAG_NO_CREATE);
//        assertNotEquals(null, pi1);
//        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 100000, pi0);
//        assertNotEquals(null, pi1);
//        alarmManager.cancel(pi0);
//        pi1 = PendingIntent.getBroadcast(context, -10, intent, PendingIntent.FLAG_NO_CREATE);
//        assertNotEquals(null, pi1);
//        pi0.cancel();
//        pi1 = PendingIntent.getBroadcast(context, -10, intent, PendingIntent.FLAG_NO_CREATE);
//        assertEquals(null, pi1);
//    }
//
//    private Intent getIntent(String action,
//                           String uuid,
//                           boolean repeat,
//                           String startTime,
//                           String endTime,
//                           @Nullable ArrayList<String> weekdays) {
//        Intent intent = new Intent(action);
//        intent.setClass(context, SilentIntervalReceiver.class);
//
//        intent.putExtra(SilentIntervalReceiver.EXTRA_UUID, uuid)
//                .putExtra(SilentIntervalReceiver.EXTRA_REPEAT, repeat)
//                .putExtra(SilentIntervalReceiver.EXTRA_START_TIME, startTime)
//                .putExtra(SilentIntervalReceiver.EXTRA_END_TIME, endTime);
//
//        if (weekdays != null) {
//            intent.putExtra(SilentIntervalReceiver.EXTRA_WEEKDAYS, weekdays);
//        }
//        return intent;
//    }
//
//    private void roll(DateTime start, DateTime end) {
//        if (start.isAfter(end)) {
//            end = end.plusDays(1);
//        } else if (start.equals(end)) {
//            end = end.plusMinutes(5);
//        }
//    }
//
//}
