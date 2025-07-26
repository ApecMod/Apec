package uk.co.hexeption.apec.hud;

public enum SkillType {
    NONE,
    FARMING,
    COMBAT,
    MINING,
    FORAGING,
    ENCHANTING,
    FISHING,
    ALCHEMY;
    public static SkillType getSkillType(String s) {
        if (s.contains("Farming")) return SkillType.FARMING;
        if (s.contains("Combat")) return SkillType.COMBAT;
        if (s.contains("Mining")) return SkillType.MINING;
        if (s.contains("Foraging")) return SkillType.FORAGING;
        if (s.contains("Enchanting")) return SkillType.ENCHANTING;
        if (s.contains("Fishing")) return SkillType.FISHING;
        if (s.contains("Alchemy")) return SkillType.ALCHEMY;
        else return SkillType.NONE;
    }
}
