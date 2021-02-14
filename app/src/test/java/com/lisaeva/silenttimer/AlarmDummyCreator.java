package com.lisaeva.silenttimer;

import com.lisaeva.silenttimer.persistence.SilentAlarmData;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AlarmDummyCreator {

    private static final int MAX_DESCRIPTION_LENGTH = 35;
    private static final int DUMMY_CNT = 10;
    private static boolean USE_PREPARED_FAKES = true;

    public static List<SilentAlarmData> generateDummies() {
        if (USE_PREPARED_FAKES)
            return getFakes();

        List<SilentAlarmData> list = new ArrayList<>();

        for (int i = 0; i<10; i++) {
            list.add(randomSilentAlarm());
        }

        return list;
    }

    private static SilentAlarmData makeDummy(String uuid, String title, String description, String startDate, String endDate, String weekdays, int repeat, int showDescription, int on, int completedTask) {
        SilentAlarmData clone = new SilentAlarmData();
        clone.setUuid(uuid);
        clone.setTitle(title);
        clone.setDescription(description);
        clone.setStartDate(startDate);
        clone.setEndDate(endDate);
        clone.setWeekdays(weekdays);
        clone.setRepeat(repeat);
        clone.setShowDescription(showDescription);
        clone.setActive(on);
        clone.setCompletedTask(completedTask);
        return clone;
    }


    private static SilentAlarmData randomSilentAlarm(){
        return makeDummy(UUID.randomUUID().toString(), randomTitle(), randomDescription(), randomTime(), randomTime(), randomWeekdays(), randomBoolean(), randomBoolean(), randomBoolean(), randomBoolean());
    }

    private static String randomTitle() {
        String[] titles = new String[] {"School", "Work", "Night", "Meeting",
                "Very tired of everyone", "Complete silence",
                "Library", "Hiding from angry animals", "Playing hide and seek",
                "FBI mission", "Movie", "Silence competition"};

        Random random = new Random();

        return titles[random.nextInt(titles.length)];
    }

    private static String randomDescription() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        double spaceChance = 0.2;

        int limit = random.nextInt(MAX_DESCRIPTION_LENGTH) +1;

        for (int i = 0; i<limit; i++) {
            if (Math.random() < spaceChance) {
                sb.append(' ');
            } else {
                sb.append((char)(random.nextInt(26)+'a'));
            }
        }

        String res = sb.toString().trim();
        if(res.isEmpty())res = "Generated description";

        return res;
    }

    private static String randomTime() {
        Random random = new Random();
        int min = random.nextInt(61);
        int hour = random.nextInt(25);

        return String.format("%1$02d:%2$02d", hour, min);
    }

    private static String randomWeekdays() {
        return "generated weekdays";
    }

    private static int randomBoolean() {
        return Math.random() < 0.5 ? 0 : 1;
    }




    // --------------------------------------------------------------------

    private void inProgress() {
        String[] titles = new String[] {"School", "Work", "Night", "Meeting",
                "Very tired of everyone", "Complete silence",
                "Library", "Hiding from angry animals", "Playing hide and seek",
                "FBI mission", "Movie", "Silence competition"};

        String[] descriptions = new String[] {
                "We never really grow up, we only learn how to act in public.",
                "Trouble knocked at the door, but, hearing laughter, hurried away.",
                "Money won’t buy happiness, but it will pay the salaries of a large research staff to study the problem.",
                "Nobody realizes that some people expend tremendous energy merely to be normal.",
                "My mother always used to say: The older you get, the better you get, unless you’re a banana.",
                "Better to remain silent and be thought a fool than to speak out and remove all doubt.",
                "If I were two-faced, would I be wearing this one?",
                "The best thing about the future is that it comes one day at a time.",
                "Why did the kamikaze pilots wear helmets?",
                "Light travels faster than sound. This is why some people appear bright until you hear them speak.",
                "To be sure of hitting the target, shoot first, and call whatever you hit the target.",
                "If you think nobody cares if you’re alive, try missing a couple of car payments."
        };

        List<String> titleList = Arrays.asList(titles);
        List<String> descList = Arrays.asList(descriptions);
        Collections.shuffle(titleList);
        Collections.shuffle(descList);




        SimpleDateFormat dFormat = new SimpleDateFormat("MMM d");
        SimpleDateFormat tFormat = new SimpleDateFormat("HH:mm");

        for (int i = 0; i<titleList.size(); i++) {


            System.out.println("\"" + UUID.randomUUID().toString() + "\""
                    + "\"" + titleList.get(i) + "\""
                    + "\"" + descList.get(i) + "\""
                    + "\"");
        }
    }



        public static List<SilentAlarmData> getFakes() {
            List<SilentAlarmData> fakes = new ArrayList<>();
            for (int i = 0; i<12; i++) {
                fakes.add(makeDummy(uuids[i],
                        titles[i],
                        descriptions[i],
                        startTimes[i],
                        endTimes[i],
                        weekdays[i],
                        repeat[i],
                        showDescription[i],
                        on[i],
                        completedTask[i]));
            }
            return fakes;
        }

        private static final String[] uuids = new String[] {
                "29fc72f6-1b94-48b3-8e2c-5539e06912f4",
                "ce47d5aa-2a41-47d7-9a72-27783d9bbe2d",
                "1585e5b4-8995-43eb-b61d-d166e3099e79",
                "2d619fb2-8469-44bb-91ba-ed10395d47c7",
                "e783fb19-a07f-4f68-9f62-2791e3be740e",
                "28bedf4a-b831-41d1-b2f0-9717c4e78b36",
                "43aa8891-95e7-4519-a483-153ce54b1e77",
                "68ee45a3-0437-45f9-b1ac-b18963844c39",
                "5e83c154-d4ae-48cf-967a-96d8ede97e16",
                "b1b7f032-0824-4922-ab95-d71d0df4f2e2",
                "93e8e782-1778-4b21-a2a2-31ae77741a75",
                "bfe69551-09b1-4272-882e-8f5af3d97949"
        };

        private static final String[] titles = new String[] {"School", "Work", "Night", "Meeting",
                "Very tired of everyone", "Complete silence",
                "Library", "Hiding from angry animals", "Playing hide and seek",
                "FBI mission", "Movie", "Silence competition"};

        private static final String[] descriptions = new String[] {
                "We never really grow up, we only learn how to act in public.",
                "Trouble knocked at the door, but, hearing laughter, hurried away.",
                "Money won’t buy happiness, but it will pay the salaries of a large research staff to study the problem.",
                "Nobody realizes that some people expend tremendous energy merely to be normal.",
                "My mother always used to say: The older you get, the better you get, unless you’re a banana.",
                "Better to remain silent and be thought a fool than to speak out and remove all doubt.",
                "If I were two-faced, would I be wearing this one?",
                "The best thing about the future is that it comes one day at a time.",
                "Why did the kamikaze pilots wear helmets?",
                "Light travels faster than sound. This is why some people appear bright until you hear them speak.",
                "To be sure of hitting the target, shoot first, and call whatever you hit the target.",
                "If you think nobody cares if you’re alive, try missing a couple of car payments."
        };

        private static final String[] startTimes = new String[] {
                "15:42",
                "00:44",
                "10:39",
                "03:12",
                "09:44",
                "10:25",
                "05:12",
                "21:48",
                "02:50",
                "12:33",
                "11:45",
                "18:48"
        };

        private static final String[] endTimes = new String[] {
                "16:36",
                "13:20",
                "12:13",
                "13:14",
                "06:54",
                "03:40",
                "14:57",
                "20:45",
                "13:38",
                "01:27",
                "17:11",
                "21:48"
        };

        private static final int[] repeat = new int[]{0, 1, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1};
        private static final String[] weekdays = new String[]{
                "0000000",
                "1110101",
                "0000000",
                "1010101",
                "0000000",
                "0110111",
                "0000000",
                "0000000",
                "1110111",
                "0000000",
                "1101010",
                "0001111"
        };

        private static final int[] showDescription = new int[] {0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 0};
        private static final int[] on = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        private static final int[] completedTask = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};


}
