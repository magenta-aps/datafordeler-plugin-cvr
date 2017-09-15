package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.arearestriction.AreaRestrictionType;
import dk.magenta.datafordeler.core.plugin.AreaRestrictionDefinition;
import dk.magenta.datafordeler.core.plugin.Plugin;

public class CvrAreaRestrictionDefinition extends AreaRestrictionDefinition {

    private CvrPlugin plugin;

    public static final String RESTRICTIONTYPE_KOMMUNEKODER = "kommunekoder";
    public static final String RESTRICTION_KOMMUNE_KUJALLEQ = "Kujalleq";
    public static final String RESTRICTION_KOMMUNE_QAASUITSUP = "Qaasuitsup";
    public static final String RESTRICTION_KOMMUNE_QEQQATA = "Qeqqata";
    public static final String RESTRICTION_KOMMUNE_SERMERSOOQ = "Sermersooq";


    public CvrAreaRestrictionDefinition(CvrPlugin plugin) {
        this.plugin = plugin;
        AreaRestrictionType kommunekodeRestriction = this.addAreaRestrictionType(RESTRICTIONTYPE_KOMMUNEKODER, "Afgrænsning på kommunekode");
        kommunekodeRestriction.addChoice(RESTRICTION_KOMMUNE_KUJALLEQ, "Kujalleq kommune", null, "955");
        kommunekodeRestriction.addChoice(RESTRICTION_KOMMUNE_QAASUITSUP, "Qaasuitsup kommune", null, "958");
        kommunekodeRestriction.addChoice(RESTRICTION_KOMMUNE_QEQQATA, "Qeqqata kommune", null, "957");
        kommunekodeRestriction.addChoice(RESTRICTION_KOMMUNE_SERMERSOOQ, "Sermersooq kommune", null, "956");
    }

    @Override
    protected Plugin getPlugin() {
        return this.plugin;
    }
}
