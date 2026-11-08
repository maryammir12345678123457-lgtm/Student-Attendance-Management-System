package com.attendance.studentattendancesystem;

import com.attendance.studentattendancesystem.i18n.I18n;
import com.attendance.studentattendancesystem.i18n.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class I18nTest {

    @AfterEach
    void resetLanguage() {
        I18n.setLanguage(Language.ENGLISH);
    }

    @Test
    void defaultsToEnglish() {
        I18n.setLanguage(Language.ENGLISH);
        assertEquals("Dashboard", I18n.t("nav.dashboard"));
    }

    @Test
    void switchesToUrduAndChinese() {
        I18n.setLanguage(Language.URDU);
        assertEquals("ڈیش بورڈ", I18n.t("nav.dashboard"));

        I18n.setLanguage(Language.CHINESE);
        assertEquals("仪表盘", I18n.t("nav.dashboard"));
        assertNotEquals("Dashboard", I18n.t("nav.dashboard"));
    }

    @Test
    void unknownKeyReturnsKey() {
        assertEquals("missing.key", I18n.t("missing.key"));
    }
}
