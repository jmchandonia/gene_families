
package us.kbase.kbasegenefamilies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


/**
 * <p>Original spec-file type: DomainAnnotation</p>
 * <pre>
 * domain_alignments_ref alignments_ref;
 * </pre>
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "genome_ref",
    "used_dms_ref",
    "data",
    "contig_to_size_and_feature_count",
    "feature_to_contig_and_index"
})
public class DomainAnnotation {

    @JsonProperty("genome_ref")
    private java.lang.String genomeRef;
    @JsonProperty("used_dms_ref")
    private java.lang.String usedDmsRef;
    @JsonProperty("data")
    private Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> data;
    @JsonProperty("contig_to_size_and_feature_count")
    private Map<String, us.kbase.common.service.Tuple2 <Long, Long>> contigToSizeAndFeatureCount;
    @JsonProperty("feature_to_contig_and_index")
    private Map<String, us.kbase.common.service.Tuple2 <String, Long>> featureToContigAndIndex;
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("genome_ref")
    public java.lang.String getGenomeRef() {
        return genomeRef;
    }

    @JsonProperty("genome_ref")
    public void setGenomeRef(java.lang.String genomeRef) {
        this.genomeRef = genomeRef;
    }

    public DomainAnnotation withGenomeRef(java.lang.String genomeRef) {
        this.genomeRef = genomeRef;
        return this;
    }

    @JsonProperty("used_dms_ref")
    public java.lang.String getUsedDmsRef() {
        return usedDmsRef;
    }

    @JsonProperty("used_dms_ref")
    public void setUsedDmsRef(java.lang.String usedDmsRef) {
        this.usedDmsRef = usedDmsRef;
    }

    public DomainAnnotation withUsedDmsRef(java.lang.String usedDmsRef) {
        this.usedDmsRef = usedDmsRef;
        return this;
    }

    @JsonProperty("data")
    public Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> data) {
        this.data = data;
    }

    public DomainAnnotation withData(Map<String, List<us.kbase.common.service.Tuple5 <String, Long, Long, Long, Map<String, List<us.kbase.common.service.Tuple5 <Long, Long, Double, Double, Double>>>>>> data) {
        this.data = data;
        return this;
    }

    @JsonProperty("contig_to_size_and_feature_count")
    public Map<String, us.kbase.common.service.Tuple2 <Long, Long>> getContigToSizeAndFeatureCount() {
        return contigToSizeAndFeatureCount;
    }

    @JsonProperty("contig_to_size_and_feature_count")
    public void setContigToSizeAndFeatureCount(Map<String, us.kbase.common.service.Tuple2 <Long, Long>> contigToSizeAndFeatureCount) {
        this.contigToSizeAndFeatureCount = contigToSizeAndFeatureCount;
    }

    public DomainAnnotation withContigToSizeAndFeatureCount(Map<String, us.kbase.common.service.Tuple2 <Long, Long>> contigToSizeAndFeatureCount) {
        this.contigToSizeAndFeatureCount = contigToSizeAndFeatureCount;
        return this;
    }

    @JsonProperty("feature_to_contig_and_index")
    public Map<String, us.kbase.common.service.Tuple2 <String, Long>> getFeatureToContigAndIndex() {
        return featureToContigAndIndex;
    }

    @JsonProperty("feature_to_contig_and_index")
    public void setFeatureToContigAndIndex(Map<String, us.kbase.common.service.Tuple2 <String, Long>> featureToContigAndIndex) {
        this.featureToContigAndIndex = featureToContigAndIndex;
    }

    public DomainAnnotation withFeatureToContigAndIndex(Map<String, us.kbase.common.service.Tuple2 <String, Long>> featureToContigAndIndex) {
        this.featureToContigAndIndex = featureToContigAndIndex;
        return this;
    }

    @JsonAnyGetter
    public Map<java.lang.String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(java.lang.String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public java.lang.String toString() {
        return ((((((((((((("DomainAnnotation"+" [genomeRef=")+ genomeRef)+", usedDmsRef=")+ usedDmsRef)+", data=")+ data)+", contigToSizeAndFeatureCount=")+ contigToSizeAndFeatureCount)+", featureToContigAndIndex=")+ featureToContigAndIndex)+", additionalProperties=")+ additionalProperties)+"]");
    }

}
