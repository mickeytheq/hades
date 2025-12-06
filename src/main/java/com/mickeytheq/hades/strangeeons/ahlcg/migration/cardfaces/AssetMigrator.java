package com.mickeytheq.hades.strangeeons.ahlcg.migration.cardfaces;

import ca.cgjennings.apps.arkham.diy.DIY;
import com.mickeytheq.hades.core.model.cardfaces.Asset;
import com.mickeytheq.hades.core.view.CardFaceSide;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.MigrationUtils;
import com.mickeytheq.hades.strangeeons.ahlcg.migration.SettingsAccessor;

public class AssetMigrator {
    public Asset build(DIY diy, CardFaceSide cardFaceSide, SettingsAccessor settingsAccessor) {
        Asset asset = new Asset();

        MigrationUtils.populateCommonCardFields(diy, cardFaceSide, settingsAccessor, asset.getCommonCardFieldsModel());
        MigrationUtils.populatePlayerCardFields(settingsAccessor, asset.getPlayerCardFieldsModel());
        MigrationUtils.populatingNumbering(diy, settingsAccessor, asset.getNumberingModel());
        MigrationUtils.populateArt(diy, settingsAccessor, asset.getPortraitWithArtistModel());

        asset.setHealth(MigrationUtils.parseStatistic(settingsAccessor, "Stamina", null));
        asset.setSanity(MigrationUtils.parseStatistic(settingsAccessor, "Sanity", null));

        asset.getCommonCardFieldsModel().setUnique(Integer.parseInt(settingsAccessor.getString("Unique")) != 0);

        asset.setAssetSlot1(parseAssetSlot(settingsAccessor.getString("Slot")));
        asset.setAssetSlot2(parseAssetSlot(settingsAccessor.getString("Slot2")));

        return asset;
    }

    private static Asset.AssetSlot parseAssetSlot(String slot) {
        if (slot == null)
            return null;

        // specific mappings for the more complex slot types
        if (slot.equals("1 Hand"))
            return Asset.AssetSlot.Hand;
        else if (slot.equals("2 Hands"))
            return Asset.AssetSlot.TwoHands;
        else if (slot.equals("1 Arcane"))
            return Asset.AssetSlot.Arcane;
        else if (slot.equals("2 Arcane"))
            return Asset.AssetSlot.TwoArcane;
        else {
            try {
                return Enum.valueOf(Asset.AssetSlot.class, slot);
            } catch (IllegalArgumentException e) {
                // invalid slot, just ignore
                return null;
            }
        }
    }
}
