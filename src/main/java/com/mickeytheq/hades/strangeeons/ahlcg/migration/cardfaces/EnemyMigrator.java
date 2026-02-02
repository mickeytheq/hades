package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import com.mickeytheq.hades.core.model.cardfaces.Enemy;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.CardFaceMigrationContext;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsFieldNames;

public class EnemyMigrator {
    public Enemy build(CardFaceMigrationContext context) {
        SettingsAccessor settingsAccessor = context.getSettingsAccessor();

        Enemy enemy = new Enemy();

        MigrationUtils.populateCommonCardFields(context, enemy.getCommonCardFieldsModel());
        MigrationUtils.populateCollection(context, enemy.getCollectionModel());
        MigrationUtils.populateEncounterSet(context, enemy.getEncounterSetModel());
        MigrationUtils.populateArt(context, enemy.getPortraitModel());

        enemy.getEnemyFieldsModel().setCombat(MigrationUtils.parseStatistic(settingsAccessor, SettingsFieldNames.ENEMY_COMBAT, SettingsFieldNames.PER_INVESTIGATOR + SettingsFieldNames.ENEMY_COMBAT));
        enemy.getEnemyFieldsModel().setHealth(MigrationUtils.parseStatistic(settingsAccessor, SettingsFieldNames.ENEMY_HEALTH, SettingsFieldNames.PER_INVESTIGATOR));
        enemy.getEnemyFieldsModel().setEvade(MigrationUtils.parseStatistic(settingsAccessor, SettingsFieldNames.ENEMY_EVADE, SettingsFieldNames.PER_INVESTIGATOR + SettingsFieldNames.ENEMY_EVADE));
        enemy.getEnemyFieldsModel().setDamage(settingsAccessor.getIntegerAllowInvalid(SettingsFieldNames.ENEMY_DAMAGE));
        enemy.getEnemyFieldsModel().setHorror(settingsAccessor.getIntegerAllowInvalid(SettingsFieldNames.ENEMY_HORROR));
        enemy.getEnemyFieldsModel().setWeaknessType(MigrationUtils.getWeaknessType(settingsAccessor.getString(SettingsFieldNames.SUBTYPE)));

        enemy.getCommonCardFieldsModel().setUnique(Integer.parseInt(settingsAccessor.getString("Unique")) != 0);

        return enemy;
    }
}
