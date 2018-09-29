package Repository.StatsDataStructures;

public class StatsData {
    public class Query {
        public static final String NUM_PEAKS_NOT_ANNOTABLE = "SELECT COUNT(DISTINCT Peak.idPeak) AS NUM " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=0";

        public static final String NUM_PEAKS_NOT_ANNOTATED_YET = "SELECT COUNT(DISTINCT Peak.idPeak) AS NUM " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND Peak.idPeak NOT IN (SELECT Annotation.idPeak " +
                "FROM crowdsourcingpeak.annotation WHERE annotation.idCampaign = ?)";

        public static final String NUM_PEAKS_ANNOTATED = "SELECT COUNT(DISTINCT Peak.idPeak) AS NUM " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND Peak.idPeak IN (SELECT Annotation.idPeak " +
                "FROM crowdsourcingpeak.annotation WHERE annotation.idCampaign = ?)";

        public static final String NUM_PEAKS_ONE_OR_MORE_ANNOTATION_REFUSED = "SELECT COUNT(DISTINCT Peak.idPeak) AS NUM " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND " +
                "Peak.idPeak IN (SELECT Annotation.idPeak FROM crowdsourcingpeak.annotation " +
                "WHERE annotation.idCampaign = ? AND annotation.state = 0)";

        public static final String NUM_CONFLICTS = "SELECT COUNT(DISTINCT Peak.idPeak) AS NUM " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND Peak.idPeak IN (SELECT Annotation.idPeak FROM crowdsourcingpeak.annotation " +
                                                                                                "WHERE annotation.idCampaign = ? " +
                                                                                                "AND annotation.validity = 1) " +
                                                                            "AND Peak.idPeak IN (SELECT Annotation.idPeak " +
                                                                                                "FROM crowdsourcingpeak.annotation " +
                                                                                                "WHERE annotation.idCampaign = ? " +
                                                                                                "AND annotation.validity = 0)";
        public static final String LIST_PEAKS_ONE_OR_MORE_ANNOTATION_REFUSED = "SELECT * " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND " +
                "Peak.idPeak IN (SELECT Annotation.idPeak FROM crowdsourcingpeak.annotation " +
                "WHERE annotation.idCampaign = ? AND annotation.state = 0)";

        public static final String LIST_PEAKS_ANNOTATED = "SELECT * " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND Peak.idPeak IN (SELECT Annotation.idPeak " +
                "FROM crowdsourcingpeak.annotation  WHERE annotation.idCampaign = ?)";

        public static final String LIST_CONFLICTS = "SELECT * " +
                "FROM crowdsourcingpeak.peak INNER JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak " +
                "WHERE PeakBelongings.idCampaign=? AND peakbelongings.toBeAnnotated=1 AND Peak.idPeak IN (SELECT Annotation.idPeak " +
                                                                                                "FROM crowdsourcingpeak.annotation " +
                                                                                                "WHERE annotation.idCampaign = ? " +
                                                                                                "AND annotation.validity = 1) " +
                                                                           "AND Peak.idPeak IN (SELECT Annotation.idPeak " +
                                                                                                "FROM crowdsourcingpeak.annotation " +
                                                                                                "WHERE annotation.idCampaign = ? " +
                                                                                                "AND annotation.validity = 0)";

    public static final String ANNOTATION_VALIDITY = "SELECT COUNT(DISTINCT annotation.idAnnotation) AS NUM " +
            "FROM crowdsourcingpeak.annotation WHERE annotation.validity = ? " +
            "AND annotation.idPeak = ? AND annotation.idCampaign = ?";

    }
}
