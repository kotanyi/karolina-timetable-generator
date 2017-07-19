package sk.karolina.timetable.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Level {
    RD_PRIPRAVKA(true), RD_LAHKY(true), RD_STREDNY(true), RD_TAZKY(true),
    SD_MS_VYUKA(false), SD_MS_TANCOVANIE(false),
    SD_PLUS_VYUKA(false), SD_PLUS_TANCOVANIE(false),
    SD_A1_VYUKA(false), SD_A1_TANCOVANIE(false),
    SD_A2_VYUKA(false), SD_A2_TANCOVANIE(false);
    
    private static final List<Level> values = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int size = values.size();
    private static final Random random = new Random();
    
    private boolean isRound;
    
    private Level(boolean isRound) {
    	this.isRound = isRound;
    }

    public static Level getRandomLevel() {
    	return values.get(random.nextInt(size));
    }

    public boolean isRound() {
    	return isRound;
    }
    
}
